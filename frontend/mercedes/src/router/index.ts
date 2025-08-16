// src/router/index.js
import { createRouter, createWebHashHistory } from 'vue-router'
import AppShell from '../views/AppShell.vue'

const routes = [
    { path: '/', component: AppShell }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
