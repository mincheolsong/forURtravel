<template>
  <div id="TheHome">
    <div id="main-wrap">
      <!-- <video src="../assets/video/main.mp4" autoplay loop></video> -->
      <video src="../assets/video/main.mp4" autoplay loop></video>
      <div id="main-article">
        <div class="cursor" @click="moveSecondPage">For Your Travel</div>
      </div>
    </div>
    <article ref="secondpage">
      <HotPlaceSlider />
      <wordcloud
        :data="wordcludData"
        nameKey="word"
        valueKey="count"
        :showTooltip="false"
        :wordClick="() => {}"
      ></wordcloud>
    </article>
  </div>
</template>

<script>
import HotPlaceSlider from "@/components/main/HotPlaceSlider.vue";
import wordcloud from "vue-wordcloud";
import axios from "axios";
export default {
  name: "TheHome",
  components: {
    HotPlaceSlider,
    wordcloud,
  },
  data() {
    return { wordcludData: [] };
  },
  created() {
    axios
      .get(`${process.env.VUE_APP_BASE_URL}travel-review/wordcloud`)
      .then(({ data }) => {
        this.wordcludData = data.wordcloud;
      });
  },
  methods: {
    moveSecondPage() {
      this.$refs.secondpage.scrollIntoView({ behavior: "smooth" });
    },
  },
};
</script>

<style scoped>
.wordCloud {
  width: 100%;
  background-color: #2c3e50;
}
#TheHome {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

#main-wrap {
  width: 100%;
  height: calc(100vh - 100px);
  overflow: hidden;
  position: relative;
  /* background-color: #83adff; */
}

video {
  width: 100%;
  object-fit: fill;
}

#main-article {
  position: absolute;
  top: 40%;
  width: 100%;
  text-align: center;
  font-weight: 900;
  color: #fff;
  font-size: 4rem;
}

button {
  border: 0;
  border-radius: 0.4rem;
  background-color: #83adff;
  width: 6rem;
  height: 2rem;
  cursor: pointer;
  color: #fff;
  font-size: 1rem;
  font-weight: 900;
  text-align: center;
}

article {
  min-height: 100vh;
  align-items: center;
  overflow: hidden;
}
</style>