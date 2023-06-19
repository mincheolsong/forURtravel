<template>
  <header>
    <div id="TheHeader" @click="goHome">
      <img id="logo" height="50%" src="@/assets/img/Logo.svg" />
    </div>
    <nav style="margin-top: 1rem;">
      <router-link :to="{ path: '/attraction' }">관광지 검색</router-link>
      <router-link :to="{ path: '/board/list' }">게시판</router-link>
      <router-link :to="{ path: '/travel-review' }">여행후기</router-link>
      <router-link :to="{ path: '/travel-plan' }">여행계획</router-link>
      <div v-if="!isLogin" @click="openModal" class="cursor">로그인</div>
      <div class="cursor profile" v-if="isLogin" @click="mvProfile">
        <div style="display: flex">
          <p style="margin:0">🙋‍♂️{{ userInfo.userName }}</p>
          <p style="font-weight: 400; margin:0">님 반갑습니다</p>
        </div>
      </div>
      <div class="cursor" @click="userLogout" v-if="isLogin">로그아웃</div>
    </nav>
  </header>
</template>

<script>
import { mapActions, mapState } from "vuex";
const userStore = "userStore";
export default {
  name: "TheHeader",
  components: {},
  data() {
    return {
      userImg: "",
    };
  },
  created() {},
  methods: {
    ...mapActions(userStore, ["userLogout", "getUserInfo"]),
    openModal() {
      this.$emit("openModal");
    },
    mvProfile() {
      this.$router
        .push({
          name: "profile",
          params: { userId: this.userInfo.userId + "" },
        })
        .catch(() => {});
    },
    goHome() {
      this.$router.push({ name: "home" }).catch(() => {});
    },
  },
  watch: {
    isLogin() {
      // userInfo로 부터 userImgSrc
      this.userImg = this.userImgSrc;
    },
  },
  computed: {
    ...mapState(userStore, ["isLogin", "userInfo"]),
    userImgSrc() {
      if (this.userInfo != null) {
        return `${process.env.VUE_APP_BASE_URL}file/download/${this.userInfo.sfolder}/${this.userInfo.ofile}/${this.userInfo.sfile}`;
      }
      return "";
    },
  },
};
</script>

<style scoped>
header {
  background-color: #ffffff;
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  white-space: nowrap;

  padding: 1rem 25rem;
  box-shadow: 5px 5px 5px 5px #9595953d;
}
.profile {
  display: flex;
  justify-content: space-around;
  align-items: center;
}
#TheHeader {
  height: 100%;
  display: flex;
  gap: 1rem;
  box-sizing: border-box;
  margin: 5px 0;
}

#TheHeader > :last-child {
  display: flex;
  align-items: center;
  justify-content: center;
}

#logo {
  cursor: pointer;
}

header img {
  user-select: none;
  -moz-user-select: none;
  -webkit-user-drag: none;
  -webkit-user-select: none;
  -ms-user-select: none;
  width: 21rem;
}

header a {
  user-select: none;
  -moz-user-select: none;
  -webkit-user-drag: none;
  -webkit-user-select: none;
  -ms-user-select: none;
}

nav {
  display: flex;
  gap: 2rem;
  font-weight: 900;
  align-items: center;
  flex-wrap: wrap;
}
</style>
