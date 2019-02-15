<template>
  <div class="row justify-content-center">
    <Caption/>

    <div class="col-7 m-3">
      <router-link to="/rooms" class="btn btn-info d-block">Выбор игры</router-link>
    </div>

    <div class="col-11 col-md-6 offset-4 mx-3">
      <img class="img-fluid" src="@/assets/man_woman.png" alt="man_woman.png">
    </div>
  </div>
</template>

<script>
  // @ is an alias to /src
  import Caption from '@/components/Caption.vue'

  export default {
    name: 'home',
    components: {
      Caption
    }
  }
</script>
