<template>
  <div id="TravelReviewCardContainer">
    <TravelReviewCard
      :attraction="item"
      v-for="(item, index) in attraction"
      :key="index"
    />
  </div>
</template>

<script>
import TravelReviewCard from "./TravelReviewCard.vue";
export default {
  name: "TravelReviewCardContainer",
  components: { TravelReviewCard },
  data() {
    return {};
  },
  created() {},
  props: { attraction: Array },
  methods: {},
};
</script>

<style scoped>
#TravelReviewCardContainer {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 88vh;
  margin-top: 1rem;
  overflow-y: auto;
  gap: 1rem;
  flex-grow: 0;
  flex-wrap: nowrap;
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
}
#TravelReviewCardContainer::-webkit-scrollbar {
  display: none;
}
</style>