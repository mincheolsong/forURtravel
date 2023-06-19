<template>
  <textarea v-bind:value="value" v-on:input="updateInput"></textarea>
</template>

<script>
export default {
  name: "TheTextArea",
  props: ["value"],
  methods: {
    updateInput: function (event) {
      this.$emit("input", event.target.value);
    },
  },
};
</script>

<style scoped>
textarea {
  border: 1px solid #00000023;
  box-sizing: border-box;
  width: 100%;
  resize: none;
}
text-area:focus {
  outline: none;
}
</style>