import DashView from './components/Dash.vue'
import LoginView from './components/Login.vue'
import NotFoundView from './components/404.vue'

// Import Views - Dash
import DashboardView from './components/views/Dashboard.vue'
import PeopleView from './components/views/People.vue'
import SettingView from './components/views/Setting.vue'
import PersonView from './components/Person.vue'
import SignupView from './components/Signup.vue'

// Routes
const routes = [{
        path: '/login',
        component: LoginView
    },
    {
        path: '/signup',
        component: SignupView
    },
    {
        path: '/',
        component: DashView,
        children: [{
                path: 'dashboard',
                alias: '',
                component: DashboardView,
                name: 'Dashboard',
                meta: { description: 'Overview of environment', 'auth': true }
            },
            {
                path: 'people',
                component: PeopleView,
                name: 'People',
                meta: { description: 'Get informations about your friends / family / ...', 'auth': true }
            }, {
                path: 'person',
                component: PersonView,
                name: 'Person',
                meta: { description: 'Add a person', auth: true }
            }, {
                path: 'person/:id',
                component: PersonView,
                name: 'Person',
                meta: { description: 'Update a person', auth: true }
            }, {
                path: 'setting',
                component: SettingView,
                name: 'Settings',
                meta: { description: 'User settings page', 'auth': true }
            }
        ]
    }, {
        // not found handler
        path: '*',
        component: NotFoundView
    }
]

export default routes