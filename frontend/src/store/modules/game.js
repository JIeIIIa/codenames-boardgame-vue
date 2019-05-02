import {GAME_AGENT, GAME_CHIEF, GAME_CREATE, GAME_EXIT, GAME_LOAD_CARDS, GAME_NEW_CARDS, GAME_SELECT_CARD, GAME_SET_ROOM} from "../actions/game";
import axios from 'axios'
import {AGENT_ROLE, BLUE, CHIEF_ROLE, KILLER, RED, UNDEFINED_ROLE} from "../../common/constants";
//const state = {token: localStorage.getItem('user-token') || '', status: '', hasLoadedOnce: false}

const ROOM_KEY = 'room'
const ROLE_KEY = 'role'
const CARDS_KEY = 'cards'
const state = {
  room: JSON.parse(localStorage.getItem(ROOM_KEY)) || {
    title: '',
    id: -1
  },
  role: localStorage.getItem(ROLE_KEY) || UNDEFINED_ROLE,
  cards: JSON.parse(localStorage.getItem(CARDS_KEY)) || []
}

const getters = {
  getCards: state => state.cards,
  isChief: state => state.role === CHIEF_ROLE,
  isAgent: state => state.role === AGENT_ROLE,
  getRole: state => state.role,
  isAuthorized: (state, getters) => getters['isAgent'] || getters['isChief'],
  roomTitle: state => state.room.title,
  roomId: state => state.room.id,
  allCardSelectedByType: (card_type, state) =>
    state.cards !== undefined &&
    state.cards
      .filter(card => !card.selected && card.role === card_type)
      .length === 0,
  isGameFinished: state =>
    getters.allCardSelectedByType(RED, state) ||
    getters.allCardSelectedByType(BLUE, state) ||
    getters.allCardSelectedByType(KILLER, state)
}

const actions = {
  [GAME_LOAD_CARDS]: ({commit, dispatch, getters}) => {
    return new Promise((resolve, reject) => {
      console.info("Loading cards for the room with id = " + getters.roomId)
      axios.get('/api/v1/rooms/' + getters.roomId + '/cards')
        .then(resp => {
          commit(GAME_LOAD_CARDS, resp.data)
          resolve()
        })
        .catch(err => {
          reject(err)
        })
    })
  },
  [GAME_AGENT]: ({commit, dispatch}, room) => {
    return new Promise((resolve, reject) => {
      commit(GAME_SET_ROOM, room)
      commit(GAME_AGENT)
      resolve()
    })
  },
  [GAME_CHIEF]: ({commit, dispatch}, room) => {
    return new Promise((resolve, reject) => {
      commit(GAME_SET_ROOM, room)
      commit(GAME_CHIEF)
      resolve()
    })
  },
  [GAME_SELECT_CARD]: ({commit, dispatch, getters}, cardId) => {
    return new Promise((resolve, reject) => {
      axios.put("/api/v1/rooms/" + getters.roomId + "/cards/" + cardId)
        .then(resp => {
          commit(GAME_SELECT_CARD, resp.data)
          resolve()
        })
        .catch(err => {
          reject(err, cardId)
        })
    })
  },
  [GAME_CREATE]: ({commit, dispatch}, room) => {
    return new Promise((resolve, reject) => {
      commit(GAME_CREATE, room)
      dispatch(GAME_NEW_CARDS)
      resolve()
    })
  },
  [GAME_NEW_CARDS]: ({commit, dispatch}) => {
    return new Promise((resolve, reject) => {
      axios.post("/api/v1/rooms/" + state.room.id + "/cards")
        .then(resp => {
          resolve()
        })
        .catch(err => {
          reject(err)
        })

    })
  },
  [GAME_EXIT]: ({commit, dispatch}) => {
    return new Promise((resolve, reject) => {
      commit(GAME_EXIT);
      resolve();
    })
  }
}

const mutations = {
  [GAME_LOAD_CARDS]: (state, cards) => {
    state.cards = cards
    localStorage.setItem(CARDS_KEY, JSON.stringify(cards))
  },
  [GAME_SET_ROOM]: (state, room) => {
    state.room.id = room.id
    state.room.title = room.title
    localStorage.setItem(ROOM_KEY, JSON.stringify(room))
  },
  [GAME_AGENT]: (state) => {
    state.role = AGENT_ROLE
    localStorage.setItem(ROLE_KEY, state.role)
  },
  [GAME_CHIEF]: (state) => {
    state.role = CHIEF_ROLE
    localStorage.setItem(ROLE_KEY, state.role)
  },
  [GAME_SELECT_CARD]: (state, newCard) => {
    state.cards.forEach(function (item, i) {
      if (item.id === newCard.id) {
        state.cards.splice(i, 1, newCard);
      }
    })
    localStorage.setItem(CARDS_KEY, JSON.stringify(state.cards))
  },
  [GAME_CREATE]: (state, room) => {
    state.room = room
    localStorage.setItem(ROOM_KEY, JSON.stringify(room))
  },
  [GAME_EXIT]: (state) => {
    state.room = {id: -1, title: ''}
    localStorage.removeItem(ROOM_KEY)
    state.cards = []
    localStorage.removeItem(CARDS_KEY)
    state.role = 'UNDEFINED'
    localStorage.setItem(ROLE_KEY, state.role)
  }
}

export default {
  state,
  getters,
  actions,
  mutations,
}
