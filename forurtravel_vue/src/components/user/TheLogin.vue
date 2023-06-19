<template>
  <div id="TheLogin">
    <img src="@/assets/img/Logo.svg" style="width: 10rem" class="logo" />
    <div v-if="!findPw">
      <h2 class="title">로그인</h2>
      <table>
        <colgroup>
          <col width="10%" />
          <col width="80%" />
          <col width="10%" />
        </colgroup>
        <tr>
          <td>아이디</td>
          <td colspan="2">
            <TheInput v-model="user.userId" />
          </td>
        </tr>
        <tr>
          <td>비밀번호</td>
          <td colspan="2">
            <TheInput :type="'password'" v-model="user.password" />
          </td>
        </tr>
        <tr>
          <td>아이디 저장<input id="saveid" :type="'checkbox'" v-model="saveid" /></td>
          <td></td>
          <td>
            <div @click="mvJoin" style="display: inline-block" class="cursor">회원가입</div>
            |
            <a @click="toggleFindPw">비밀번호찾기</a>
          </td>
        </tr>
        <tr>
          <td colspan="3">
            <button @click="login">로그인</button>
          </td>
        </tr>
      </table>
    </div>
    <div v-else>
      <h2 class="title">비밀번호 찾기</h2>
      <table>
        <colgroup>
          <col width="10%" />
          <col width="80%" />
          <col width="10%" />
        </colgroup>
        <tr>
          <td>아이디</td>
          <td colspan="2">
            <TheInput v-model="findPwData.userId" />
          </td>
        </tr>
        <tr>
          <td>이메일</td>
          <td colspan="2">
            <div id="emailWrap">
              <TheInput class="emailInput" v-model="emailId" />@
              <TheInput class="emailInput" v-model="emailDomain" />
              <select v-model="emailDomain">
                <option v-for="option in options" :key="option.value" :value="option.value">
                  {{ option.text }}
                </option>
              </select>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <div>
              <button class="btn-findpw" @click="sendCode">이메일 확인</button>
            </div>
          </td>
          <td colspan="2">
            <TheInput v-model="inputcode" placeholder="임시 비밀번호 발급을 위해 이메일 확인을 눌러 임시코드를 입력해주세요" />
          </td>
        </tr>
        <tr v-if="isSendCode">
          <td colspan="3">이메일코드를 전송했습니다.</td>
        </tr>
        <tr>
          <td colspan="2"></td>
          <td>
            <div @click="mvJoin" style="display: inline-block" class="cursor">회원가입</div>
            |
            <a @click="toggleFindPw">로그인</a>
          </td>
        </tr>
        <tr>
          <td colspan="3">
            <button @click="getFindPw">임시 비밀번호 발급</button>
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
import TheInput from "../common/TheInput.vue";
import { mapActions, mapState } from "vuex";
import axios from "axios";
const userStore = "userStore";
export default {
  name: "TheLogin",
  components: {
    TheInput,
  },
  data() {
    return {
      findPw: false,
      saveid: "",
      user: {
        userId: "",
        password: "",
      },
      findPwData: {
        userId: "",
        email: "",
        type: ""
      },
      emailId: "",
      emailDomain: "naver.com",
      options: [
        { text: "네이버", value: "naver.com" },
        { text: "카카오", value: "kakao.com" },
        { text: "구글", value: "google.com" },
      ],
      code: "",
      inputcode: "",
      isSendCode: false
    }
  },
  created() {
    if (this.isLogin) {
      this.$router.push({ name: "home" }).catch(() => { });
    }
    this.user.userId = this.getId();
    if (this.user.userId && this.user.userId !== "") {
      this.saveid = true;
    }
  },
  computed: {
    ...mapState(userStore, ["isLogin"]),
  },
  methods: {
    sendCode() {
      this.isSendCode = false;
      this.findPwData.email = `${this.emailId}@${this.emailDomain}`;
      this.findPwData.type = "register";
      axios({
        method: "post",
        url: `${process.env.VUE_APP_BASE_URL}user/sendmail`,
        data: this.findPwData
      }).then(({ data }) => {
        if (data.code === "200") {
          this.code = data.emailCode;
          this.isSendCode = true;
        } else if (data.code === "401") {
          alert("이메일주소 및 아이디가 일치하지 않습니다. 확인해주세요!");
        }
      })
    },
    getFindPw() {
      if (this.code !== this.inputcode) {
        alert("이메일코드가 맞지 않습니다. 확인해주세요!");
        return;
      }
      this.findPwData.email = `${this.emailId}@${this.emailDomain}`;
      this.findPwData.type = "findPw";
      axios({
        method: "post",
        url: `${process.env.VUE_APP_BASE_URL}user/sendmail`,
        data: this.findPwData
      }).then(({ data }) => {
        if (data.code === "200") {
          this.code = "";
          this.emailCode = "";
          this.emailId = "";
          this.emailDomain = "naver.com";
          this.findPwData.userId = "";
          this.findPwData.email = "";
          this.findPwData.type = "";
          this.isSendCode = false;
          this.findPw = false;
          alert("임시 비밀번호를 전송했습니다.");
        } else if (data.code === "401") {
          alert("이메일주소 및 아이디가 일치하지 않습니다. 확인해주세요!");
        }
      })
    },
    toggleFindPw() {
      this.findPw = !this.findPw;
    },
    ...mapActions(userStore, ["userConfirm", "getUserInfo"]),
    login() {
      if (this.saveid) {
        this.setId(this.user.userId);
      } else {
        this.deleteId();
      }
      let msg = "";
      if (!this.user.userId || this.userId === "") {
        msg = "아이디를 입력해주세요.";
      }

      if (!this.user.password || this.user.password === "") {
        msg = "비밀번호를 입력해주세요.";
      }

      if (msg !== "") {
        alert(msg);
        return;
      }
      this.userConfirm(this.user);
    },
    setId(payload) {
      localStorage.setItem("enjoytrip_id", payload);
    },
    getId() {
      return localStorage.getItem("enjoytrip_id");
    },
    deleteId() {
      localStorage.removeItem("enjoytrip_id");
    },
    mvJoin() {
      this.$emit("closeModal");
      this.$router.push({ name: "join" });
    },
  },
};
</script>

<style scoped>
#saveid {
  width: auto;
}

#TheLogin>h2 {
  margin: 0 auto;
  width: fit-content;
  margin-top: 2rem;
}

.logo {
  width: 14rem;
  text-align: center;
  margin: 0 auto;
  display: inherit;
}

table {
  padding: 0 1rem;
  font-size: 1.2rem;
  font-weight: 900;
  height: 35rem;
  box-sizing: border-box;
  white-space: nowrap;
}

a {
  cursor: pointer;
}

input {
  height: 2rem;
  vertical-align: middle;
  border: 0;
  border-bottom: 1px solid #838383ba;
  width: 100%;
}

button {
  color: #007bff;
  font-weight: 900;
  font-size: 1.5rem;
  background-color: #ffffff;
  border: 1px solid;
  border-radius: 10px 10px 10px 10px;
  width: 30rem;
  height: 3rem;
  cursor: pointer;
}

button:hover {
  background-color: #007bff;
  color: white;
}

table> :last-child> :last-child {
  text-align: center;
}

.title {
  width: 100%;
  text-align: center;
}

.emailWrap {
  width: 100%;
}

.emailInput {
  width: 35%;
}

.btn-findpw {
  width: 12rem;
  font-size: 1.25rem;
}
</style>
