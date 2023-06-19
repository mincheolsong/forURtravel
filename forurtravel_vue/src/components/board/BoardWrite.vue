<template>
    <div id="BoardWrite">
        <h1>글작성</h1>
        <table>
            <colgroup>
                <col width="10%" />
                <col width="10%" />
                <col width="80%" />
            </colgroup>
            <tr>
                <td>제목</td>
                <td colspan="2">
                    <TheInput v-model="board.subject" />
                </td>
            </tr>
            <tr style="height: 1rem">
                <td colspan="3"></td>
            </tr>
            <tr>
                <td>본문</td>
                <td colspan="2">
                    <quill-editor class="editor" ref="myTextEditor" :disabled="false" :value="board.content"
                        :options="editorOption" @change="onEditorChange" />
                </td>
            </tr>
            <tr>
                <td>첨부파일</td>
                <td colspan="2">
                    <input accept="image/*" :type="'file'" @change="setFile" />
                </td>
            </tr>
            <tr style="height: 1rem">
                <td colspan="3"></td>
            </tr>
            <tr>
                <td colspan="3">
                    <input type="button" id="btn-submit" value="글작성" @click="write" />
                </td>
            </tr>
        </table>
    </div>
</template>

<script>
import TheInput from "../common/TheInput.vue";
import axios from "axios";
import { mapState } from "vuex";
const userStore = "userStore";
import hljs from "highlight.js";
import debounce from "lodash/debounce";
import { quillEditor } from "vue-quill-editor";

// highlight.js style
import "highlight.js/styles/tomorrow.css";

// import theme style
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
export default {
    name: "BoardWrite",
    components: { TheInput, quillEditor },
    data() {
        return {
            board: {
                subject: "",
                content: "",
                file: "",
                boardType: this.$route.params.boardType,
            },
            editorOption: {
                placeholder: "글을 작성해 주세요",
                modules: {
                    toolbar: [
                        ["bold", "italic", "underline", "strike"], // <strong>, <em>, <u>, <s>
                        ["blockquote", "code-block"], // <blockquote>, <pre class="ql-syntax" spellcheck="false">
                        [{ header: 1 }, { header: 2 }], // <h1>, <h2>
                        [{ list: "ordered" }, { list: "bullet" }],
                        [{ script: "sub" }, { script: "super" }], // <sub>, <sup>
                        [{ indent: "-1" }, { indent: "+1" }], // class제어
                        [{ direction: "rtl" }], //class 제어
                        [{ size: ["small", false, "large", "huge"] }], //class 제어 - html로 되도록 확인
                        [{ header: [1, 2, 3, 4, 5, 6, false] }], // <h1>, <h2>, <h3>, <h4>, <h5>, <h6>, normal
                        [{ font: [] }], // 글꼴 class로 제어
                        [{ color: [] }, { background: [] }], //style="color: rgb(230, 0, 0);", style="background-color: rgb(230, 0, 0);"
                        [{ align: [] }], // class
                        // ["clean"],
                        ["link", "image", "video"],
                    ],
                    syntax: {
                        highlight: (text) => hljs.highlightAuto(text).value,
                    },
                },
            },
        };
    },
    created() { },
    computed: {
        ...mapState(userStore, ["isLogin", "userInfo"]),
        editor() {
            return this.$refs.myTextEditor.quill;
        },
        contentCode() {
            return hljs.highlightAuto(this.board.content).value;
        },
    },
    methods: {
        onEditorChange: debounce(function (value) {
            this.board.content = value.html;
        }, 466),
        write() {
            if (!this.isLogin) {
                this.$router.push({ name: "home" });
                alert("로그인해주시길 바랍니다.");
                return;
            }
            let msg = "";

            if (!this.board.subject || this.board.subject === "") {
                msg = "제목을 입력해 주세요!";
            }
            if (!this.board.content || this.board.content === "") {
                msg = "내용을 입력해 주세요!";
            }

            if (msg.length !== 0) {
                alert(msg);
                return;
            }

            if (this.board.file.length === 0) {
                // 이미지 없는 게시글 업로드 write
                axios
                    .post(`${process.env.VUE_APP_BASE_URL}board/write-text`, {
                        subject: this.board.subject,
                        content: this.board.content,
                        boardType: this.board.boardType,
                        userNo: this.userInfo.userNo,
                    })
                    .then(({ data }) => {
                        if (data.code === "200") {
                            alert("게시글을 성공적으로 작성했습니다 :)");
                            this.$router.push({ name: "board-list" });
                        }
                    })
                    .catch(() => { });
            } else {
                // 이미지 있는 게시글 업로드 write
                const formData = new FormData();
                formData.append("subject", this.board.subject);
                formData.append("content", this.board.content);
                formData.append("file", this.board.file);
                formData.append("boardType", this.board.boardType);
                formData.append("userNo", this.userInfo.userNo);
                axios({
                    method: "post",
                    url: `${process.env.VUE_APP_BASE_URL}board/write-file`,
                    headers: {
                        "Content-Type": "multipart/form-data",
                    },
                    data: formData,
                })
                    .then(({ data }) => {
                        if (data.code === "200") {
                            alert("게시글을 성공적으로 작성했습니다 :)");
                            this.$router.push({ name: "board-list" });
                        } else {
                            alert(data);
                        }
                    })
                    .catch(() => {
                        alert("작성을 실패했습니다. 잠시 후 다시 시도해 주세요");
                    });
            }
        },
        setFile(event) {
            this.board.file = event.target.files[0];
        },
    },
};
</script>

<style scoped>
table {
    font-size: 1.2rem;
    font-weight: 900;
    box-sizing: border-box;
    white-space: nowrap;
    box-shadow: 2px 3px 5px 0px rgba(0, 0, 0, 0.329);
    padding: 3rem;
}

a {
    cursor: pointer;
}

input {
    height: 2rem;
    vertical-align: middle;
    font-size: 1.2rem;
    width: 100%;
    border: 0;
    border-bottom: 1px solid #00000021;
}

#fileInput {
    position: absolute;
    width: 0;
    height: 0;
    padding: 0;
    overflow: hidden;
    border: 0;
}

#fileName {
    text-align: center;
}

label {
    padding: 0.2rem 1rem;
    color: #fff;
    font-size: 1rem;
    font-weight: 900;
    background-color: #83adff;
    border: 0;
    border-radius: 0.2rem;
    cursor: pointer;
}

#btn-submit {
    color: #fff;
    font-weight: 900;
    background-color: #007bff;
    border: 0;
    border-radius: 0.2rem;
    cursor: pointer;
    width: 30rem;
    height: 3rem;
    font-size: 1.5rem;
}

.editor {
    font-size: 1.2rem;
}

.editor>:last-child>:first-child {
    min-height: 25rem;
}

.editor>:first-child>:last-child {
    display: none;
}

table> :last-child> :last-child {
    text-align: center;
}

#BoardWrite {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

h1 {
    margin-top: 4rem;
}
</style>
