<template>
  <div id="LoginTest">
    <button @click="confirm">로그인</button>
    <button @click="getUserInfo">유저정보</button>
    <button @click="userLogout">로그아웃</button>
    <button @click="test">테스트</button>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
const userStore = "userStore";
export default {
  name: "LoginTest",
  components: {},
  data() {
    return {
      testData: {
        planlist: {
          user_no: 1,
          title: "test",
          image: "test.png",
          content: "test1",
          plan: [
            {
              money: 0,
              planDate: "2023-04-14 16:30:53",
              planAttraction: [
                {
                  attractionNo: 125266,
                  planDateTime: "2023-04-14 16:30:53",
                },
                {
                  attractionNo: 125266,
                  planDateTime: "2023-04-14 16:30:53",
                },
              ],
            },
            {
              money: 0,
              planDate: "2023-04-14 16:30:53",
              planAttraction: [
                {
                  attractionNo: 125266,
                  planDateTime: "2023-04-14 16:30:53",
                },
                {
                  attractionNo: 125266,
                  planDateTime: "2023-04-14 16:30:53",
                },
              ],
            },
          ],
        },
      },
    };
  },
  computed: {
    ...mapState(userStore, ["isLogin", "isLoginError", "userInfo"]),
  },
  methods: {
    test() {
      axios({
        method: "post",
        headers: {
          "access-token": sessionStorage.getItem("access-token"),
        },
        url: `${process.env.VUE_APP_BASE_URL}travel-plan/write`,
        data: this.testData,
      })
        .then(({ data }) => console.log(data))
        .catch((err) => console.log(err));
    },
    ...mapActions(userStore, ["userConfirm", "getUserInfo", "userLogout"]),
    confirm() {
      this.userConfirm(this.user);
      let token = sessionStorage.getItem("access-token");
      // console.log("1. confirm() token >> " + token);
      if (this.isLogin) {
        this.getUserInfo(token);
        // console.log("4. confirm() userInfo :: ", this.userInfo);
        // this.$router.push({ name: "main" });
      }
    },
  },
};
</script>

<style scoped>
#LoginTest {
  padding-top: 100px;
  min-height: 100vh;
}
</style>