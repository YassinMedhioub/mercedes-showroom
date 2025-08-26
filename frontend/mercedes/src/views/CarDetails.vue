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
      <!-- Header -->
      <div class="flex flex-col sm:flex-row sm:items-baseline sm:justify-between gap-3 mb-4">
        <div>
          <h1 class="font-extrabold text-3xl sm:text-4xl">{{ car?.model || '—' }}</h1>
          <p class="mt-1 text-sm sm:text-base" :class="props.isDark ? 'text-zinc-300' : 'text-zinc-600'">
            {{ car?.type }} • <span class="font-semibold">{{ car?.price }} DT</span>
          </p>
        </div>
        <button @click="goBack"
          class="px-3 py-2 rounded-lg text-sm font-medium"
          :class="props.isDark ? 'bg-zinc-800 text-white hover:bg-zinc-700' : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
          ← {{ props.language === 'FR' ? 'Retour' : 'Back' }}
        </button>
      </div>

      <!-- 360 + Gallery -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
        <!-- 360 Viewer -->
        <section class="rounded-2xl p-4 sm:p-6 shadow-xl"
          :class="props.isDark ? 'bg-zinc-900/90 border border-zinc-800' : 'bg-white border border-zinc-200'">
          <h2 class="text-lg font-semibold mb-3">360°</h2>
          <div class="relative w-full aspect-[16/9] rounded-xl overflow-hidden select-none cursor-grab active:cursor-grabbing"
               @pointerdown="onPointerDown" @pointerup="onPointerUp" @pointerleave="onPointerUp" @pointermove="onPointerMove">
            <img v-if="frames.length" :src="frames[currentFrame]" class="absolute inset-0 w-full h-full object-contain" />
            <div v-else class="absolute inset-0 grid place-items-center text-sm">No 360° frames</div>
          </div>
        </section>

        <!-- Gallery -->
        <section class="rounded-2xl p-4 sm:p-6 shadow-xl"
          :class="props.isDark ? 'bg-zinc-900/90 border border-zinc-800' : 'bg-white border border-zinc-200'">
          <h2 class="text-lg font-semibold mb-3">{{ props.language === 'FR' ? 'Galerie' : 'Gallery' }}</h2>
          <div class="relative w-full aspect-[16/9] rounded-xl overflow-hidden mb-3">
            <img v-if="activeImage" :src="activeImage" class="absolute inset-0 w-full h-full object-contain" />
          </div>
          <div class="flex items-center gap-2 overflow-x-auto pb-1">
            <button v-for="(img, idx) in gallery" :key="idx"
                    class="w-24 h-16 rounded-lg overflow-hidden border"
                    :class="activeIndex === idx ? 'border-zinc-800' : 'border-zinc-300'"
                    @click="setActive(idx)">
              <img :src="img" class="w-full h-full object-cover" />
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

/* Props */
const props = defineProps({
  carId: { type: [Number, String], required: true },
  isDark: { type: Boolean, default: false },
  language: { type: String, default: 'FR' },
})
const emit = defineEmits(['navigate'])
function goBack() { emit('navigate', { page: 'cars', sub: 'cars' }) }

/* State */
const car = ref(null)
const gallery = ref([])
const frames = ref([])
const activeIndex = ref(0)
const activeImage = computed(() => gallery.value?.[activeIndex.value] || null)

/* Fetch car */
onMounted(async () => {
  try {
    const token = localStorage.getItem("authToken")
    const res = await fetch(`http://localhost:8080/api/cars/${props.carId}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (!res.ok) throw new Error(`HTTP ${res.status}`)
    const data = await res.json()
    car.value = data
    gallery.value = data.images.filter(img => !img.is360).map(img => img.imageUrl)
    frames.value  = data.images.filter(img => img.is360).map(img => img.imageUrl)
    const mainIndex = data.images.findIndex(img => img.main)
    activeIndex.value = mainIndex >= 0 ? mainIndex : 0
  } catch (err) {
    console.error("Failed to load car", err)
  }
})
function setActive(i){ activeIndex.value = i }

/* 360 Viewer */
const currentFrame = ref(0)
let dragging = false, startX = 0, spinTimer = null
const sensitivity = 6, spinning = ref(true)
function onPointerDown(e){ dragging = true; startX = e.clientX }
function onPointerUp(){ dragging = false }
function onPointerMove(e){
  if (!dragging || !frames.value.length) return
  const dx = e.clientX - startX
  if (Math.abs(dx) >= sensitivity){
    currentFrame.value = (currentFrame.value - Math.sign(dx) + frames.value.length) % frames.value.length
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
