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
        @click="goHome"
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
        @click="openDetails(car.id)"
        class="rounded-2xl p-4 sm:p-6 shadow-xl flex flex-col items-center transition-all duration-200 group cursor-pointer hover:scale-[1.02]"
        :class="props.isDark
          ? 'bg-zinc-900/90 text-white border border-zinc-800 hover:bg-gray-300 hover:text-black'
          : 'bg-white text-zinc-800 border border-zinc-200 hover:bg-zinc-100'"
      >
        <img :src="car.image" :alt="car.model" class="mb-4 rounded-xl w-full h-44 object-cover shadow-md bg-white" />
        <div class="font-semibold text-lg sm:text-xl mb-1">{{ car.model }}</div>
        <div class="text-sm mb-2" :class="props.isDark ? 'text-zinc-300' : 'text-zinc-600'">{{ car.type }}</div>
        <div class="font-bold text-lg mb-4">{{ car.price }} DT</div>

        <!-- action button (stops the card click from firing again) -->
        <button
          @click.stop="openDetails(car.id)"
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
/* Props from parent (same as ChatWidget) */
const props = defineProps({
  isDark: { type: Boolean, default: false },
  language: { type: String, default: 'FR' },
})

/* Emits */
const emit = defineEmits(['navigate'])

/* Images */
import aClassAmgLine from '/src/assets/cars/AClassAmgLine.png'
import amgGt from '/src/assets/cars/amg_gt.png'
import c200 from '/src/assets/cars/C200.png'
import gla200d from '/src/assets/cars/GLA200D.png'
import gleS63 from '/src/assets/cars/GLES63.avif'
import sClass2012 from '/src/assets/cars/Sclass2012.png'

/* i18n */
const i18n = {
  FR: { availableCars: 'Voitures Disponibles', details: 'Voir détails' },
  EN: { availableCars: 'Available Cars',       details: 'See details'  },
}
const t = (k) => (i18n[props.language] || i18n.FR)[k] || k

/* Cars data */
const cars = [
  { id: 1, model: 'Classe S 2012',                       type: 'Berline de Luxe',   price: '110 900', image: sClass2012 },
  { id: 2, model: 'Classe A 200 d AMG Line',             type: 'Citadine Compacte', price: '159 200', image: aClassAmgLine },
  { id: 3, model: 'Mercedes Benz GLE S 63 4MATIC Coupé', type: 'SUV',               price: '240 500', image: gleS63 },
  { id: 4, model: 'Mercedes AMG GT',                     type: 'Sports Car',        price: '170 000', image: amgGt },
  { id: 5, model: 'Classe C 200 Avantgarde',             type: 'Break',             price: '110 000', image: c200 },
  { id: 6, model: 'GLA 200 D',                           type: 'SUV Coupé',         price: '92 400',  image: gla200d },
]

/* Navigation helpers */
function openDetails(id) {
  emit('navigate', { page: 'car', id })
}
function goHome() {
  emit('navigate', { page: 'dashboard' })
}
</script>
