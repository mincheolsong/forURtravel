<template>
    <div id="BoardListNavigation" class="notDrag">
        <div class="cursor" @click="mvFirst">처음</div>
        <div class="cursor" @click="mvBefore">이전</div>
        <div :class="[{ disablePage: getActive(startPage + index) }, { cursor: true }]"
            v-for="(_, index) in (endPage - startPage + 1)" :key="index" @click="mvPage(startPage + index)">
            {{ startPage + index }}
        </div>
        <div class="cursor" @click="mvNext">다음</div>
        <div class="cursor" @click="mvEnd">끝</div>
    </div>
</template>

<script>
import { mapMutations, mapState } from "vuex";
const boardStore = "boardStore";
export default {
    name: 'BoardListNavigation',
    components: {},
    data() {
        return {
            pageCount: 5,
        };
    },
    computed: {
        ...mapState(boardStore, ["boardSearchData"]),
        startPage: {
            get() {
                return Math.floor((this.boardSearchData.curPage - 1) / this.pageCount) * this.pageCount + 1;
            }
        },
        endPage: {
            get() {
                let temp = this.startPage + this.pageCount - 1;
                if (temp > this.totalPage) {
                    temp = this.totalPage;
                }
                return temp;
            }
        },
        totalPage: {
            get() {
                return this.boardSearchData.totalCount % this.boardSearchData.listCount > 0 ? Math.floor(this.boardSearchData.totalCount / this.boardSearchData.listCount) + 1 : Math.floor(this.boardSearchData.totalCount / this.boardSearchData.listCount);
            }
        },
        curPage: {
            get() {
                if (this.totalPage < this.boardSearchData.curPage) {
                    this.SET_BOARD_SEARCH_DATA({ ...this.boardSearchData, curPage: this.totalPage });
                    return this.totalPage;
                }
                return this.boardSearchData.curPage;
            },
            set(value) {
                this.SET_BOARD_SEARCH_DATA({ ...this.boardSearchData, curPage: value });
            }
        },
    },
    created() { },
    methods: {
        ...mapMutations(boardStore, ["SET_BOARD_SEARCH_DATA"]),
        mvFirst() {
            this.SET_BOARD_SEARCH_DATA({ ...this.boardSearchData, curPage: 1 });
        },
        mvBefore() {
            if (this.startPage >= 2) {
                this.SET_BOARD_SEARCH_DATA({ ...this.boardSearchData, curPage: this.startPage - 1 });
            }
        },
        mvPage(value) {
            this.SET_BOARD_SEARCH_DATA({ ...this.boardSearchData, curPage: value });
            this.$emit('update');
        },
        mvNext() {
            if (this.endPage * this.boardSearchData.listCount < this.boardSearchData.totalCount) {
                this.SET_BOARD_SEARCH_DATA({ ...this.boardSearchData, curPage: this.endPage + 1 });
            }
        },
        mvEnd() {
            this.SET_BOARD_SEARCH_DATA({ ...this.boardSearchData, curPage: this.totalPage });
        },
        getActive(value) {
            return value !== this.boardSearchData.curPage;
        }
    },
};
</script>

<style scoped>
#BoardListNavigation {
    margin-top: 5rem;
    width: 100%;
    display: flex;
    font-weight: 900;
    gap: 1rem;
    justify-content: center;
}

.disablePage {
    color: #808080;
}
</style>