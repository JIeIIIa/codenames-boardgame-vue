import {ROOM_CREATE, ROOM_FAILURE, ROOM_SUCCESS} from '../actions/room'
import {ROOMS_LIST_ADD} from '../actions/roomsList'
import {GAME_CREATE} from "../actions/game";
import axios from 'axios'

const state = {status: ''}

const getters = {
  newRoomStatus: state => state.status,
}

const actions = {
  [ROOM_CREATE]: ({commit, dispatch}, room) => {
    return new Promise((resolve, reject) => {
      commit(ROOM_CREATE)
      axios.post('/api/v1/rooms', room)
        .then(resp => {
          let room = resp.data
          commit(ROOM_SUCCESS)
          commit(ROOMS_LIST_ADD, room)
          dispatch(GAME_CREATE, room)
          resolve(room)
        })
        .catch(err => {
          commit(ROOM_FAILURE)
          reject()
        })
    })
  }
}

const mutations = {
  [ROOM_CREATE]: (state) => {
    state.status = 'creating'
  },
  [ROOM_SUCCESS]: (state) => {
    state.status = 'created'
  },
  [ROOM_FAILURE]: (state) => {
    state.status = 'failure'
  }
}

export default {
  state,
  getters,
  actions,
  mutations,
}
