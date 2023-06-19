<template id="TheBoard">
  <div id="BoardList">
    <div>
      <div>
        <select v-model="boardType">
          <option :value="'1'" selected>Q&A</option>
          <option :value="'2'">공지사항</option>
        </select>
      </div>
      <BoardSearch @getBoards="getBoards" />
      <select v-model="listCount">
        <option :value="10">10개</option>
        <option :value="15">15개</option>
        <option :value="30">30개</option>
      </select>
    </div>
    <div v-for="board in boards" :key="board.boardNo" @click="mvDetail(board.boardNo)" class="board-top">
      <div class="board-left">
        <div class="subject">{{ board.subject }}</div>
      </div>

      <div class="board-right">
        <div class="writer">작성자 : {{ board.user.userName }}</div>
        <div>{{ board.modifiedTime !== null ? board.modifiedTime : board.createdTime }}</div>
      </div>
    </div>
    <BoardListNavigationVue @update="getBoards" />
    <div @click="mvWrite" class="write cursor" v-if="boardType==='1' || (boardType==='2' && userInfo.authority==='master')">
      <img src="@/assets/img/plus-btn.svg" alt="" />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import BoardListNavigationVue from "./BoardListNavigation.vue";
import BoardSearch from "./BoardSearch.vue";
import { mapMutations, mapState } from "vuex";
const boardStore = "boardStore";

export default {
  name: "BoardList",
  components: { BoardListNavigationVue, BoardSearch },
  data() {
    return {
      boardType: "1", // 1번 Q&A, 2번 공지사항
      boards: [],
    };
  },

  created() {
    this.getBoards();
  },

  methods: {
    ...mapMutations(boardStore, ["SET_BOARD_SEARCH_DATA"]),
    mvDetail(boardNo) {
      this.$router.push({ name: "board-detail", params: { boardNo: boardNo + "" } });
    },
    mvWrite() {
      this.$router.push({ name: "board-write", params: { boardType: this.boardType } });
    },
    getBoards() {
      const wordData = this.boardSearchData.word === "" ? "*" : this.boardSearchData.word;

      axios
        .get(
          `${process.env.VUE_APP_BASE_URL}board/list/${this.boardType}/${this.boardSearchData.key}/${wordData}/${this.boardSearchData.listCount}/${this.curIndex}`
        )
        .then(({ data }) => {
          if (data.code === "200") {
            this.boards = data.list;
          } else {
            this.boards = [];
          }
        })
        .catch(() => console.log("서버에러"));

      axios
        .get(
          `${process.env.VUE_APP_BASE_URL}board/total-count/${this.boardType}/${this.boardSearchData.key}/${wordData}` // totalCount 추가하기
        )
        .then(({ data }) => {
          if (data.code === "200") {
            this.SET_BOARD_SEARCH_DATA({ ...this.boardSearchData, totalCount: data.totalCount });
          } else {
            this.SET_BOARD_SEARCH_DATA({ ...this.boardSearchData, totalCount: 0 });
          }
        })
        .catch(() => console.log("서버에러"));
    },
  },
  computed: {
    ...mapState(boardStore, ["boardSearchData"]),
    ...mapState("userStore",["userInfo"]),

    listCount: {
      get() {
        return this.boardSearchData.listCount;
      },
      set(value) {
        this.SET_BOARD_SEARCH_DATA({ ...this.boardSearchData, listCount: value });
      },
    },
    curIndex: {
      get() {
        return (this.boardSearchData.curPage - 1) * this.listCount;
      },
    },
  },

  watch: {
    boardType() {
      const wordData = this.boardSearchData.word === "" ? "*" : this.boardSearchData.word;
      axios
        .get(
          `${process.env.VUE_APP_BASE_URL}board/list/${this.boardType}/${this.boardSearchData.key}/${wordData}/${this.boardSearchData.listCount}/${this.curIndex}`
        )
        .then(({ data }) => {
          if (data.code === "200") {
            this.boards = data.list;
          } else {
            this.boards = [];
          }
        })
        .catch(() => console.log("서버에러"));
    },
  },
};
</script>

<style scoped>
@keyframes hover {
  50% {
    transform: translateY(-3px);
  }

  100% {
    transform: translateY(-6px);
  }
}

#BoardList {
  padding: 5rem 25rem;
  white-space: nowrap;
  height: 100%;
  min-width: 44rem;
}

#BoardList> :first-child {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

select {
  box-sizing: border-box;
  height: 1.5rem;
  border: 1px solid #d1d1d1;
  border-radius: 5px;
  margin: 1rem 0;
}

.board-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: none;
  border-radius: 15px;
  cursor: pointer;
  height: 9rem;
  margin-bottom: 10px;
  padding: 5px;
  box-shadow: 2px 3px 5px 0px rgba(0, 0, 0, 0.329);
}

.board-top:hover {
  transform: translateY(-1px);
  animation-name: hover;
  animation-duration: 0.5s;
  animation-timing-function: linear;
  animation-iteration-count: infinite;
  animation-direction: alternate;
}

.write:hover {
  transform: translateY(-1px);
  animation-name: hover;
  animation-duration: 0.5s;
  animation-timing-function: linear;
  animation-iteration-count: infinite;
  animation-direction: alternate;
}

.board-left {
  display: flex;
  flex-direction: column;
  height: 100%;
  justify-content: space-around;
  padding-left: 2rem;
}

.board-right {
  display: flex;
  flex-direction: column;
  height: 100%;
  justify-content: space-around;
  padding-right: 2rem;
}

.subject {
  font-size: 1rem;
}

.writer {}

.content {
  color: #00000094;
}

.write {
  position: fixed;
  bottom: 100px;
  right: 200px;
}

.write>img {
  width: 3rem;
}

table {
  width: 100%;
  box-sizing: border-box;
  text-align: center;
  margin-top: 2rem;
}

tr {
  margin: 2rem 0;
}
</style>
