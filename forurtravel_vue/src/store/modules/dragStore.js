const dragStore = {
  namespaced: true,
  state: {
    start: null,
  },
  getters: {},
  mutations: {
    SET_START(state, payload) {
      state.start = payload;
    },
  },
  actions: {},
};

export default dragStore;
