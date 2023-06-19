<template>
  <div class="travelReviewItem">
    <div>
      <h2>{{ review.title }}</h2>
      <div class="review-img" v-if="review" :style="{ backgroundImage: `url('${review.attraction.image}')` }"></div>
      <div class="review-header">
        <div>장소 : {{ review.attraction.title }}</div>
        <div>기간 : {{ review.startDay.slice(0, 10) }}~{{ review.endDay.slice(0, 10) }}</div>
        <hr />
        <div>{{ review.review }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "TravelReviewItem",
  components: {},
  props: {
    selectedReview: Object,
  },
  data() {
    return {
      review: this.selectedReview,
    };
  },
  created() {
    this.review.attraction.image = this.review.attraction.image || this.review.attraction.image !== '' ? this.review.attraction.image : `${process.env.VUE_APP_BASE_URL}file/download/noimg/noimg.png/noimg.png`;
    this.imgSrc = this.review.user.sfolder || this.review.user.sfolder !== '' ? `${process.env.VUE_APP_BASE_URL}file/download/${this.review.user.sfolder}/${this.review.user.ofile}/${this.review.user.sfile}` : `${process.env.VUE_APP_BASE_URL}file/download/noimg/noimg.png/noimg.png`;
  },
  methods: {},
};
</script>

<style scoped>
.review-header {
  width: 100%;
}

textarea {
  width: 90%;
  height: 3rem;
  resize: none;
  border: 1px solid #83adff;
  background-color: transparent;
}

.review-img {
  background-size: cover;
  width: 100%;
  height: 69%;
  background-repeat: no-repeat;
}

.travelReviewItem {
  width: 100%;
  height: 100%;
}

.travelReviewItem div:first-child {
  width: 100%;
  height: 100%;
}
</style>
