<template>
  <div class="travelPlanItem">
    <div>
      <h2 class="center">{{ planlist.title }}</h2>
      <h3 class="center">
        {{ startDay }} ~
        {{ endDay }}
      </h3>
      <div class="plan-img" v-if="!imgSrc"></div>
      <div class="plan-img" v-else :style="{ backgroundImage: `url('${imgSrc}')` }"></div>
      <div class="plan-header">
        <div style="text-align: right; color: rgb(0 0 0 / 44%)">
          작성시간 : {{ planlist.createdTime }}
        </div>
        <div>{{ planlist.content }}</div>
      </div>
      <hr />
      <div class="plan-body">
        <div v-for="(plan, idx) in planlist.plan" :key="idx">
          <h2 class="center">{{ plan.planDate.split(" ")[0] }} 일정</h2>
          <div v-for="(attraction, idx) in plan.planAttraction" :key="idx" class="plan-item">
            <div>
              <h4>{{ attraction.title }}</h4>
              <p>{{ attraction.addr }}</p>
            </div>
            <p style="text-align: right; width : 15rem">⏰{{ attraction.startTime }} ~ {{ attraction.endTime }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "travelPlanItem",
  components: {},
  props: {
    planlistNo: Number,
  },
  data() {
    return {
      planNo: this.planlistNo,
      planlist: {},
      startDay: "",
      endDay: "",
      imgSrc: ""
    };
  },
  created() {
    axios
      .get(`${process.env.VUE_APP_BASE_URL}travel-plan/planlist/${this.planNo}`)
      .then(({ data }) => {
        if (data.code === "200") {
          this.planlist = data.planlist;
          this.imgSrc = data.planlist.image ? data.planlist.image : `${process.env.VUE_APP_BASE_URL}file/download/noimg/noimg.png/noimg.png`;
          this.startDay = this.planlist.plan[0].planDate.split(" ")[0];
          this.endDay = this.planlist.plan[this.planlist.plan.length - 1].planDate.split(" ")[0];
        }
      })
      .catch(() => { });
  },
  methods: {},
};
</script>

<style scoped>
.center {
  text-align: center;
}

.plan-header {
  width: 100%;
}

.plan-item {
  box-shadow: 2px 3px 5px 0px rgba(0, 0, 0, 0.329);
  padding: 1rem;
  border-radius: 10px;
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

textarea {
  width: 90%;
  height: 3rem;
  resize: none;
  border: 1px solid #0000002d;
  background-color: transparent;
}

.plan-img {
  background-size: cover;
  width: 100%;
  height: 69%;
  background-repeat: no-repeat;
}

.travelPlanItem {
  width: 100%;
  height: 100%;
}

.travelPlanItem div:first-child {
  width: 100%;
  height: 100%;
}
</style>
