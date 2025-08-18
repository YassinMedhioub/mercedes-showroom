<template>
  <div :class="[{ dark: isDark }, 'min-h-screen','bg-gradient-to-br',
                isDark ? 'from-zinc-900 via-black to-gray-800'
                       : 'from-[#ffffffc8] via-[#ffffffc8] to-[#ffffffc8]',
                'flex','flex-col']">

    <!-- HEADER -->
    <header class="flex flex-wrap items-center justify-between w-full px-4 md:px-8 py-2
                   backdrop-blur sticky top-0 z-50 shadow-md gap-y-2"
            :class="isDark ? 'bg-black' : 'bg-white/60'">
      <div class="flex items-center gap-2 md:gap-4 min-w-0">
        <img src="../assets/mercedes-logo.png" alt="Mercedes-Benz Logo"
             class="w-10 h-10 sm:w-12 sm:h-12 object-contain shrink-0" />
        <div class="w-[2px] h-8 sm:h-10 bg-zinc-700 mx-2 sm:mx-3 shrink-0"></div>
        <div class="flex flex-col min-w-0">
          <span :class="[isDark ? 'text-white' : 'text-black',
                         'text-base sm:text-lg font-bold tracking-wide truncate']">
            Star Silver Sfax
          </span>
          <span class="text-[10px] sm:text-xs tracking-widest truncate"
                :class="isDark ? 'text-white/80' : 'text-black/80'">Mercedes-Benz</span>
        </div>
      </div>

      <div class="flex items-center gap-2 sm:gap-4 w-auto">
        <!-- Language selector -->
        <div class="relative">
          <select v-model="language"
                  class="appearance-none border rounded-lg text-xs sm:text-sm px-2 py-1 focus:outline-none pr-6"
                  :class="isDark ? 'bg-black text-white' : 'bg-white text-black'">
            <option value="EN">EN</option>
            <option value="FR">FR</option>
          </select>
          <span class="pointer-events-none absolute right-2 top-1/2 -translate-y-1/2 text-zinc-300">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
            </svg>
          </span>
        </div>

        <!-- Dark/Light toggle -->
        <button @click="toggleDark"
                class="ml-1 sm:ml-2 bg-zinc-800 hover:bg-zinc-600 p-2 rounded-lg text-white transition"
                :aria-label="language==='FR' ? 'Basculer le thème' : 'Toggle theme'">
          <svg v-if="!isDark" class="w-4 h-4 sm:w-5 sm:h-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round"
                  d="M21 12.79A9 9 0 1111.21 3a7 7 0 009.79 9.79z" />
          </svg>
          <svg v-else class="w-4 h-4 sm:w-5 sm:h-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <circle cx="12" cy="12" r="5" />
            <path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42" />
          </svg>
        </button>
      </div>
    </header>

    <!-- MAIN -->
    <main class="flex-1 flex flex-col items-center justify-center px-2 sm:px-0 py-4 relative">
      <img src="../assets/mercedes-logo.png" alt="Mercedes-Benz Logo"
           class="w-16 h-16 sm:w-20 sm:h-20 mb-4 sm:mb-6 opacity-90 drop-shadow-lg mx-auto" />

      <div class="w-full max-w-xs sm:max-w-md backdrop-blur-lg rounded-2xl shadow-2xl p-4 sm:p-8 mx-auto"
           :class="[isDark? 'bg-zinc-900/80' :'bg-zinc-200/90'] , {'animate-shake': !!error}">
        <h1 :class="[isDark ? 'text-white' : 'text-black',
                     'text-2xl sm:text-3xl font-extrabold tracking-wider mb-1 text-center']">
          {{ t('title') }}
        </h1>
        <p class="text-sm sm:text-base mb-4 tracking-tight text-center"
           :class="isDark? 'text-zinc-200/80' :'text-zinc-900/60'">
          {{ t('desc') }}
        </p>

        <form @submit.prevent="handleLogin" class="space-y-4 sm:space-y-6">
          <!-- Username -->
          <div>
            <label for="username" class="block mb-1 font-medium text-xs sm:text-sm"
                   :class="isDark? 'text-white' :'text-black'">{{ t('username') }}</label>
            <input id="username" v-model.trim="username" type="text" required autocomplete="username"
                   :placeholder="t('usernamePlaceholder')" :class="inputClass" />
          </div>

          <!-- Password -->
          <div>
            <label for="password" class="block mb-1 font-medium text-xs sm:text-sm"
                   :class="isDark? 'text-white' :'text-black'">{{ t('password') }}</label>
            <div class="relative">
              <input id="password" v-model.trim="password" :type="showPwd ? 'text' : 'password'"
                     required autocomplete="current-password" :placeholder="t('passwordPlaceholder')"
                     :class="inputClass + ' pr-10'"/>
              <button type="button" class="absolute inset-y-0 right-0 px-3 text-xs"
                      @click="showPwd = !showPwd" :aria-pressed="showPwd">
                {{ showPwd ? (language==='FR'?'Cacher':'Hide') : (language==='FR'?'Afficher':'Show') }}
              </button>
            </div>
          </div>

          <!-- Remember me -->
          <label class="inline-flex items-center gap-2 text-xs sm:text-sm"
                 :class="isDark ? 'text-zinc-200' : 'text-zinc-700'">
            <input type="checkbox" v-model="remember" class="rounded border-zinc-400">
            {{ language==='FR' ? 'Se souvenir de moi' : 'Remember me' }}
          </label>

          <!-- Submit -->
          <button type="submit" :disabled="loading"
                  class="w-full bg-gradient-to-r from-[#5d737e] via-gray-900 to-[#5d737e] disabled:opacity-60
                         hover:from-gray-800 hover:to-gray-900 text-white font-bold py-2.5 rounded-xl
                         shadow-lg transition-all duration-300 focus:ring-2 focus:ring-[#5d737e] text-sm sm:text-base">
            <span v-if="!loading">{{ t('signIn') }}</span>
            <span v-else>{{ language==='FR' ? 'Connexion…' : 'Signing in…' }}</span>
          </button>
        </form>

        <div v-if="error" class="text-red-400 text-center mt-3 text-xs sm:text-base">{{ error }}</div>
      </div>
    </main>

    <!-- FOOTER -->
    <footer class="w-full bg-black mt-4">
      <img v-if="isDark" src="../assets/footer.png" alt="Mercedes-Benz Brands" class="block w-full object-contain" />
      <img v-else src="../assets/footer-inverted.png" alt="Mercedes-Benz Brands" class="block w-full object-contain" />
      <hr>
      <div :class="[isDark ? 'bg-black text-zinc-300' : 'bg-white text-black',
                   'flex flex-col sm:flex-row items-center justify-center gap-1 sm:gap-2 text-xs sm:text-sm md:text-base py-2 px-2 w-full text-center']">
        &copy; 2025 Mercedes-Benz. All rights reserved.
      </div>
    </footer>
  </div>
</template>

<script setup>
import axios from 'axios'
import { computed, onMounted, ref, watch } from 'vue'

/** CONFIG **/
const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080'
const LOGIN_URL = `${API_BASE}/api/auth/login`

/** STATE **/
const emit = defineEmits(['login'])
const username = ref('')
const password = ref('')
const remember = ref(true)
const showPwd  = ref(false)
const loading  = ref(false)
const error    = ref('')

const language = ref(localStorage.getItem('lang') || 'EN')
const isDark   = ref(localStorage.getItem('darkMode') === 'true')

/** i18n **/
const translations = {
  EN: { title:'Mercedes Login', desc:'Sign in to your luxury experience',
        username:'Username', usernamePlaceholder:'Enter your username',
        password:'Password', passwordPlaceholder:'Enter your password',
        signIn:'Sign In' },
  FR: { title:'Connexion Mercedes', desc:'Connectez-vous à votre expérience de luxe',
        username:"Nom d'utilisateur", usernamePlaceholder:"Entrez votre nom d'utilisateur",
        password:'Mot de passe', passwordPlaceholder:'Entrez votre mot de passe',
        signIn:'Se connecter' }
}
const t = (key) => translations[language.value][key] || ''

/** UI classes **/
const inputClass = computed(() =>
  `w-full px-3 py-2.5 rounded-xl ${
    isDark.value
      ? 'bg-zinc-900/80 text-white placeholder:text-zinc-400 border border-white/20 focus:border-[#5d737e]'
      : 'bg-white text-black placeholder:text-zinc-500 border border-zinc-300 focus:border-[#5d737e]'
  } focus:ring-2 focus:ring-[#5d737e] outline-none transition text-sm`
)

/** Theme toggling **/
const updateHtmlClass = () => {
  const html = document.documentElement
  isDark.value ? html.classList.add('dark') : html.classList.remove('dark')
}
const toggleDark = () => {
  isDark.value = !isDark.value
  localStorage.setItem('darkMode', String(isDark.value))
  updateHtmlClass()
}
watch(language, val => localStorage.setItem('lang', val))
watch(isDark, () => localStorage.setItem('darkMode', String(isDark.value)))

/** Auto-handoff if already logged **/
onMounted(() => {
  updateHtmlClass()
  const logged = localStorage.getItem('isLoggedIn') === 'true'
  if (logged) {
    const role = localStorage.getItem('role') || 'USER'
    const landing = role === 'ADMIN' ? 'adminDashboard' : 'dashboard'
    localStorage.setItem('currentPage', landing)
    emit('login', { role })               // let AppShell switch without URL change
  }
})

/** Submit **/
async function handleLogin() {
  error.value = ''
  if (!username.value || !password.value) {
    error.value = language.value === 'FR'
      ? 'Veuillez entrer vos informations'
      : 'Please enter your credentials'
    return
  }

  loading.value = true
  try {
    const { data } = await axios.post(
      LOGIN_URL,
      { username: username.value, password: password.value },
      { headers: { 'Content-Type': 'application/json' }, timeout: 15000 }
    )

    // Expecting { token, role }
    const token = data?.token
    const role  = data?.role || 'USER'

    localStorage.setItem('isLoggedIn', 'true')
    localStorage.setItem('role', role)
    if (token) localStorage.setItem('authToken', token)

    const landing = role === 'ADMIN' ? 'adminDashboard' : 'dashboard'
    localStorage.setItem('currentPage', landing)

    // hand control to AppShell (no URL change)
    emit('login', { role })
  } catch (e) {
    error.value = language.value === 'FR'
      ? 'Identifiants incorrects ou serveur indisponible'
      : 'Invalid credentials or server unreachable'
  } finally {
    loading.value = false
  }
}
</script>

<style>
@keyframes shake { 0%{transform:translateX(0)}25%{transform:translateX(-5px)}50%{transform:translateX(5px)}75%{transform:translateX(-5px)}100%{transform:translateX(0)} }
.animate-shake { animation: shake 0.3s ease-in-out; }
</style>
