
<template>
  <header
    :class="[
      // responsive height variable (no px)
      'min-h-[var(--appbar)] [--appbar:3.75rem] sm:[--appbar:4.5rem] lg:[--appbar:5rem]',
      'flex items-center justify-between p-5 flex-wrap gap-y-2 border-b',
      isDark
        ? '!bg-black !border-zinc-800 !text-white'
        : '!bg-white/90 !border-zinc-200 !text-zinc-900'
    ]"
  >
    <!-- Logo + title -->
    <div class="flex items-center gap-3">
      <img :src="logoUrl" alt="Mercedes-Benz logo" class="w-10 h-10 sm:w-12 sm:h-12" />
      <div class="w-[2px] h-8 sm:h-10 bg-zinc-700 dark:bg-zinc-500 mx-2 sm:mx-3 shrink-0"></div>
      <div>
        <div :class="['text-lg font-bold', isDark ? '!text-white' : '!text-zinc-900']">
          {{ t(props.isAdmin ? 'HeaderadminTitle' : 'Headertitle') }}
        </div>
        <div :class="['text-xs tracking-widest', isDark ? '!text-zinc-300' : '!text-zinc-500']">
          {{ t('MercedesBenz') }}
        </div>
      </div>
    </div>

    <!-- Actions -->
    <div class="flex items-center gap-2 sm:gap-4 flex-wrap">
      <!-- Dark/Light -->
      <button
        type="button"
        @click="toggleDark"
        class="text-xl text-zinc-700 dark:text-zinc-100 hover:scale-110 transition-transform"
        :title="isDark ? t('lightMode') : t('darkMode')"
        aria-label="Toggle dark mode"
      >
        <span v-if="isDark">🌙</span><span v-else>☀️</span>
      </button>

      <!-- Language -->
      <label class="relative inline-block">
        <span class="sr-only">{{ t('language') }}</span>
        <select
          v-model="language"
          class="appearance-none border rounded-lg text-xs sm:text-sm px-2 py-1 pr-6
                 bg-white dark:bg-black/40
                 border-zinc-300 dark:border-zinc-700
                 text-zinc-700 dark:text-white
                 focus:outline-none"
        >
          <option value="EN">EN</option>
          <option value="FR">FR</option>
        </select>
        <span
          class="pointer-events-none absolute right-2 top-1/2 -translate-y-1/2
                 text-zinc-500 dark:text-zinc-300"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-4 w-4"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
          >
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
          </svg>
        </span>
      </label>

      <!-- Time + Date -->
      <div class="text-right mr-2 sm:mr-4" :class="isDark ? 'text-white' : 'text-zinc-900'">
        <div class="text-sm font-mono">{{ time }}</div>
        <div class="text-xs font-light">{{ date }}</div>
      </div>

      <!-- Login/Logout -->
      <div v-if="isAuthenticated">
        <button
          type="button"
          @click="handleLogout"
          class="px-4 py-2 rounded-lg font-semibold transition-colors
                 bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200
                 dark:bg-[#5d737e] dark:text-white dark:hover:bg-zinc-700"
        >
          {{ t('Headerlogout') }}
        </button>
      </div>
      <div v-else>
        <router-link
          :to="{ path: '/login', query: { redirect: currentRoute } }"
          class="px-4 py-2 rounded-lg font-semibold transition-colors
                 bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200
                 dark:bg-[#5d737e] dark:text-white dark:hover:bg-zinc-700"
        >
          {{ t('Headerlogin') }}
        </router-link>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import logoUrl from '../assets/mercedes-logo.png'
import { usePrefs } from '../components/usePrefs'
import { useI18n } from '../composables/useI18n'

/** Props */
const props = defineProps({
  /** Set true on admin pages to switch title */
  isAdmin: { type: Boolean, default: false },
})

/** Events */
const emit = defineEmits(['logout'])
const handleLogout = () => {
  const confirmMessage = language.value === 'FR' ? 'Voulez-vous vraiment vous déconnecter ?' : 'Are you sure you want to log out?'
  if (window.confirm(confirmMessage)) {
    localStorage.removeItem('isLoggedIn')
    localStorage.removeItem('authToken')
    localStorage.removeItem('role')
    localStorage.removeItem('adminName')
    localStorage.removeItem('username')
    isAuthenticated.value = false // Update UI immediately
    emit('logout')
    router.push('/dashboard') // Redirect to dashboard
  }
}

/** Global prefs */
const { lang, isDark, toggleDark } = usePrefs()
const { t } = useI18n()
const language = lang

/** Authentication status */
const isAuthenticated = ref(localStorage.getItem('isLoggedIn') === 'true')

/** Router for redirect */
const router = useRouter()
const route = useRoute()
const currentRoute = computed(() => route.fullPath)

/** Time/Date */
const time = ref('')
const date = ref('')
function tick() {
  const now = new Date()
  const loc = language.value === 'FR' ? 'fr-FR' : 'en-GB'
  time.value = now.toLocaleTimeString(loc, { hour: '2-digit', minute: '2-digit', second: '2-digit' })
  date.value = now.toLocaleDateString(loc, { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })
}
let timer
onMounted(() => {
  tick()
  timer = setInterval(tick, 1000)
})
onUnmounted(() => clearInterval(timer))
watch(language, tick)
</script>