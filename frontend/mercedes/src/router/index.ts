import { createRouter, createWebHistory } from 'vue-router'
import AdminDashboard from '../views/AdminDashboard.vue'
import Login from '../views/Login.vue'
import UserDashboard from '../views/UserDashboard.vue'

const routes = [
    { path: '/login', component: Login },
    { path: '/adminDashboard', component: AdminDashboard },
    { path: '/userDashboard', component: UserDashboard }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
