<template>
  <div
    :class="[
      'min-h-screen flex flex-col bg-gradient-to-br',
      props.isDark
        ? 'from-[#0b0b0e] via-[#0f1115] to-[#1a1d22] text-zinc-100'
        : 'from-[#f6f7fb] via-[#f1f2f6] to-[#e7eaef] text-zinc-900'
    ]"
  >
    <div class="w-full max-w-6xl mx-auto px-4 sm:px-6 py-8">
      <!-- Header row -->
      <div class="flex flex-col sm:flex-row sm:items-baseline sm:justify-between gap-3 mb-4">
        <div>
          <h1 class="font-extrabold leading-tight text-3xl sm:text-4xl text-current">
            {{ car?.model || '—' }}
          </h1>
          <p class="mt-1 text-sm sm:text-base" :class="props.isDark ? 'text-zinc-300' : 'text-zinc-600'">
            {{ car?.type }} • <span class="font-semibold">{{ car?.price }} DT</span>
          </p>
        </div>

        <button
          type="button"
          @click="goBack"
          class="inline-flex items-center justify-center px-3 py-2 rounded-lg text-sm font-medium transition-colors shrink-0"
          :class="props.isDark
            ? 'bg-zinc-800 text-white hover:bg-zinc-700'
            : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
          ← {{ props.language === 'FR' ? 'Retour' : 'Back' }}
        </button>
      </div>

      <!-- 360 + Gallery -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
        <!-- 360 Viewer Card -->
        <section
          :class="[
            'rounded-2xl p-4 sm:p-6 shadow-xl',
            props.isDark ? 'bg-zinc-900/90 text-white border border-zinc-800' : 'bg-white text-zinc-800 border border-zinc-200'
          ]"
        >
          <div class="flex items-center justify-between mb-3">
            <h2 class="text-lg font-semibold">360°</h2>
            <div class="flex items-center gap-2 text-xs" :class="props.isDark ? 'text-zinc-300' : 'text-zinc-600'">
              <span>{{ props.language === 'FR' ? 'Faites glisser pour tourner' : 'Drag to rotate' }}</span>
              <button @click="toggleSpin" class="px-2 py-1 rounded border"
                :class="props.isDark ? 'border-zinc-700 hover:bg-zinc-800' : 'border-zinc-300 hover:bg-zinc-100'">
                {{ spinning ? (props.language === 'FR' ? 'Pause' : 'Pause') : (props.language === 'FR' ? 'Lecture' : 'Play') }}
              </button>
            </div>
          </div>

          <div
            class="relative w-full aspect-[16/9] rounded-xl overflow-hidden select-none cursor-grab active:cursor-grabbing"
            @pointerdown="onPointerDown"
            @pointerup="onPointerUp"
            @pointerleave="onPointerUp"
            @pointermove="onPointerMove"
          >
            <img
              v-if="frames.length"
              :src="frames[currentFrame]"
              :alt="`${car?.model} 360`"
              class="absolute inset-0 w-full h-full object-contain bg-[radial-gradient(ellipse_at_center,rgba(255,255,255,0.08),rgba(0,0,0,0))]"
              draggable="false"
            />
            <div v-else class="absolute inset-0 grid place-items-center text-sm"
                 :class="props.isDark ? 'text-zinc-300' : 'text-zinc-600'">
              {{ props.language === 'FR' ? 'Aucune image 360°' : 'No 360° frames' }}
            </div>
          </div>
        </section>

        <!-- Photo Gallery Card -->
        <section
          :class="[
            'rounded-2xl p-4 sm:p-6 shadow-xl',
            props.isDark ? 'bg-zinc-900/90 text-white border border-zinc-800' : 'bg-white text-zinc-800 border border-zinc-200'
          ]"
        >
          <h2 class="text-lg font-semibold mb-3">
            {{ props.language === 'FR' ? 'Galerie' : 'Gallery' }}
          </h2>

          <!-- Main image -->
          <div class="relative w-full aspect-[16/9] rounded-xl overflow-hidden mb-3">
            <img
              v-if="activeImage"
              :src="activeImage"
              :alt="`${car?.model} photo`"
              class="absolute inset-0 w-full h-full object-contain bg-[radial-gradient(ellipse_at_center,rgba(255,255,255,0.08),rgba(0,0,0,0))]"
              loading="eager"
            />
          </div>

          <!-- Thumbnails -->
          <div class="flex items-center gap-2 overflow-x-auto pb-1">
            <button
              v-for="(img, idx) in gallery"
              :key="idx"
              class="shrink-0 w-24 h-16 rounded-lg overflow-hidden border transition hover:scale-[1.02]"
              :class="activeIndex === idx
                ? (props.isDark ? 'border-white' : 'border-zinc-800')
                : (props.isDark ? 'border-zinc-700' : 'border-zinc-300')"
              @click="setActive(idx)"
            >
              <img :src="img" class="w-full h-full object-cover" :alt="`${car?.model} thumb ${idx+1}`" />
            </button>
          </div>
        </section>
      </div>

      <!-- Description (AI-generated) -->
      <section
        :class="[
          'rounded-2xl p-6 shadow-xl',
          props.isDark ? 'bg-zinc-900/90 text-white border border-zinc-800' : 'bg-white text-zinc-800 border border-zinc-200'
        ]"
      >
        <div class="flex items-center justify-between mb-2">
          <h2 class="text-lg font-semibold">
            {{ props.language === 'FR' ? 'À propos du modèle' : 'About this model' }}
          </h2>
          <button
            type="button"
            @click="fetchAiDescription(true)"
            class="text-xs px-2 py-1 rounded border"
            :class="props.isDark ? 'border-zinc-700 hover:bg-zinc-800' : 'border-zinc-300 hover:bg-zinc-100'">
            {{ props.language === 'FR' ? 'Régénérer' : 'Regenerate' }}
          </button>
        </div>

        <!-- Loading skeleton -->
        <div v-if="aiLoading" class="space-y-2">
          <div class="h-4 rounded animate-pulse" :class="props.isDark ? 'bg-zinc-800' : 'bg-zinc-200'"></div>
          <div class="h-4 rounded animate-pulse" :class="props.isDark ? 'bg-zinc-800' : 'bg-zinc-200'"></div>
          <div class="h-4 rounded w-2/3 animate-pulse" :class="props.isDark ? 'bg-zinc-800' : 'bg-zinc-200'"></div>
        </div>

        <!-- Error -->
        <p v-else-if="aiError" class="leading-relaxed" :class="props.isDark ? 'text-zinc-300' : 'text-zinc-700'">
          {{ aiError }}
        </p>

        <!-- AI text -->
        <p v-else-if="aiDesc" class="leading-relaxed" :class="props.isDark ? 'text-zinc-300' : 'text-zinc-700'">
          {{ aiDesc }}
        </p>

        <!-- Fallback -->
        <p v-else class="leading-relaxed" :class="props.isDark ? 'text-zinc-300' : 'text-zinc-700'">
          {{ defaultDescription }}
        </p>
      </section>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'

/** Props **/
const props = defineProps({
  carId: { type: [Number, String], default: null },
  car:   { type: Object, default: null },
  isDark: { type: Boolean, default: false },
  language: { type: String, default: 'FR' },
})
const emit = defineEmits(['navigate'])
function goBack() { emit('navigate', { page: 'dashboard', sub: 'cars' }) }

/** Car DB (replace/extend with your assets) **/
import aClassAmgLine from '/src/assets/cars/AClassAmgLine.png'
import amgGt from '/src/assets/cars/amg_gt.png'
import c200 from '/src/assets/cars/C200.png'
import gla200d from '/src/assets/cars/GLA200D.png'
import gleS63 from '/src/assets/cars/GLES63.avif'
import sClass2012 from '/src/assets/cars/Sclass2012.png'

const carsDb = [
  { id: 1, model: 'Classe S 2012', type: 'Berline de Luxe', price: '110 900', hero: sClass2012, frames360: [], gallery: [sClass2012] },
  { id: 2, model: 'Classe A 200 d AMG Line', type: 'Citadine Compacte', price: '159 200', hero: aClassAmgLine, frames360: [], gallery: [aClassAmgLine] },
  { id: 3, model: 'Mercedes Benz GLE S 63 4MATIC Coupé', type: 'SUV', price: '240 500', hero: gleS63, frames360: [], gallery: [gleS63] },
  { id: 4, model: 'Mercedes AMG GT', type: 'Sports Car', price: '170 000', hero: amgGt, frames360: [], gallery: [amgGt] },
  { id: 5, model: 'Classe C 200 Avantgarde', type: 'Break', price: '110 000', hero: c200, frames360: [], gallery: [c200] },
  { id: 6, model: 'GLA 200 D', type: 'SUV Coupé', price: '92 400', hero: gla200d, frames360: [], gallery: [gla200d] },
]

const car = computed(() => {
  if (props.car) return props.car
  if (props.carId) return carsDb.find(c => String(c.id) === String(props.carId)) || null
  const stored = localStorage.getItem('selectedCarId')
  return stored ? carsDb.find(c => String(c.id) === String(stored)) : null
})

/** 360 frames & gallery **/
const frames  = computed(() => car.value?.frames360 || [])
const gallery = computed(() => (car.value?.gallery?.length ? car.value.gallery : [car.value?.hero].filter(Boolean)))

/** Gallery state **/
const activeIndex = ref(0)
const activeImage = computed(() => gallery.value?.[activeIndex.value] || null)
function setActive(i){ activeIndex.value = i }

/** Fallback description **/
const defaultDescription = computed(() =>
  props.language === 'FR'
    ? 'Découvrez le design, les performances et les technologies de ce modèle à travers la vue 360° et la galerie.'
    : 'Explore the design, performance and technology of this model through the 360° view and gallery.'
)

/** Simple 360 viewer **/
const currentFrame = ref(0)
let dragging = false
let startX = 0
const sensitivity = 6
let spinTimer = null
const spinning = ref(true)
function onPointerDown(e){ dragging = true; startX = e.clientX }
function onPointerUp(){ dragging = false }
function onPointerMove(e){
  if (!dragging || frames.value.length === 0) return
  const dx = e.clientX - startX
  if (Math.abs(dx) >= sensitivity){
    const step = Math.sign(dx)
    currentFrame.value = (currentFrame.value - step + frames.value.length) % frames.value.length
    startX = e.clientX
  }
}
function toggleSpin(){ spinning.value = !spinning.value; handleSpin() }
function handleSpin(){
  clearInterval(spinTimer)
  if (spinning.value && frames.value.length){
    spinTimer = setInterval(() => {
      currentFrame.value = (currentFrame.value + 1) % frames.value.length
    }, 80)
  }
}
watch(frames, () => { currentFrame.value = 0; handleSpin() })
onMounted(handleSpin)
onUnmounted(() => clearInterval(spinTimer))

/** ---------------- AI Description (with cache) ---------------- **/
const aiDesc = ref('')
const aiLoading = ref(false)
const aiError = ref('')

const carName = computed(() => car.value?.model || '')
const descCacheKey = computed(() => `ai-desc:${props.language}:${carName.value}`)

async function fetchAiDescription(force = false) {
  if (!car.value) return
  aiError.value = ''

  if (!force) {
    const cached = localStorage.getItem(descCacheKey.value)
    if (cached) {
      try { aiDesc.value = JSON.parse(cached)?.long || '' } catch {}
      if (aiDesc.value) return
    }
  }

  aiLoading.value = true
  try {
    const res = await fetch('/api/ai/describe-car', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        model: car.value.model,
        type: car.value.type,
        year: car.value.model.match(/\d{4}/)?.[0] ?? '',
        language: props.language === 'FR' ? 'fr' : 'en'
      })
    })
    if (!res.ok) throw new Error('bad response')
    const data = await res.json()   // { long: string }
    aiDesc.value = (data?.long || '').trim()
    localStorage.setItem(descCacheKey.value, JSON.stringify({ long: aiDesc.value, ts: Date.now() }))
  } catch (e) {
    aiError.value = props.language === 'FR'
      ? "La génération est momentanément indisponible."
      : "Generation is temporarily unavailable."
  } finally {
    aiLoading.value = false
  }
}
watch([car, () => props.language], () => fetchAiDescription(), { immediate: true })
</script>

<style scoped>
img { image-rendering: -webkit-optimize-contrast; }
</style>
