// Import System requirements
import Vue from 'vue'
import VueRouter from 'vue-router'

import { sync } from 'vuex-router-sync'
import routes from './routes'
import store from './store'
import api from './api'
import axios from 'axios'

// Import Helpers for filters
import { domain, count, prettyDate, pluralize } from './filters'

// Import Views - Top level
import AppView from './components/App.vue'

// Import Install and register helper items
Vue.filter('count', count)
Vue.filter('domain', domain)
Vue.filter('prettyDate', prettyDate)
Vue.filter('pluralize', pluralize)

Vue.use(VueRouter)

// Routing logic
var router = new VueRouter({
    routes: routes,
    mode: 'history',
    scrollBehavior: function(to, from, savedPosition) {
        return savedPosition || { x: 0, y: 0 }
    }
})

function checkHeader() {
    let token = store.state.token
    if (token) {
        axios.defaults.headers.common['Authorization'] = token
    } else {
        axios.defaults.headers.common['Authorization'] = null
    }
}

// Some middleware to help us ensure the user is authenticated.
router.beforeEach((to, from, next) => {
    // window.console.log('Transition', transition)
    if (to.meta.auth) {
        if (window.localStorage.getItem('token') == null) {
            window.console.log('Not authenticated')
                // Check local storage to handle refreshes
            next({
                path: '/login',
                query: { redirect: to.fullPath }
            });
        } else {
            if (window.localStorage && store.state.token == null) {
                var localUserString = window.localStorage.getItem('user') || null
                var localUser = JSON.parse(localUserString)

                if (localUser && store.state.user !== localUser) {
                    store.commit('SET_USER', localUser)
                    store.commit('SET_TOKEN', window.localStorage.getItem('token'))
                }
            }
            checkHeader()
            api.request('get', '/people/user/me')
                .then(response => {
                    console.log('debug response => ', response)
                    store.commit('SET_PEOPLE', response.data)
                    next()
                })
                .catch(error => {
                    console.log('debug token => ', window.localStorage.getItem('token'))
                    store.commit('SET_PEOPLE', [])
                    console.log('error people ==> ', error)
                    next()
                })
        }
    } else {
        if (to.path === '/login' && window.localStorage.getItem('token') != null) {
            next({ path: '/' })
        } else {
            next()
        }
    }
})

sync(store, router)

// Start out app!
// eslint-disable-next-line no-new
new Vue({
    el: '#root',
    router: router,
    store: store,
    render: h => h(AppView)
})

// Check local storage to handle refreshes
if (window.localStorage) {
    var localUserString = window.localStorage.getItem('user') || null
    var localUser = JSON.parse(localUserString)

    if (localUser && store.state.user !== localUser) {
        store.commit('SET_USER', localUser)
        store.commit('SET_TOKEN', window.localStorage.getItem('token'))
    }
}