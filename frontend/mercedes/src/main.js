import { createApp } from 'vue'
import App from './App.vue'
import './assets/main.css'
import router from './router'

const app = createApp(App)
app.use(router)

router.isReady().then(() => {
    if (location.hash === '#/' || location.hash === '') {
        history.replaceState(null, '', '#')
    }
    app.mount('#app')
})
