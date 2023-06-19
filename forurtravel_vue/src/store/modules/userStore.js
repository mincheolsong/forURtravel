import jwtDecode from "jwt-decode";
import router from "@/router";
import axios from "axios";

const userStore = {
  namespaced: true,
  state: {
    isLogin: false,
    isLoginError: false,
    userInfo: null,
    isValidToken: false,
    followings: [],
  },
  getters: {
    checkUserInfo: function (state) {
      return state.userInfo;
    },
    checkToken: function (state) {
      return state.isValidToken;
    },
  },
  mutations: {
    SET_IS_LOGIN: (state, isLogin) => {
      state.isLogin = isLogin;
    },
    SET_IS_LOGIN_ERROR: (state, isLoginError) => {
      state.isLoginError = isLoginError;
    },
    SET_IS_VALID_TOKEN: (state, isValidToken) => {
      state.isValidToken = isValidToken;
    },
    SET_USER_INFO: (state, userInfo) => {
      state.userInfo = userInfo;
    },
    SET_FOLLOWING: (state, following) => {
      state.followings = following;
    },
  },
  actions: {
    userConfirm({ commit, dispatch }, user) {
      axios({
        method: "post",
        url: `${process.env.VUE_APP_BASE_URL}user/login`,
        data: user,
      })
        .then(({ data }) => {
          if (data.code === "200") {
            let accessToken = data["access-token"];
            let refreshToken = data["refresh-token"];
            // console.log("login success token created!!!! >> ", accessToken, refreshToken);
            commit("SET_IS_LOGIN", true);
            commit("SET_IS_LOGIN_ERROR", false);
            commit("SET_IS_VALID_TOKEN", true);
            sessionStorage.setItem("access-token", accessToken);
            sessionStorage.setItem("refresh-token", refreshToken);
            dispatch("getUserInfo");
            alert("로그인 성공 :)");
            router.push({ name: "home" }).catch(() => {});
          } else {
            alert("로그인 실패! 비밀번호를 확인해주세요");
            commit("SET_IS_LOGIN", false);
            commit("SET_IS_LOGIN_ERROR", true);
            commit("SET_IS_VALID_TOKEN", false);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getUserInfo({ commit, dispatch }) {
      let decodeToken = jwtDecode(sessionStorage.getItem("access-token"));
      axios({
        method: "get",
        headers: {
          "access-token": sessionStorage.getItem("access-token"),
        },
        url: `${process.env.VUE_APP_BASE_URL}user/info/${decodeToken.user.userId}`,
      })
        .then(({ data }) => {
          if (data.code === "200") {
            commit("SET_USER_INFO", data.userInfo);
            dispatch("setFollowing");
          } else {
            console.log("getUserInfo() error code [토큰 만료되어 사용 불가능.] ::: ");
            commit("SET_IS_VALID_TOKEN", false);
            dispatch("tokenRegeneration");
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    setFollowing({ commit, state }) {
      axios
        .get(`${process.env.VUE_APP_BASE_URL}follow/following/${state.userInfo.userNo}`)
        .then(({ data }) => {
          commit("SET_FOLLOWING", data.following);
        })
        .catch(() => {
          console.log("following 가져오는 중 에러 발생");
        });
    },
    tokenRegeneration({ commit, state }) {
      console.log("토큰 재발급 >> 기존 토큰 정보 : {}", sessionStorage.getItem("access-token"));
      axios({
        method: "put",
        headers: {
          "refresh-token": sessionStorage.getItem("refresh-token"),
        },
        url: `${process.env.VUE_APP_BASE_URL}user/refresh`,
        data: JSON.stringify(state.userInfo),
      })
        .then(({ data }) => {
          if (data.code === "200") {
            let accessToken = data["access-token"];
            console.log("재발급 완료 >> 새로운 토큰 : {}", accessToken);
            sessionStorage.setItem("access-token", accessToken);
            commit("SET_IS_VALID_TOKEN", true);
          } else {
            axios({
              method: "put",
              url: `${process.env.VUE_APP_BASE_URL}user/logout/${state.userInfo.userNo}`,
            })
              .then(({ data }) => {
                if (data.code === "200") {
                  console.log("리프레시 토큰 제거 성공");
                } else {
                  console.log("리프레시 토큰 제거 실패");
                }
                alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.");
                commit("SET_IS_LOGIN", false);
                commit("SET_USER_INFO", null);
                commit("SET_IS_VALID_TOKEN", false);
                router.push({ name: "login" });
              })
              .catch((err) => {
                console.log(err);
                commit("SET_IS_LOGIN", false);
                commit("SET_USER_INFO", null);
              });
          }
        })
        .catch((err) => {
          console.log(err);
          commit("SET_IS_LOGIN", false);
          commit("SET_USER_INFO", null);
        });
    },
    userLogout({ commit, dispatch }) {
      axios({
        method: "put",
        url: `${process.env.VUE_APP_BASE_URL}user/logout`,
        headers: {
          "access-token": sessionStorage.getItem("access-token"),
        },
      })
        .then(({ data }) => {
          if (data.code === "200") {
            commit("SET_IS_LOGIN", false);
            commit("SET_USER_INFO", null);
            commit("SET_IS_VALID_TOKEN", false);
            sessionStorage.removeItem("access-token");
            sessionStorage.removeItem("refresh-token");

            router.push({ name: "home" }).catch(() => {});
          } else if (data.code === "401") {
            dispatch("tokenRegeneration");
            axios({
              method: "put",
              url: `${process.env.VUE_APP_BASE_URL}user/logout`,
              headers: {
                "access-token": sessionStorage.getItem("access-token"),
              },
            })
              .then(({ data }) => {
                if (data.code === "200") {
                  commit("SET_IS_LOGIN", false);
                  commit("SET_USER_INFO", null);
                  commit("SET_IS_VALID_TOKEN", false);
                  sessionStorage.removeItem("access-token");
                  sessionStorage.removeItem("refresh-token");

                  console.log(data);
                  router.push({ name: "home" }).catch(() => {});
                } else {
                  console.log("재발행 후 로그아웃 실패!");
                }
              })
              .catch((err) => console.log(err));
          }
        })
        .catch((err) => console.log(err));
    },
  },
};

export default userStore;
