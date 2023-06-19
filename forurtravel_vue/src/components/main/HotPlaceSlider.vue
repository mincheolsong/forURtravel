<template>
  <div id="HotPlaceSlider">
    <div class="title">가장 🔥한 관광지</div>
    <div class="hotplace-wrap">
      <HotplaceItem
        :hotplace="item"
        v-for="(item, index) in hotplaceList"
        :key="index"
      />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import HotplaceItem from "./HotplaceItem.vue";
export default {
  name: "HotPlaceSlider",
  components: { HotplaceItem },
  data() {
    return {
      hotplaceList: [],
    };
  },
  created() {
    axios
      .get(`${process.env.VUE_APP_BASE_URL}travel-review/hotplace`)
      .then(({ data }) => {
        this.hotplaceList = data.hotplace;
      })
      .catch((err) => console.log(err));
  },
  methods: {},
};
</script>

<style scoped>
#HotPlaceSlider {
  width: 100%;
  margin: 8rem 0;
  height: 42rem;
}

img {
  width: 5rem;
  height: 5rem;
}
.hotplace-wrap {
  display: flex;
  align-items: center;
  width: calc(100% - 4rem);
  height: 100%;
  padding: 0 2rem;
  flex-wrap: nowrap;
  overflow-x: auto;
  flex-grow: 0;
  gap: 2rem;
}
.hotplace-wrap::-webkit-scrollbar {
  width: 0.5rem;
}
.hotplace-wrap::-webkit-scrollbar-track {
  background-color: #e4e4e4;
  border-radius: 5rem;
}
.hotplace-wrap::-webkit-scrollbar-thumb {
  background-color: #d4aa70;
  border-radius: 5rem;
}
.hotplace-wrap {
  scrollbar-color: #d4aa70 #e4e4e4;
}
.hotplace-wrap::-webkit-scrollbar-thumb {
  background-image: linear-gradient(180deg, #d0368a 0%, #708ad4 99%);
  box-shadow: inset 0.1rem 0.1rem 0.2rem 0 rgba(#fff, 0.5);
  border-radius: 5rem;
}
.title {
  width: 100%;
  padding: 0 3rem;
  font-weight: 900;
  font-size: 1.5rem;
}
</style>