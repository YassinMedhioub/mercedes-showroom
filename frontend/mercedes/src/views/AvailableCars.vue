<template>
  <div
    :class="[
      'min-h-screen flex flex-col bg-gradient-to-br',
      props.isDark
        ? 'from-[#0b0b0e] via-[#0f1115] to-[#1a1d22] text-zinc-100'
        : 'from-[#f6f7fb] via-[#f1f2f6] to-[#e7eaef] text-zinc-900'
    ]"
  >
    <!-- PAGE TITLE -->
    <div class="w-full max-w-6xl mx-auto px-3 sm:px-6 mt-6 mb-2 flex items-center justify-between">
      <h1 class="font-bold text-2xl sm:text-3xl text-current">
        {{ t('availableCars') }}
      </h1>

      <button
        @click="router.push('/dashboard')"
        class="px-3 py-2 rounded-lg text-sm font-medium transition-colors"
        :class="props.isDark
          ? 'bg-zinc-800 text-white hover:bg-zinc-700'
          : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'"
      >
        ← {{ props.language === 'FR' ? 'Retour' : 'Back' }}
      </button>
    </div>

    <!-- CARS GRID -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 w-full max-w-6xl mx-auto px-3 mt-10 sm:px-6 pb-10">
      <div
        v-for="car in cars"
        :key="car.id"
        @click="handleCarClick(car)"
        class="rounded-2xl p-4 sm:p-6 shadow-xl flex flex-col items-center transition-all duration-200 group cursor-pointer hover:scale-[1.02]"
        :class="props.isDark
          ? 'bg-zinc-900/90 text-white border border-zinc-800 hover:bg-gray-300 hover:text-black'
          : 'bg-white text-zinc-800 border border-zinc-200 hover:bg-zinc-100'"
      >
        <!-- Main car image -->
        <img
          :src="getMainImage(car)"
          :alt="car.model"
          class="mb-4 rounded-xl w-full h-44 object-cover shadow-md bg-white"
        />

        <div class="font-semibold text-lg sm:text-xl mb-1">{{ car.model }}</div>
        <div class="text-sm mb-2" :class="props.isDark ? 'text-zinc-300' : 'text-zinc-600'">
          {{ car.type }}
        </div>
        <div class="font-bold text-lg mb-4">{{ car.price }} DT</div>

        <button
          @click.stop="handleCarClick(car)"
          class="px-4 py-2 rounded-lg text-sm font-semibold transition-colors"
          :class="props.isDark
            ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
            : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'"
        >
          {{ t('details') }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from '../composables/useI18n'
import { fetchCars } from '../services/api'

/* Props */
const props = defineProps({
  isDark: { type: Boolean, default: false },
  language: { type: String, default: 'FR' },
})

const router = useRouter()
const { t } = useI18n()

/* Cars */
const cars = ref([])

onMounted(async () => {
  try {
    cars.value = await fetchCars()
    console.log('🚗 Cars loaded:', cars.value.map(car => ({ id: car.id, model: car.model })))
  } catch (err) {
    console.error('Failed to fetch cars:', err)
  }
})

/* Navigation handler */
function handleCarClick(car) {
  console.log('🚗 Car clicked:', car.id, car.model)
  console.log('📍 Current route:', router.currentRoute.value.path)
  console.log('🔗 Router instance:', router)
  
  try {
    console.log('➡️ Attempting navigation to:', `/car/${car.id}`)
    router.push(`/car/${car.id}`)
  } catch (error) {
    console.error('❌ Navigation error:', error)
  }
}

/* Helpers */
function getMainImage(car) {
  return car.images?.find(img => img.main)?.imageUrl || '/fallback.jpg'
}
</script>