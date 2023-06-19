<template>
  <div id="setting-top">
    <h2>내정보 관리</h2>
    <table>
      <colgroup>
        <col width="10%" />
        <col width="80%" />
        <col width="10%" />
      </colgroup>
      <tr>
        <td>아이디</td>
        <td colspan="2"><TheInput :value="user.userId" disabled/></td>
      </tr>
      
      <tr>
        <td>이름</td>
        <td colspan="2"><TheInput :type="'text'" v-model="user.userName" /></td>
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
        <td colspan="2" class="Valid">비밀번호가 일치합니다 :)</td>
      </tr>
      <tr v-else>
        <td></td>
        <td colspan="2" class="NotValid">비밀번호가 일치하지 않습니다 :(</td>
      </tr>
      <tr id="emailTr">
        <td>이메일</td>
        <td colspan="2">
          <div id="emailWrap">
            <TheInput class="emailInput" v-model="emailId" />@
            <TheInput class="emailInput" v-model="emailDomain" />
            <select v-model="emailDomain">
              <option
                v-for="option in options"
                :key="option.value"
                :value="option.value"
              >
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
        <td colspan="2">
          <button @click="modify">수정</button>
        </td>
      </tr>
    </table>
  </div>
</template>

<script>
import TheInput from "../common/TheInput.vue";
import axios from 'axios';
import { mapActions, mapState } from "vuex";

export default {
  name: "TheSetting",
  components: { TheInput },
  data() {
    return {
      user: {
        userId: "",
        userName: "",
        password: "",
        email: "",
        file:"",
      },
      passwordCheck : "",
      emailId: "",
      emailDomain: "naver.com",
      options: [
        { text: "네이버", value: "naver.com" },
        { text: "카카오", value: "kakao.com" },
        { text: "구글", value: "google.com" },
      ],
    };
  },
  methods: {
    ...mapActions("userStore",["getUserInfo"]),
    
    modify() {
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

      const formData = new FormData();
      formData.append("userId", this.user.userId);
      formData.append("userNo", this.userInfo.userNo);
      formData.append("userName", this.user.userName);
      formData.append("password", this.user.password);
      formData.append("email", this.user.email);
      formData.append("file", this.user.file);
      axios({
        method: 'put',
        url: `${process.env.VUE_APP_BASE_URL}user/update-image`,
        headers: {
          'Content-Type':'multipart/form-data'
        },
        data:formData
      })
        .then(({ data }) => {
          console.log(data);
          if (data.code==="200") {
            alert("회원정보 수정완료");
            this.getUserInfo();
            this.$router.push({ name: "home" });
          }
        })
        .catch(()=>alert("잠시 후 시도해 주세요"));
    },
    setFile(event) {
      this.user.file = event.target.files[0];
    }
  },
  created() {
    this.user.userId = this.userInfo.userId;
    this.user.userName = this.userInfo.userName;
  },
  computed : {
    ...mapState("userStore", ["userInfo"]),
  }
  
};
</script>

<style scoped>

#setting-top{
  display : flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
table {
  padding: 1rem;
  font-size: 1.2rem;
  font-weight: 900;
  height: 35rem;
  box-sizing: border-box;
  width : 2vw;
  white-space: nowrap;
  box-shadow: 5px 5px 5px 5px #9595953d;
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
  align-items: center;
  justify-content: space-between;
}
.emailInput {
  width: 35%;
}
select {
  width: 22%;
  height: 2.3rem;
  border: none;
  box-shadow: 1px 1px 1px 1px #9595953d;
}
#btn-check-id {
  margin-left: 1rem;
  width: 6rem;
  height: 2rem;
  font-size: 1rem;
}
button {
  color: #0d6efd;
  border: 1px solid #0d6efd;
  font-weight: 900;
  font-size: 1.5rem;
  background-color: #fff;
  border-radius: 0.2rem;
  width: 30rem;
  height: 3rem;
  cursor: pointer;
}
button:hover{
  color: #fff;
  background-color: #0d6efd;

}
table > :last-child > :last-child {
  text-align: center;
}
</style>