<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div class="row justify-content-center">
    <div class="col-12 mt-4">
      <form>
        <div class="form-group mb-4">
          <h2><label for="roomTitle">Новая комната:</label></h2>
          <input type="text" class="form-control" id="roomTitle" placeholder="Введите название комнаты"
                 v-model="title">
        </div>
        <div class="row mx-0 my-5 justify-content-center">
          <router-link to="/rooms"
                       class="col-11 col-md-4 mb-3 mb-md-0
                        btn btn-primary form-control">
            К списку
          </router-link>
          <button type="button"
                  class="col-11 col-md-6
                        offset-0 offset-md-1
                        btn btn-success form-control"
                  v-on:click="createRoom">
            Создать
          </button>
        </div>
      </form>

      <alert
        v-bind:message="alertModal.text"
        v-bind:caption="alertModal.caption"
        v-bind:when-applied="alertModal.whenApplied">
      </alert>

    </div>
  </div>
</template>

<script>
  import Alert from '@/components/Alert.vue'
  import {ROOM_CREATE} from '../store/actions/room'
  import JQuery from 'jquery'
  import {GAME_CHIEF} from "../store/actions/game";

  export default {
    name: "NewRoom",
    data: function () {
      return {
        title: "",
        alertModal: {
          text: "default",
          caption: "default",
          whenApplied: function () {
          },
        }
      }
    },
    components: {
      Alert
    },
    methods: {
      createRoom: function () {
        console.info(`Start creating the room with the name = ${this.title}`)
        let room = new FormData();
        room.append('title', this.title);
        var ref = this;

        this.$store.dispatch(ROOM_CREATE, room)
          .then((room) => {
            let callback = function () {
              ref.$store.dispatch(GAME_CHIEF, room)
                .then(() => {
                  ref.$router.push("/game");
                })
                .catch(() => console.error("Cannot load the game"));
            };
            this.alert(
              'Комната успешно создана',
              'Информация',
              callback);
          })
          .catch((err) => {
            ref.alert(
              'Не удалось создать комнату ;(',
              'Ошибка',
              () => {
              });
            console.error("New room was not created");
          })
      },
      alert: function (msg, caption, whenApplied) {
        this.alertModal.text = msg;
        this.alertModal.caption = caption;
        this.alertModal.whenApplied = whenApplied;
        JQuery('#alertModal').modal('show');
      }
    }
  }
</script>

<style scoped>

</style>
