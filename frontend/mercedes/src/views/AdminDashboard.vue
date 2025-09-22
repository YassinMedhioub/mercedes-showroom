
<template>
  <div
    :class="[ 
      'min-h-[calc(100vh-180px)] flex flex-col',
      'bg-gradient-to-br',
      isDark
        ? 'from-zinc-900 via-black to-gray-800 text-white'
        : 'from-[#f7f7fa] via-[#f2f2f5] to-[#e5e7eb] text-zinc-900'
    ]"
  >
    <br>

    <main class="flex-1 flex flex-col items-center justify-start px-6 pt-12 pb-12">
      <h1 class="text-3xl mb-4 font-bold">
        {{ t('welcome') }}, {{ displayName }}
      </h1>

      <p class="mb-8 opacity-80">
        {{ t('subtitle') }}
      </p>

      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 w-full max-w-5xl">
        <!-- USERS -->
        <button @click="router.push('/manage-users')" :class="cardClass">
          <div class="text-4xl mb-2">👤</div>
          <div class="font-semibold mb-1">{{ t('users') }}</div>
          <div class="text-xs">{{ t('usersDesc') }}</div>
        </button>

        <!-- CARS -->
        <button @click="router.push('/manage-cars')" :class="cardClass">
          <div class="text-4xl mb-2">🚗</div>
          <div class="font-semibold mb-1">{{ t('cars') }}</div>
          <div class="text-xs">{{ t('carsDesc') }}</div>
        </button>

        <!-- STATS -->
        <button @click="router.push('/statistics')" :class="cardClass">
          <div class="text-4xl mb-2">📊</div>
          <div class="font-semibold mb-1">{{ t('stats') }}</div>
          <div class="text-xs">{{ t('statsDesc') }}</div>
        </button>
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { usePrefs } from '../components/usePrefs';
import { useI18n } from '../composables/useI18n'; // New shared i18n composable

const router = useRouter()
const props = defineProps({
  adminName: { type: String, default: '' }
})

const { isDark, lang } = usePrefs()
const { t } = useI18n() // Use shared i18n composable

// Validate and sanitize adminName
const displayName = computed(() => {
  const name = (props.adminName || '').trim()
  return name.length > 50 ? name.slice(0, 50) + '...' : name || 'Administrateur'
})

// Classes for cards with hover based on theme
const cardClass = computed(() =>
  [
    'rounded-xl p-6 shadow-xl text-center transition-all duration-200 cursor-pointer group w-full min-h-[170px]',
    isDark.value
      ? 'bg-zinc-900/80 text-white hover:bg-gray-300 hover:text-black hover:scale-[1.03]'
      : 'bg-white/90 text-zinc-800 border border-zinc-200 hover:bg-gray-700 hover:text-white hover:scale-[1.03]'
  ].join(' ')
)
</script>