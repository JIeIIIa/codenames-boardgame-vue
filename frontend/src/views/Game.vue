<template>
  <div class="row">
    <div class="col-12 text-center mb-3">
      <h1>Комната: <i class="text-primary">{{ roomTitle }}</i></h1>
    </div>
    <div class="col-12 text-center mb-5">
      <Field v-bind:cards="getCards"/>
    </div>
  </div>
</template>

<script>
  import {GAME_LOAD_CARDS} from '../store/actions/game'
  import Field from "../components/Field";
  import {mapGetters} from 'vuex';

  export default {
    name: "Game",
    data: function () {
      return {
        interval: null
      }
    },
    components: {Field},
    computed: {
      ...mapGetters([
        'getCards', 'roomTitle'
      ])
    },
    methods: {
      tick: function (doAjax, interval) {
        setTimeout(doAjax, 50000);
      },
      loadCards: function () {
        this.$store.dispatch(GAME_LOAD_CARDS)
          .then(() => {
            console.info(new Date() + ":  " + "Cards were loaded")
          })
          .catch(() => {
            console.error("Error! Cards weren't loaded")
          })
      }
    },
    mounted() {
      this.loadCards();

      this.interval = setInterval(function () {
        this.loadCards();
      }.bind(this), 5000);
    },
    beforeDestroy() {
      clearInterval(this.interval);
    }

  }
</script>

<style scoped>
  .field {
    min-height: 300px;
  }

</style>
