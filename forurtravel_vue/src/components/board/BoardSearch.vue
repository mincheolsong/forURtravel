<template>
  <div id="BoardSearch">
    <select v-model="key">
      <option :value="0">제목</option>
      <option :value="1">내용</option>
      <option :value="2">제목+내용</option>
    </select>
    <TheInput v-model="word" />
    <button @click="searchBoard">
      <img src="@/assets/img/search.svg" alt="">
      </button>
  </div>
</template>

<script>
import { mapMutations, mapState } from "vuex";
import TheInput from "../common/TheInput.vue";
const boardStore = "boardStore";
export default {
  name: 'BoardSearch',
  components: {
    TheInput
  },
  data() {
    return {

    };
  },
  created() { },
  computed: {
    ...mapState(boardStore, ["boardSearchData"]),
    key: {
      get() {
        return this.boardSearchData.key;
      },
      set(value) {
        this.SET_BOARD_SEARCH_DATA({ ...this.boardSearchData, key: value });
      }
    },
    word: {
      get() {
        return this.boardSearchData.word;
      },
      set(value) {
        this.SET_BOARD_SEARCH_DATA({ ...this.boardSearchData, word: value });
      }
    }
  },
  methods: {
    ...mapMutations(boardStore, ["SET_BOARD_SEARCH_DATA"]),
    searchBoard() {
      this.$emit("getBoards");
    }
  },
};
</script>

<style scoped>
#BoardSearch {
  display: flex;
  white-space: nowrap;
  height: 2rem;
  box-sizing: border-box;
  width: 30rem;
}

select {
  border : none;
}

button {
  border:none;
  background-color: #ffff;
  cursor: pointer;
}
button > img{
  width : 2rem;
  
}
</style>