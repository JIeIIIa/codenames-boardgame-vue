import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store/store'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import JQuery from 'jquery'

import {library} from '@fortawesome/fontawesome-svg-core'
import {faEnvelope} from '@fortawesome/free-solid-svg-icons'
import {faGithubSquare, faLinkedin, faSkype, faTelegramPlane, faViber} from '@fortawesome/free-brands-svg-icons'
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome'

library.add(faLinkedin, faGithubSquare, faEnvelope,
  faSkype, faViber, faTelegramPlane)

Vue.component('font-awesome-icon', FontAwesomeIcon)

Vue.config.productionTip = false;

/**
 * Enable Vuejs managing for Bootstrap tooltips for elements that have 'v-tooltip' attribute
 */
Vue.directive("tooltip", {
  bind: function (el) {
    JQuery(el).tooltip({trigger: "hover", 'delay': {show: 1000, hide: 100}});
  }
});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
