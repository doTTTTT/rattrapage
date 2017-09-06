<template>
  <div>
    <h1 class="text-center">Settings</h1>
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <div class="box box-info">
            <!-- Input Addons -->
            <div class="box-header with-border">
              <h3 class="box-title">Update informations</h3>
            </div>
            <div class="box-body">
              <div v-if=response class="text-red"><p>{{response}}</p></div>
              <form class="ui form" @submit.prevent="updateInfos">
                <div class="input-group">
                  <span class="input-group-addon">
                    <i class="fa fa-fw fa-at" aria-hidden="true"></i>
                  </span>
                  <input class="form-control" placeholder="Firstname" type="text" v-model="firstname">
                </div>
                <br />
                <div class="input-group">
                  <span class="input-group-addon">
                    <i class="fa fa-fw fa-at" aria-hidden="true"></i>
                  </span>
                  <input class="form-control" placeholder="Lastname" type="text" v-model="lastname">
                </div>
                <br />

                <!-- with icons from font awesome -->
                <h4>Update password</h4>
                <div v-if=responsePassword class="text-red"><p>{{responsePassword}}</p></div>
                <div class="input-group">
                  <span class="input-group-addon">
                    <i class="fa fa-fw fa-usd" aria-hidden="true"></i>
                  </span>
                  <input class="form-control" type="password" placeholder="Password" v-model="password">
                </div>
                <br />
                <div class="input-group">
                  <span class="input-group-addon">
                    <i class="fa fa-fw fa-usd" aria-hidden="true"></i>
                  </span>
                  <input class="form-control" type="password" placeholder="Password confirmation" v-model="passwordConfirmation">
                </div>
                <br>
                <button type="submit" v-bind:class="'btn pull-right btn-primary btn-lg' + loading">Update Informations</button>
              </form>

              <!-- /input-group -->
            </div>
            <!-- /.box-body -->
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
<script>
require('moment')
import api from '../../api'
import _ from 'lodash'

export default {
  name: 'Settings',
  data (router) {
    return {
      section: 'Settings',
      loading: '',
      firstname: this.$store.state.user.firstname || null,
      lastname: this.$store.state.user.lastname || null,
      password: null,
      passwordConfirmation: null,
      response: '',
      responsePassword: '',
    }
  },
  computed: {
    datetime () {
      return new Date()
    }
  },
  methods: {
    clearInput (vueModel) {
      vueModel = ''
    },
    updateInfos() {
      const {firstname, lastname, password, passwordConfirmation} = this
      console.log('debug')
      this.toggleLoading()
      this.resetResponse()
      this.$store.commit('TOGGLE_LOADING')
      console.log('debug password: ', this.password, ', passwordConfirmation: ', this.passwordConfirmation)
      if (this.password !== null && (this.password !== this.passwordConfirmation || this.password.length == 0)) {
        console.log('debug here')
        this.toggleLoading()
        this.responsePassword = "Passwords must match and size > 0"
        return
      }
      this.firstname = this.firstname == '' ? null : this.firstname
      this.lastname = this.lastname == '' ? null : this.lastname
      if (!(this.firstname != null || this.lastname != null || this.password != null)) {
        this.toggleLoading()
        this.response = "You must update at least one field"
        return
      }
      let updated = _.merge(this.$store.state.user, {firstname: this.firstname, lastname: this.lastname, password: this.password})
      api.request('put', '/users/me', updated, {headers: {'Authorization': this.$store.state.token}})
      .then(response => {
        this.toggleLoading()
        var data = response.data
        this.$store.commit('SET_USER', data)

        if (window.localStorage) {
          window.localStorage.setItem('user', JSON.stringify(data))
        }
        this.$router.push('/setting')
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
      this.responsePassword = ''
    }
  }
}
</script>

<style>
.datetime-picker input {
  height: 4em !important;
}
</style>
