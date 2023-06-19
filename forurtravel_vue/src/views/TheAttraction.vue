<template>
  <div id="TheAttraction">
    <div id="map">
      <div id="attraction-search">
        <input v-model="word" @keyup.enter="setAttraction" placeholder="검색어를 입력해주세요.." />
        <img style="padding: 4px" class="cursor" src="../assets/img/search.svg" @click="setAttraction" />
      </div>
      <div id="attraction-select">
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
        <div class="nav-button">
          <img class="cursor" src="../assets/img/marker/tour.png" @click="setContentType(12)" title="투어" />
          <img class="cursor" src="../assets/img/marker/culture.png" @click="setContentType(14)" title="문화" />
          <img class="cursor" src="../assets/img/marker/fetival.png" @click="setContentType(15)" title="페스티벌" />
          <img class="cursor" src="../assets/img/marker/travel.png" @click="setContentType(25)" title="여행" />
          <img class="cursor" src="../assets/img/marker/leports.png" @click="setContentType(28)" title="레포츠" />
          <img class="cursor" src="../assets/img/marker/hotel.png" @click="setContentType(32)" title="숙박" />
          <img class="cursor" src="../assets/img/marker/shopping.png" @click="setContentType(38)" title="쇼핑" />
          <img class="cursor" src="../assets/img/marker/food.png" @click="setContentType(39)" title="맛집" />
        </div>
      </div>
      <div id="AttractionDetail" v-if="isDetail" @click="closeDetail">
        <div id="AttractionDetailWrap">
          <div id="attraction-detail-left" :style="{ backgroundImage: `url('${detailData.image}')` }">
          </div>
          <div id="attraction-detail-right">
            <table>
              <tr>
                <td>제목 :</td>
                <td>{{ detailData.title }}</td>
              </tr>
              <tr>
                <td>주소 :</td>
                <td>{{ detailData.addr }}</td>
              </tr>
              <tr style="height: 2rem;">
                <td colspan="2"></td>
              </tr>
              <tr>
                <td colspan="2">
                  <textarea :value="detailData.description" readonly style="font-size: 1rem;
                                  font-family: sans-serif;"></textarea>
                </td>
              </tr>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "TheAttraction",
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
      isDetail: false,
      detailData: "",
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
  },
  methods: {
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
              const image = el.image || el.image != "" ? el.image : `${process.env.VUE_APP_BASE_URL}file/download/noimg/noimg.png/noimg.png`;
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
                `                <img src="${image}" width="73" height="70">` +
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
      const container = document.getElementById("map");
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
          this.detailData = position.data;
          this.detailData.image = this.detailData.image || this.detailData.image != "" ? this.detailData.image : `${process.env.VUE_APP_BASE_URL}file/download/noimg/noimg.png/noimg.png`;
          this.isDetail = true;
        });
        kakao.maps.event.addListener(
          marker,
          "mouseover",
          this.makeOverListener(this.map, customOverlay)
        );
        kakao.maps.event.addListener(marker, "mouseout", this.makeOutListener(customOverlay));
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
    closeDetail() {
      this.isDetail = false;
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
};
</script>

<style scoped>
#attraction-search {
  position: absolute;
  top: 0.5rem;
  left: 50%;
  transform: translate(-50%, 0);
  z-index: 2;
  height: 3rem;
  display: flex;
  background-color: rgba(255, 255, 255, 0.904);
  border-radius: 2rem;
  box-shadow: 2px 3px 5px 0px rgba(0, 0, 0, 0.329);
  
}

#attraction-search input {
  width: 31rem;
  background-color: rgb(0, 0, 0, 0);
  border: 0;
  color: rgb(0, 0, 0);
  font-weight: 900;
  font-size: 1rem;
  padding: 1rem;
}

#attraction-select {
  position: absolute;
  z-index: 2;
  top: 3rem;
  display: flex;
  width: 100%;
  justify-content: center;
  height: 5rem;
  align-items: center;
}

#attraction-select select:first-child {
  border-bottom-left-radius: 1.5rem;
  border-top-left-radius: 1.5rem;
  text-align: center;
  border-right: 1px solid #0000001f;
  box-shadow: 2px 3px 5px 0px rgba(0, 0, 0, 0.329);
  background-color: rgba(255, 255, 255, 0.904);
}

#attraction-select select:nth-child(2) {
  border-bottom-right-radius: 1.5rem;
  border-top-right-radius: 1.5rem;
  text-align: center;
  width: 6rem;
  box-shadow: 2px 3px 5px 0px rgba(0, 0, 0, 0.329);
  background-color: rgba(255, 255, 255, 0.904);
}

#attraction-select img {
  height: 3rem;
  user-select: none;

  -moz-user-select: none;
  -webkit-user-drag: none;
  -webkit-user-select: none;
  -ms-user-select: none;
}

#TheAttraction {
  position: relative;
  width: 100%;
  height: 100%;
}

#map {
  width: 100%;
  height: 100%;
}

select {
  height: 3rem;
  border: none;
  -webkit-appearance: none;
  /* 크롬 화살표 없애기 */
  -moz-appearance: none;
  /* 파이어폭스 화살표 없애기 */
  appearance: none;
  /* 화살표 없애기 */
  font-weight: 900;
}

.nav-button img {
  border-radius: 50%;
  overflow: hidden;
  box-shadow: 2px 3px 5px 0px rgba(0, 0, 0, 0.329);
}

#AttractionDetail {
  position: absolute;
  cursor: pointer;
  top: 0;
  width: 100%;
  height: 100%;
  z-index: 3;
  background-color: rgb(0, 0, 0, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
}

#AttractionDetailWrap {
  display: flex;
  width: 70%;
  height: 70%;
  background-color: #fff;
  border-radius: 1rem;
  overflow: hidden;
}

#attraction-detail-left {
  width: 50%;
  height: 100%;
  overflow: hidden;
  background-repeat: no-repeat;
  background-size: cover;
}

#attraction-detail-left img {
  height: 100%;
}

#attraction-detail-right {
  width: 50%;
  padding: 5%;
}

table {
  width: 100%;
}

textarea {
  width: 100%;
  height: 30rem;
  border: 0;
  resize: none;
  cursor: pointer;
}
</style>
