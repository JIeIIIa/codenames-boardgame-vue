import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import store from './store/store'
import {GAME_EXIT} from "./store/actions/game";

Vue.use(Router);

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/rooms',
      name: 'rooms',
      component: () => import('./views/Rooms.vue')
    },
    {
      path: '/rooms/new',
      name: 'newRooms',
      component: () => import('./views/NewRoom.vue')
    },
    {
      path: "/game",
      name: "game",
      beforeEnter(to, from, next) {
        const isAuthorized = store.getters['isAuthorized'];
        if (isAuthorized) {
          next();
        } else {
          next('/rooms');
        }
      },
      component: () => import('./views/Game.vue')
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/About.vue')
    },
    {
      path: '/rules',
      name: 'rules',
      component: () => import('./views/Rules.vue')
    },
    {
      path: '*',
      beforeEnter(to, from, next) {
        next('/');
      }
    }
  ]
})

router.beforeResolve((to, from, next) => {
  if (to.name !== 'game') {
    store.dispatch(GAME_EXIT)
      .catch(() => console.log("Exit game failure"));
  }
  next();
})

export default router;
