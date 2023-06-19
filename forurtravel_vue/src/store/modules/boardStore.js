const boardStore = {
  namespaced: true,
  state: {
    boardSearchData: {
      word: "",
      key: 0,
      curPage: 1,
      totalCount: 0,
      listCount: 10,
    },
  },
  getters: {},
  mutations: {
    SET_BOARD_SEARCH_DATA(state, payload) {
      state.boardSearchData = payload;
    },
  },
  actions: {},
};

export default boardStore;
