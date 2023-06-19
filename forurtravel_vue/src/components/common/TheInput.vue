<template>
  <input v-bind:value="value" v-on:input="updateInput" />
</template>

<script>
export default {
  name: "TheInput",
  props: ["value"],
  methods: {
    updateInput: function (event) {
      this.$emit("input", event.target.value);
    },
    
  },
};
</script>

<style scoped>
input {
  width: 80%;
  box-sizing: border-box;
  border : none;
  border-bottom: 1px solid #00000024;
}
input:focus {
  outline: none;
}
input::placeholder {
  text-align: center;
  color: #4e4e4e;
}
</style>