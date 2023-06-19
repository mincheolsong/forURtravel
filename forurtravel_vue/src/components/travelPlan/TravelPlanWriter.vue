<template>
  <div id="TravelPlanWriter">
    <TravelPlanlistContainer
      v-if="isPlanlist"
      @togglePlanlist="togglePlanlist"
    />
    <TravelPlanContainer v-else @togglePlanlist="togglePlanlist" />
  </div>
</template>

<script>
import TravelPlanContainer from "./TravelPlanContainer.vue";
import TravelPlanlistContainer from "./TravelPlanlistContainer.vue";
export default {
  name: "TravelPlanWriter",
  components: { TravelPlanContainer, TravelPlanlistContainer },
  data() {
    return {
      isPlanlist: true,
    };
  },
  created() {},
  methods: {
    togglePlanlist() {
      this.isPlanlist = !this.isPlanlist;
    },
  },
};
</script>

<style scoped>
#TravelPlanWriter {
  position: absolute;
  top: 0;
  right: 0;
  z-index: 2;
  width: 20%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.8);
}
</style>