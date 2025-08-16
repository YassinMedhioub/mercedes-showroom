<template>
  <header :class="[
      'flex items-center justify-between p-5 flex-wrap gap-y-2',
      isDarkMode
        ? 'bg-black/80'
        : 'bg-white/90 border-b border-zinc-200'
    ]">
    <!-- Logo and Title -->
    <div class="flex items-center gap-3">
      <img src="../assets/mercedes-logo.png" class="w-10 h-10 sm:w-12 sm:h-12" />
      <div class="w-[2px] h-8 sm:h-10 bg-zinc-700 mx-2 sm:mx-3 shrink-0"></div>
      <div>
        <div :class="['text-lg font-bold', isDarkMode ? 'text-white' : 'text-zinc-900']">
          {{ texts[language].title }}
        </div>
        <div :class="['text-xs tracking-widest', isDarkMode ? 'text-zinc-300' : 'text-zinc-500']">
          Mercedes-Benz
        </div>
      </div>
    </div>

    <!-- Actions -->
    <div class="flex items-center gap-2 sm:gap-4 flex-wrap">
      <!-- Dark/Light mode toggle -->
      <button @click="toggleDarkMode"
        :class="['text-xl', isDarkMode ? 'text-white' : 'text-zinc-700', 'hover:scale-110 transition-transform']"
        :title="isDarkMode ? texts[language].lightMode : texts[language].darkMode">
        <span v-if="isDarkMode">🌙</span>
        <span v-else>☀️</span>
      </button>

      <!-- Language Switcher -->
      <div class="relative">
        <select
          v-model="language"
          @change="saveSettings"
          :class="[
            'appearance-none border rounded-lg text-xs sm:text-sm px-2 py-1 focus:outline-none pr-6',
            isDarkMode
              ? 'bg-black/40 border-zinc-700 text-white'
              : 'bg-white border-zinc-300 text-zinc-700'
          ]"
        >
          <option value="EN">EN</option>
          <option value="FR">FR</option>
        </select>
        <span class="pointer-events-none absolute right-2 top-1/2 -translate-y-1/2"
              :class="isDarkMode ? 'text-zinc-300' : 'text-zinc-500'">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
          </svg>
        </span>
      </div>

      <!-- Time and Date -->
      <div class="text-right mr-2 sm:mr-4">
        <div :class="['text-sm font-mono', isDarkMode ? 'text-zinc-200' : 'text-zinc-700']">{{ time }}</div>
        <div :class="['text-xs font-light', isDarkMode ? 'text-zinc-400' : 'text-zinc-500']">{{ date }}</div>
      </div>

      <!-- Logout -->
      <button @click="logout"
        :class="[
          'px-4 py-2 rounded-lg font-semibold transition-colors',
          isDarkMode
            ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
            : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'
        ]">
        {{ texts[language].logout }}
      </button>
    </div>
  </header>
</template>

<script setup>
import { onMounted, onUnmounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const logout = () => router.push('/login')

// State
const isDarkMode = ref(localStorage.getItem('darkMode') === 'true')
const language = ref(localStorage.getItem('lang') || 'FR')
const time = ref('')
const date = ref('')

// Texts
const texts = {
  FR: { title: "Mon espace", logout: "Déconnexion", lightMode: "Mode clair", darkMode: "Mode sombre" },
  EN: { title: "My Space", logout: "Logout", lightMode: "Light mode", darkMode: "Dark mode" }
}

// Functions
const toggleDarkMode = () => {
  isDarkMode.value = !isDarkMode.value
  saveSettings()
}

const saveSettings = () => {
  localStorage.setItem('darkMode', isDarkMode.value)
  localStorage.setItem('lang', language.value)
}

const updateDateTime = () => {
  const now = new Date()
  time.value = now.toLocaleTimeString(language.value === 'FR' ? 'fr-FR' : 'en-GB', { hour: '2-digit', minute: '2-digit', second: '2-digit' })
  date.value = now.toLocaleDateString(language.value === 'FR' ? 'fr-FR' : 'en-GB', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })
}

// Lifecycle
let intervalId
onMounted(() => {
  updateDateTime()
  intervalId = setInterval(updateDateTime, 1000)
})
onUnmounted(() => {
  clearInterval(intervalId)
})
watch(language, updateDateTime)
</script>
