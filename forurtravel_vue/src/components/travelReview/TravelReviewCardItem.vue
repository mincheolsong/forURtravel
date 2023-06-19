<template>
  <div class="TravelReviewCardItem">
    <div>
      <div class="user-profile">
        <div class="user-img">
          <img :src="imgSrc" width="100%" height="100%" />
        </div>
        <div>{{ review.user.userName }}</div>
      </div>
      <div class="review-header">
        <div class="title">{{ review.title }}</div>
        <div class="day">{{ review.startDay }}~{{ review.endDay }}</div>
        <textarea :value="review.review" readonly ></textarea>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "TravelReviewCardItem",
  components: {},
  data() {
    return {
      imgSrc: "",
    };
  },
  created() {
    this.imgSrc = this.review.image ? `${process.env.VUE_APP_BASE_URL}file/download/${this.review.user.sfolder}/${this.review.user.ofile}/${this.review.user.sfile}` : `${process.env.VUE_APP_BASE_URL}file/download/noimg/noimg.png/noimg.png`;
  },
  methods: {},
  props: { review: Object },
};
</script>

<style scoped>
.TravelReviewCardItem {
  flex: 0 0 auto;
  box-sizing: border-box;
  width: 99%;
  border-bottom: 1px solid #0000001a;
  padding: 1rem;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.TravelReviewCardItem> :first-child {
  display: flex;
  width: 100%;
  text-align: center;
}

.user-profile {
  width: 5rem;
  height: 7rem;
  text-align: center;
}

.user-img {
  border: 1px solid #000;
  border-radius: 100%;
  width: 5rem;
  height: 5rem;
  overflow: hidden;
}

.review-header {
  width: 100%;
}

textarea {
  width: 90%;
  height: 3rem;
  resize: none;
  border: 0;
  background-color: transparent;
}

.title {
  font-weight: 900;
  font-size: 1.25rem;
}
</style>
