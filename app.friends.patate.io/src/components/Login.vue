<template>
  <div class="container container-table">
      <div class="row vertical-10p">
        <div class="container">
          <img src="/static/img/logo.png" class="center-block logo">
          <div class="text-center col-md-4 col-sm-offset-4">
            <!-- errors -->
            <div v-if=response class="text-red"><p>{{response}}</p></div>

            <!-- login form -->
            <form class="ui form loginForm"  @submit.prevent="checkCreds">

              <div class="input-group">
                <span class="input-group-addon"><i class="fa fa-envelope"></i></span>
                <input class="form-control" name="email" placeholder="Email" required="required" type="email" v-model="email">
              </div>

              <div class="input-group">
                <span class="input-group-addon"><i class="fa fa-lock"></i></span>
                <input class="form-control" name="password" placeholder="Password" required="required" type="password" v-model="password">
              </div>
              <a href="/signup" v-bind:class="'btn pull-left btn-default btn-lg'">Create an account</a>
              <button type="submit" v-bind:class="'btn pull-right btn-primary btn-lg ' + loading">Signin</button>
            </form>

          </div>
        </div>
      </div>
  </div>
</template>

<script>
import api from '../api'

export default {
  name: 'Login',
  data (router) {
    return {
      section: 'Login',
      loading: '',
      email: '',
      password: '',
      response: ''
    }
  },
  methods: {
    checkCreds () {
      const {email, password} = this

      this.toggleLoading()
      this.resetResponse()
      this.$store.commit('TOGGLE_LOADING')

      /* Making API call to authenticate a user */
      api.request('post', '/auth/basic', {email, password})
      .then(response => {
        this.toggleLoading()
        var data = response.data
        /* Checking if error object was returned from the server */
        if (data.error) {
          var errorName = data.error.name // Not sure about, I must check on the server
          if (errorName) {
            this.response = errorName === 'InvalidCredentialsError'
            ? 'Email/Password incorrect. Please try again.'
            : errorName
          } else {
            this.response = data.error
          }
          return
        }
        /* Setting user in the state and caching record to the localStorage */
        if (data.user) {
          var token = 'Bearer ' + data.token

          this.$store.commit('SET_USER', data.user)
          this.$store.commit('SET_TOKEN', token)

          if (window.localStorage) {
            window.localStorage.setItem('user', JSON.stringify(data.user))
            window.localStorage.setItem('token', token)
          }
          data.redirect = data.redirect != undefined ? data.redirect : '/'
          this.$router.push(data.redirect)
        }
      })
      .catch(error => {
        this.$store.commit('TOGGLE_LOADING')
        if (error.response != undefined && error.response.status != undefined && error.response.status === 401) {
          this.response = 'Email/Password incorrect. Please try again'
        } else {
          this.response = 'Server appears to be offline'
        }
        this.toggleLoading()
      })
    },
    toggleLoading () {
      this.loading = (this.loading === '') ? 'loading' : ''
    },
    resetResponse () {
      this.response = ''
    }
  }
}
</script>

<style>
html, body, .container-table {
  height: 100%;
  background-color: #282B30 !important;
}
.container-table {
    display: table;
    color: white;
}
.vertical-center-row {
    display: table-cell;
    vertical-align: middle;
}
.vertical-20p {
  padding-top: 20%;
}
.vertical-10p {
  padding-top: 10%;
}
.logo {
  width: 15em;
  padding: 3em;
}
.loginForm .input-group {
  padding-bottom: 1em;
  height: 4em;
}
.input-group input {
  height: 4em;
}
</style>
