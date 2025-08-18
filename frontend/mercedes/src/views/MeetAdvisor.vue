<template>
  <div
    :class="[
      'min-h-screen flex flex-col bg-gradient-to-br',
      props.isDark
        ? 'from-[#0b0b0e] via-[#0f1115] to-[#1a1d22] text-zinc-100'
        : 'from-[#f6f7fb] via-[#f1f2f6] to-[#e7eaef] text-zinc-900'
    ]"
  >
    <!-- PAGE CONTAINER -->
    <main class="flex-1 w-full">
      <div class="w-full max-w-5xl mx-auto px-4 sm:px-6 py-8">
        <!-- Title + Back -->
        <div class="flex flex-col sm:flex-row sm:items-baseline sm:justify-between gap-3 mb-2">
          <h1 class="font-extrabold leading-tight text-3xl sm:text-4xl text-current">
            {{ texts[props.language].pageTitle }}
          </h1>

          <button
            type="button"
            @click="emit('navigate','dashboard')"
            class="inline-flex items-center justify-center px-3 py-2 rounded-lg text-sm font-medium transition-colors shrink-0"
            :class="props.isDark
              ? 'bg-zinc-800 text-white hover:bg-zinc-700'
              : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
            ← {{ props.language === 'FR' ? 'Retour' : 'Back' }}
          </button>
        </div>

        <!-- Subtitle -->
        <p class="mb-8 max-w-3xl" :class="props.isDark ? 'text-zinc-300' : 'text-zinc-700'">
          {{ texts[props.language].intro }}
        </p>

        <!-- FORM CARD -->
        <form
          @submit.prevent="submitForm"
          :class="[
            'rounded-2xl p-6 sm:p-8 shadow-xl',
            props.isDark
              ? 'bg-zinc-900/90 text-white border border-zinc-800'
              : 'bg-white text-zinc-800 border border-zinc-200'
          ]"
        >
          <!-- Section 1: Your info -->
          <h2 class="text-lg font-semibold mb-4">
            {{ props.language === 'FR' ? 'Vos informations' : 'Your information' }}
          </h2>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-6">
            <div class="md:col-span-2">
              <label class="block mb-1 font-medium">{{ texts[props.language].name }}</label>
              <input
                v-model="form.name"
                type="text"
                required
                autocomplete="name"
                :placeholder="texts[props.language].namePlaceholder"
                class="w-full px-3 py-2 rounded-lg border focus:outline-none focus:ring focus:ring-zinc-400/40"
                :class="props.isDark
                  ? 'bg-black/40 border-zinc-700 text-white placeholder:text-zinc-400'
                  : 'bg-white border-zinc-300 text-black placeholder:text-zinc-500'"
              />
            </div>
          </div>

          <!-- Separator -->
          <div class="my-6 h-px" :class="props.isDark ? 'bg-zinc-800' : 'bg-zinc-200'"></div>

          <!-- Section 2: Appointment details -->
          <h2 class="text-lg font-semibold mb-4">
            {{ props.language === 'FR' ? 'Détails du rendez-vous' : 'Appointment details' }}
          </h2>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-6">
            <div>
              <label class="block mb-1 font-medium">{{ texts[props.language].advisorType }}</label>
              <select
                v-model="form.advisor"
                required
                class="w-full px-3 py-2 rounded-lg border focus:outline-none focus:ring focus:ring-zinc-400/40"
                :class="props.isDark
                  ? 'bg-black/40 border-zinc-700 text-white'
                  : 'bg-white border-zinc-300 text-black'">
                <option value="">{{ texts[props.language].advisorPlaceholder }}</option>
                <option>{{ texts[props.language].technicalService }}</option>
                <option>{{ texts[props.language].manager }}</option>
                <option>{{ texts[props.language].itManager }}</option>
                <option>{{ texts[props.language].receptionist }}</option>
              </select>
            </div>

            <div>
              <label class="block mb-1 font-medium">{{ texts[props.language].meetingReason }}</label>
              <select
                v-model="form.reason"
                required
                class="w-full px-3 py-2 rounded-lg border focus:outline-none focus:ring focus:ring-zinc-400/40"
                :class="props.isDark
                  ? 'bg-black/40 border-zinc-700 text-white'
                  : 'bg-white border-zinc-300 text-black'">
                <option value="">{{ texts[props.language].reasonPlaceholder }}</option>
                <option>{{ texts[props.language].buyCar }}</option>
                <option>{{ texts[props.language].submitCV }}</option>
                <option>{{ texts[props.language].serviceInquiry }}</option>
                <option>{{ texts[props.language].partnership }}</option>
              </select>
            </div>
          </div>

          <!-- Actions -->
          <div class="mt-8 flex flex-col sm:flex-row items-center justify-end gap-3">
            <button
              type="button"
              @click="emit('navigate','dashboard')"
              class="w-full sm:w-auto px-4 py-2 rounded-lg font-medium transition-colors"
              :class="props.isDark
                ? 'bg-zinc-800 text-white hover:bg-zinc-700'
                : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
              {{ props.language === 'FR' ? 'Annuler' : 'Cancel' }}
            </button>

            <button
              type="submit"
              class="w-full sm:w-auto px-5 py-2.5 rounded-lg font-semibold transition-colors"
              :class="props.isDark
                ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
                : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'">
              {{ texts[props.language].submit }}
            </button>
          </div>
        </form>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'

/* Parent controls theme & language, like ChatWidget */
const props = defineProps({
  isDark: { type: Boolean, default: false },
  language: { type: String, default: 'FR' },
})

const emit = defineEmits(['navigate'])

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
  }
}

const form = ref({ name: '', advisor: '', reason: '' })

const submitForm = () => {
  alert(`${texts[props.language].submit}: ${JSON.stringify(form.value)}`)
}
</script>
