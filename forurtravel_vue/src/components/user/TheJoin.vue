<template>
  <div>
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
      <tr v-if="idValid">
        <td></td>
        <td colspan="2" class="Valid">
          사용 가능한 아이디 입니다.<button id="btn-check-id" @click="isValidId">
            아이디 확인
          </button>
        </td>
      </tr>
      <tr v-else>
        <td></td>
        <td colspan="2" class="NotValid">
          사용 불가능한 아이디 입니다.<button id="btn-check-id" @click="isValidId">
            아이디 확인
          </button>
        </td>
      </tr>
      <tr>
        <td>이름</td>
        <td colspan="2">
          <TheInput :type="'text'" v-model="user.userName" />
        </td>
      </tr>
      <tr>
        <td>비밀번호</td>
        <td colspan="2">
          <TheInput :type="'password'" v-model="user.password" />
        </td>
      </tr>
      <tr>
        <td>비밀번호 확인</td>
        <td colspan="2">
          <TheInput :type="'password'" v-model="passwordCheck" />
        </td>
      </tr>
      <tr v-if="user.password != '' && user.password == passwordCheck">
        <td></td>
        <td colspan="2" class="Valid">사용 가능한 비밀번호 입니다.</td>
      </tr>
      <tr v-else>
        <td></td>
        <td colspan="2" class="NotValid">사용 불가능한 비밀번호 입니다.</td>
      </tr>
      <tr id="emailTr">
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
        <input type="file" @change="setFile">
      </tr>
      <tr>
        <td colspan="3">
          <button @click="join">회원가입</button>
        </td>
      </tr>
    </table>
  </div>
</template>

<script>
import TheInput from "../common/TheInput.vue";
import axios from "axios";
export default {
  name: "TheJoin",
  components: { TheInput },
  data() {
    return {
      user: {
        userId: "",
        userName: "",
        password: "",
        email: "",
        file: "",
      },
      idValid: false,
      emailId: "",
      emailDomain: "naver.com",
      passwordCheck: "",
      options: [
        { text: "네이버", value: "naver.com" },
        { text: "카카오", value: "kakao.com" },
        { text: "구글", value: "google.com" },
      ],
    };
  },
  created() { },
  methods: {
    join() {
      let msg = "";
      if (!this.user.userId || this.user.userId === "") {
        msg = "아이디를 입력해 주세요!";
      }
      if (!this.user.userName || this.user.userName === "") {
        msg = "이름을 입력해 주세요!";
      }
      if (!this.user.password || this.user.password === "") {
        msg = "이름을 입력해 주세요!";
      }
      if (!this.emailId || this.emailId === "") {
        msg = "이메일을 입력해 주세요!";
      }
      if (!msg && msg !== "") {
        alert(msg);
        return;
      }

      this.user.email = `${this.emailId}@${this.emailDomain}`;
      if (!this.user.file || this.user.file === "") {
        axios({
          method: 'post',
          url: `${process.env.VUE_APP_BASE_URL}user/join-json`,
          data: {
            "userId": this.user.userId,
            "userName": this.user.userName,
            "password": this.user.password,
            "email": this.user.email,
          }
        })
          .then(({ data }) => {
            if (data.code === "200") {
              alert("회원가입이 완료되었습니다.");
              this.$router.push({ name: "home" });
            }
          })
          .catch(() => alert("잠시 후 시도해 주세요"));
        return;
      }
      const formData = new FormData();
      formData.append("userId", this.user.userId);
      formData.append("userName", this.user.userName);
      formData.append("password", this.user.password);
      formData.append("email", this.user.email);
      formData.append("file", this.user.file);
      axios({
        method: 'post',
        url: `${process.env.VUE_APP_BASE_URL}user/join-form`,
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        data: formData
      })
        .then(({ data }) => {
          if (data.code === "200") {
            alert("회원가입이 완료되었습니다.");
            this.$router.push({ name: "home" });
          }
        })
        .catch(() => alert("잠시 후 시도해 주세요"));
    },
    isValidId() {
      if (!this.user.userId || this.user.userId === "") {
        return;
      }
      axios
        .get(`${process.env.VUE_APP_BASE_URL}user/id-check/${this.user.userId}`)
        .then(({ data }) => this.idValid = (data.code === "200"))
        .catch(() => alert("잠시 후 시도해 주세요"));
    },
    setFile(event) {
      this.user.file = event.target.files[0];
    }
  },
};
</script>

<style scoped>
#TheJoin {
  width: 100vw;
}

table {
  padding: 1rem;
  font-size: 1.2rem;
  font-weight: 900;
  height: 35rem;
  box-sizing: border-box;
  box-shadow: 2px 3px 5px 0px rgba(0, 0, 0, 0.329);
  margin: 8rem 30rem;
  white-space: nowrap;
}

a {
  cursor: pointer;
}

input {
  height: 2rem;
  vertical-align: middle;
  text-align: center;
}

.Valid {
  color: #47e1a8;
}

.NotValid {
  color: #ff0000;
}

#emailWrap {
  display: flex;
  justify-content: space-between;
}

.emailInput {
  width: 35%;
}

select {
  width: 20%;
  height: 2.3rem;
  border: 1px solid #0000002d;
  border-radius: 5px;
}

#btn-check-id {
  margin-left: 1rem;
  width: 6rem;
  height: 2rem;
  font-size: 1rem;
}

button {
  color: #fff;
  font-weight: 900;
  font-size: 1.5rem;
  background-color: #007bff;
  border: 0;
  border-radius: 0.2rem;
  width: 30rem;
  height: 3rem;
  cursor: pointer;
}

table> :last-child> :last-child {
  text-align: center;
}
</style>