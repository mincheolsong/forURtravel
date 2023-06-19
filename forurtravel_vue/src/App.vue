<template>
  <div id="app">
    <!-- :class="{'show-popup' : isLoginClicked}" -->
    <TheHeaderVue @openModal="openModal" />
    <router-view class="view"></router-view>

    <div class="modal" v-if="isLoginClicked">
      <div class="bg" @click="closeModal"></div>
      <div class="modalBox">
        <TheLoginVue @closeModal="closeModal" />
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import TheHeaderVue from "./components/common/TheHeader.vue";
import TheLoginVue from "./components/user/TheLogin.vue";

export default {
  name: "TheApp",
  data() {
    return {
      isLoginClicked: false,
    };
  },
  components: {
    TheHeaderVue,
    TheLoginVue,
  },
  computed: {
    ...mapState("userStore", ["isLogin"]),
  },
  created() {
    if (this.isLogin) {
      this.isLoginClicked = false;
    }
  },
  watch: {
    isLogin(newValue, oldValue) {
      if (newValue) {
        this.isLoginClicked = false;
      }
      oldValue;
    },
    // isLoginClicked(newValue,oldValue){
    //   if(newValue){
    //     document.body.style.overflow = "hidden"
    //   }else{
    //     document.body.style.overflow = ""
    //   }
    //   oldValue
    // }
  },
  methods: {
    openModal() {
      this.isLoginClicked = true;
    },
    closeModal() {
      this.isLoginClicked = false;
    },
  },
};
</script>

<style scoped>
#app {
  font-family: sans-serif, Avenir, Helvetica, Arial;
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
}

#app * {
  box-sizing: border-box;
}
.show-popup {
  overflow: hidden;
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
  padding: 15px;
}
.modalBox button {
  display: block;
  width: 80px;
  margin: 0 auto;
}
button {
  background-color: #83adff;
  color: #fff;
  border: none;
  padding: 5px 10px;
  border-radius: 4px;
  cursor: pointer;
}
</style>

<style>
.cursor {
  cursor: pointer;
}

a {
  text-decoration: none;
}

a:link,
:visited,
:active,
:hover {
  color: #000;
}

*:focus {
  outline: none;
}

.notDrag {
  -ms-user-select: none;
  -moz-user-select: -moz-none;
  -khtml-user-select: none;
  -webkit-user-select: none;
  user-select: none;
}

/* 오버레이 css */
.overlay-wrap {
  position: absolute;
  left: 0;
  bottom: 40px;
  width: 288px;
  height: 132px;
  margin-left: -144px;
  text-align: left;
  overflow: hidden;
  font-size: 12px;
  font-family: "Malgun Gothic", dotum, "돋움", sans-serif;
  line-height: 1.5;
}
.overlay-wrap * {
  padding: 0;
  margin: 0;
}
.overlay-wrap .overlay-info {
  width: 286px;
  height: 120px;
  border-radius: 5px;
  border-bottom: 2px solid #ccc;
  border-right: 1px solid #ccc;
  overflow: hidden;
  background: #fff;
}
.overlay-wrap .overlay-info:nth-child(1) {
  border: 0;
  box-shadow: 0px 1px 2px #888;
}
.overlay-info .overlay-title {
  padding: 5px 0 0 10px;
  height: 30px;
  background: #eee;
  border-bottom: 1px solid #ddd;
  font-size: 18px;
  font-weight: bold;
}
.overlay-info .overlay-body {
  position: relative;
  overflow: hidden;
}
.overlay-info .overlay-desc {
  position: relative;
  margin: 13px 0 0 90px;
  height: 75px;
}
.overlay-desc .overlay-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.overlay-desc .overlay-jibun {
  font-size: 11px;
  color: #888;
  margin-top: -2px;
}
.overlay-info .overlay-img {
  position: absolute;
  top: 6px;
  left: 5px;
  width: 73px;
  height: 71px;
  border: 1px solid #ddd;
  color: #888;
  overflow: hidden;
}
.overlay-info:after {
  content: "";
  position: absolute;
  margin-left: -12px;
  left: 50%;
  bottom: 0;
  width: 22px;
  height: 12px;
  background: url("https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png");
}
.overlay-info .overlay-link {
  color: #5085bb;
}
</style>
