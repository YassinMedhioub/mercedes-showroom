import { createApp } from 'vue'
import App from './App.vue'
import './assets/main.css'
import router from './router'

const app = createApp(App)
app.use(router)

router.isReady().then(() => {
    // Force hash to be exactly "#" (no trailing "/")
    if (location.hash === '#/' || location.hash === '') {
        history.replaceState(null, '', '#')
    }
    app.mount('#app')
})
