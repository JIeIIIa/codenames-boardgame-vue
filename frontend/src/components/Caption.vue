<template>
  <div class="row justify-content-center mb-2">
    <div class="col-11 text-primary text-center">
      <router-link to="/">
        <img class="img-fluid w-100 caption" src="@/assets/title.png" alt="codenames">
      </router-link>
    </div>
  </div>
</template>

<script>
  import Menu from "./Menu";

  export default {
    components: {Menu},
    name: "Caption"
  }
</script>

<style scoped>
  .bg-black {
    background: #000;
  }
  .menu {
    height: 0;
  }
</style>
