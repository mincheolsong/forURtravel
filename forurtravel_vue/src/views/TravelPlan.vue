<template>
  <div id="TravelPlan">
    <div id="travel-plan-map">
      <div class="search-container">
        <div>
          <div class="sido-search">
            <select v-model="sido" @change="setGugunList">
              <option v-for="item in sidoList" :key="item.sidoCode" :value="item.sidoCode">
                {{ item.sidoName }}
              </option>
            </select>
            <select v-model="gugun" @change="setAttraction">
              <option v-for="item in gugunList" :key="item.gugunCode" :value="item.gugunCode">
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
              placeholder="검색할 관광지를 입력해주세요"
            />
            <img class="cursor" src="../assets/img/search.svg" @click="setAttraction" />
          </div>
        </div>
        <TravelReviewCardContainer :attraction="attraction" />
      </div>
      <TravelPlanWriter />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import TravelReviewCardContainer from "@/components/travelReview/TravelReviewCardContainer.vue";
import TravelPlanWriter from "@/components/travelPlan/TravelPlanWriter.vue";
import { mapGetters, mapMutations } from "vuex";
export default {
  name: "TravelPlan",
  components: { TravelReviewCardContainer, TravelPlanWriter },
  data() {
    return {
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
      makerImages: {},
      linePath: [],
      line: null,
    };
  },
  created() {
    this.SET_PLANLIST({
      title: "",
      image: "",
      content: "여행에 필요한 글,메모를 적어주세요",
      plan: [],
    });
    this.SET_DAY({
      startDay: "",
      endDay: "",
    });
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
    ...mapMutations("travelPlanStore", ["SET_PLANLIST", "SET_DAY"]),
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
      const container = document.getElementById("travel-plan-map");
      const options = {
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
        level: 3, // 지도의 확대 레벨
      };
      const imgSize = new kakao.maps.Size(24, 24);
      this.makerImages = {
        12: new kakao.maps.MarkerImage(require("../assets/img/marker/tour.png"), imgSize),
        14: new kakao.maps.MarkerImage(require("../assets/img/marker/culture.png"), imgSize),
        15: new kakao.maps.MarkerImage(require("../assets/img/marker/fetival.png"), imgSize),
        25: new kakao.maps.MarkerImage(require("../assets/img/marker/travel.png"), imgSize),
        28: new kakao.maps.MarkerImage(require("../assets/img/marker/leports.png"), imgSize),
        32: new kakao.maps.MarkerImage(require("../assets/img/marker/hotel.png"), imgSize),
        38: new kakao.maps.MarkerImage(require("../assets/img/marker/shopping.png"), imgSize),
        39: new kakao.maps.MarkerImage(require("../assets/img/marker/food.png"), imgSize),
      };
      this.map = new window.kakao.maps.Map(container, options);
      this.setAttraction();
    },
    // 지정한 위치에 마커 불러오기
    loadMaker() {
      // 현재 표시되어있는 marker들이 있다면 marker에 등록된 map을 없애준다.
      this.deleteMarker();
      // 마커 이미지를 생성합니다

      // 마커 이미지의 이미지 크기 입니다

      // 마커를 생성합니다
      this.markers = [];
      if (!this.positions.length) return;
      this.positions.forEach((position) => {
        const marker = new kakao.maps.Marker({
          map: this.map, // 마커를 표시할 지도
          position: position.latlng, // 마커를 표시할 위치
          image: this.makerImages[position.data.contentTypeId], // 마커의 이미지
        });
        const customOverlay = new kakao.maps.CustomOverlay({
          position: position.latlng,
          content: position.content,
        });
        kakao.maps.event.addListener(
          marker,
          "mouseover",
          this.makeOverListener(this.map, customOverlay)
        );
        kakao.maps.event.addListener(marker, "mouseout", this.makeOutListener(customOverlay));
        this.markers.push(marker);
      });
      if (this.line != null) this.line.setMap(null);
      this.line = new kakao.maps.Polyline({
        path: this.linePath, // 선을 구성하는 좌표배열 입니다
        strokeWeight: 5, // 선의 두께 입니다
        strokeColor: "#FF0000", // 선의 색깔입니다
        strokeOpacity: 0.7, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
        strokeStyle: "solid", // 선의 스타일입니다
      });
      this.line.setMap(this.map);

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
    ...mapGetters("travelPlanStore", ["attractionList"]),
  },
  watch: {
    attractionList() {
      if (this.attractionList.length === 0 && this.line != null) {
        this.line.setMap(null);
      }
      this.positions = [];
      this.linePath = [];
      this.attractionList.forEach((el) => {
        let obj = {};
        obj.title = el.title;
        obj.latlng = new kakao.maps.LatLng(el.latitude, el.longitude);
        this.linePath.push(obj.latlng);
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
    },
  },
};
</script>

<style scoped>
#TravelPlan {
  position: relative;
  width: 100%;
  height: 100%;
}

#travel-plan-map {
  width: 100%;
  height: 100%;
}

.search-container {
  position: absolute;
  left: 0;
  z-index: 2;
  width: 25%;
  height: 100%;
  box-sizing: border-box;
  padding: 1rem;
  background-color: rgb(255, 255, 255);
  overflow: hidden;
  min-width: 23rem;
  box-shadow: 5px 5px 5px 5px #9595953d;
  border-top-right-radius: 0.5rem;
  border-bottom-right-radius: 0.5rem;
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
  color:black;
}

.attraction-search > img {
  width: 2rem;
  height: 2rem;
}

select {
  width: 33.33%;
  text-align: center;
  font-size: 1.2rem;
  border: 1px solid #83adff;
  box-sizing: border-box;
}
.sido-search {
  margin-bottom: 1rem;
}
.sido-search select {
  border: none;
}
</style>
