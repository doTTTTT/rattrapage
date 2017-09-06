<template>
  <div class="container container-table">
      <div class="row vertical-10p">
        <div class="container">
          <img src="/static/img/logo.png" class="center-block logo">
          <div class="text-center col-md-4 col-sm-offset-4">
            <!-- errors -->
            <div v-if=response class="text-red"><p>{{response}}</p></div>

            <!-- login form -->
            <form class="ui form signupForm"  @submit.prevent="checkCreds">

              <div class="input-group">
                <span class="input-group-addon"><i class="fa fa-envelope"></i></span>
                <input class="form-control" name="email" required="required" placeholder="Email" type="email" v-model="email">
              </div>

              <div class="input-group">
                <span class="input-group-addon"><i class="fa fa-lock"></i></span>
                <input class="form-control" name="password" required="required" placeholder="Password" type="password" v-model="password">
              </div>
              <div class="input-group">
                <span class="input-group-addon"><i class="fa fa-lock"></i></span>
                <input class="form-control" name="passwordConfirmation" required="required" placeholder="Password Confirmation" type="password" v-model="passwordConfirmation">
              </div>
              <a href="/login" v-bind:class="'btn pull-left btn-default btn-lg'">Signin</a>
              <button type="submit" v-bind:class="'btn pull-right btn-primary btn-lg ' + loading">Create an account</button>
            </form>

          </div>
        </div>
      </div>
  </div>
</template>

<script>
import api from '../api'

export default {
  name: 'Signup',
  data (router) {
    return {
      section: 'Signup',
      loading: '',
      email: '',
      password: '',
      passwordConfirmation: '',
      response: ''
    }
  },
  methods: {
    checkCreds () {
      const {email, password, passwordConfirmation} = this

      this.toggleLoading()
      this.resetResponse()
      this.$store.commit('TOGGLE_LOADING')

      if (this.password !== this.passwordConfirmation) {
        console.log('debug password issue')
        this.toggleLoading()
        this.response = "Your passwords must match"
        return
      }
      /* Making API call to authenticate a user */
      api.request('post', '/users', {email, password, passwordConfirmation})
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
        } else {
          data.redirect = data.redirect != undefined ? data.redirect : '/login'
          this.$router.push(data.redirect)
        }
      })
      .catch(error => {
        this.$store.commit('TOGGLE_LOADING')
        if (error.response != undefined && error.response.status != undefined && error.response.status === 401) {
          this.response = 'Email/Password incorrect. Please try again'
        } else if (error.response != undefined && error.response.data.message != undefined) {
          this.response = error.response.data.message
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
.signupForm .input-group {
  padding-bottom: 1em;
  height: 4em;
}
.input-group input {
  height: 4em;
}
</style>
