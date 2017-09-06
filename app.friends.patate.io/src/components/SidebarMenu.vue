<template>
  <ul class="sidebar-menu">
    <li class="header">Personal Relationship Management</li>
      <li class="active pageLink" v-on:click="toggleMenu">
          <router-link to="/"><i class="fa fa-desktop"></i>
            <span class="page">Dashboard</span>
          </router-link>
      </li>
    <li class="treeview">
      <a href="#">
        <i class="fa fa-users"></i>
        <span>People</span>
        <span class="pull-right-container">
          <i class="fa fa-angle-left fa-fw pull-right"></i>
        </span>
      </a>
      <ul class="treeview-menu">
        <li class="pageLink" v-on:click="toggleMenu">
          <router-link to="/person">
            <i class="fa fa-user-plus"></i><span class="page">Add a person</span>
          </router-link>
        </li>
        <li class="pageLink" v-on:click="toggleMenu">
          <router-link to="/people">
            <i class="fa fa-users"></i><span class="page">Manage people</span>
          </router-link>
        </li>
      </ul>
    </li>
    <li class="pageLink" v-on:click="toggleMenu">
      <router-link to="/setting">
        <i class="fa fa-cog"></i>
        <span class="page">Settings</span>
      </router-link>
    </li>

  </ul>
</template>
<script>
export default {
  name: 'SidebarName',
  methods: {
    toggleMenu (event) {
      // remove active from li
      var active = document.querySelector('li.pageLink.active')

      if (active) {
        active.classList.remove('active')
      }
      // window.$('li.pageLink.active').removeClass('active')
      // Add it to the item that was clicked
      event.toElement.parentElement.className = 'pageLink active'
    }
  }
}
</script>
<style>
  /* override default */
  .sidebar-menu>li>a {
    padding: 12px 15px 12px 15px;
  }

  .sidebar-menu li.active>a>.fa-angle-left, .sidebar-menu li.active>a>.pull-right-container>.fa-angle-left {
    animation-name: rotate;
    animation-duration: .2s;
    animation-fill-mode: forwards;
  }

  @keyframes rotate {
    0% {
      transform: rotate(0deg);
    }

    100% {
      transform: rotate(-90deg);
    }
  }
</style>
