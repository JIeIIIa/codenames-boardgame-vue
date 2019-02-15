<template>
  <div id="app" class="container-fluid">
    <router-view/>
  </div>
</template>

<style lang="scss">
  [v-cloak] {
    display: none;
  }

  body {
    min-height: 100vh;
    background-color: #fdc830; /* fallback for old browsers */
    background: -webkit-radial-gradient(ellipse at center, #fdc830, #f37335); /* Chrome 10-25, Safari 5.1-6 */
    background: radial-gradient(ellipse at center, #fdc830, #f37335); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
    background: url("assets/background.jpg") no-repeat center center fixed;
    background-size: cover;
  }
</style>
