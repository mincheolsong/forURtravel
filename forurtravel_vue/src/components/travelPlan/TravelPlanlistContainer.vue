<template>
  <div id="TravelPlanlistContainer">
    <div class="title"><label>제목:</label><input v-model="title" /></div>
    <div class="date-wrap">
      <label>기간:</label>
      <input type="date" v-model="startDay" @change="checkDay(true)" />~
      <input type="date" v-model="endDay" @change="checkDay(false)" />
    </div>
    <div class="money">총 비용 : {{ totalMoney }}💲</div>
    <button @click="togglePlanlist" class="planBtn">일정짜기</button>
    <div><textarea v-model="content"></textarea></div>
    <div v-if="attractionList.length != 0" class="image-title">
      <h2>대표 이미지</h2>
      <p>(표시할 대표이미지를 선택해주세요!)</p>
      <h3>{{ attractionList[imageIdx].title }}</h3>
    </div>
    <!-- <div v-else class="image-title">
      <h2>여행일정을 추가해주세요!</h2>
    </div> -->
    <div v-if="attractionList.length != 0" class="image-wrap">
      <img src="../../assets/img/before.svg" @click="mvIdx(true)" />
      <div :style="{ backgroundImage: `url('${this.planlist.image}')` }"></div>
      <img src="../../assets/img/next.svg" @click="mvIdx(false)" />
    </div>
    <button @click="write" class="registBtn">최종등록</button>
  </div>
</template>

<script>
import axios from "axios";
import { mapMutations, mapState, mapGetters } from "vuex";
export default {
  name: "TravelPlanlistContainer",
  components: {},
  data() {
    return {};
  },
  created() {
    this.SET_PLANLIST({
      ...this.planlist,
      title: `${this.userInfo.userName}님의 여행`,
      image:
        this.attractionList.length === 0
          ? ""
          : this.attractionList[this.imageIdx].image ||
            this.attractionList[this.imageIdx].image !== ""
          ? this.attractionList[this.imageIdx].image
          : `${process.env.VUE_APP_BASE_URL}file/download/noimg/noimg.png/noimg.png`,
    });
  },
  methods: {
    mvIdx(type) {
      if (type) {
        if (this.imageIdx === 0) {
          return;
        }
        this.SET_IMAGE_IDX(this.imageIdx - 1);
        this.SET_PLANLIST({
          ...this.planlist,
          image:
            this.attractionList[this.imageIdx].image ||
            this.attractionList[this.imageIdx].image !== ""
              ? this.attractionList[this.imageIdx].image
              : `${process.env.VUE_APP_BASE_URL}file/download/noimg/noimg.png/noimg.png`,
        });
      } else {
        if (this.imageIdx + 1 === this.attractionList.length) {
          return;
        }
        this.SET_IMAGE_IDX(this.imageIdx + 1);
        this.SET_PLANLIST({
          ...this.planlist,
          image:
            this.attractionList[this.imageIdx].image ||
            this.attractionList[this.imageIdx].image !== ""
              ? this.attractionList[this.imageIdx].image
              : `${process.env.VUE_APP_BASE_URL}file/download/noimg/noimg.png/noimg.png`,
        });
      }
    },
    ...mapMutations("travelPlanStore", ["SET_PLANLIST", "SET_DAY", "SET_IMAGE_IDX"]),
    checkDay(type) {
      const start = new Date(this.startDay);
      if (type) {
        const cur = new Date();
        const curFomat = `${cur.getFullYear()}-${
          cur.getMonth() + 1 >= 10 ? cur.getMonth() + 1 : "0" + (cur.getMonth() + 1)
        }-${cur.getDate() >= 10 ? cur.getDate() : "0" + cur.getDate()}`;
        if (start.getTime() - new Date(curFomat).getTime() < 0) {
          alert("현재 날짜보다 이전 날짜를 여행시작날짜로 정할 수 없습니다!");
          this.startDay = "";
          this.SET_PLANLIST({ ...this.planlist, plan: [] });
          return;
        }
      }
      if (this.endDay === "" || this.startDay === "") {
        return;
      }
      const timeDiff = new Date(this.endDay).getTime() - start.getTime();
      if (timeDiff < 0) {
        alert("여행 종료일이 시작일보다 빠릅니다!");
        if (type) {
          this.startDay = "";
        } else {
          this.endDay = "";
        }
        return;
      }
      const day = Math.floor(timeDiff / (1000 * 60 * 60 * 24)) + 1;
      const temp = [];
      for (let i = 0; i < day; i++) {
        const curDay = new Date(start);
        curDay.setDate(start.getDate() + i);
        temp.push({
          money: 0,
          planAttraction: [],
          planDate: `${curDay.getFullYear()}-${
            curDay.getMonth() + 1 >= 10 ? curDay.getMonth() + 1 : "0" + (curDay.getMonth() + 1)
          }-${curDay.getDate() >= 10 ? curDay.getDate() : "0" + curDay.getDate()} 00:00:00`,
        });
      }
      this.SET_PLANLIST({ ...this.planlist, plan: temp });
    },
    write() {
      if (this.startDay === "" || this.endDay === "") {
        alert("여행일정을 입력해주세요!");
        return;
      }
      console.log(typeof this.planlist.image);
      const temp = { ...this.planlist, image: this.planlist.image };
      temp.plan = [];
      this.planlist.plan.forEach((e) => {
        temp.plan.push({
          money: e.money,
          planDate: e.planDate,
          planAttraction: [],
        });
      });
      let flag = true;
      this.planlist.plan.forEach((el, idx1) => {
        el.planAttraction.forEach((el2) => {
          if (!el2.isValid) {
            flag = false;
          }
          temp.plan[idx1].planAttraction.push({
            title: el2.title,
            addr: el2.addr,
            attractionNo: el2.attractionNo,
            startTime: `${el2.startTime.a}:${el2.startTime.hh}:${el2.startTime.mm}`,
            endTime: `${el2.endTime.a}:${el2.endTime.hh}:${el2.endTime.mm}`,
          });
        });
      });
      console.log(temp);
      if (flag) {
        axios({
          method: "post",
          headers: {
            "access-token": sessionStorage.getItem("access-token"),
          },
          url: `${process.env.VUE_APP_BASE_URL}travel-plan/write`,
          data: temp,
        })
          .then(({ data }) => {
            if (data.code === "200") {
              alert("등록완료!");
              this.SET_PLANLIST({
                ...this.initPlanlist,
                title: `${this.userInfo.userName}님의 여행`,
              });
              this.SET_DAY({
                startDay: "",
                endDay: "",
              });
              this.SET_IMAGE_IDX(0);
            }
          })
          .catch((err) => console.log(err));
      } else {
        alert("여행일정을 다시 확인해 주세요!");
      }
    },
    togglePlanlist() {
      if (this.startDay === "" || this.endDay === "") {
        alert("여행기간을 설정해주세요");
        return;
      }
      this.$emit("togglePlanlist");
    },
  },
  computed: {
    ...mapState("travelPlanStore", ["planlist", "initPlan", "initPlanlist", "day", "imageIdx"]),
    ...mapState("userStore", ["userInfo"]),
    ...mapGetters("travelPlanStore", ["totalMoney", "attractionList"]),
    title: {
      get() {
        return this.planlist.title;
      },
      set(value) {
        this.SET_PLANLIST({ ...this.planlist, title: value });
      },
    },
    startDay: {
      get() {
        return this.day.startDay;
      },
      set(value) {
        this.SET_DAY({ ...this.day, startDay: value });
      },
    },
    endDay: {
      get() {
        return this.day.endDay;
      },
      set(value) {
        this.SET_DAY({ ...this.day, endDay: value });
      },
    },
    content: {
      get() {
        return this.planlist.content;
      },
      set(value) {
        this.planlist.content = value;
      },
    },
  },
};
</script>

<style scoped>
#TravelPlanlistContainer {
  width: calc(100% - 4rem);
  height: 100%;
  padding: 2rem;
}
.planBtn {
  background-color: #fff;
  border: 1px solid #198754;
  color: #198754;
}
.planBtn:hover {
  background-color: #198754;
  color: #fff;
}
.registBtn {
  background-color: #fff;
  border: 1px solid #0d6efd;
  color: #0d6efd;
}
.registBtn:hover {
  background-color: #0d6efd;
  color: #fff;
}
textarea {
  resize: none;
      box-shadow: 2px 3px 5px 0px rgba(0, 0, 0, 0.329);
    border: none;
    padding: 1rem;
    box-sizing: border-box;
}
.title {
  display: flex;
  white-space: nowrap;
  justify-content: center;
  align-items: center;
}
.title > input {
  width: 100%;
  height: 2rem;
  text-align: center;
  font-size: 1rem;
  background-color: #fff;
  box-sizing: border-box;
  border: 0;
  border-bottom: 1px solid rgba(56, 56, 56, 0.301);
  box-shadow: 2px 3px 5px 0px rgba(0, 0, 0, 0.329);
  border-radius: 1rem;
}

.date-wrap {
  margin: 1rem 0;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  white-space: nowrap;
}

.date-wrap > input {
  width: 100%;
  text-align: center;
  border-radius: 1rem;
  box-shadow: 2px 3px 5px 0px rgba(0, 0, 0, 0.329);
  background-color: rgba(255, 255, 255, 0.904);
  padding: 0.35rem;
  border: none;
}

.money {
  width: 100%;
  text-align: center;
  font-weight: 900;
  font-size: 1.5rem;
}

button {
  width: 100%;
  background-color: #83adff;
  height: 2rem;
  border: 0;
  border-radius: 1rem;
  font-weight: 900;
  font-size: 1.25rem;
  color: #fff;
  cursor: pointer;
  margin: 1rem 0;
}

textarea {
  width: 100%;
  height: 10rem;
  border-radius: 0.5rem;
}

.image-wrap {
  display: flex;
  width: 100%;
  height: 15rem;
  align-items: center;
}

.image-wrap > img {
  height: 2rem;
  cursor: pointer;
}

.image-wrap > div {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-color: #83adff;
}

.image-title {
  width: 100%;
  text-align: center;
  white-space: nowrap;
}
</style>
