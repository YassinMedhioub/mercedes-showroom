<template>
  <div :class="[
      { dark: isDark },
      'min-h-screen',
      'bg-gradient-to-br',
      isDark
        ? 'from-zinc-900 via-black to-gray-800'
        : 'from-[#f7f7fa] via-[#f2f2f5] to-[#e5e7eb]',
      'flex','flex-col', 
    ]">



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
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import { usePrefs } from '../components/usePrefs'


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
  { emoji: "🎁", title: texts[language.value].offers,      desc: texts[language.value].offersDesc,   action: () => emit('navigate','offers') },
])



/* Expose isDark for template (alias) */
const isDarkMode = isDark
</script>
