<template>
  <div id="BoardComment">
    <h3>댓글 수 : {{ !comments ? 0 : comments.length }}</h3>
    <div id="commentInputWrap">
      <div class="commentImage">
        <img :src="userImgSrc" alt="" style="width: 2rem; height: 2rem" />
      </div>
      <TheInput :placeHolder="'댓글을 입력해 주세요'" v-model="newComments.content" />
      <button @click="registComment">등록</button>
    </div>
    <board-comment-item
      :comment="comment"
      :index="index"
      v-for="(comment, index) in comments"
      :key="index"
      @modifyComment="modifyComment"
      @deleteComment="deleteComment"
    ></board-comment-item>
  </div>
</template>

<script>
import TheInput from "../common/TheInput.vue";
import { mapState } from "vuex";
import BoardCommentItem from "./BoardCommentItem.vue";
import axios from "axios";
const userStore = "userStore";
export default {
  name: "BoardComment",
  components: { TheInput, BoardCommentItem },
  props: {
    boardNo: String,
  },
  data() {
    return {
      newComments: {
        boardNo: this.boardNo,
        content: "",
        userNo: null,
        user: null,
      },
      comments: [],
      profileInfo: {},
    };
  },
  created() {
    // axios로 boardId에 해당하는 comment를 가져와서 data에 넣어주기
    this.getComments();
  },
  methods: {
    getComments() {
      axios
        .get(`${process.env.VUE_APP_BASE_URL}boardcomment/list/${this.boardNo}`)
        .then(({ data }) => {
          if (data.code === "200") {
            this.comments = data.list;
          }
        })
        .catch(() => {
          console.log("comment일어오는 중 error 발생");
        });
    },
    modifyComment(param) {
      axios
        .put(`${process.env.VUE_APP_BASE_URL}boardcomment/update`, param)
        .then(({ data }) => {
          console.log(data);
          alert("댓글 수정 성공!");
          this.getComments();
        })
        .catch(() => {
          console.log("댓글 수정 중 error 발생");
        });
    },
    deleteComment(param) {
      console.log(param);
      axios
        .delete(`${process.env.VUE_APP_BASE_URL}boardcomment/delete/${param}`)
        .then(({ data }) => {
          alert("댓글 삭제 성공!");
          this.getComments();
          console.log(data);
        })
        .catch(() => {
          console.log("댓글 삭제중 error 발생");
        });
    },
    registComment() {
      if (!this.isLogin) {
        alert("로그인을 해주세요");
        return;
      } else {
        this.newComments.userNo = this.userInfo.userNo;
      }

      axios
        .post(`${process.env.VUE_APP_BASE_URL}boardcomment/write`, this.newComments)
        .then(({ data }) => {
          console.log(data);
          alert("댓글 등록성공!");
          this.newComments.content = "";
          this.getComments();
        })
        .catch(() => {});
    },
  },
  computed: {
    ...mapState(userStore, ["isLogin", "userInfo", "followings"]),
    userImgSrc() {
      return this.userInfo
        ? `${process.env.VUE_APP_BASE_URL}file/download/${this.userInfo.sfolder}/${this.userInfo.ofile}/${this.userInfo.sfile}`
        : "";
    },
  },
};
</script>

<style scoped>
#BoardComment {
  box-sizing: border-box;
}

#commentInputWrap {
  display: flex;
  margin-bottom: 15px;
  gap: 1rem;
  justify-content: space-between;
}

.commentImage {
  width: 2rem;
  height: 2rem;
  background-color: #e8e8e8;
  border-radius: 2rem;
  overflow: hidden;
}

.commentImage img {
  object-fit: cover;
}

input {
  border: 0;
  border-bottom: 1px solid #00000024;
}

button {
  color: #fff;
  background-color: #007bff;
  border: none;
  border-radius: 0.3rem;
  white-space: nowrap;
  cursor: pointer;
}
</style>
