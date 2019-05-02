<template>
  <div class="row justify-content-center">
    <div class="col-11">
      <RoomsTable/>
    </div>
    <div class="col-11">
      <div class="row justify-content-around">
        <div class="col-5">
          <router-link to="/rooms/new" class="btn btn-info d-block">
            Создать
          </router-link>
        </div>
        <div class="col-5">
          <a href="javascript:void(0)" v-on:click="refresh" class="btn btn-info d-block">Обновить</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import RoomsTable from '@/components/RoomsTable.vue'
  import {ROOMS_LIST_REQUEST} from "../store/actions/roomsList";

  export default {
    name: "Rooms",
    components: {
      RoomsTable
    },
    methods: {
      refresh: function () {
        console.info('Rooms list updating...')
        this.$store.dispatch(ROOMS_LIST_REQUEST)
          .then(() => {
            console.info('The rooms list was successfully loaded')
          })
          .catch(() => {
            console.info('The rooms list loading failure')
          })
      }
    },
    mounted() {
      this.refresh();
    }
  }
</script>

<style scoped>

</style>
