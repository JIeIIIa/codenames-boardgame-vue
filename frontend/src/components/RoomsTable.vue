<template>
  <div class="row">
    <div class="col-12">
      <table class="table table-warning table-striped">
        <thead class="thead-dark">
        <tr class="row m-0 text-center">
          <th scope="col" class="col-1">#</th>
          <th scope="col" class="col">Название</th>
          <th scope="col" class="col-4">Роль</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(value, key) in roomsList" class="row m-0">
          <th scope="row" class="col-1"> {{ key + 1 }}</th>
          <td class="col">{{ value.title }}</td>
          <td class="col-4">
            <div class="row justify-content-around">
              <a v-on:click.prevent="loadGame('GAME_CHIEF', value)"
                 class="col-11 col-md-5 mb-2 mb-md-0 btn btn-secondary cursor-pointer">
                Капитан
              </a>
              <a v-on:click.prevent="loadGame('GAME_AGENT', value)"
                 class="col-11 col-md-5 btn btn-success cursor-pointer">
                Агент
              </a>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
  import {mapGetters, mapState} from 'vuex'
  import {GAME_AGENT, GAME_CHIEF, GAME_EXIT} from "../store/actions/game";

  export default {
    name: "RoomsTable",
    computed: {
      ...mapGetters(['roomsList']),
      ...mapState({})
    },
    methods: {
      loadGame: function (role, room) {
        if(role !== GAME_CHIEF && role !== GAME_AGENT) {
          console.error("Error! Unknown role: " + role)
          return
        }
        this.$store.dispatch(role, room)
          .then(() => this.$router.push("/game"))
          .catch(() => {
            console.error("Error! Can't load the game for the " + role)
            this.$store.dispatch(GAME_EXIT)
              .catch(() => console.error("Exit game failure"))
          })
      }
    }
  }
</script>

<style scoped>

</style>
