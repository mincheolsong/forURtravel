const travelReviewStore = {
  namespaced: true,
  state: {
    attractionNo: null,
  },
  getters: {},
  mutations: {
    SET_ATTRACTION_NO(state, payload) {
      state.attractionNo = payload;
    },
  },
  actions: {},
};

export default travelReviewStore;
