<template>
  <div>
    <h1 class="text-center">Create or update a person</h1>
    <section class="content">
      <div class="row">
        <div class="col-md-12">
          <div class="box box-info">
            <!-- Input Addons -->
            <div class="box-header with-border">
              <h3 class="box-title">Information</h3>
            </div>

            <div class="box-body">                     
              <div v-if=response class="text-red"><p>{{response}}</p></div>
              <form class="ui form" @submit.prevent="updatePerson">
                <div class="input-group">
                  <span class="input-group-addon">
                    <i class="fa fa-fw fa-user" aria-hidden="true"></i>
                  </span>
                  <input class="form-control" placeholder="Firstname" type="text" v-model="person.firstname" required="required">
                </div>
                <div class="input-group">
                  <span class="input-group-addon">
                    <i class="fa fa-fw fa-user" aria-hidden="true"></i>
                  </span>
                  <input class="form-control" placeholder="Lastname" type="text" v-model="person.lastname" required="required">
                </div>
                <div class="input-group">
                  <span class="input-group-addon">
                    <i class="fa fa-fw fa-calendar"></i>
                  </span>
                  <input class="form-control" placeholder="Date of birth" type="date" v-model="person.date_of_birth">
                </div>
                <div class="input-group">
                  <span class="input-group-addon">
                    <i class="fa fa-fw fa-phone"></i>
                  </span>
                  <input class="form-control" placeholder="Phone number" type="text" v-model="person.phone_number">
                </div>
                <br />
                <!-- with icons from font awesome -->
                <h4>Address</h4>
                <div class="input-group">
                  <span class="input-group-addon"><i class="fa fa-fw fa-globe"></i></span>
                  <input class="form-control" placeholder="Street" type="text" v-model="person.address.street">
                </div>
                <div class="input-group">
                  <span class="input-group-addon"><i class="fa fa-fw fa-globe"></i></span>
                  <input class="form-control" placeholder="Zipcode" type="text" v-model="person.address.zipcode">
                </div>
                <div class="input-group">
                  <span class="input-group-addon"><i class="fa fa-fw fa-globe"></i></span>
                  <input class="form-control" placeholder="City" type="text" v-model="person.address.city">
                </div>
                <div class="input-group">
                  <span class="input-group-addon"><i class="fa fa-fw fa-globe"></i></span>
                  <input class="form-control" placeholder="State" type="text" v-model="person.address.state">
                </div>
                <div class="input-group">
                  <span class="input-group-addon"><i class="fa fa-fw fa-globe"></i></span>
                  <input class="form-control" placeholder="Country" type="text" v-model="person.address.country">
                </div>
                <br />
                <h4>Relationship</h4>
                <div class="form-group">
                  <select class="form-control" v-model="person.relationship" required="required">
                    <option>family</option>
                    <option>friend</option>
                    <option>colleague</option>
                    <option>acquaintance</option>
                  </select>
                </div>
                <br />
                <h4>Description</h4>
                <div class="input-group">
                  <span class="input-group-addon"><i class="fa fa-fw fa-info"></i></span>
                  <input class="form-control" placeholder="Description" type="text" v-model="person.description">
                </div>
                <br />
                <br />
                <button type="submit" v-bind:class="'btn pull-right btn-primary btn-lg' + loading">Submit</button>
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
import api from '../api'
import _ from 'lodash'

export default {
  name: 'Person',
  data (router) {
    return {
      person: {
        firstname: '',
        lastname: '',
        date_of_birth: null,
        relationship: 'friend',
        phone_number: '',
        address: {
          street: '',
          city: '',
          zipcode: '',
          country: '',
          state: '',
        },
        description: '',
        user_id: this.$store.state.user._id
      },
      loading: '',
      response: '',
      method: 'post',
      uri: '/people'
    }
  },
  mounted () {
    this.$nextTick(() => {
      console.log('debug id => ', this.$route.params.id)
      if (this.$route.params.id && this.$route.params.id < this.$store.state.people.length) {
        this.person = this.$store.state.people[this.$route.params.id]
        this.method = 'put'
        this.uri = '/people/me/' + this.person._id
      }
    })
  },
  methods: {
    updatePerson () {
      this.toggleLoading()
      this.resetResponse()
      this.$store.commit('TOGGLE_LOADING')

      api.request(this.method, this.uri, this.person)
      .then(response => {
        console.log('debug updatePerson => ', response.data)
        api.request('put', '/people/address/me/' + response.data._id, this.person.address)
        .then(response => {
          console.log('debug updateAddress => ', response.data.address)
          this.$router.push('/people')
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

