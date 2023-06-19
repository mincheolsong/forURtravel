<template>
  <div class="HotplaceItem">
    <div class="hotplace-img" :style="{ backgroundImage: `url('${imgSrc}')` }"></div>
    <div class="title">
      {{ hotplace.title }}
    </div>
    <div class="review-count">리뷰 수 : {{ hotplace.reviewCount }}</div>
  </div>
</template>

<script>
export default {
  name: "HotplaceItem",
  components: {},
  props: {
    hotplace: Object,
  },
  data() {
    return {
      imgSrc: this.hotplace.image ? `${this.hotplace.image}` : `${process.env.VUE_APP_BASE_URL}file/download/noimg/noimg.png/noimg.png`
    };
  },
};
</script>

<style scoped>
.HotplaceItem {
  flex: 0 0 auto;
  width: 30rem;
  height: 30rem;
}

.hotplace-img {
  width: 100%;
  height: 100%;
  background-color: #83adff;
  background-size: cover;
  background-repeat: no-repeat;
}

.title {
  width: 100%;
  font-weight: 900;
  font-size: 1.25rem;
  text-align: center;
}

.review-count {
  width: 100%;
  text-align: center;
}
</style>