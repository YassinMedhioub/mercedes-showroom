<template>
  <div :class="[
      { dark: isDark },
      'min-h-screen',
      'bg-gradient-to-br',
      isDark
        ? 'from-zinc-900 via-black to-gray-800'
        : 'from-[#f7f7fa] via-[#f2f2f5] to-[#e5e7eb]',
      'flex','flex-col'
    ]">

    <!-- HEADER (same vibe as AppShell) -->
    <header :class="[
        'flex items-center justify-between p-5 flex-wrap gap-y-2',
        isDark ? 'bg-black/80' : 'bg-white/90 border-b border-zinc-200'
      ]">
      <div class="flex items-center gap-3">
        <img src="../assets/mercedes-logo.png" class="w-10 h-10 sm:w-12 sm:h-12" />
        <div class="w-[2px] h-8 sm:h-10 bg-zinc-700 mx-2 sm:mx-3 shrink-0"></div>
        <div>
          <div :class="['text-lg font-bold', isDark ? 'text-white' : 'text-zinc-900']">
            {{ texts[language].title }}
          </div>
          <div :class="['text-xs tracking-widest', isDark ? 'text-zinc-300' : 'text-zinc-500']">
            Mercedes-Benz
          </div>
        </div>
      </div>

      <div class="flex items-center gap-2 sm:gap-4 flex-wrap">
        <!-- Dark/Light toggle -->
        <button
          @click="toggleDark()"
          :class="['text-xl', isDark ? 'text-white' : 'text-zinc-700', 'hover:scale-110 transition-transform']"
          :title="isDark ? texts[language].lightMode : texts[language].darkMode"
        >
          <span v-if="isDark">🌙</span>
          <span v-else>☀️</span>
        </button>

        <!-- Language -->
        <div class="relative">
          <select
            v-model="language"
            class="appearance-none border rounded-lg text-xs sm:text-sm px-2 py-1 focus:outline-none pr-6"
            :class="isDark ? 'bg-black/40 border-zinc-700 text-white' : 'bg-white border-zinc-300 text-zinc-700'"
          >
            <option value="EN">EN</option>
            <option value="FR">FR</option>
          </select>
          <span class="pointer-events-none absolute right-2 top-1/2 -translate-y-1/2"
                :class="isDark ? 'text-zinc-300' : 'text-zinc-500'">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
            </svg>
          </span>
        </div>

        <!-- Time & Date -->
        <div class="text-right mr-2 sm:mr-4">
          <div :class="['text-sm font-mono', isDark ? 'text-zinc-200' : 'text-zinc-700']">{{ time }}</div>
          <div :class="['text-xs font-light', isDark ? 'text-zinc-400' : 'text-zinc-500']">{{ date }}</div>
        </div>

        <!-- Logout -->
        <button
          @click="emit('logout')"
          :class="[
            'px-4 py-2 rounded-lg font-semibold transition-colors',
            isDark ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
                   : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'
          ]"
        >
          {{ texts[language].logout }}
        </button>
      </div>
    </header>

    <!-- MAIN (cards only) -->
    <main class="flex-1 w-full">
      <section class="flex flex-col items-center justify-center py-8 sm:py-12 w-full">
        <h1 :class="['mb-4 font-bold text-2xl sm:text-3xl', isDark ? 'text-white' : 'text-zinc-800']">
          {{ texts[language].welcome }}
        </h1>
        <p :class="['mb-8', isDark ? 'text-zinc-300' : 'text-zinc-600', 'text-sm sm:text-base']">
          {{ texts[language].intro }}
        </p>

        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 sm:gap-8 w-full max-w-5xl px-2 sm:px-4 md:px-8">
          <button
            v-for="(card, idx) in cardList"
            :key="idx"
            @click="card.action()"
            :class="[
              'rounded-xl p-5 sm:p-6 shadow-xl text-center transition-all duration-200 cursor-pointer group w-full min-h-[170px] hover:scale-[1.03] block',
              isDark
                ? 'bg-zinc-900/80 text-white hover:bg-gray-300 hover:text-black'
                : 'bg-white/90 text-zinc-800 border border-zinc-200 hover:bg-gray-600 hover:text-white'
            ]"
          >
            <div class="mb-2 transition-colors duration-200 text-3xl sm:text-4xl"
                 :class="isDark ? 'text-white group-hover:text-black' : 'text-[#2e3a45] group-hover:text-white'">
              {{ card.emoji }}
            </div>
            <div class="font-semibold text-base sm:text-lg mb-1">{{ card.title }}</div>
            <div class="text-xs sm:text-sm transition-colors duration-200"
                 :class="isDark ? 'dark:text-white group-hover:text-black' : 'text-zinc-600 group-hover:text-white'">
              {{ card.desc }}
            </div>
          </button>
        </div>
      </section>
    </main>

    <!-- FOOTER -->
    <footer class="w-full bg-black mt-4">
      <img
        v-if="isDark"
        src="../assets/footer.png"
        alt="Mercedes-Benz Brands"
        class="block w-full object-contain"
        style="margin-bottom:0; padding-bottom:0;"
      />
      <img
        v-else
        src="../assets/footer-inverted.png"
        alt="Mercedes-Benz Brands"
        class="block w-full object-contain"
        style="margin-bottom:0; padding-bottom:0;"
      />
      <hr />
      <div
        :class="[
          isDark ? 'bg-black text-zinc-300' : 'bg-white text-black',
          'flex flex-col sm:flex-row items-center justify-center gap-1 sm:gap-2 text-xs sm:text-sm md:text-base py-2 px-2 w-full text-center'
        ]"
      >
        &copy; 2025 Mercedes-Benz. All rights reserved.
      </div>
    </footer>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import { usePrefs } from '../components/usePrefs'

onMounted(() => console.log('[mount] UserDashboard'))
onUnmounted(() => console.log('[unmount] UserDashboard'))

/* Global prefs (single source of truth) */
const { lang, isDark, toggleDark } = usePrefs()
const language = lang

/* Emit to parent (AppShell) */
const emit = defineEmits(['logout','navigate'])

/* Date & time reactive to language */
const time = ref('')
const date = ref('')
function updateDateTime() {
  const now = new Date()
  const loc = language.value === 'FR' ? 'fr-FR' : 'en-GB'
  time.value = now.toLocaleTimeString(loc, { hour:'2-digit', minute:'2-digit', second:'2-digit' })
  date.value = now.toLocaleDateString(loc, { weekday:'long', year:'numeric', month:'long', day:'numeric' })
}
let intervalId
onMounted(() => {
  updateDateTime()
  intervalId = setInterval(updateDateTime, 1000)
})
onUnmounted(() => clearInterval(intervalId))
watch(language, updateDateTime)

/* i18n */
const texts = {
  FR: {
    title: "Mon espace",
    welcome: "Bienvenue, Cher Visiteur",
    intro: "Retrouvez ici les services et informations de votre showroom.",
    logout: "Déconnexion",
    lightMode: "Mode clair",
    darkMode: "Mode sombre",
    models: "Découvrir nos modèles",
    modelsDesc: "Gamme, nouveautés, prix",
    tech: "Technologies Mercedes",
    techDesc: "Innovation, sécurité, connectivité",
    testDrive: "Demander un essai",
    testDriveDesc: "Réservez un essai sur route",
    advisor: "Rencontrer un conseiller",
    advisorDesc: "Prenez rendez-vous aujourd'hui",
    offers: "Offres spéciales",
    offersDesc: "Promotions et avantages en showroom"
  },
  EN: {
    title: "My Space",
    welcome: "Welcome, Dear Visitor",
    intro: "Find here your showroom services and information.",
    logout: "Logout",
    lightMode: "Light mode",
    darkMode: "Dark mode",
    models: "Explore Our Models",
    modelsDesc: "Range, new arrivals, prices",
    tech: "Mercedes Technologies",
    techDesc: "Innovation, safety, connectivity",
    testDrive: "Book a Test Drive",
    testDriveDesc: "Reserve a road test",
    advisor: "Meet an Advisor",
    advisorDesc: "Book an appointment today",
    offers: "Special Offers",
    offersDesc: "Showroom promotions and advantages"
  }
}

/* Cards → emit to AppShell (no internal routing here) */
const cardList = computed(() => [
  { emoji: "🚗", title: texts[language.value].models,      desc: texts[language.value].modelsDesc,   action: () => emit('navigate','cars') },
  { emoji: "💡", title: texts[language.value].tech,        desc: texts[language.value].techDesc,     action: () => emit('navigate','techs') },
  { emoji: "📝", title: texts[language.value].testDrive,   desc: texts[language.value].testDriveDesc,action: () => emit('navigate','driveBook') },
  { emoji: "📅", title: texts[language.value].advisor,     desc: texts[language.value].advisorDesc,  action: () => emit('navigate','meetAdvisor') },
  { emoji: "🎁", title: texts[language.value].offers,      desc: texts[language.value].offersDesc,   action: () => emit('navigate','specials') },
])

/* Expose isDark for template (alias) */
const isDarkMode = isDark
</script>
