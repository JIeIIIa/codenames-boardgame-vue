import {ROOMS_LIST_ADD, ROOMS_LIST_ERROR, ROOMS_LIST_REQUEST, ROOMS_LIST_SUCCESS} from '../actions/roomsList'
import axios from 'axios'

const state = {rooms: [], status: ''}

const getters = {
  roomsStatus: state => state.status,
  roomsList: state => state.rooms.sort((a, b) => a.id - b.id)
}

const actions = {
  [ROOMS_LIST_REQUEST]: ({commit, dispatch}) => {
    return new Promise((resolve, reject) => {
      commit(ROOMS_LIST_REQUEST)
      axios.get('/api/v1/rooms')
        .then(resp => {
          let rooms = resp.data
          commit(ROOMS_LIST_SUCCESS, rooms)
          resolve()
        })
        .catch(err => {
          commit(ROOMS_LIST_ERROR)
          reject(err)
        })
    })
  }
}

const mutations = {
  [ROOMS_LIST_REQUEST]: (state) => {
    state.status = 'loading'
  },
  [ROOMS_LIST_SUCCESS]: (state, rooms) => {
    state.status = 'success'
    state.rooms = rooms
  },
  [ROOMS_LIST_ERROR]: (state) => {
    state.status = 'error'
    state.rooms = []
  },
  [ROOMS_LIST_ADD]: (state, room) => {
    state.status = 'updated'
    state.rooms.push(room)
  }

}

export default {
  state,
  getters,
  actions,
  mutations,
}
