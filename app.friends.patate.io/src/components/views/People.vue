<template>
  <section class="content">
    <div v-if=person class="row center-block">
      <h2>People information</h2>
      <div class="col-md-12">
        <div class="box">
          <div class="box-header">
            <h3 class="box-title">{{person.firstname}} {{person.lastname}}</h3>
            <a href="javascript:;" v-on:click="deletePerson()" class="btn btn-danger pull-right">Delete</a>
            <a href="javascript:;" v-on:click="updatePerson()" class="btn btn-info pull-right">Update</a>
          </div>
          <!-- /.box-header -->
          <div class="box-body no-padding table-responsive">
            <table class="table table-striped">
              <tbody>
                <tr>
                  <th>Field</th>
                  <th>Information</th>
                </tr>
                <tr>
                  <td>Fullname</td>
                  <td>{{person.firstname}} {{person.lastname}}</td>
                </tr>
                <tr>
                  <td>Date of birth</td>
                  <td>{{person.date_of_birth}}</td>
                </tr>
                <tr>
                  <td>Relationship</td>
                  <td>{{person.relationship}}</td>
                </tr>
                <tr>
                  <td>Phone number</td>
                  <td>{{person.phone_number}}</td>
                </tr>
                <tr>
                  <td>Description</td>
                  <td>{{person.description}}</td>
                </tr>
                <tr>
                  <td>Address</td>
                  <td>{{person.address.street}}, {{person.address.zipcode}} {{person.address.city}}, {{person.address.state}}, {{person.address.country}}</td>
                </tr>
              </tbody>
            </table>
          </div>
          <!-- /.box-body -->
        </div>
      </div>
    </div>

    <div class="row center-block">
      <h2>People</h2>
      <div class="col-md-12">
        <div class="box">
          <div class="box-header">
            <h3 class="box-title">People list</h3>
          </div>
          <!-- /.box-header -->
          <div class="box-body">
<!--            <div class="dataTables_wrapper form-inline dt-bootstrap" id="example1_wrapper">
              <div class="row">
                <div class="col-sm-6">
                  <div id="example1_length" class="dataTables_length">

                  </div>
                </div>
              </div>-->

              <div class="row">
                <div class="col-sm-12 table-responsive">
                  <table aria-describedby="example1_info" role="grid" id="example1" class="table table-bordered table-striped dataTable">
                    <thead>
                      <tr role="row">
                        <th aria-label="Firstname: activate to sort column descending" aria-sort="ascending" style="width: 167px;" colspan="1" rowspan="1" aria-controls="example1" tabindex="0" class="sorting_asc">Firstname</th>
                        <th aria-label="Lastname: activate to sort column ascending" style="width: 207px;" colspan="1" rowspan="1" aria-controls="example1" tabindex="0" class="sorting">Lastname</th>
                        <th aria-label="Relationship: activate to sort column ascending" style="width: 207px;" colspan="1" rowspan="1" aria-controls="example1" tabindex="0" class="sorting">Relationship</th>
                        <th aria-label="Date of Birth: activate to sort column ascending" style="width: 182px;" colspan="1" rowspan="1" aria-controls="example1" tabindex="0" class="sorting">Date of Birth</th>
                        <th aria-label="Phone Number: activate to sort column ascending" style="width: 142px;" colspan="1" rowspan="1" aria-controls="example1" tabindex="0" class="sorting">Phone Number</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="(person, index) in people" v-bind:class="{'even' : index % 2 === 0, 'odd': index % 2 !== 0}" v-on:click="getPerson(index)" role="row">
                        <td class="sorting_1">{{person.firstname}}</td>
                        <td>{{person.lastname}}</td>
                        <td>{{person.relationship}}</td>
                        <td>{{person.date_of_birth}}</td>
                        <td>{{person.phone_number}}</td>
                      </tr>
                    </tbody>
                    <tfoot>
                      <tr>
                        <th colspan="1" rowspan="1">Firstname</th>
                        <th colspan="1" rowspan="1">Lastname</th>
                        <th colspan="1" rowspan="1">Relationship</th>
                        <th colspan="1" rowspan="1">Date of Birth</th>
                        <th colspan="1" rowspan="1">Phone Number</th>
                      </tr>
                    </tfoot>
                  </table>
                </div>
              </div>
            </div>
            <!-- /.box-body -->
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import $ from 'jquery'
// Require needed datatables modules
import 'datatables.net'
import 'datatables.net-bs'
import api from '../../api'

export default {
  name: 'Tables',
  data () {
    return {
      person: null,
      index: 0,
      people: this.$store.state.people
    }
  },
  mounted () {
    this.$nextTick(() => {
      $('#example1').DataTable()
    })
  },
  methods: {
    getPerson(index) {
      this.person = this.people[index]
      this.index = index
    },
    deletePerson () {
      api.request('delete', '/people/me/' + this.person._id)
      .then(response => {
        api.request('get', '/people/user/me')
          .then(response => {
            console.log('debug response => ', response)
            this.people = response.data
          })
          .catch(error => {
            console.log('error people ==> ', error)
        })
      })
      .catch(error => {
        console.log('error => ', error)
        this.$router.push('/people')
      })
    },
    updatePerson () {
      this.$router.push('/person/' + this.index)
    }
  }
}
</script>

<style>
/* Using the bootstrap style, but overriding the font to not draw in
   the Glyphicons Halflings font as an additional requirement for sorting icons.

   An alternative to the solution active below is to use the jquery style
   which uses images, but the color on the images does not match adminlte.

@import url('/static/js/plugins/datatables/jquery.dataTables.min.css');
*/

@import url('/static/js/plugins/datatables/dataTables.bootstrap.css');

table.dataTable thead .sorting:after,
table.dataTable thead .sorting_asc:after,
table.dataTable thead .sorting_desc:after {
  font-family: 'FontAwesome';
}

table.dataTable thead .sorting:after {
  content: "\f0dc";
}

table.dataTable thead .sorting_asc:after {
  content: "\f0dd";
}

table.dataTable thead .sorting_desc:after {
  content: "\f0de";
}
</style>
