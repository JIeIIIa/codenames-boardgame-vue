import Vue from 'vue'
import Vuex from 'vuex'
import roomsList from './modules/roomsList'
import room from './modules/room'
import game from './modules/game'

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    roomsList,
    room,
    game
  },
  state: {},
  mutations: {},
  actions: {}
})
