<template>
  <div class="row justify-content-center">
    <div class="col-11 text-center py-2 text-primary">
      <img class="img-fluid w-100" src="@/assets/title.png" alt="codenames">
    </div>

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
  import HelloWorld from '@/components/HelloWorld.vue'

  export default {
    name: 'home',
    components: {
      HelloWorld
    }
  }
</script>
