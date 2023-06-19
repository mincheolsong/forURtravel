<template>
  <div class="follower-item" @click="mvProfile">
    <div class="img-header">
      <img :src="userImgSrc" id="profile-img" v-if="user.sfile != null" />
    </div>
    <div style="display:flex; flex-direction : column; justify-content : center; align-items : center;">
      <h3 style="margin : 0;">{{ user.userName }}</h3>
      {{user.userId}}
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "TravelReviewItem",
  components: {},
  props: {
    followerNo: Number,
  },
  data() {
    return {
      sfollower: this.followerNo,
      user: {},
    };
  },
  created() {
    // this.imgSrc = `${process.env.VUE_APP_BASE_URL}file/download/${this.review.user.sfolder}/${this.review.user.ofile}/${this.review.user.sfile}`;
    axios.get(`${process.env.VUE_APP_BASE_URL}user/info/no/${this.sfollower}`).then(({ data }) => {
      this.user = data.userInfo;
    });
  },
  methods: {
    mvProfile(){
      this.$router
        .push({
          name: "profile",
          params: { userId: this.user.userId + "" },
        })
        .catch(() => { });

      this.$emit("closeModal");
    },
  },
  computed: {
    userImgSrc() {
      return `${process.env.VUE_APP_BASE_URL}file/download/${this.user.sfolder}/${this.user.ofile}/${this.user.sfile}`;
    },
  },
};
</script>

<style scoped>
.follower-item {
  border: 1px solid rgba(153, 153, 153, 0.075);
  border-radius: 5px;
  display: flex;
  align-items: center;
}
.img-header {
  overflow: hidden;
  border: 1px solid #0000001f;
  border-radius: 50%;
  width: 5rem;
  height: 5rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-right : 1rem;
}
</style>
