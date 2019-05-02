<template>
  <div class="col-12">
    <div class="row justify-content-start" id="words-list" v-cloak>
      <div class="col-2 col-lg-2 text-center my-1 h-100 agent-card" v-for="(card, index) in cards"
           v-bind:class="{'offset-1' : index % 5 === 0}">
        <Card v-bind:card="card"></Card>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex';
  import Card from "../components/Card";

  export default {
    props: ['cards'],
    name: "Field",
    computed: {
      ...mapGetters([
        'isGameFinished'
      ])
    },
    components: {Card},
  }
</script>

<style scoped lang="scss">
</style>
