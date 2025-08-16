<template>
  <div >

    <!-- HERO -->
    <section class="w-full max-w-6xl mx-auto px-3 sm:px-6 pt-8 sm:pt-12">
      
      <div class="w-full max-w-6xl mx-auto px-3 sm:px-6 mb-2 flex items-center justify-between">
        <!-- Left: Title only -->
        <h1 :class="[
              'font-extrabold leading-tight',
              isDarkMode ? 'text-white' : 'text-zinc-900',
              'text-3xl sm:text-4xl'
            ]">
          {{ t('techTitle') }}
        </h1>

        <!-- Right: Back button -->
        <button
          @click="emit('navigate','home')"
          class="px-3 py-2 rounded-lg text-sm font-medium transition-colors"
          :class="isDarkMode
            ? 'bg-zinc-800 text-white hover:bg-zinc-700'
            : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'"
        >
          ← {{ language === 'FR' ? 'Retour' : 'Back' }}
        </button>
      </div>

<!-- Subtitle on the next row -->
<p :class="['max-w-3xl mb-6 sm:mb-8',
            isDarkMode ? 'text-zinc-300' : 'text-zinc-700']">
  {{ t('techSubtitle') }}
</p>


      

      <!-- Quick tags -->
      <div class="flex flex-wrap gap-2 sm:gap-3 mb-8">
        <span class="px-3 py-1 rounded-full text-xs sm:text-sm border border-zinc-300/60 dark:border-white/20 text-zinc-700 dark:text-zinc-200">MBUX</span>
        <span class="px-3 py-1 rounded-full text-xs sm:text-sm border border-zinc-300/60 dark:border-white/20 text-zinc-700 dark:text-zinc-200">ADAS</span>
        <span class="px-3 py-1 rounded-full text-xs sm:text-sm border border-zinc-300/60 dark:border-white/20 text-zinc-700 dark:text-zinc-200">4MATIC</span>
        <span class="px-3 py-1 rounded-full text-xs sm:text-sm border border-zinc-300/60 dark:border-white/20 text-zinc-700 dark:text-zinc-200">EQ</span>
        <span class="px-3 py-1 rounded-full text-xs sm:text-sm border border-zinc-300/60 dark:border-white/20 text-zinc-700 dark:text-zinc-200">AMG</span>
        <span class="px-3 py-1 rounded-full text-xs sm:text-sm border border-zinc-300/60 dark:border-white/20 text-zinc-700 dark:text-zinc-200">Connectivity</span>
      </div>
    </section>

    <!-- FEATURE CARDS -->
    <section class="w-full max-w-6xl mx-auto px-3 sm:px-6 pb-14">
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        <article
          v-for="(f, i) in features"
          :key="i"
          :class="[
            'rounded-2xl p-5 shadow-xl transition-all duration-200 group',
            isDarkMode
              ? 'bg-zinc-900/90 text-white hover:bg-gray-300 hover:text-black'
              : 'bg-white text-zinc-800 border border-zinc-200 hover:bg-gray-600 hover:text-white'
          ]"
        >
          <div class="text-4xl mb-3">{{ f.emoji }}</div>
          <h3 class="font-semibold text-lg mb-1">{{ f.title }}</h3>
          <p :class="[isDarkMode ? 'text-zinc-300' : 'text-zinc-600', 'text-sm mb-4']">
            {{ f.desc }}
          </p>

          <!-- bullets -->
          <ul class="space-y-1.5 mb-4">
            <li v-for="(b, j) in f.bullets" :key="j"
                class="flex items-start gap-2 text-sm">
              <span class="mt-0.5">•</span>
              <span :class="isDarkMode ? 'text-zinc-200' : 'text-zinc-100 group-hover:text-white'">
                {{ b }}
              </span>
            </li>
          </ul>

          <!-- details -->
          <details class="rounded-lg"
                   :class="isDarkMode ? 'bg-black/20' : 'bg-white/40'">
            <summary class="cursor-pointer px-3 py-2 rounded-lg text-sm font-semibold
                            hover:underline select-none">
              {{ t('learnMore') }}
            </summary>
            <div :class="[isDarkMode ? 'text-zinc-200' : 'text-zinc-100 group-hover:text-white', 'text-sm px-3 py-2']">
              {{ f.more }}
            </div>
          </details>
        </article>
      </div>
    </section>

    <!-- CTA -->
    <section class="w-full max-w-6xl mx-auto px-3 sm:px-6 pb-16">
      <div :class="[
            'rounded-2xl p-6 sm:p-8 shadow-xl text-center',
            isDarkMode ? 'bg-zinc-900/80 text-white' : 'bg-white text-zinc-800 border border-zinc-200'
          ]">
        <h2 class="text-xl sm:text-2xl font-bold mb-2">{{ t('ctaTitle') }}</h2>
        <p :class="[isDarkMode ? 'text-zinc-300' : 'text-zinc-600', 'mb-6']">{{ t('ctaSubtitle') }}</p>
        <div class="flex flex-col sm:flex-row items-center justify-center gap-3">
          <router-link
            to="/cars"
            :class="[
              'px-5 py-3 rounded-lg font-semibold transition-colors',
              isDarkMode
                ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
                : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'
            ]"
          >
            {{ t('exploreModels') }}
          </router-link>
          <router-link
            to="/test-drive"
            :class="[
              'px-5 py-3 rounded-lg font-semibold transition-colors',
              isDarkMode
                ? 'bg-zinc-800 text-white hover:bg-zinc-700'
                : 'bg-white text-zinc-700 border border-zinc-200 hover:bg-zinc-200'
            ]"
          >
            {{ t('bookTestDrive') }}
          </router-link>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'

/* ---- Router / Logout ---- */
const router = useRouter()
const logout = () => router.push('/login')
const emit = defineEmits(['navigate'])

/* ---- Persistent preferences ---- */
const isDarkMode = ref(localStorage.getItem('darkMode') === 'true')
const language = ref(localStorage.getItem('lang') || 'FR')

watch(isDarkMode, (val) => {
  localStorage.setItem('darkMode', val)
  val
    ? document.documentElement.classList.add('dark')
    : document.documentElement.classList.remove('dark')
})
watch(language, (val) => localStorage.setItem('lang', val))

/* ---- Time & Date ---- */
const time = ref('')
const date = ref('')
function updateDateTime() {
  const now = new Date()
  const locale = language.value === 'FR' ? 'fr-FR' : 'en-GB'
  time.value = now.toLocaleTimeString(locale, { hour: '2-digit', minute: '2-digit', second: '2-digit' })
  date.value = now.toLocaleDateString(locale, { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })
}

let intervalId
onMounted(() => {
  isDarkMode.value
    ? document.documentElement.classList.add('dark')
    : document.documentElement.classList.remove('dark')

  updateDateTime()
  intervalId = setInterval(updateDateTime, 1000)
  window.addEventListener('keydown', onKey)
})
onUnmounted(() => {
  clearInterval(intervalId)
  window.removeEventListener('keydown', onKey)
})

/* ---- i18n (simple) ---- */
const i18n = {
  FR: {
    back: 'Retour',
    logout: 'Déconnexion',
    darkMode: 'Mode sombre',
    lightMode: 'Mode clair',
    techTitle: 'Technologies Mercedes',
    techSubtitle: "Innovation, sécurité et connectivité au service d'une expérience de conduite unique.",
    learnMore: 'En savoir plus',
    ctaTitle: 'Prêt à vivre l’innovation Mercedes ?',
    ctaSubtitle: 'Découvrez les modèles équipés des dernières technologies ou réservez un essai.',
    exploreModels: 'Découvrir nos modèles',
    bookTestDrive: 'Demander un essai',
  },
  EN: {
    back: 'Back',
    logout: 'Logout',
    darkMode: 'Dark mode',
    lightMode: 'Light mode',
    techTitle: 'Mercedes Technologies',
    techSubtitle: 'Innovation, safety and connectivity for a remarkable driving experience.',
    learnMore: 'Learn more',
    ctaTitle: 'Ready to experience Mercedes innovation?',
    ctaSubtitle: 'Explore models with the latest tech or book a test drive.',
    exploreModels: 'Explore Our Models',
    bookTestDrive: 'Book a Test Drive',
  }
}
const t = (k) => i18n[language.value][k] || k

/* ---- Theme toggle ---- */
const toggleDarkMode = () => { isDarkMode.value = !isDarkMode.value }

/* ---- Small-screen menu state + ESC to close ---- */
const menuOpen = ref(false)
function onKey(e) {
  if (e.key === 'Escape') menuOpen.value = false
}

/* ---- Feature content ---- */
const features = ref([
  {
    emoji: '🧠',
    title: 'MBUX & Voice',
    desc: 'Assistant vocal naturel, écrans haute résolution, navigation augmentée pour une interaction intuitive.',
    bullets: [
      'Commande vocale “Hey Mercedes”',
      'Écrans tactiles & HUD',
      'Navigation AR et suggestions proactives',
    ],
    more: 'MBUX s’adapte à vos habitudes : itinéraires favoris, confort, multimédia. L’interface évolue avec des mises à jour en ligne.'
  },
  {
    emoji: '🛡️',
    title: 'ADAS & Sécurité',
    desc: 'Aides avancées à la conduite pour plus de sérénité au quotidien.',
    bullets: [
      'DISTRONIC (régulateur de distance)',
      'PRE-SAFE et freinage d’urgence assisté',
      'Maintien de voie & angle mort actif',
    ],
    more: 'Les systèmes ADAS surveillent l’environnement, avertissent et peuvent intervenir pour éviter ou atténuer un accident.'
  },
  {
    emoji: '⚡',
    title: 'EQ & Électrification',
    desc: 'Technologies EQ pour l’électrique et l’hybride rechargeable.',
    bullets: [
      'Gestion intelligente de l’énergie',
      'Planification de charge sur itinéraire',
      'Récupération d’énergie au freinage',
    ],
    more: 'Les modèles EQ optimisent performance et autonomie, avec infos de charge en temps réel et services connectés.'
  },
  {
    emoji: '🧭',
    title: '4MATIC',
    desc: 'Transmission intégrale intelligente pour une motricité optimale.',
    bullets: [
      'Répartition de couple adaptative',
      'Adhérence améliorée route/pluie/neige',
      'Conduite stable et dynamique',
    ],
    more: 'Le système 4MATIC gère en continu la motricité selon l’adhérence et le style de conduite pour plus de sécurité.'
  },
  {
    emoji: '🏁',
    title: 'AMG Performance',
    desc: 'Châssis, freins et modes de conduite calibrés pour la sportivité.',
    bullets: [
      'Modes Sport/Track configurables',
      'Échappement et direction spécifiques',
      'Freinage haute performance',
    ],
    more: 'Les modèles AMG offrent une réponse moteur/transmission et un comportement châssis taillés pour l’émotion.'
  },
  {
    emoji: '📶',
    title: 'Connectivité & Services',
    desc: 'App Mercedes me, mises à jour à distance et fonctions à la demande.',
    bullets: [
      'Contrôle à distance (verrouillage, clim)',
      'OTA (Over-the-Air) & diagnostics',
      'Intégration smartphone & CarPlay/Android Auto',
    ],
    more: 'La connectivité permet d’ajouter des options logicielles et de garder votre véhicule à jour dans le temps.'
  },
])
</script>

<style>
.fade-scale-enter-active, .fade-scale-leave-active { transition: opacity .15s ease, transform .15s ease; }
.fade-scale-enter-from, .fade-scale-leave-to { opacity: 0; transform: translateY(-6px) scale(.98); }
</style>
