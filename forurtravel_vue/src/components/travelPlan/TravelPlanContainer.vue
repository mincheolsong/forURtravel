<template>
  <div id="TravelPlanConatiner">
    <div class="title">
      <img
        class="cursor"
        src="../../assets/img/before.svg"
        height="100%"
        @click="mvPlan(true)"
      />
      <h4>{{ `${month}월 ${day}일` }}</h4>
      <img
        class="cursor"
        src="../../assets/img/next.svg"
        height="100%"
        @click="mvPlan(false)"
      />
    </div>
    <div class="money">
      예산(원) :
      <TheInput type="number" v-model="money" />
    </div>
    <div
      class="attraction-wrap"
      @drop.prevent="onDrop"
      @dragendter.prevent
      @dragover.prevent
    >
      <travel-plan-attraction
        :index="index"
        :idx="idx"
        :attraction="item"
        v-for="(item, index) in attractionList"
        :key="index"
      ></travel-plan-attraction>
    </div>
    <button class="cursor" @click="togglePlanlist">일정 등록</button>
  </div>
</template>

<script>
import { mapMutations, mapState } from "vuex";
import TheInput from "../common/TheInput.vue";
import TravelPlanAttraction from "./TravelPlanAttraction.vue";
export default {
  name: "TravelPlanConatiner",
  components: { TheInput, TravelPlanAttraction },
  data() {
    return {
      idx: 0,
    };
  },
  created() {},
  methods: {
    ...mapMutations("travelPlanStore", ["SET_PLANLIST"]),
    togglePlanlist() {
      this.$emit("togglePlanlist");
    },
    mvPlan(type) {
      if (type) {
        if (this.idx <= 0) return;
        this.idx--;
      } else {
        if (this.idx + 1 >= this.planlist.plan.length) return;
        this.idx++;
      }
    },
    onDrop() {
      this.planlist.plan[this.idx].planAttraction.push({
        contentTypeId: this.start.start.contentTypeId,
        image: this.start.start.image,
        addr: this.start.start.addr,
        latitude: this.start.start.latitude,
        longitude: this.start.start.longitude,
        title: this.start.start.title,
        attractionNo: this.start.start.contentId,
        startTime: {
          hh: "12",
          mm: "00",
          a: "am",
        },
        endTime: {
          hh: "12",
          mm: "00",
          a: "am",
        },
      });
    },
  },
  computed: {
    ...mapState("travelPlanStore", ["planlist"]),
    ...mapState("dragStore", ["start"]),
    month() {
      return new Date(this.planlist.plan[this.idx].planDate).getMonth() + 1;
    },
    day() {
      return new Date(this.planlist.plan[this.idx].planDate).getDate();
    },
    money: {
      get() {
        return this.planlist.plan[this.idx].money;
      },
      set(value) {
        const temp = this.planlist.plan;
        temp[this.idx].money = value;
        this.SET_PLANLIST({ ...this.planlist, plan: temp });
      },
    },
    attractionList() {
      const temp = [...this.planlist.plan[this.idx].planAttraction];
      return temp;
    },
  },
};
</script>

<style scoped>
#TravelPlanConatiner {
  width: 100%;
  height: 100%;
  padding-top: 2rem;
}

.title {
  display: flex;
  font-weight: 900;
  font-size: 1.25rem;
  height: 2rem;
  justify-content: space-around;
  align-items: center;
}

.money {
  width: 100%;
  padding: 1rem;
  height: 2rem;
  white-space: nowrap;
  font-weight: 900;
  font-size: 1.25rem;
}

.attraction-wrap {
  box-sizing: border-box;
  width: 100%;
  height: 80%;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  flex-wrap: nowrap;
  flex-grow: 0;
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
}
.attraction-wrap::-webkit-scrollbar {
  display: none;
}

input {
  background-color: transparent;
  border: 0;
  font-weight: 900;
  font-size: 1.25rem;
}
button {
  width: calc(100% - 2rem);
  margin-left: 1rem;
  height: 2rem;
  background-color: #83adff;
  border: 0;
  border-radius: 0.5rem;
  font-weight: 900;
  font-size: 1.25rem;
  color: #fff;
}
</style>