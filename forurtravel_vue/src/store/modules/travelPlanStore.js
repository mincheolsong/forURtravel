const travelPlanStore = {
  namespaced: true,
  state: {
    imageIdx: 0,
    day: {
      startDay: "",
      endDay: "",
    },
    planlist: {
      title: "",
      image: "",
      content: "여행에 필요한 글,메모를 적어주세요",
      plan: [],
    },
    initPlan: {
      money: 0,
      planDate: "",
      planAttraction: [],
    },
    initPlanlist: {
      title: "",
      image: "",
      content: "여행에 필요한 글,메모를 적어주세요",
      plan: [
        {
          money: 0,
          planDate: "",
          planAttraction: [],
        },
      ],
    },
  },
  getters: {
    totalMoney(state) {
      return state.planlist.plan.reduce((acc, cur) => acc + Number(cur.money), 0);
    },
    attractionList(state) {
      const temp = [];
      state.planlist.plan.forEach((e) => {
        e.planAttraction.forEach((e2) => {
          temp.push(e2);
        });
      });
      return temp;
    },
  },
  mutations: {
    SET_PLANLIST(state, payload) {
      state.planlist = payload;
    },
    SET_DAY(state, payload) {
      state.day = payload;
    },
    SET_IMAGE_IDX(state, payload) {
      state.imageIdx = payload;
    },
  },
  actions: {},
};

export default travelPlanStore;
