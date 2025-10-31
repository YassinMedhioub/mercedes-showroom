
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
          @click="router.push('/dashboard')"
          class="px-3 py-2 rounded-lg text-sm font-medium transition-colors"
          :class="props.isDark
            ? 'bg-zinc-800 text-white hover:bg-zinc-700'
            : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'"
        >
          ← {{ t('back') }}
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
            : 'bg-white text-zinc-800 border border-zinc-200 hover:bg-gray-300'"
        >
          <div class="text-4xl mb-3">{{ f.emoji }}</div>
          <h3 class="font-semibold text-lg mb-1">{{ f.title }}</h3>
          <p :class="[  'text-sm mb-4' ]">
            {{ f.desc }}
          </p>

          <!-- bullets -->
          <ul class="space-y-1.5 mb-4">
            <li v-for="(b, j) in f.bullets" :key="j" class="flex items-start gap-2 text-sm">
              <span class="mt-0.5">•</span>
              <span >
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
            <div :class="[  'text-sm px-3 py-2' ]">
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
          <button @click="router.push('/cars')"
            class="px-5 py-3 rounded-lg font-semibold transition-colors"
            :class="props.isDark
              ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
              : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'"
          >
            {{ t('exploreModels') }}
          </button>
          <button
            @click="router.push('/driveBook')"
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
import { useRouter } from 'vue-router'
import { useI18n } from '../composables/useI18n'

/** Props exactly like ChatWidget: parent controls them */
const props = defineProps({
  language: { type: String,  default: 'FR' },
  isDark:   { type: Boolean, default: false },
})

/** Services */
const router = useRouter()
const { t } = useI18n()

/** chip class helper (same pattern as ChatWidget) */
const chipCls = computed(() =>
  props.isDark
    ? 'px-3 py-1 rounded-full text-xs sm:text-sm border border-zinc-700 text-zinc-200'
    : 'px-3 py-1 rounded-full text-xs sm:text-sm border border-zinc-300/60 text-zinc-700'
)

/** content */
const features = ref([
  { 
    emoji: '🧠', 
    title: t('mbuxTitle'), 
    desc: t('mbuxDesc'),
    bullets: [
      t('mbuxBullet1'),
      t('mbuxBullet2'),
      t('mbuxBullet3')
    ],
    more: t('mbuxMore')
  },
  { 
    emoji: '🛡️', 
    title: t('adasTitle'), 
    desc: t('adasDesc'),
    bullets: [
      t('adasBullet1'),
      t('adasBullet2'),
      t('adasBullet3')
    ],
    more: t('adasMore')
  },
  { 
    emoji: '⚡', 
    title: t('eqTitle'), 
    desc: t('eqDesc'),
    bullets: [
      t('eqBullet1'),
      t('eqBullet2'),
      t('eqBullet3')
    ],
    more: t('eqMore')
  },
  { 
    emoji: '🧭', 
    title: t('maticTitle'), 
    desc: t('maticDesc'),
    bullets: [
      t('maticBullet1'),
      t('maticBullet2'),
      t('maticBullet3')
    ],
    more: t('maticMore')
  },
  { 
    emoji: '🏁', 
    title: t('amgTitle'), 
    desc: t('amgDesc'),
    bullets: [
      t('amgBullet1'),
      t('amgBullet2'),
      t('amgBullet3')
    ],
    more: t('amgMore')
  },
  { 
    emoji: '📶', 
    title: t('connectivityTitle'), 
    desc: t('connectivityDesc'),
    bullets: [
      t('connectivityBullet1'),
      t('connectivityBullet2'),
      t('connectivityBullet3')
    ],
    more: t('connectivityMore')
  }
])
</script>

<style>
.fade-scale-enter-active, .fade-scale-leave-active { transition: opacity .15s ease, transform .15s ease; }
.fade-scale-enter-from, .fade-scale-leave-to { opacity: 0; transform: translateY(-6px) scale(.98); }
</style>