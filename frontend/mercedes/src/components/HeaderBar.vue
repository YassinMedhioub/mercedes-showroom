<!-- src/components/HeaderBar.vue -->
<template>
  <header :class="[
      'flex items-center justify-between p-5 flex-wrap gap-y-2',
      isDark ? 'bg-black/80' : 'bg-white/90 border-b border-zinc-200'
    ]">
    <!-- Logo + title -->
    <div class="flex items-center gap-3">
      <img src="../assets/mercedes-logo.png" class="w-10 h-10 sm:w-12 sm:h-12" />
      <div class="w-[2px] h-8 sm:h-10 bg-zinc-700 mx-2 sm:mx-3 shrink-0"></div>
      <div>
        <div :class="['text-lg font-bold', isDark ? 'text-white' : 'text-zinc-900']">
          {{ texts[language].title }}
        </div>
        <div :class="['text-xs tracking-widest', isDark ? 'text-zinc-300' : 'text-zinc-500']">Mercedes-Benz</div>
      </div>
    </div>

    <!-- Actions -->
    <div class="flex items-center gap-2 sm:gap-4 flex-wrap">
      <!-- Dark/Light -->
      <button
        @click="toggleDark()"
        :class="['text-xl', isDark ? 'text-white' : 'text-zinc-700', 'hover:scale-110 transition-transform']"
        :title="isDark ? texts[language].lightMode : texts[language].darkMode">
        <span v-if="isDark">🌙</span><span v-else>☀️</span>
      </button>

      <!-- Language -->
      <div class="relative">
        <select
          v-model="language"
          class="appearance-none border rounded-lg text-xs sm:text-sm px-2 py-1 focus:outline-none pr-6"
          :class="isDark ? 'bg-black/40 border-zinc-700 text-white' : 'bg-white border-zinc-300 text-zinc-700'">
          <option value="EN">EN</option>
          <option value="FR">FR</option>
        </select>
        <span class="pointer-events-none absolute right-2 top-1/2 -translate-y-1/2"
              :class="isDark ? 'text-zinc-300' : 'text-zinc-500'">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
          </svg>
        </span>
      </div>

      <!-- Time + Date -->
      <div class="text-right mr-2 sm:mr-4">
        <div :class="['text-sm font-mono', isDark ? 'text-zinc-200' : 'text-zinc-700']">{{ time }}</div>
        <div :class="['text-xs font-light', isDark ? 'text-zinc-400' : 'text-zinc-500']">{{ date }}</div>
      </div>

      <!-- Logout -->
      <button
        @click="$emit('logout')"
        :class="[
          'px-4 py-2 rounded-lg font-semibold transition-colors',
          isDark ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
                 : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'
        ]">
        {{ texts[language].logout }}
      </button>
    </div>
  </header>
</template>

<script setup>
import { onMounted, onUnmounted, ref, watch } from 'vue'
import { usePrefs } from './usePrefs'

const emit = defineEmits(['logout'])

/* global prefs */
const { lang, isDark, toggleDark } = usePrefs()
const language = lang

/* time/date */
const time = ref('')
const date = ref('')
function tick() {
  const now = new Date()
  const loc = language.value === 'FR' ? 'fr-FR' : 'en-GB'
  time.value = now.toLocaleTimeString(loc, { hour:'2-digit', minute:'2-digit', second:'2-digit' })
  date.value = now.toLocaleDateString(loc, { weekday:'long', year:'numeric', month:'long', day:'numeric' })
}
let timer
onMounted(() => { tick(); timer = setInterval(tick, 1000) })
onUnmounted(() => clearInterval(timer))
watch(language, tick)

/* i18n */
const texts = {
  FR: { title: 'Mon espace', logout: 'Déconnexion', lightMode: 'Mode clair', darkMode: 'Mode sombre' },
  EN: { title: 'My Space',   logout: 'Logout',      lightMode: 'Light mode', darkMode: 'Dark mode'   }
}
</script>
