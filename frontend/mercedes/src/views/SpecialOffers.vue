
<template>
  <div
    :class="[
      'min-h-screen flex flex-col bg-gradient-to-br',
      props.isDark
        ? 'from-[#0b0b0e] via-[#0f1115] to-[#1a1d22] text-zinc-100'
        : 'from-[#f6f7fb] via-[#f1f2f6] to-[#e7eaef] text-zinc-900'
    ]"
  >
    <!-- MAIN CONTENT -->
    <main class="flex-1 flex flex-col items-center py-8 px-4 sm:px-6">
      <div class="w-full max-w-6xl mx-auto px-3 sm:px-6 mb-2 flex items-center justify-between">
        <!-- Title -->
        <h1 class="font-extrabold leading-tight text-3xl sm:text-4xl text-current">
          {{ t('pageTitle') }}
        </h1>

        <!-- Back -->
        <button
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
      <p class="max-w-3xl mb-6 sm:mb-8" :class="props.isDark ? 'text-zinc-300' : 'text-zinc-700'">
        {{ t('intro') }}
      </p>

      <!-- Offers -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 w-full max-w-6xl">
        <div
          v-for="offer in offers"
          :key="offer.id"
          class="rounded-2xl shadow-xl overflow-hidden flex flex-col transition-all duration-200 hover:scale-[1.02]"
          :class="props.isDark
            ? 'bg-zinc-900/90 text-white border border-zinc-800'
            : 'bg-white text-zinc-800 border border-zinc-200'"
        >
          <img :src="offer.image" :alt="offer.title" class="h-48 w-full object-cover" />

          <div class="p-4 flex flex-col flex-1">
            <h2 class="text-lg font-bold mb-2">{{ offer.title }}</h2>
            <p class="text-sm flex-1 mb-4" :class="props.isDark ? 'text-zinc-300' : 'text-zinc-600'">
              {{ offer.description }}
            </p>

            <div class="flex items-center justify-between mt-auto">
              <span class="text-lg font-bold text-[#5d737e]">{{ offer.price }}</span>
              <button
                @click="viewOffer(offer)"
                class="px-3 py-1 rounded-lg text-sm font-semibold transition-colors"
                :class="props.isDark
                  ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
                  : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'"
              >
                {{ t('learnMore') }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from '../composables/useI18n'
import { fetchSpecialOffers } from '../services/api'

/* Props */
const props = defineProps({
  isDark: { type: Boolean, default: false },
  language: { type: String, default: 'FR' },
})

const router = useRouter()
const { t } = useI18n()

const offers = ref([])
const loading = ref(false)

/* Fetch offers on mount */
onMounted(async () => {
  try {
    loading.value = true
    offers.value = await fetchSpecialOffers()
  } catch (error) {
    console.error('Failed to fetch special offers:', error)
    // Fallback data
    offers.value = [
      {
        id: 1,
        title: t('classecTitle'),
        description: t('classecDesc'),
        price: '54 900 DT',
        image: '/src/assets/offers/classec.jpg',
      },
      {
        id: 2,
        title: t('glecoupeTitle'),
        description: t('glecoupeDesc'),
        price: '210 000 DT',
        image: '/src/assets/offers/glecoupe.jpg',
      },
      {
        id: 3,
        title: t('aclassTitle'),
        description: t('aclassDesc'),
        price: '89 000 DT',
        image: '/src/assets/offers/aclass.jpg',
      }
    ]
  } finally {
    loading.value = false
  }
})

/* Handle offer click */
const viewOffer = (offer) => {
  // Navigate to offer details or open modal
  alert(`${t('viewOffer')}: ${offer.title}\n${offer.description}\n${offer.price}`)
  // Or router.push(`/offers/${offer.id}`)
}
</script>