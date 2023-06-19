import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

// module import
import userStore from "./modules/userStore";
import boardStore from "./modules/boardStore";
import travelReviewStore from "./modules/travelReviewStore";
import travelPlanStore from "./modules/travelPlanStore";
import dragStore from "./modules/dragStore";
Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    userStore,
    boardStore,
    travelReviewStore,
    travelPlanStore,
    dragStore
  },
  plugins: [
    createPersistedState({
      // 브라우저 종료시 제거하기 위해 localStorage가 아닌 sessionStorage로 변경. (default: localStorage)
      storage: sessionStorage,
    }),
  ],
});
