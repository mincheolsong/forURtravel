<template>
  <div id="BoardDetail">
    <header>
      <div id="boardProfileWrap">
        <div class="profileImage">
          <img :src="userImgSrc" style="width: 3rem" />
        </div>
        {{ board.user.userName }}
      </div>
      <h1>{{ board.subject }}</h1>
      <div>
        <div>{{ board.modifiedTime !== null ? board.modifiedTime : board.createdTime }}</div>
        <div>조회수 : {{ board.hit }}</div>
        <div
          class="btn-more"
          @click="togleMore"
          v-if="userInfo != null && userInfo.userNo === board.user.userNo"
        >
          <img src="@/assets/img/more.svg" />
          <div v-if="isMore" class="more-wrap">
            <div class="more-item" @click="togleModify">
              <img width="1rem" src="@/assets/img/edit.svg" />수정하기
            </div>
            <div class="more-item" @click="deleteBoard">
              <img width="1rem" src="@/assets/img/delete.svg" />삭제하기
            </div>
          </div>
        </div>
      </div>
    </header>
    <div v-if="isModify" class="modify-input-wrap">
      <quill-editor
        class="editor"
        ref="myTextEditor"
        :options="editorOption"
        v-model="board.content"
        id="modify-input"
      ></quill-editor>
      <div style="display: flex; justify-content: center; align-items: center">
        <button class="btn-modify" @click="modify">수정하기</button>
        <button class="btn-cancel" @click="cancel">취소</button>
      </div>
    </div>
    <div v-else class="content-wrap output ql-snow">
      <quill-editor
        class="editor"
        ref="myTextEditor"
        :disabled="true"
        :value="board.content"
        :options="editorOption"
      />
      <div class="imgDown">
        <a v-if="imgExist" :href="imgDownLink">💾{{ board.ofile }}</a>
      </div>
    </div>
    <BoardComment :boardNo="boardNo" />
  </div>
</template>

<script>
import BoardComment from "./BoardComment.vue";
import { mapState } from "vuex";
import axios from "axios";
import { quillEditor } from "vue-quill-editor";
import hljs from "highlight.js";
// highlight.js style
import "highlight.js/styles/tomorrow.css";

// import theme style
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
const userStore = "userStore";

export default {
  name: "BoardDetail",
  components: { BoardComment, quillEditor },
  data() {
    return {
      editorOption: {
        placeholder: "place holder test",
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
      boardNo: this.$route.params.boardNo,
      originContent: "",
      isModify: false,
      isMore: false,
      imgExist: false,
      imgDownLink: "",
      board: {
        user: {
          sfolder: "noimg",
          sfile: "noimg.png",
          ofile: "noimg.png",
        },
      },
    };
  },
  created() {
    axios({
      method: "put",
      url: `${process.env.VUE_APP_BASE_URL}board/hit/${this.$route.params.boardNo}`,
    }).catch((err) => console.log(err));
    axios
      .get(`${process.env.VUE_APP_BASE_URL}board/detail/${this.$route.params.boardNo}`)
      .then(({ data }) => {
        this.board = data.board;
        if (this.board.sfolder) {
          this.imgExist = true;
          this.imgDownLink = `${process.env.VUE_APP_BASE_URL}file/download/${this.board.sfolder}/${this.board.ofile}/${this.board.sfile}`;
        }
      })
      .catch(() => {
        console.log("board created err");
      });
  },
  methods: {
    deleteBoard() {
      // axios로 서버에 삭제요청
      axios
        .delete(`${process.env.VUE_APP_BASE_URL}board/delete/${this.boardNo}`)
        .then(({ data }) => {
          console.log(data);
        })
        .catch(() => {
          console.log("delete error");
        });

      alert("삭제 완료");
      this.$router.push({ name: "board-list" });
    },
    togleModify() {
      this.originContent = this.board.content;
      this.isModify = !this.isModify;
    },
    togleMore() {
      this.isMore = !this.isMore;
    },
    modify() {
      this.originContent = this.board.content;

      if (this.isModify) {
        var today = new Date();

        var year = today.getFullYear();
        var month = ("0" + (today.getMonth() + 1)).slice(-2);
        var day = ("0" + today.getDate()).slice(-2);
        var hours = ("0" + today.getHours()).slice(-2);
        var minutes = ("0" + today.getMinutes()).slice(-2);
        var seconds = ("0" + today.getSeconds()).slice(-2);

        var dateString =
          year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;

        axios
          .put(`${process.env.VUE_APP_BASE_URL}board/update`, {
            ...this.board,
            modifiedTime: dateString,
          })
          .then(({ data }) => {
            console.log(data);
          })
          .catch(() => {
            console.log("서버에러");
          });
      }

      this.isModify = !this.isModify;
      //this.$emit("modifyComment", this.index);
    },
    cancel() {
      this.board.content = this.originContent;
      this.isModify = !this.isModify;
    },
  },
  computed: {
    ...mapState(userStore, ["isLogin", "userInfo"]),
    isUserComment: function () {
      return this.user.id == this.board.userNo;
    },
    boardImgSrc() {
      return this.board.sfolder
        ? `${process.env.VUE_APP_BASE_URL}file/download/${this.board.sfolder}/${this.board.ofile}/${this.board.sfile}`
        : "";
    },
    userImgSrc() {
      return `${process.env.VUE_APP_BASE_URL}file/download/${this.board.user.sfolder}/${this.board.user.ofile}/${this.board.user.sfile}`;
    },
  },
};
</script>

<style scoped>
#BoardDetail {
  box-sizing: border-box;
  /* border : 1px solid #00000024; */
  min-width: 30rem;
  box-shadow: 2px 3px 5px 0px rgba(0, 0, 0, 0.329);
  border-radius: 5px;
  padding: 5rem;
  margin: 2rem 25em;
}

#boardProfileWrap {
  display: flex;
  align-items: center;
  gap: 1rem;
  font-weight: 900;
  white-space: nowrap;
}

header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #00000021;
}

header > :last-child {
  text-align: right;
}

.boardContent {
  position: relative;
}

.profileImage {
  width: 3rem;
  height: 3rem;
  border-radius: 5rem;
  overflow: hidden;
  border: 1px solid rgba(0, 0, 0, 0.171);
  display: flex;
  justify-content: center;
}

.btn-more {
  position: relative;
  width: 1rem;
  right: 0px;
  display: inline-block;
}

.btn-more img {
  width: 1rem;
}

.btn-more > img {
  cursor: pointer;
}

.more-wrap {
  position: absolute;
  top: 0rem;
  left: 1rem;
  white-space: nowrap;
  background-color: white;
  border: 1px solid #7a7a7a88;
  border-radius: 4px;
  padding: 0.3rem;
}

.more-item {
  cursor: pointer;
}
button {
  color: #fff;
  background-color: #007bff;
  border: none;
  border-radius: 0.3rem;
  white-space: nowrap;
  cursor: pointer;
  margin: 0.5rem;
}
#modify-input {
  margin-top: 1rem;
  min-height: 25rem;
}

.content-img {
  max-width: 40vw;
}

.imgDown {
  width: 100%;
  height: 2rem;
}

.imgDown > a {
  color: blue;
}

.content-wrap {
  width: 100%;
  margin-top: 1rem;
}
.editor {
  font-size: 1.2rem;
}

.editor > :last-child > :first-child {
  min-height: 25rem;
}
.content-wrap > :first-child {
  min-height: 20rem;
}

.content-wrap > :first-child > :first-child {
  display: none;
}

.content-wrap > :first-child > :last-child {
  min-height: 20rem;
  border: 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.171);
}
</style>
