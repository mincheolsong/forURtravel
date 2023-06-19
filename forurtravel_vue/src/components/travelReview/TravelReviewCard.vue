<template>
  <div class="TravelReviewCard" @dragstart="dragStart" draggable="true">
    <div style="width:99%">
      <div>
        <div :style="{ backgroundImage: `url('${imgSrc}')` }"></div>
        <div>
          <div class="title">{{ attraction.title }}</div>
          <div class="addr">{{ attraction.addr }}</div>
          <textarea :value="attraction.description"  disabled></textarea>
        </div>
      </div>
      <div class="comment-wrap" v-if="isOpen">
        <div id="comment-list">
          <TravelReviewCardItem :review="item" v-for="(item, index) in commentList" :key="index" />
        </div>
        <div class="travel-review-open-btn-wrap cursor" v-if="isOpen" @click="toggleOpen">
          <img height="100%" src="../../assets/img/close.svg" />
        </div>
      </div>
    </div>
    <div class="travel-review-open-btn-wrap cursor" v-if="!isOpen" @click="toggleOpen">
      <img height="100%" src="../../assets/img/open.svg" />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import TravelReviewCardItem from "./TravelReviewCardItem.vue";
import { mapState, mapMutations } from "vuex";
export default {
  name: "TravelReviewCard",
  components: { TravelReviewCardItem },
  data() {
    return {
      commentList: [],
      isOpen: false,
      start: null,
      imgSrc: this.attraction.image ? this.attraction.image : `${process.env.VUE_APP_BASE_URL}file/download/noimg/noimg.png/noimg.png`
    };
  },
  props: { attraction: Object },
  created() { },
  methods: {
    ...mapMutations("travelReviewStore", ["SET_ATTRACTION_NO"]),
    ...mapMutations("dragStore", ["SET_START"]),
    toggleOpen() {
      if (!this.isOpen && this.commentList.length === 0) {
        axios({
          method: "get",
          url: `${process.env.VUE_APP_BASE_URL}travel-review/list-attraction/${this.attraction.contentId}`,
        })
          .then(({ data }) => {
            this.commentList = data.travelReview;
            this.commentList.forEach((e, idx) => {
              this.commentList[idx]["startDay"] = e["startDay"].split(" ")[0];
              this.commentList[idx]["endDay"] = e["endDay"].split(" ")[0];
            });
          })
          .catch((err) => {
            console.log(err);
          });
      }
      this.isOpen = !this.isOpen;
    },
    dragStart() {
      this.SET_START({ start: { ...this.attraction } });
    }
  },
  computed: {
    ...mapState("travelReviewStore", ["attractionNo"]),
    isReset() {
      if (!this.attractionNo) {
        return false;
      }
      return this.attraction.contentId === this.attractionNo.attractionNo;
    },
  },
  watch: {
    isReset(val) {
      if (val) {
        axios({
          method: "get",
          url: `${process.env.VUE_APP_BASE_URL}travel-review/list-attraction/${this.attraction.contentId}`,
        })
          .then(({ data }) => {
            this.commentList = data.travelReview;
            this.commentList.forEach((e, idx) => {
              this.commentList[idx]["startDay"] = e["startDay"].split(" ")[0];
              this.commentList[idx]["endDay"] = e["endDay"].split(" ")[0];
            });
          })
          .catch((err) => {
            console.log(err);
          });
        this.SET_ATTRACTION_NO({ attractionNo: null });
      }
    },
  },
};
</script>

<style scoped>
.TravelReviewCard {
  box-sizing: border-box;
  flex: 0 0 auto;
  width: 100%;
  min-height: 10rem;
  display: flex;
  flex-direction: column;
}

.TravelReviewCard> :first-child {
  border-radius: 1rem;
  background-color: #fff;
  box-shadow: 2px 3px 5px 0px rgba(0, 0, 0, 0.329);
}

.TravelReviewCard> :first-child> :first-child {
  display: flex;
  padding: 1rem;
  font-weight: 900;
}

.TravelReviewCard> :first-child> :first-child> :first-child {
  width: 50%;
  height: 10rem;
  background-size: cover;
  background-color: #8f8f8f6c;
}

.TravelReviewCard> :first-child> :first-child> :nth-child(2) {
  width: 50%;
  height: 10rem;
  text-align: center;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0);
}

.title {
  padding: 0.25rem 2rem;
  width: 100%;
  display: flex;
  flex: 0 0 auto;
  white-space: nowrap;
  transition: 0.3s;
  font-size: 1rem;
  font-weight: bold;
  color: #000;
}

.addr {
  padding: 0.25rem 2rem;
  width: 100%;
  display: flex;
  flex: 0 0 auto;
  white-space: nowrap;
  transition: 0.3s;
  font-size: 1rem;
  font-weight: bold;
  color: #000;
}

.title:hover {
  animation: textLoop 10s linear infinite;
}

.addr:hover {
  animation: textLoop 10s linear infinite;
}

@keyframes textLoop {
  0% {
    -webkit-transform: translate3d(0, 0, 0);
    transform: translate3d(0, 0, 0);
  }

  100% {
    -webkit-transform: translate3d(-100%, 0, 0);
    transform: translate3d(-100%, 0, 0);
  }
}

textarea {
  height: 100%;
  border: 0;
  background-color: transparent;
  font-size: 1rem;
  -ms-overflow-style: none;
  /* IE and Edge */
  scrollbar-width: none;
  /* Firefox */
  
}

textarea::-webkit-scrollbar {
  display: none;
}

.travel-review-open-btn-wrap {
  margin: 0.5rem 0;
  width: 100%;
  height: 1rem;
  text-align: center;
}

#comment-list {
  display: flex;
  flex-direction: column;
  flex-wrap: nowrap;
  overflow-x: auto;
  -ms-overflow-style: none;
  /* IE and Edge */
  scrollbar-width: none;
  /* Firefox */
}

#comment-list::-webkit-scrollbar {
  display: none;
}
</style>