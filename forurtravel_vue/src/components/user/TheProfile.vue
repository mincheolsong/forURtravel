<template>
  <div id="TheProfile">
    <div id="header">
      <div id="img-header">
        <img :src="userImgSrc" id="profile-img" v-if="profileInfo.sfile != null" />
      </div>
      <div id="info-header">
        <h2>{{ profileInfo.userName }}</h2>
        <div id="following">
          <div>
            <p>팔로워</p>
            <p class="num cursor" @click="showFollower">{{ follower.length }}</p>
          </div>
          <div>
            <p>팔로우</p>
            <p class="num cursor" @click="showFollowing">{{ following.length }}</p>
          </div>
        </div>
        <button id="followBtn" class="cursor" v-if="followBtn" @click="addFollow">팔로우</button>
        <button id="followBtn" class="cursor" v-if="!followBtn && notMe" @click="deleteFollow">
          언팔로우
        </button>
        <!-- <button id="followBtn" class="cursor">팔로잉</button> -->
      </div>
    </div>
    <!-- <router-link  :to="{ path: '/user/setting' }">
      <img src="@/assets/img/setting.svg" alt="" id="profile-setting" />
    </router-link> -->
    <div class="search-wrap" :class="{ 'active-search': isSearch }">
      <div>
        <the-input v-if="isSearch" @input="searchId"></the-input>
        <div class="search-result-wrap">
          <div
            v-for="(item, index) in searchList"
            :key="index"
            class="search-result-item"
            @click="mvProfile(item.userId)"
          >
            <div class="search-item-img">
              <img :src="getImgSrc(item)" alt="" />
            </div>
            <div>{{ item.userId }}</div>
          </div>
        </div>
      </div>
      <img @click="toggleSearch" src="@/assets/img/search.svg" alt="" class="cursor btn-search" />
    </div>
    <div id="setting" v-if="userInfo != null && userInfo.userNo === profileInfo.userNo">
      <img
        src="@/assets/img/setting.svg"
        alt=""
        id="profile-setting"
        @click="togleMore"
        class="cursor"
      />
      <div v-if="isMore" class="more-wrap">
        <div class="more-item" @click="mvSetting">정보수정</div>
        <div class="more-item" @click="deleteUser">회원탈퇴</div>
      </div>
    </div>

    <div id="body">
      <div id="nav">
        <div class="cursor profile-tab" :class="{ active: tabNo === 1 }" @click="tabNo = 1">
          여행후기
        </div>
        <div class="cursor profile-tab" :class="{ active: tabNo === 2 }" @click="tabNo = 2">
          여행계획
        </div>
      </div>
      <div v-if="tabNo == 1" class="profile-content">
        <div
          v-for="(review, idx) in travelReview"
          :key="idx"
          class="reviewCard cursor"
          @click="openModal(review, 1)"
        >
          <img
            :src="
              review.attraction.image != null && review.attraction.image.length !== 0
                ? review.attraction.image
                : noimg
            "
            alt=""
          />
          <p style="text-align: center">"{{ review.title }}"</p>
          <!-- <p>📌{{ review.attraction.title }}</p> -->
        </div>
      </div>
      <div v-else class="profile-content">
        <div
          v-for="(plan, idx) in plans"
          :key="idx"
          class="reviewCard cursor"
          @click="openModal(plan, 2)"
        >
          <img :src="plan.image != null && plan.image.length !== 0 ? plan.image : noimg" alt="" />
          <p>{{ plan.title }}</p>
        </div>
      </div>
    </div>

    <div id="add-button" class="cursor" @click="addContent(tabNo)">
      <img src="@/assets/img/plus-btn.svg" alt="" />
    </div>

    <div class="modal" v-if="reviewModal">
      <div class="bg" @click="closeReviewModal"></div>
      <div class="modalBox">
        <TravelReviewItemVue :selectedReview="selectedReview" />
      </div>
    </div>

    <div class="modal" v-if="planModal">
      <div class="bg" @click="closePlanModal"></div>
      <div class="modalBox">
        <TravelPlanItemVue :planlistNo="selectedPlan.planlistNo" />
      </div>
    </div>

    <div class="modal" v-if="followerModal">
      <div class="bg" @click="closeFollowerModal"></div>

      <div class="modalBox">
        <h2>팔로워 : {{ followerCnt }}명</h2>
        <div v-for="(follow, idx) in follower" :key="idx" style="margin-bottom: 10px">
          <FollowerItemVue :followerNo="follow" @closeModal="closeFollowerModal" />
        </div>
      </div>
    </div>

    <div class="modal" v-if="followingModal">
      <div class="bg" @click="closeFollowingrModal"></div>

      <div class="modalBox">
        <h2>팔로우 : {{ followingCnt }}명</h2>
        <div v-for="(follow, idx) in following" :key="idx" style="margin-bottom: 10px">
          <FollowingItemVue :followingNo="follow" @closeModal="closeFollowingrModal" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapMutations, mapState } from "vuex";
import axios from "axios";
import TravelReviewItemVue from "../travelReview/TravelReviewItem.vue";
import FollowerItemVue from "../follow/FollowerItem.vue";
import FollowingItemVue from "../follow/FollowingItem.vue";
import TravelPlanItemVue from "../travelPlan/TravelPlanItem.vue";
import TheInput from "../common/TheInput.vue";
const userStore = "userStore";

export default {
  name: "TheProfile",
  components: {
    TravelReviewItemVue,
    FollowerItemVue,
    FollowingItemVue,
    TravelPlanItemVue,
    TheInput,
  },
  data() {
    return {
      reviewModal: false,
      planModal: false,
      followerModal: false,
      followingModal: false,
      isMore: false,
      tabNo: 1,
      followBtn: false,
      notMe: false,
      profileInfo: {},
      travelReview: [],
      selectedReview: {},
      selectedPlan: {},
      plans: [],
      follower: [],
      following: [],
      isSearch: false,
      searchList: [],
      noimg: `${process.env.VUE_APP_BASE_URL}file/download/noimg/noimg.png/noimg.png`,
    };
  },
  created() {
    // this.userId를 이용해서 사용자 정보 얻어오기 팔로워, review, plan 불러오기

    axios
      .get(`${process.env.VUE_APP_BASE_URL}user/info/${this.userId}`)
      .then(({ data }) => {
        this.profileInfo = data.userInfo;

        if (this.profileInfo.userNo === this.userInfo.userNo) {
          this.notMe = false; // 내가 맞으므로 notMe = false
          this.followBtn = false; // follow 버튼 보이면 안됨
        } else if (this.followings.includes(this.profileInfo.userNo)) {
          this.notMe = true; // 내가 아님, notMe = true
          this.followBtn = false; // follow버튼 false
        } else {
          this.followBtn = true; // 팔로우 버튼 보여지고,
          this.notMe = true; // 내가 아님
        }

        axios
          .get(`${process.env.VUE_APP_BASE_URL}travel-review/list-user/${this.profileInfo.userNo}`)
          .then(({ data }) => {
            this.travelReview = data.travelReview;
          })
          .catch(() => {
            console.log("review 가져오는 중 에러 발생");
          });

        axios
          .get(
            `${process.env.VUE_APP_BASE_URL}travel-plan/planlist-preview/${this.profileInfo.userNo}`
          )
          .then(({ data }) => {
            this.plans = data.planlist;
          })
          .catch(() => {
            console.log("planlist 가져오는 중 에러 발생");
          });

        axios
          .get(`${process.env.VUE_APP_BASE_URL}follow/follower/${this.profileInfo.userNo}`)
          .then(({ data }) => {
            this.follower = data.follower;
          })
          .catch(() => {
            console.log("follower 가져오는 중 에러 발생");
          });

        axios
          .get(`${process.env.VUE_APP_BASE_URL}follow/following/${this.profileInfo.userNo}`)
          .then(({ data }) => {
            this.following = data.following;
          })
          .catch(() => {
            console.log("following 가져오는 중 에러 발생");
          });
      })
      .catch(({ err }) => {
        console.log(err);
      });
  },
  methods: {
    ...mapActions(userStore, ["setFollowing"]),
    ...mapMutations(userStore, ["SET_IS_LOGIN", "SET_USER_INFO", "SET_IS_VALID_TOKEN"]),
    mvProfile(userId) {
      this.$router.push({ path: `/user/profile/${userId}` }).catch(() => {});
    },
    getImgSrc(item) {
      return `${process.env.VUE_APP_BASE_URL}file/download/${item.sfolder}/${item.ofile}/${item.sfile}`;
    },
    searchId(word) {
      if (!word || word === "") {
        this.searchList = [];
        return;
      }
      axios({
        method: "get",
        url: `${process.env.VUE_APP_BASE_URL}user/search/${word}`,
      })
        .then(({ data }) => {
          if (data.code === "200") {
            console.log(data);
            this.searchList = data.userList;
          } else {
            this.searchList = [];
          }
        })
        .catch((err) => console.log(err));
    },
    toggleSearch() {
      this.isSearch = !this.isSearch;
      if (!this.isSearch) {
        this.searchList = [];
      }
    },
    deleteUser() {
      axios({
        method: "delete",
        url: `${process.env.VUE_APP_BASE_URL}user/delete`,
        headers: {
          "access-token": sessionStorage.getItem("access-token"),
        },
      }).then(({ data }) => {
        if (data.code === "200") {
          this.SET_IS_LOGIN(false);
          this.SET_USER_INFO(null);
          this.SET_IS_VALID_TOKEN(false);
          sessionStorage.removeItem("access-token");
          sessionStorage.removeItem("refresh-token");
          alert("회원 탈퇴되었습니다");
          this.$router.push({ name: "home" }).catch(() => {});
        } else {
          alert("access토큰이 만료되었습니다. 다시 로그인 해주세요");
        }
      });
    },
    togleMore() {
      this.isMore = !this.isMore;
    },
    mvSetting() {
      this.$router.push({ name: "setting" });
    },
    openModal(param, type) {
      if (type === 1) {
        // type=1 (리뷰)
        this.selectedReview = param;
        this.reviewModal = true;
      } else if (type === 2) {
        // type=2 (계획)
        this.selectedPlan = param;
        this.planModal = true;
      }
    },
    closeReviewModal() {
      this.reviewModal = false;
    },
    closePlanModal() {
      this.planModal = false;
    },
    closeFollowerModal() {
      this.followerModal = false;
    },
    closeFollowingrModal() {
      this.followingModal = false;
    },
    showFollower() {
      this.followerModal = true;
    },
    showFollowing() {
      this.followingModal = true;
    },
    addFollow() {
      axios
        .post(`${process.env.VUE_APP_BASE_URL}follow/add`, {
          followerNo: this.userInfo.userNo,
          followingNo: this.profileInfo.userNo,
        })
        .then(({ data }) => {
          data;
          this.setFollowing();
          this.followBtn = false;
          alert("팔로우 성공!");
          location.reload();
        })
        .catch(() => {
          console.log("add follow 중 오류 발생");
        });
    },
    deleteFollow() {
      // followerNo 랑 followingNo 를 활용하여 제거
      axios
        .delete(
          `${process.env.VUE_APP_BASE_URL}follow/delete/${this.userInfo.userNo}/${this.profileInfo.userNo}`
        )
        .then(({ data }) => {
          if (data.code === "200") {
            this.setFollowing();
            this.followBtn = true;
            alert("언팔로우 했습니다");
            location.reload();
          }
        })
        .catch(() => {});
    },
    addContent(param) {
      if (param === 1) {
        this.$router.push({ name: "travelReview" }).catch(() => {});
      } else if (param === 2) {
        this.$router.push({ name: "travelPlan" }).catch(() => {});
      }
    },
  },
  computed: {
    ...mapState(userStore, ["isLogin", "userInfo", "followings"]),
    userImgSrc() {
      return `${process.env.VUE_APP_BASE_URL}file/download/${this.profileInfo.sfolder}/${this.profileInfo.ofile}/${this.profileInfo.sfile}`;
    },
    userId() {
      return this.$route.params.userId;
    }, // param으로 userId를 가져옴}
    followerCnt() {
      return this.follower.length;
    },
    followingCnt() {
      return this.following.length;
    },
  },
  watch: {
    userId() {
      axios
        .get(`${process.env.VUE_APP_BASE_URL}user/info/${this.userId}`)
        .then(({ data }) => {
          this.profileInfo = data.userInfo;

          axios
            .get(
              `${process.env.VUE_APP_BASE_URL}travel-review/list-user/${this.profileInfo.userNo}`
            )
            .then(({ data }) => {
              this.travelReview = data.travelReview;
            })
            .catch(() => {
              console.log("review 가져오는 중 에러 발생");
            });

          axios
            .get(
              `${process.env.VUE_APP_BASE_URL}travel-plan/planlist-preview/${this.profileInfo.userNo}`
            )
            .then(({ data }) => {
              this.plans = data.planlist;
            })
            .catch(() => {
              console.log("planlist 가져오는 중 에러 발생");
            });

          axios
            .get(`${process.env.VUE_APP_BASE_URL}follow/follower/${this.profileInfo.userNo}`)
            .then(({ data }) => {
              this.follower = data.follower;
            })
            .catch(() => {
              console.log("follower 가져오는 중 에러 발생");
            });

          axios
            .get(`${process.env.VUE_APP_BASE_URL}follow/following/${this.profileInfo.userNo}`)
            .then(({ data }) => {
              this.following = data.following;
            })
            .catch(() => {
              console.log("following 가져오는 중 에러 발생");
            });

          if (this.profileInfo.userNo === this.userInfo.userNo) {
            // 내 프로필인 경우
            this.notMe = false; // 내가 맞으므로 notMe = false
            this.followBtn = false; // follow 버튼 보이면 안됨
          } else if (this.followings.includes(this.profileInfo.userNo)) {
            this.notMe = true; // 내가 아님, notMe = true
            this.followBtn = false; // follow버튼 false
          } else {
            this.followBtn = true; // 팔로우 버튼 보여지고,
            this.notMe = false; // 내가 아님
          }
        })
        .catch(({ err }) => {
          console.log(err);
        });
    },
  },
};
</script>

<style scoped>
@keyframes hover {
  50% {
    transform: translateY(-4px);
  }

  100% {
    transform: translateY(-7px);
  }
}

#TheProfile {
  position: relative;
  margin-left: 25rem;
  margin-right: 25rem;
  margin-top: 3rem;
  min-width: 22rem;
}

#header {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 3rem;
  align-items: center;
  border-bottom: 1px solid #95909047;
  padding-bottom: 64px;
  min-width: 22rem;
}

#body {
  min-width: 22rem;
}

#img-header {
  overflow: hidden;
  margin-top: 3rem;
  border: 1px solid #0000001f;
  border-radius: 50%;
  width: 10rem;
  height: 10rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

#info-header {
  width: 20rem;
  padding-top: 2rem;
}

#setting {
  display: flex;
  position: absolute;
  top: 0;
  right: 0;
}

#following {
  display: flex;
  justify-content: space-evenly;
}

#following>div {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 4rem;
}

#nav {
  display: flex;
  justify-content: space-evenly;
}

#followBtn {
  background: #0d6efd;
  color: #fff;
  border: none;
  margin-top: 20px;
  width: 100%;
  height: 2rem;
  border-radius: 6px;
}

#add-button {
  position: fixed;
  bottom: 25px;
  right: 300px;
}

#add-button:hover {
  transform: translateY(-1px);
  animation-name: hover;
  animation-duration: 0.5s;
  animation-timing-function: linear;
  animation-iteration-count: infinite;
  animation-direction: alternate;
}

.profile-tab {
  margin-top: 15px;
  padding-bottom: 15px;
  width: 50%;
  text-align: center;
}

.profile-content {
  display: grid;
  grid-template-columns: repeat(auto-fill, 357px);
  justify-content: center;
  margin-top: 15px;
}

.reviewCard {
  border: 1px solid rgb(221 218 218 / 52%);
  width: 22rem;
  height: 22rem;
  box-sizing: border-box;
  padding: 4px;
  margin-bottom: 4px;
}

.reviewCard:hover {
  transform: translateY(-1px);
  animation-name: hover;
  animation-duration: 0.5s;
  animation-timing-function: linear;
  animation-iteration-count: infinite;
  animation-direction: alternate;
}

.reviewCard>img {
  width: 100%;
  height: 90%;
}

.reviewCard>p {
  margin: 0;
}

.active {
  border-bottom: 1px solid #000;
}

.more-wrap {
  position: absolute;
  top: 1rem;
  right: -5rem;
  white-space: nowrap;
  background-color: white;
  border: 1px solid #00000038;
  border-radius: 4px;
  padding: 0.3rem;
}

.more-item {
  cursor: pointer;
}

.search-wrap {
  display: flex;
  white-space: nowrap;
  height: 2rem;
  margin: 1rem;
  box-sizing: border-box;
  border-radius: 1rem;
  position: absolute;
    top: -16px;
    right: 24px;

}



.active-search {
  background-color: rgb(0, 0, 0, 0.11);
  animation: active-input 0.5s;
}

.search-wrap input {
  height: 100%;
  padding: 0.25rem;
  margin-left: 1rem;
  width: 100%;
  background-color: transparent;
  border: none;
}

.search-result-wrap {
  width: 100%;
  margin: 0 1rem;
  border-radius: 0 0 0.5rem 0.5rem;
  background-color: #fff;
  box-shadow: 1px 3px 5px 0px rgba(0, 0, 0, 0.329);
}

.search-result-item {
  cursor: pointer;
  width: 100%;
  padding: 0.5rem 1rem;
  display: flex;
  align-items: center;
}

.search-item-img {
  overflow: hidden;
  border: 1px solid #0000001f;
  border-radius: 50%;
  width: 3rem;
  height: 3rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-right: 1rem;
  ;
}

@keyframes active-input {
  0% {
    width: 0%;
  }

  100% {
    width: 13rem;
  }
}

.btn-search {
  height: 100%;
}

.num {
  font-weight: bold;
}

p {
  margin-bottom: 0;
}

#profile-img {
  width: 9rem;
}

#profile-setting {
  width: 2rem;
}

h2 {
  text-align: center;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2;
}

.modal .bg {
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
}

.modalBox {
  position: absolute;
  background-color: #fff;
  border-radius: 5px;
  padding: 15px;
  margin-top: 120px;
  width: 45rem;
  height: 45rem;
  overflow: scroll;
  -ms-overflow-style: none;
  /* IE and Edge */
  scrollbar-width: none;
  /* Firefox */
}

.modalBox::-webkit-scrollbar {
  display: none;
}

.modalBox button {
  display: block;
  width: 80px;
  margin: 0 auto;
}
</style>
