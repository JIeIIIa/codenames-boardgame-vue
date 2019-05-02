<template>
  <div id="app" class="container-fluid content">
    <div class="row">
      <div class="col-12 caption">
        <Caption/>
      </div>
      <div class="col-12 menu">
        <Menu/>
      </div>
    </div>
    <transition name="fade-component" mode="out-in">
      <router-view class="field"/>
    </transition>
    <div class="filler"></div>
    <div class="row">
      <Footer></Footer>
    </div>
  </div>
</template>

<style lang="scss">
  [v-cloak] {
    display: none;
  }

  html, body {
    height: 100%;
    min-width: 350px;
  }

  .content {
    min-height: 100%;
    display: flex;
    flex-direction: column;
  }

  body {
    min-height: 100vh;
    background-color: #fdc830; /* fallback for old browsers */
    background: -webkit-radial-gradient(ellipse at center, #fdc830, #f37335); /* Chrome 10-25, Safari 5.1-6 */
    background: radial-gradient(ellipse at center, #fdc830, #f37335); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
    background: url("assets/background.jpg") no-repeat center center fixed;
    background-size: cover;
  }

  .caption {
    background: #000;
    z-index: 1000;
  }

  .menu {
    height: 0;
    position: relative;
  }

  .field {
    margin-top: 7vw;
  }

  .cursor-pointer {
    cursor: pointer;
  }

  .fade-component {
    $transition-fade-delay: 0.5s;

    &-enter {
      opacity: 0;
    }

    &-enter-active {
      transition: opacity $transition-fade-delay ease;
    }

    &-leave {
    }

    &-leave-active {
      transition: opacity $transition-fade-delay ease;
      opacity: 0;
    }
  }

  .filler {
    flex: 1 0 auto;
  }
</style>
<script>
  import Caption from "./components/Caption";
  import Menu from "./components/Menu";
  import Footer from "./components/Footer/Footer";

  export default {
    components: {Menu, Caption, Footer}
  }
</script>
