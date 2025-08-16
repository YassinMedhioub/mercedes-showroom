<template>
  <div >

    

    <!-- MAIN CONTENT -->
    <main class="flex-1 flex flex-col items-center py-8 px-4 sm:px-6">
            <div class="w-full max-w-6xl mx-auto px-3 sm:px-6 mb-2 flex items-center justify-between">
        <!-- Left: Title only -->
        <h1 :class="[
              'font-extrabold leading-tight',
              isDarkMode ? 'text-white' : 'text-zinc-900',
              'text-3xl sm:text-4xl'
            ]">
          {{ texts[language].pageTitle }}
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
        {{ texts[language].intro }}
      </p>

      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 w-full max-w-6xl">
        <div v-for="offer in offers" :key="offer.id"
          :class="['rounded-2xl shadow-xl overflow-hidden flex flex-col transition-all duration-200 hover:scale-[1.02]',
            isDarkMode ? 'bg-zinc-900/90 text-white' : 'bg-white text-zinc-800 border border-zinc-200']">
          
          <img :src="offer.image" :alt="offer.title" class="h-48 w-full object-cover" />
          
          <div class="p-4 flex flex-col flex-1">
            <h2 class="text-lg font-bold mb-2">{{ offer.title }}</h2>
            <p class="text-sm flex-1 mb-4" :class="isDarkMode ? 'text-zinc-300' : 'text-zinc-600'">
              {{ offer.description }}
            </p>
            <div class="flex items-center justify-between mt-auto">
              <span class="text-lg font-bold text-[#5d737e]">{{ offer.price }}</span>
              <button class="px-3 py-1 rounded-lg text-sm font-semibold transition-colors"
                :class="isDarkMode
                  ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
                  : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'">
                {{ texts[language].learnMore }}
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

const isDarkMode = ref(localStorage.getItem('darkMode') === 'true')
const language = ref(localStorage.getItem('lang') || 'FR')
const emit = defineEmits(['navigate'])

const toggleDarkMode = () => {
  isDarkMode.value = !isDarkMode.value
  saveSettings()
}

const saveSettings = () => {
  localStorage.setItem('darkMode', isDarkMode.value)
  localStorage.setItem('lang', language.value)
}

const texts = {
  FR: {
    title: "Mon espace",
    pageTitle: "Offres Spéciales",
    intro: "Découvrez nos offres spéciales disponibles uniquement en showroom.",
    learnMore: "Voir plus",
    lightMode: "Mode clair",
    darkMode: "Mode sombre"
  },
  EN: {
    title: "My Space",
    pageTitle: "Special Offers",
    intro: "Discover our exclusive showroom offers available for a limited time.",
    learnMore: "Learn more",
    lightMode: "Light mode",
    darkMode: "Dark mode"
  }
}

const offers = ref([
  {
    id: 1,
    title: "Mercedes Classe C - Edition Limitée",
    description: "Profitez d'une remise exceptionnelle sur la Classe C avec pack AMG.",
    price: "54 900 DT",
    image: "/src/assets/offers/classec.jpg"
  },
  {
    id: 2,
    title: "GLE Coupé - Pack Luxe",
    description: "Économisez sur le SUV coupé sportif avec options haut de gamme.",
    price: "210 000 DT",
    image: "/src/assets/offers/glecoupe.jpg"
  },
  {
    id: 3,
    title: "A-Class Sport",
    description: "Offre spéciale sur la citadine premium avec intérieur cuir.",
    price: "89 000 DT",
    image: "/src/assets/offers/aclass.jpg"
  }
])

onMounted(() => {
  saveSettings()
})
</script>
