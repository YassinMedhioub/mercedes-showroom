
import { createRouter, createWebHistory } from 'vue-router'
import { verifyUser } from '../services/auth'

/* Pages */
import AdminDashboard from '../views/AdminDashboard.vue'
import AvailableCars from '../views/AvailableCars.vue'
import CarDetails from '../views/CarDetails.vue'
import DriveBook from '../views/DriveBook.vue'
import Login from '../views/Login.vue'
import ManageCars from '../views/ManageCars.vue'
import ManageUsers from '../views/ManageUsers.vue'
import MeetAdvisor from '../views/MeetAdvisor.vue'
import MercedesTechs from '../views/MercedesTech.vue'
import SpecialOffers from '../views/SpecialOffers.vue'
import Statistics from '../views/Statistics.vue'
import UserDashboard from '../views/UserDashboard.vue'

const routes = [
    { path: '/login', name: 'login', component: Login },
    { path: '/dashboard', name: 'dashboard', component: UserDashboard },
    {
        path: '/admin',
        name: 'adminDashboard',
        component: AdminDashboard,
        meta: { requiresAdmin: true }
    },
    { path: '/cars', name: 'cars', component: AvailableCars },
    {
        path: '/car/:id',
        name: 'car',
        component: CarDetails,
        props: true,
        beforeEnter: (to, from, next) => {
            const id = Number(to.params.id)
            if (isNaN(id) || id <= 0) {
                return next({ name: 'cars' })
            }
            next()
        }
    },
    { path: '/driveBook', name: 'driveBook', component: DriveBook },
    { path: '/advisor', name: 'meetAdvisor', component: MeetAdvisor },
    { path: '/techs', name: 'techs', component: MercedesTechs },
    { path: '/offers', name: 'offers', component: SpecialOffers },
    {
        path: '/manage-users',
        name: 'manageUsers',
        component: ManageUsers,
        meta: { requiresAdmin: true }
    },
    {
        path: '/manage-cars',
        name: 'manageCars',
        component: ManageCars,
        meta: { requiresAdmin: true }
    },
    {
        path: '/statistics',
        name: 'statistics',
        component: Statistics,
        meta: { requiresAdmin: true }
    },
    { path: '/:pathMatch(.*)*', redirect: '/login' },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

/* Navigation guard for login + admin */
router.beforeEach(async (to, from, next) => {
    console.log('🟡 ROUTER GUARD STARTED:', {
        toPath: to.path,
        toName: to.name,
        fromPath: from.path,
        fromName: from.name
    })

    try {
        // Skip auth check for login page
        if (to.name === 'login') {
            console.log('🟢 SKIPPING AUTH - GOING TO LOGIN')
            next()
            return
        }

        const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'
        const role = localStorage.getItem('role') || null
        const token = localStorage.getItem('authToken')

        console.log('🔍 AUTH CHECK:', {
            isLoggedIn,
            role,
            hasToken: !!token,
            toRequiresAdmin: to.meta.requiresAdmin
        })

        // If not logged in, redirect to login
        if (!isLoggedIn) {
            console.log('🔴 NOT LOGGED IN - REDIRECT TO LOGIN')
            return next({ name: 'login' })
        }

        // Verify user session with backend
        console.log('🔍 VERIFYING USER SESSION...')
        const user = await verifyUser()
        console.log('🔍 VERIFICATION RESULT:', user ? 'SUCCESS' : 'FAILED', user)

        if (!user) {
            // Clear invalid session
            console.log('🔴 INVALID SESSION - CLEARING LOCALSTORAGE')
            localStorage.removeItem('isLoggedIn')
            localStorage.removeItem('authToken')
            localStorage.removeItem('role')
            localStorage.removeItem('adminName')
            return next({ name: 'login' })
        }

        // Admin route protection
        if (to.meta.requiresAdmin && role !== 'ADMIN') {
            console.log('🔴 ADMIN ROUTE - USER NOT ADMIN - REDIRECT TO DASHBOARD')
            return next({ name: 'dashboard' })
        }

        console.log('🟢 AUTH PASSED - ALLOWING NAVIGATION')
        // SUCCESS: Allow navigation
        next()

    } catch (error) {
        console.error('🔴 ROUTER GUARD ERROR:', error)
        // Clear auth on verification failure
        localStorage.removeItem('isLoggedIn')
        localStorage.removeItem('authToken')
        localStorage.removeItem('role')
        localStorage.removeItem('adminName')
        next({ name: 'login' })
    }
})

export default router