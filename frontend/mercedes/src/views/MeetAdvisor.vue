<template>
  <div>


<!-- PAGE CONTAINER -->
<main class="flex-1 w-full">
  <div class="w-full max-w-5xl mx-auto px-4 sm:px-6 py-8">

    <!-- Title + Back (same line, baseline aligned) -->
    <div class="flex flex-col sm:flex-row sm:items-baseline sm:justify-between gap-3 mb-2">
      <h1 :class="[
          'font-extrabold leading-tight',
          isDarkMode ? 'text-white' : 'text-zinc-900',
          'text-3xl sm:text-4xl'
        ]">
        {{ texts[language].pageTitle }}
      </h1>

      <button
        type="button"
        @click="emit('navigate','home')"
        class="inline-flex items-center justify-center px-3 py-2 rounded-lg text-sm font-medium transition-colors shrink-0"
        :class="isDarkMode
          ? 'bg-zinc-800 text-white hover:bg-zinc-700'
          : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
        ← {{ language === 'FR' ? 'Retour' : 'Back' }}
      </button>
    </div>

    <!-- Subtitle -->
    <p class="mb-8 max-w-3xl"
       :class="isDarkMode ? 'text-zinc-300' : 'text-zinc-600'">
      {{ texts[language].intro }}
    </p>

    <!-- FORM CARD -->
    <form @submit.prevent="submitForm"
      :class="[
        'rounded-2xl p-6 sm:p-8 shadow-xl',
        isDarkMode ? 'bg-zinc-900/90 text-white' : 'bg-white text-zinc-800'
      ]">

      <!-- Section 1: Your info -->
      <h2 class="text-lg font-semibold mb-4" :class="isDarkMode ? 'text-white' : 'text-zinc-900'">
        {{ language === 'FR' ? 'Vos informations' : 'Your information' }}
      </h2>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-6">
        <!-- Full name spans 2 columns -->
        <div class="md:col-span-2">
          <label class="block mb-1 font-medium">{{ texts[language].name }}</label>
          <input
            v-model="form.name"
            type="text"
            required
            autocomplete="name"
            :placeholder="texts[language].namePlaceholder"
            class="w-full px-3 py-2 rounded-lg border focus:outline-none focus:ring focus:ring-zinc-400/40"
            :class="isDarkMode
              ? 'bg-black/40 border-zinc-700 text-white placeholder:text-zinc-400'
              : 'bg-white border-zinc-300 text-black placeholder:text-zinc-400'" />
        </div>
      </div>

      <!-- Separator -->
      <div class="my-6 h-px"
           :class="isDarkMode ? 'bg-zinc-800' : 'bg-zinc-200'"></div>

      <!-- Section 2: Appointment details -->
      <h2 class="text-lg font-semibold mb-4" :class="isDarkMode ? 'text-white' : 'text-zinc-900'">
        {{ language === 'FR' ? 'Détails du rendez-vous' : 'Appointment details' }}
      </h2>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-6">
        <div>
          <label class="block mb-1 font-medium">{{ texts[language].advisorType }}</label>
          <select v-model="form.advisor" required
            class="w-full px-3 py-2 rounded-lg border focus:outline-none focus:ring focus:ring-zinc-400/40"
            :class="isDarkMode
              ? 'bg-black/40 border-zinc-700 text-white'
              : 'bg-white border-zinc-300 text-black'">
            <option value="">{{ texts[language].advisorPlaceholder }}</option>
            <option>{{ texts[language].technicalService }}</option>
            <option>{{ texts[language].manager }}</option>
            <option>{{ texts[language].itManager }}</option>
            <option>{{ texts[language].receptionist }}</option>
          </select>
        </div>

        <div>
          <label class="block mb-1 font-medium">{{ texts[language].meetingReason }}</label>
          <select v-model="form.reason" required
            class="w-full px-3 py-2 rounded-lg border focus:outline-none focus:ring focus:ring-zinc-400/40"
            :class="isDarkMode
              ? 'bg-black/40 border-zinc-700 text-white'
              : 'bg-white border-zinc-300 text-black'">
            <option value="">{{ texts[language].reasonPlaceholder }}</option>
            <option>{{ texts[language].buyCar }}</option>
            <option>{{ texts[language].submitCV }}</option>
            <option>{{ texts[language].serviceInquiry }}</option>
            <option>{{ texts[language].partnership }}</option>
          </select>
        </div>
      </div>

      <!-- Actions -->
      <div class="mt-8 flex flex-col sm:flex-row items-center justify-end gap-3">
        <button
          type="button"
          @click="emit('navigate','home')"
          class="w-full sm:w-auto px-4 py-2 rounded-lg font-medium transition-colors"
          :class="isDarkMode
            ? 'bg-zinc-800 text-white hover:bg-zinc-700'
            : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
          {{ language === 'FR' ? 'Annuler' : 'Cancel' }}
        </button>

        <button
          type="submit"
          class="w-full sm:w-auto px-5 py-2.5 rounded-lg font-semibold transition-colors"
          :class="isDarkMode
            ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
            : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'">
          {{ texts[language].submit }}
        </button>
      </div>
    </form>
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
    pageTitle: "Rencontrer un conseiller",
    intro: "Sélectionnez le type de conseiller que vous souhaitez rencontrer et la raison de votre visite.",
    name: "Nom complet",
    namePlaceholder: "Entrez votre nom complet",
    advisorType: "Type de conseiller",
    advisorPlaceholder: "Sélectionnez un conseiller",
    technicalService: "Service technique",
    manager: "Gérant",
    itManager: "Responsable informatique",
    receptionist: "Réceptionniste",
    meetingReason: "Raison du rendez-vous",
    reasonPlaceholder: "Sélectionnez une raison",
    buyCar: "Acheter une voiture",
    submitCV: "Déposer un CV",
    serviceInquiry: "Demande d'informations sur un service",
    partnership: "Proposition de partenariat",
    submit: "Envoyer la demande",
    lightMode: "Mode clair",
    darkMode: "Mode sombre"
  },
  EN: {
    title: "My Space",
    pageTitle: "Meet an Advisor",
    intro: "Select the type of advisor you want to meet and the reason for your visit.",
    name: "Full Name",
    namePlaceholder: "Enter your full name",
    advisorType: "Advisor Type",
    advisorPlaceholder: "Select an advisor",
    technicalService: "Technical Service",
    manager: "Manager",
    itManager: "IT Manager",
    receptionist: "Receptionist",
    meetingReason: "Meeting Reason",
    reasonPlaceholder: "Select a reason",
    buyCar: "Buy a car",
    submitCV: "Submit a CV",
    serviceInquiry: "Service inquiry",
    partnership: "Partnership proposal",
    submit: "Submit Request",
    lightMode: "Light mode",
    darkMode: "Dark mode"
  }
}

const form = ref({
  name: '',
  advisor: '',
  reason: ''
})

const submitForm = () => {
  alert(`${texts[language.value].submit}: ${JSON.stringify(form.value)}`)
}

onMounted(() => {
  saveSettings()
})
</script>
