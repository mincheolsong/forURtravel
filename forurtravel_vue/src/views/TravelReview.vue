<template>
  <div id="travel-review">
    <div id="travel-review-map">
      <div class="search-container">
        <div>
          <div class="sido-search">
            <select v-model="sido" @change="setGugunList">
              <option
                v-for="item in sidoList"
                :key="item.sidoCode"
                :value="item.sidoCode"
              >
                {{ item.sidoName }}
              </option>
            </select>
            <select v-model="gugun" @change="setAttraction">
              <option
                v-for="item in gugunList"
                :key="item.gugunCode"
                :value="item.gugunCode"
              >
                {{ item.gugunName }}
              </option>
            </select>
            <select v-model="contentType" @change="setAttraction">
              <option
                v-for="item in contentTypeList"
                :key="item.contentTypeId"
                :value="item.contentTypeId"
              >
                {{ item.contentTypeName }}
              </option>
            </select>
          </div>
          <div class="attraction-search">
            <input
              v-model="word"
              @keyup.enter="setAttraction"
              placeholder="후기를 남길 관광지를 입력해주세요"
            />
            <img
              class="cursor"
              src="../assets/img/search.svg"
              @click="setAttraction"
            />
          </div>
        </div>
        <TravelReviewCardContainer :attraction="attraction" />
      </div>
      <div
        :class="{
          'write-wrap-open': isWriteWrap,
          'write-wrap-close': !isWriteWrap,
        }"
      >
        <div
          class="togle-wrap"
          v-if="!isWriteWrap"
          style="background-color: rgba(255, 255, 255, 0.8)"
        >
          <img
            class="cursor"
            src="../assets/img/before.svg"
            @click="toggleWriteWrap"
          />
        </div>
        <div
          v-if="isWriteWrap"
          class="write-main"
          @drop.prevent="onDrop"
          @dragendter.prevent
          @dragover.prevent
        >
          <div class="togle-wrap">
            <img
              class="cursor"
              src="../assets/img/next.svg"
              @click="toggleWriteWrap"
            />
          </div>
          <div>
            <table>
              <tr>
                <th>관광지번호</th>
                <td>
                  {{ travelReview.attractionNo }}
                </td>
              </tr>
              <tr>
                <th>관광지 명</th>
                <td>
                  {{ curAttraction.title }}
                </td>
              </tr>
              <tr>
                <th>관광지 주소</th>
                <td>
                  {{ curAttraction.addr }}
                </td>
              </tr>
              <tr>
                <th>관광지 이미지</th>
                <td></td>
              </tr>
              <tr>
                <td colspan="2">
                  <img
                    width="100%"
                    v-if="curAttraction.image"
                    :src="curAttraction.image"
                    alt=""
                  />
                </td>
              </tr>
              <tr>
                <th>여행제목</th>
                <td>
                  <TheInput type="text" v-model="travelReview.title" />
                </td>
              </tr>
              <tr>
                <th>리뷰</th>
                <td>
                  <the-text-area
                    v-model="travelReview.review"
                    cols="30"
                    rows="10"
                  ></the-text-area>
                </td>
              </tr>
              <tr>
                <th>시작일</th>
                <td>
                  <TheInput type="date" v-model="travelReview.startDay" />
                </td>
              </tr>
              <tr>
                <th>종료일</th>
                <td>
                  <TheInput type="date" v-model="travelReview.endDay" />
                </td>
              </tr>
              <tr>
                <th>예산 (원)</th>
                <td>
                  <TheInput type="number" v-model="travelReview.money" />
                </td>
              </tr>
            </table>
            <button @click="write" class="registBtn">후기등록</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import TheInput from "@/components/common/TheInput.vue";
import TheTextArea from "@/components/common/TheTextArea.vue";
import TravelReviewCardContainer from "@/components/travelReview/TravelReviewCardContainer.vue";
import { mapMutations, mapState } from "vuex";
export default {
  name: "TravelReview",
  components: { TheInput, TheTextArea, TravelReviewCardContainer },
  data() {
    return {
      travelReview: {
        attractionNo: null,
        endDay: "",
        review: "",
        startDay: "",
        title: "",
        money: 0,
      },
      curAttraction: {
        title: "",
        addr: "",
        image: "",
      },
      word: "",
      sido: 1,
      gugun: 1,
      contentType: 12,
      sidoList: [],
      gugunLists: {},
      gugunList: [],
      contentTypeList: [],
      attraction: [],
      map: null,
      positions: [],
      markers: [],
      isWriteWrap: true,
    };
  },
  created() {
    axios({
      method: "get",
      url: `${process.env.VUE_APP_BASE_URL}attraction/sido`,
    })
      .then(({ data }) => {
        if (data.code === "200") {
          data.sido.forEach((e) => {
            this.sidoList.push({
              sidoCode: e.sidoCode,
              sidoName: e.sidoName,
            });
          });
        }
      })
      .catch((err) => console.log(err));
    axios({
      method: "get",
      url: `${process.env.VUE_APP_BASE_URL}attraction/gugun`,
    })
      .then(({ data }) => {
        if (data.code === "200") {
          data.gugun.forEach((e) => {
            if (!this.gugunLists[e.sidoCode]) {
              this.gugunLists[e.sidoCode] = [];
            }
            this.gugunLists[e.sidoCode].push({
              gugunCode: e.gugunCode,
              gugunName: e.gugunName,
            });
          });
          this.gugunList = this.gugunLists[this.sido];
        }
      })
      .catch((err) => console.log(err));
    axios({
      method: "get",
      url: `${process.env.VUE_APP_BASE_URL}attraction/content-type`,
    }).then(({ data }) => {
      if (data.code === "200") {
        this.contentTypeList = data.contentType;
      }
    });
  },
  methods: {
    ...mapMutations("travelReviewStore", ["SET_ATTRACTION_NO"]),
    onDrop() {
      this.travelReview.attractionNo = this.start.start.contentId;
      this.curAttraction.title = this.start.start.title;
      this.curAttraction.addr = this.start.start.addr;
      this.curAttraction.image = this.start.start.image;
    },
    write() {
      if (
        !sessionStorage.getItem("access-token") ||
        sessionStorage.getItem("access-token") === ""
      ) {
        alert("로그인을 해주세요");
        return;
      }
      if (!this.travelReview.attractionNo) {
        alert("관광지를 선택해 주세요");
        return;
      }
      if (!this.travelReview.title) {
        alert("제목을 입력해주세요");
        return;
      }
      if (!this.travelReview.review) {
        alert("리뷰를 입력해주세요");
        return;
      }
      if (!this.travelReview.startDay) {
        alert("시작일을 선택해 주세요");
        return;
      }
      if (!this.travelReview.endDay) {
        alert("종료일을 선택해 주세요");
        return;
      }

      axios({
        method: "post",
        url: `${process.env.VUE_APP_BASE_URL}travel-review/write`,
        data: this.travelReview,
        headers: {
          "access-token": sessionStorage.getItem("access-token"),
        },
      })
        .then(({ data }) => {
          if (data.code === "200") {
            this.SET_ATTRACTION_NO({
              attractionNo: this.travelReview.attractionNo,
            });
            alert("여행후기를 등록했습니다!");
            this.travelReview = {
              attractionNo: null,
              endDay: "",
              review: "",
              startDay: "",
              title: "",
              money: 0,
            };
            this.curAttraction = {
              title: "",
              addr: "",
              image: "",
            };
          } else {
            alert("여행후기 등록에 실패했습니다! 잠시 후 다시 시도해 주세요");
          }
        })
        .catch((err) => console.log(err));
    },
    setContentType(value) {
      this.setAttraction();
      this.contentType = value;
    },
    setGugunList() {
      this.gugunList = this.gugunLists[this.sido];
      this.gugun = this.gugunList[0].gugunCode;
      this.setAttraction();
    },
    setAttraction() {
      this.attraction = [];
      let url = "";
      if (!this.word || this.word === "") {
        url += `${process.env.VUE_APP_BASE_URL}attraction/list/${this.contentType}/${this.sido}/${this.gugun}/*`;
      } else {
        url += `${process.env.VUE_APP_BASE_URL}attraction/list/${this.contentType}/${this.sido}/${this.gugun}/${this.word}`;
      }
      axios({
        method: "get",
        url: url,
      })
        .then(({ data }) => {
          if (data.code === "200") {
            this.attraction = data.attraction;
            this.positions = [];
            this.attraction.forEach((el) => {
              let obj = {};
              obj.title = el.title;
              obj.latlng = new kakao.maps.LatLng(el.latitude, el.longitude);
              obj.content =
                '<div class="overlay-wrap">' +
                '    <div class="overlay-info">' +
                '        <div class="overlay-title">' +
                `            ${el.title}` +
                "        </div>" +
                '        <div class="overlay-body">' +
                '            <div class="overlay-img">' +
                `                <img src="${el.image}" width="73" height="70">` +
                "           </div>" +
                '            <div class="overlay-desc">' +
                `                <div class="overlay-ellipsis">${el.addr}</div>` +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "</div>";
              obj.data = el;
              this.positions.push(obj);
            });
            this.loadMaker();
          }
        })
        .catch((err) => console.log(err));
    },
    // api 불러오기
    loadScript() {
      const script = document.createElement("script");
      script.src =
        "//dapi.kakao.com/v2/maps/sdk.js?appkey=" +
        process.env.VUE_APP_KAKAO_MAP_API_KEY +
        "&libraries=services,clusterer,drawing&autoload=false";
      /* global kakao */
      script.onload = () => window.kakao.maps.load(this.loadMap);

      document.head.appendChild(script);
    },
    // 맵 출력하기
    loadMap() {
      const container = document.getElementById("travel-review-map");
      const options = {
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
        level: 3, // 지도의 확대 레벨
      };

      this.map = new window.kakao.maps.Map(container, options);
      this.setAttraction();
    },
    // 지정한 위치에 마커 불러오기
    loadMaker() {
      // 현재 표시되어있는 marker들이 있다면 marker에 등록된 map을 없애준다.
      this.deleteMarker();
      // 마커 이미지를 생성합니다
      let imgSrc = "";
      switch (this.contentType) {
        case 12:
          imgSrc = require("../assets/img/marker/tour.png");
          break;
        case 14:
          imgSrc = require("../assets/img/marker/culture.png");
          break;
        case 15:
          imgSrc = require("../assets/img/marker/fetival.png");
          break;
        case 25:
          imgSrc = require("../assets/img/marker/travel.png");
          break;
        case 28:
          imgSrc = require("../assets/img/marker/leports.png");
          break;
        case 32:
          imgSrc = require("../assets/img/marker/hotel.png");
          break;
        case 38:
          imgSrc = require("../assets/img/marker/shopping.png");
          break;
        case 39:
          imgSrc = require("../assets/img/marker/food.png");
          break;
      }
      // 마커 이미지의 이미지 크기 입니다
      const imgSize = new kakao.maps.Size(24, 24);
      const markerImage = new kakao.maps.MarkerImage(imgSrc, imgSize);

      // 마커를 생성합니다
      this.markers = [];
      if (!this.positions.length) return;
      this.positions.forEach((position) => {
        const marker = new kakao.maps.Marker({
          map: this.map, // 마커를 표시할 지도
          position: position.latlng, // 마커를 표시할 위치
          image: markerImage, // 마커의 이미지
        });
        const customOverlay = new kakao.maps.CustomOverlay({
          position: position.latlng,
          content: position.content,
        });
        kakao.maps.event.addListener(marker, "click", () => {
          this.travelReview.attractionNo = position.data.contentId;
          this.curAttraction.title = position.data.title;
          this.curAttraction.addr = position.data.addr;
          this.curAttraction.image = position.data.image;
        });
        kakao.maps.event.addListener(
          marker,
          "mouseover",
          this.makeOverListener(this.map, customOverlay)
        );
        kakao.maps.event.addListener(
          marker,
          "mouseout",
          this.makeOutListener(customOverlay)
        );
        this.markers.push(marker);
      });

      // 4. 지도를 이동시켜주기
      // 배열.reduce( (누적값, 현재값, 인덱스, 요소)=>{ return 결과값}, 초기값);
      const bounds = this.positions.reduce(
        (bounds, position) => bounds.extend(position.latlng),
        new kakao.maps.LatLngBounds()
      );

      this.map.setBounds(bounds);
    },
    deleteMarker() {
      if (this.markers.length > 0) {
        this.markers.forEach((item) => {
          item.setMap(null);
        });
      }
    },
    makeOverListener(map, customOverlay) {
      return function () {
        customOverlay.setMap(map);
      };
    },
    makeOutListener(customOverlay) {
      return function () {
        customOverlay.setMap(null);
      };
    },
    toggleWriteWrap() {
      this.isWriteWrap = !this.isWriteWrap;
    },
  },
  mounted() {
    // api 스크립트 소스 불러오기 및 지도 출력
    if (window.kakao && window.kakao.maps) {
      this.loadMap();
    } else {
      this.loadScript();
    }
  },
  computed: {
    ...mapState("dragStore", ["start"]),
  },
};
</script>

<style scoped>
#travel-review {
  width: 100%;
  height: 100%;
  white-space: nowrap;
  text-overflow: ellipsis;
}

#travel-review-map {
  width: 100%;
  height: 100%;
}
.registBtn {
  background-color: white;
  color: #007bff;
  border: 1px solid;
}

.registBtn:hover {
  background-color: #007bff;
  color: white;
}
.write-wrap-open {
  box-sizing: border-box;
  z-index: 2;
  position: absolute;
  right: 0;
  width: 25%;
  height: 100%;
  text-overflow: ellipsis;
}

.write-wrap-close {
  box-sizing: border-box;
  z-index: 2;
  position: absolute;
  right: 0;
  height: 100%;
  text-overflow: ellipsis;
}

.togle-wrap {
  max-width: 1rem;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.write-main {
  box-sizing: border-box;
  display: flex;
  background-color: rgba(255, 255, 255, 0.8);
  width: 100%;
  height: 100%;
  animation: fadeInLeft 1s;
}

.write-main > :last-child {
  box-sizing: border-box;
  width: 100%;
}

@keyframes fadeInLeft {
  0% {
    opacity: 0;
    transform: translate3d(+100%, 0, 0);
  }

  to {
    opacity: 1;
    transform: translateZ(0);
  }
}

button {
  cursor: pointer;
  background-color: #83adff;
  border: 0;
  border-radius: 0.3rem;
  color: #fff;
  font-size: 1.5rem;
  font-weight: 900;
  width: 100%;
  padding: 0.5rem;
}

select {
  width: 33.33%;
  text-align: center;
  font-size: 1.2rem;
  border: 1px solid #83adff;
  box-sizing: border-box;
}

.attraction-search {
  width: 100%;
  display: flex;
  background-color: rgb(0, 0, 0, 0.11);
  border-radius: 1rem;
}

.attraction-search > input {
  width: 100%;
  background-color: rgb(0, 0, 0, 0);
  border: 0;
  color: black;
  font-size: 1rem;
  padding-left: 1rem;
}

.attraction-search > input::placeholder {
  color: black;
}

.attraction-search > img {
  width: 2rem;
  height: 2rem;
}

.search-container {
  position: absolute;
  left: 0;
  z-index: 2;
  width: 25%;
  height: 100%;
  box-sizing: border-box;
  padding: 1rem;
  background-color: rgba(255, 255, 255);
  overflow: hidden;
  min-width: 23rem;
  box-shadow: 5px 5px 5px 5px #9595953d;
  border-top-right-radius: 0.5rem;
  border-bottom-right-radius: 0.5rem;
}

table,
tr,
td,
input,
textarea {
  width: 100%;
  max-width: 100%;
}

td {
  white-space: normal;
}
.sido-search {
  margin-bottom: 1rem;
}
.sido-search select {
  border: none;
}
</style>
