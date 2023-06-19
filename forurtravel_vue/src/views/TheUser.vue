<template>
  <div id="TheUser">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'TheUser',
  components: {

  },
  data() {
    return {
      isLogin: false,
    };
  },
  created() {
    
  },
  computed: {

  },
  methods: {},
};
</script>

<style scoped>
#TheUser{

}
</style>