<template>
  <div
    class="TravelPlanAttraction"
    :class="{ valid: isValid, 'not-valid': !isValid }"
  >
    <div class="title">
      {{ attraction.title }}
    </div>
    <div class="time-wrap">
      <VueTimepicker
        hide-clear-button
        input-width="100%"
        :format="'hh:mm a'"
        v-model="planlist.plan[idx].planAttraction[index].startTime"
        @change="checkTime"
      />
      ~
      <VueTimepicker
        hide-clear-button
        @change="checkTime"
        input-width="100%"
        :format="'hh:mm a'"
        v-model="planlist.plan[idx].planAttraction[index].endTime"
      />
    </div>
    <div class="cursor btn-delete" @click="deleteAttraction">X</div>
  </div>
</template>

<script>
import { mapState } from "vuex";
// MainJS
import VueTimepicker from "vue2-timepicker";
//CSS
import "vue2-timepicker/dist/VueTimepicker.css";
export default {
  name: "TravelPlanAttraction",
  components: { VueTimepicker },
  props: {
    attraction: Object,
    idx: Number,
    index: Number,
  },
  data() {
    return {
      isValid: true,
    };
  },
  created() {},
  computed: {
    ...mapState("travelPlanStore", ["planlist"]),
  },
  methods: {
    deleteAttraction() {
      this.planlist.plan[this.idx].planAttraction.splice(this.index, 1);
    },
    checkTime() {
      const start =
        this.planlist.plan[this.idx].planAttraction[this.index].startTime;
      const startTime =
        (start.a === "am" && start.hh === "12" ? 0 : parseInt(start.hh) * 60) +
        parseInt(start.mm) +
        (start.a === "am" ? 0 : 12 * 60);
      const end =
        this.planlist.plan[this.idx].planAttraction[this.index].endTime;
      const endTime =
        (end.a === "am" && end.hh === "12" ? 0 : parseInt(end.hh) * 60) +
        parseInt(end.mm) +
        (end.a === "am" ? 0 : 12 * 60);
      if (startTime > endTime) {
        this.isValid = false;
        this.planlist.plan[this.idx].planAttraction[this.index].isValid = false;
        return;
      }
      this.isValid = true;
      this.planlist.plan[this.idx].planAttraction[this.index].isValid = true;
      this.planlist.plan[this.idx].planAttraction.sort((a, b) => {
        const AstartTime =
          (a.startTime.a === "am" && a.startTime.hh === "12"
            ? 0
            : parseInt(a.startTime.hh) * 60) +
          parseInt(a.startTime.mm) +
          (a.startTime.a === "am" ? 0 : 12 * 60);
        const BstartTime =
          (b.startTime.a === "am" && b.startTime.hh === "12"
            ? 0
            : parseInt(b.startTime.hh) * 60) +
          parseInt(b.startTime.mm) +
          (b.startTime.a === "am" ? 0 : 12 * 60);
        return AstartTime - BstartTime;
      });
    },
  },
};
</script>

<style scoped>
.TravelPlanAttraction {
  box-sizing: border-box;
  width: calc(100%-1rem);
  height: 6rem;
  border: 1px solid #83adff;
  border-radius: 0.5rem;
  margin: 1rem;
  padding: 0.5rem 1rem;
  position: relative;
}

.time-wrap {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 900;
}

.title {
  padding: 0.25rem 0;
  width: 100%;
  height: 1.5rem;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  font-weight: 900;
}

.btn-delete {
  position: absolute;
  top: 0;
  right: 0.5rem;
  font-weight: 900;
  color: #ff0000;
}
.valid {
  background-color: rgb(127, 255, 212);
}
.not-valid {
  background-color: rgb(255, 202, 226);
}
</style>