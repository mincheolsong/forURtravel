<template>
  <div id="BoardCommentItem">
    <div class="commentImage"><img :src="userImgSrc" alt="" style="width: 2rem; height: 2rem; object-fit:cover"/></div>
    <div class="commentWrap">
      <div class="commentContent">
        <span>작성자 : {{ comment.user.userName }}</span>
        <span v-if="comment.modified != null">{{ comment.modifiedTime }} (수정)</span>
        <span v-else>{{ comment.createdTime }}</span>
      </div>
      <div class="contentwrap">
        <div v-if="isModify" class="modify-input-wrap">
          <the-input
            v-model="modifyContent"
            :placeHolder="'수정할 내용을 입력해 주세요'"
          ></the-input
          ><button class="btn-modify" @click="modify">수정하기</button>
        </div>
        <div v-else class="content-item">{{ comment.content }}</div>
        <div v-if="isUserComment" class="btn-more" @click="togleMore">
          <img src="@/assets/img/more.svg" />
          <div v-if="isMore" class="more-wrap">
            <div class="more-item" @click="togleModify">
              <img width="1rem" src="@/assets/img/edit.svg" />수정하기
            </div>
            <div class="more-item" @click="deleteComment">
              <img width="1rem" src="@/assets/img/delete.svg" />삭제하기
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import TheInput from "../common/TheInput.vue";


const userStore = "userStore";
export default {
  name: "BoardCommentItem",
  components: { TheInput },
  props: {
    comment: Object,
    index: Number,
  },
  data() {
    return {
      modifyContent: "",
      isModify: false,
      isMore: false,
    };
  },
  created() {
    this.modifyContent = this.comment.content;
  },
  methods: {
    togleModify() {
      this.isModify = !this.isModify;
    },
    togleMore() {
      this.isMore = !this.isMore;
    },
    modify() {
      this.$emit("modifyComment", { ...this.comment, content: this.modifyContent });
      this.togleModify();
    },
    deleteComment() {
      this.$emit("deleteComment", this.comment.commentNo);
    },
  },
  computed: {
    ...mapState(userStore, ["userInfo"]),
    isUserComment: function () {
      return this.isLogin && this.userInfo.userNo == this.comment.user.userNo;
    },
    userImgSrc() {
      return `${process.env.VUE_APP_BASE_URL}file/download/${this.comment.user.sfolder}/${this.comment.user.ofile}/${this.comment.user.sfile}`;
    },
  },
};
</script>

<style scoped>
#BoardCommentItem {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
  align-items: center;
}

.commentContent {
  display: flex;
  justify-content: space-between;
}

.commentImage {
  width: 2rem;
  height: 2rem;
  background-color: #e8e8e8;
  border-radius: 2rem;
  overflow: hidden;
}

.commentWrap {
  width: 100%;
}

.contentwrap {
  width: 100%;
  display: flex;
}

.content-item {
  width: 100%;
}

input {
  width: 100%;
  border: 0;
  border-bottom: 1px solid #83adff;
}

.modify-input-wrap {
  display: flex;
  white-space: nowrap;
  width: 100%;
}

.btn-more {
  position: relative;
  width: 1rem;
}

.btn-more img {
  width: 1rem;
}

.btn-more > img {
  cursor: pointer;
}

.more-wrap {
  position: absolute;
  top: 0;
  left: 1rem;
  white-space: nowrap;
}

.more-item {
  cursor: pointer;
}
</style>
