<template>
  <div class="row justify-content-around">
    <div class="col-4">
      <router-link to="/rooms">
        <img class="img-fluid w-100 menu-img" src="@/assets/rooms.png" alt="rooms">
      </router-link>
    </div>
    <div class="col-4">
      <img class="img-fluid w-100 menu-img cursor-pointer" src="@/assets/new_agents.png" alt="agents"
           v-on:click="newGame"
           v-if="isChief">
    </div>
    <div class="col-4">
      <router-link to="/rules">
        <img class="img-fluid w-100 menu-img" src="@/assets/rules.png" alt="rooms">
      </router-link>
    </div>

    <alert
      v-bind:message="alertModal.text"
      v-bind:caption="alertModal.caption"
      v-bind:when-applied="alertModal.whenApplied">
    </alert>
  </div>


</template>

<script>

  import {GAME_NEW_CARDS} from "../store/actions/game";
  import {mapGetters} from 'vuex';
  import Alert from '@/components/Alert.vue'
  import JQuery from 'jquery'

  export default {
    name: "Menu",
    data: function () {
      return {
        alertMsg: '',
        alertModal: {
          text: '',
          caption: '',
          whenApplied: function () {
          }
        }
      }
    },
    computed: {
      ...mapGetters([
        'isChief',
      ])
    },
    components: {
      Alert
    },
    methods: {
      newGame: function () {
        this.$store.dispatch(GAME_NEW_CARDS)
          .then(() => {
            this.alert("Игра создана", "Информация");
          })
          .catch(() => {
            this.alert("Ошибка создания игры", "Ошибка");
          })
      },
      alert: function (msg, caption) {
        this.alertWithCallback(msg, caption, () => {
        });
      },
      alertWithCallback: function (msg, caption, whenApplied) {
        this.alertModal.text = msg;
        this.alertModal.caption = caption;
        this.alertModal.whenApplied = whenApplied;
        JQuery('#alertModal').modal('show');
      }
    }
  }
</script>

<style scoped lang="scss">
  .menu-img {
    position: relative;
    transform: rotate(-5deg);
    transition-duration: 300ms;
    z-index: 0;
    top: -80%;

    &:hover {
      transform: rotate(0deg);
      transition-duration: 300ms;
      z-index: 900;
      top: -5%;
    }
  }
</style>
