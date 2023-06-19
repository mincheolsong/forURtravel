import Vue from "vue";
import VueRouter from "vue-router";
import TheHome from "@/views/TheHome";
import store from "@/store";
Vue.use(VueRouter);
const onlyAuthUser = async (to, from, next) => {
  const checkUserInfo = store.getters["userStore/checkUserInfo"];
  const checkToken = store.getters["userStore/checkToken"];
  let token = sessionStorage.getItem("access-token");
  // console.log("로그인 처리 전", checkUserInfo, token);

  if (checkUserInfo != null && token) {
    // console.log("토큰 유효성 체크하러 가자!!!!");
    await store.dispatch("userStore/getUserInfo", token);
  }
  if (!checkToken || checkUserInfo === null) {
    alert("로그인이 필요합니다.");
    // next({ name: "login" });
    // router.push({ name: "home" });
  } else {
    // console.log("로그인 했다!!!!!!!!!!!!!.");
    next();
  }
};

const routes = [
  {
    path: "/",
    name: "home",
    component: TheHome,
  },
  {
    path: "/travel-plan",
    name: "travelPlan",
    beforeEnter: onlyAuthUser,
    component: () => import(/* webpackChunkName: "travel-plan" */ "@/views/TravelPlan.vue"),
  },
  {
    path: "/travel-review",
    name: "travelReview",
    component: () => import(/* webpackChunkName: "travel-review" */ "@/views/TravelReview.vue"),
  },
  {
    path: "/test",
    name: "test",
    component: () => import(/* webpackChunkName: "test" */ "@/components/user/LoginTest.vue"),
  },

  {
    path: "/user",
    name: "user",
    component: () => import(/* webpackChunkName: "user" */ "@/views/TheUser.vue"),
    children: [
      {
        path: "login",
        name: "login",
        component: () => import(/* webpackChunkName: "login" */ "@/components/user/TheLogin.vue"),
      },
      {
        path: "profile/:userId",
        name: "profile",
        component: () =>
          import(/* webpackChunkName: "profile" */ "@/components/user/TheProfile.vue"),
      },
      {
        path: "join",
        name: "join",
        component: () => import(/* webpackChunkName: "join" */ "@/components/user/TheJoin.vue"),
      },
      {
        path: "setting",
        name: "setting",
        component: () => import(/* webpackChunkName: "user" */ "@/components/user/TheSetting.vue"),
      },
    ],
  },
  {
    path: "/board",
    name: "board",
    component: () => import(/* webpackChunkName: "board" */ "@/views/TheBoard.vue"),
    children: [
      {
        path: "list",
        name: "board-list",
        component: () =>
          import(/* webpackChunkName: "board-list" */ "@/components/board/BoardList.vue"),
      },
      {
        path: "detail/:boardNo",
        name: "board-detail",
        component: () =>
          import(/* webpackChunkName: "board-detail" */ "@/components/board/BoardDetail.vue"),
      },
      {
        path: "modify",
        name: "board-modify",
        beforeEnter: onlyAuthUser,
        component: () =>
          import(/* webpackChunkName: "board-modify" */ "@/components/board/BoardModify.vue"),
      },
      {
        path: "write/:boardType",
        name: "board-write",
        beforeEnter: onlyAuthUser,
        component: () =>
          import(/* webpackChunkName: "board-write" */ "@/components/board/BoardWrite.vue"),
      },
    ],
  },
  {
    path: "/attraction",
    name: "attraction",
    component: () => import(/* webpackChunkName: "user" */ "@/views/TheAttraction.vue"),
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
