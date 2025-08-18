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
    <!-- HERO -->
    <section class="w-full max-w-6xl mx-auto px-3 sm:px-6 pt-8 sm:pt-12">
      <div class="w-full max-w-6xl mx-auto px-3 sm:px-6 mb-2 flex items-center justify-between">
        <!-- Title -->
        <h1
          :class="[
            'font-extrabold leading-tight text-3xl sm:text-4xl',
            props.isDark ? 'text-white' : 'text-zinc-900'
          ]"
        >
          {{ t('techTitle') }}
        </h1>

        <!-- Back -->
        <button
          type="button"
          @click="emit('navigate','dashboard')"
          class="px-3 py-2 rounded-lg text-sm font-medium transition-colors"
          :class="props.isDark
            ? 'bg-zinc-800 text-white hover:bg-zinc-700'
            : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'"
        >
          ← {{ props.language === 'FR' ? 'Retour' : 'Back' }}
        </button>
      </div>

      <!-- Subtitle -->
      <p
        :class="[
          'max-w-3xl mb-6 sm:mb-8',
          props.isDark ? 'text-zinc-300' : 'text-zinc-700'
        ]"
      >
        {{ t('techSubtitle') }}
      </p>

      <!-- Quick tags -->
      <div class="flex flex-wrap gap-2 sm:gap-3 mb-8">
        <span
          class="px-3 py-1 rounded-full text-xs sm:text-sm"
          :class="props.isDark
            ? 'border border-zinc-700 text-zinc-200'
            : 'border border-zinc-300/60 text-zinc-700'"
        >MBUX</span>
        <span :class="chipCls">ADAS</span>
        <span :class="chipCls">4MATIC</span>
        <span :class="chipCls">EQ</span>
        <span :class="chipCls">AMG</span>
        <span :class="chipCls">Connectivity</span>
      </div>
    </section>

    <!-- FEATURE CARDS -->
    <section class="w-full max-w-6xl mx-auto px-3 sm:px-6 pb-14">
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        <article
          v-for="(f, i) in features"
          :key="i"
          class="rounded-2xl p-5 shadow-xl transition-all duration-200 group"
          :class="props.isDark
            ? 'bg-zinc-900/90 text-white hover:bg-gray-300 hover:text-black border border-zinc-800'
            : 'bg-white text-zinc-800 border border-zinc-200 hover:bg-gray-100'"
        >
          <div class="text-4xl mb-3">{{ f.emoji }}</div>
          <h3 class="font-semibold text-lg mb-1">{{ f.title }}</h3>
          <p :class="[ props.isDark ? 'text-zinc-300' : 'text-zinc-600', 'text-sm mb-4' ]">
            {{ f.desc }}
          </p>

          <!-- bullets -->
          <ul class="space-y-1.5 mb-4">
            <li v-for="(b, j) in f.bullets" :key="j" class="flex items-start gap-2 text-sm">
              <span class="mt-0.5">•</span>
              <span :class="props.isDark ? 'text-zinc-200' : 'text-zinc-700'">
                {{ b }}
              </span>
            </li>
          </ul>

          <!-- details -->
          <details
            class="rounded-lg"
            :class="props.isDark ? 'bg-black/20' : 'bg-zinc-100'"
          >
            <summary class="cursor-pointer px-3 py-2 rounded-lg text-sm font-semibold hover:underline select-none">
              {{ t('learnMore') }}
            </summary>
            <div :class="[ props.isDark ? 'text-zinc-200' : 'text-zinc-700', 'text-sm px-3 py-2' ]">
              {{ f.more }}
            </div>
          </details>
        </article>
      </div>
    </section>

    <!-- CTA -->
    <section class="w-full max-w-6xl mx-auto px-3 sm:px-6 pb-16">
      <div
        class="rounded-2xl p-6 sm:p-8 shadow-xl text-center"
        :class="props.isDark
          ? 'bg-zinc-900/80 text-white border border-zinc-800'
          : 'bg-white text-zinc-800 border border-zinc-200'"
      >
        <h2 class="text-xl sm:text-2xl font-bold mb-2">{{ t('ctaTitle') }}</h2>
        <p :class="[ props.isDark ? 'text-zinc-300' : 'text-zinc-600', 'mb-6' ]">
          {{ t('ctaSubtitle') }}
        </p>
        <div class="flex flex-col sm:flex-row items-center justify-center gap-3">
          <button @click="emit('navigate','cars')"
            class="px-5 py-3 rounded-lg font-semibold transition-colors"
            :class="props.isDark
              ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
              : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'"
          >
            {{ t('exploreModels') }}
          </button>
          <button
            @click="emit('navigate','driveBook')"
            class="px-5 py-3 rounded-lg font-semibold transition-colors"
            :class="props.isDark
              ? 'bg-zinc-800 text-white hover:bg-zinc-700'
              : 'bg-white text-zinc-700 border border-zinc-200 hover:bg-zinc-200'"
          >
            {{ t('bookTestDrive') }}
          </button>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'

/** Props exactly like ChatWidget: parent controls them */
const props = defineProps({
  language: { type: String,  default: 'FR' },
  isDark:   { type: Boolean, default: false },
})

/** Emits */
const emit = defineEmits(['navigate'])

/** i18n */
const i18n = {
  FR: {
    techTitle: 'Technologies Mercedes',
    techSubtitle: "Innovation, sécurité et connectivité au service d'une expérience de conduite unique.",
    learnMore: 'En savoir plus',
    ctaTitle: 'Prêt à vivre l’innovation Mercedes ?',
    ctaSubtitle: 'Découvrez les modèles équipés des dernières technologies ou réservez un essai.',
    exploreModels: 'Découvrir nos modèles',
    bookTestDrive: 'Demander un essai',
  },
  EN: {
    techTitle: 'Mercedes Technologies',
    techSubtitle: 'Innovation, safety and connectivity for a remarkable driving experience.',
    learnMore: 'Learn more',
    ctaTitle: 'Ready to experience Mercedes innovation?',
    ctaSubtitle: 'Explore models with the latest tech or book a test drive.',
    exploreModels: 'Explore Our Models',
    bookTestDrive: 'Book a Test Drive',
  }
}
const t = (k) => (i18n[props.language] || i18n.FR)[k] || k

/** chip class helper (same pattern as ChatWidget) */
const chipCls = computed(() =>
  props.isDark
    ? 'px-3 py-1 rounded-full text-xs sm:text-sm border border-zinc-700 text-zinc-200'
    : 'px-3 py-1 rounded-full text-xs sm:text-sm border border-zinc-300/60 text-zinc-700'
)

/** content */
const features = ref([
  { emoji: '🧠', title: 'MBUX & Voice',
    desc: 'Assistant vocal naturel, écrans haute résolution, navigation augmentée pour une interaction intuitive.',
    bullets: ['Commande vocale “Hey Mercedes”','Écrans tactiles & HUD','Navigation AR et suggestions proactives'],
    more: 'MBUX s’adapte à vos habitudes : itinéraires favoris, confort, multimédia. L’interface évolue avec des mises à jour en ligne.' },
  { emoji: '🛡️', title: 'ADAS & Sécurité',
    desc: 'Aides avancées à la conduite pour plus de sérénité au quotidien.',
    bullets: ['DISTRONIC (régulateur de distance)','PRE-SAFE et freinage d’urgence assisté','Maintien de voie & angle mort actif'],
    more: 'Les systèmes ADAS surveillent l’environnement, avertissent et peuvent intervenir pour éviter ou atténuer un accident.' },
  { emoji: '⚡', title: 'EQ & Électrification',
    desc: 'Technologies EQ pour l’électrique et l’hybride rechargeable.',
    bullets: ['Gestion intelligente de l’énergie','Planification de charge sur itinéraire','Récupération d’énergie au freinage'],
    more: 'Les modèles EQ optimisent performance et autonomie, avec infos de charge en temps réel et services connectés.' },
  { emoji: '🧭', title: '4MATIC',
    desc: 'Transmission intégrale intelligente pour une motricité optimale.',
    bullets: ['Répartition de couple adaptative','Adhérence améliorée route/pluie/neige','Conduite stable et dynamique'],
    more: 'Le système 4MATIC gère en continu la motricité selon l’adhérence et le style de conduite pour plus de sécurité.' },
  { emoji: '🏁', title: 'AMG Performance',
    desc: 'Châssis, freins et modes de conduite calibrés pour la sportivité.',
    bullets: ['Modes Sport/Track configurables','Échappement et direction spécifiques','Freinage haute performance'],
    more: 'Les modèles AMG offrent une réponse moteur/transmission et un comportement châssis taillés pour l’émotion.' },
  { emoji: '📶', title: 'Connectivité & Services',
    desc: 'App Mercedes me, mises à jour à distance et fonctions à la demande.',
    bullets: ['Contrôle à distance (verrouillage, clim)','OTA (Over-the-Air) & diagnostics','Intégration smartphone & CarPlay/Android Auto'],
    more: 'La connectivité permet d’ajouter des options logicielles et de garder votre véhicule à jour dans le temps.' },
])
</script>

<style>
.fade-scale-enter-active, .fade-scale-leave-active { transition: opacity .15s ease, transform .15s ease; }
.fade-scale-enter-from, .fade-scale-leave-to { opacity: 0; transform: translateY(-6px) scale(.98); }
</style>
