<template>
  <transition v-bind:name="getTransitionType()" mode="out-in">
    <div class="h-100 cursor-pointer"
         v-if="card.selected"
         v-bind:key="'text_' + card.id">
      <img class="img-fluid border-danger"
           v-bind:src="getObverse(card)"
           alt="image"
           v-bind:alt="getObverse(card)">
      <h5 class="word" v-if="isGameFinished">
        {{ card.word }}
      </h5>
    </div>
    <div class="h-100 cursor-pointer" v-bind:key="'card_' + card.id"
         v-on:click="select()"
         v-else>
      <img class="img-fluid border-danger"
           v-bind:class="{transparent: isChief}"
           v-bind:src="getReverse(card)"
           alt="getBack(card)">
      <h5 class="word">
        {{ card.word }}
      </h5>
    </div>
  </transition>
</template>

<script>
  import {mapGetters} from 'vuex';
  import {GAME_SELECT_CARD} from "../store/actions/game";

  export default {
    props: ['card'],
    name: "Card",
    computed: {
      ...mapGetters([
        'isAgent',
        'isChief',
        'getRole',
        'isGameFinished'
      ])
    },
    methods: {
      getObverse: function (card) {
        return require(`@/assets/${card.role}_${card.cover}.png`);
      },
      getReverse: function (card) {
        if (this.isAgent ) {
          return require(`@/assets/FON.png`);
        }
        return this.getObverse(card);
      },
      getTransitionType: function () {
        if (this.isChief) {
          return 'fade-cards'
        } else {
          return 'flip-to-cards'
        }
      },
      select: function () {
        if (this.isAgent && !this.card.selected) {
          this.$store.dispatch(GAME_SELECT_CARD, this.card.id)
            .catch(({err, cardId}) => console.error("card.id=", cardId, ": ", err));
        }
      }
    }
  }
</script>

<style scoped lang="scss">
  .cursor-pointer {
    cursor: pointer;
  }

  .agent-card .word {
    position: absolute;
    left: 50%;
    top: 60%;
    -webkit-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    color: black;
    text-shadow: 0px 0px 4px #fdc830,
    0px 0px 8px #f37335,
    0px 0px 16px #ffffff;
  }

  .transparent {
    opacity: 0.3;
  }

  .flip-to-cards {
    &-enter-active {
      transition: all 0.23s cubic-bezier(0.55, 0.085, 0.68, 0.53); //ease-in-quad
    }

    &-leave-active {
      transition: all 0.25s cubic-bezier(0.25, 0.46, 0.45, 0.94); //ease-out-quad
    }

    &-enter, &-leave-to {
      transform: scaleX(0) translateZ(0);
      opacity: 0;
    }
  }

  .fade-cards {
    &-enter-active {
      transition: opacity 0.5s;
      opacity: 1;
    }

    &-leave-active {
      transition: opacity 0.2s ;
    }

    &-enter {
      opacity: 0.3;
    }

    &-leave-to {
      opacity: 0.3;
    }

  }
</style>
