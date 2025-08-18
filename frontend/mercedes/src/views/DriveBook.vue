<template>
  <div
    :class="[
      'min-h-screen flex flex-col bg-gradient-to-br',
      props.isDark
        ? 'from-[#0b0b0e] via-[#0f1115] to-[#1a1d22] text-zinc-100'
        : 'from-[#f6f7fb] via-[#f1f2f6] to-[#e7eaef] text-zinc-900'
    ]"
  >
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
              : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'"
          >
            ← {{ props.language === 'FR' ? 'Retour' : 'Back' }}
          </button>
        </div>

        <!-- Intro -->
        <p class="mb-8 max-w-3xl" :class="props.isDark ? 'text-zinc-300' : 'text-zinc-700'">
          {{ texts[props.language].intro }}
        </p>

        <!-- Form -->
        <form
          @submit.prevent="submitForm"
          :class="[
            'rounded-2xl p-6 sm:p-8 shadow-xl',
            props.isDark
              ? 'bg-zinc-900/90 text-white border border-zinc-800'
              : 'bg-white text-zinc-800 border border-zinc-200'
          ]"
          novalidate
        >
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-6">
            <!-- Name (full width) -->
            <div class="md:col-span-2">
              <label :for="ids.name" class="block mb-1 font-medium">
                {{ texts[props.language].name }}
              </label>
              <input
                :id="ids.name"
                v-model.trim="form.name"
                type="text"
                required
                autocomplete="name"
                :placeholder="texts[props.language].namePlaceholder"
                class="w-full px-3 py-2 rounded-lg border focus:outline-none focus:ring focus:ring-zinc-400/40"
                :class="props.isDark
                  ? 'bg-black/40 border-zinc-700 text-white placeholder:text-zinc-400'
                  : 'bg-white border-zinc-300 text-black placeholder:text-zinc-400'"
                :aria-invalid="!form.name ? 'true' : 'false'"
              />
            </div>

            <!-- Phone -->
            <div>
              <label :for="ids.phone" class="block mb-1 font-medium">
                {{ texts[props.language].phone }}
              </label>
              <input
                :id="ids.phone"
                v-model.trim="form.phone"
                type="tel"
                inputmode="tel"
                required
                :placeholder="texts[props.language].phonePlaceholder"
                pattern="^[+]?[\\d\\s()-]{8,}$"
                autocomplete="tel"
                class="w-full px-3 py-2 rounded-lg border focus:outline-none focus:ring focus:ring-zinc-400/40"
                :class="props.isDark
                  ? 'bg-black/40 border-zinc-700 text-white placeholder:text-zinc-400'
                  : 'bg-white border-zinc-300 text-black placeholder:text-zinc-400'"
                :aria-invalid="!isPhoneValid ? 'true' : 'false'"
              />
              <p class="mt-1 text-xs" :class="props.isDark ? 'text-zinc-400' : 'text-zinc-500'">
                {{ props.language === 'FR' ? 'Ex. +216 12 345 678' : 'e.g. +216 12 345 678' }}
              </p>
            </div>

            <!-- Model -->
            <div>
              <label :for="ids.model" class="block mb-1 font-medium">
                {{ texts[props.language].model }}
              </label>
              <select
                :id="ids.model"
                v-model="form.model"
                required
                class="w-full px-3 py-2 rounded-lg border focus:outline-none focus:ring focus:ring-zinc-400/40"
                :class="props.isDark
                  ? 'bg-black/40 border-zinc-700 text-white'
                  : 'bg-white border-zinc-300 text-black'"
                :aria-invalid="!form.model ? 'true' : 'false'"
              >
                <option value="">{{ texts[props.language].modelPlaceholder }}</option>
                <option>Classe S</option>
                <option>Classe A</option>
                <option>GLE</option>
                <option>AMG GT</option>
                <option>Classe C</option>
                <option>GLA</option>
              </select>
            </div>

            <!-- Date -->
            <div class="md:col-span-2 md:col-start-2">
              <label :for="ids.date" class="block mb-1 font-medium">
                {{ texts[props.language].date }}
              </label>
              <input
                :id="ids.date"
                v-model="form.date"
                type="date"
                required
                :min="today"
                class="w-full px-3 py-2 rounded-lg border focus:outline-none focus:ring focus:ring-zinc-400/40"
                :class="props.isDark
                  ? 'bg-black/40 border-zinc-700 text-white'
                  : 'bg-white border-zinc-300 text-black'"
                :aria-invalid="!form.date ? 'true' : 'false'"
              />
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
                : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'"
            >
              {{ props.language === 'FR' ? 'Annuler' : 'Cancel' }}
            </button>

            <button
              type="submit"
              class="w-full sm:w-auto px-5 py-2.5 rounded-lg font-semibold transition-colors"
              :class="props.isDark
                ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
                : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'"
            >
              {{ texts[props.language].submit }}
            </button>
          </div>
        </form>
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'

/* Props like ChatWidget: parent controls theme & language */
const props = defineProps({
  isDark: { type: Boolean, default: false },
  language: { type: String, default: 'FR' },
})

const emit = defineEmits(['navigate'])

/* i18n */
const texts = {
  FR: {
    title: 'Mon espace',
    pageTitle: 'Demander un essai',
    intro: 'Remplissez ce formulaire pour réserver un essai sur route dans notre showroom Mercedes-Benz.',
    name: 'Nom complet',
    namePlaceholder: 'Entrez votre nom complet',
    phone: 'Téléphone',
    phonePlaceholder: 'Entrez votre numéro de téléphone',
    model: 'Modèle',
    modelPlaceholder: 'Sélectionnez un modèle',
    date: 'Date souhaitée',
    submit: 'Envoyer la demande',
  },
  EN: {
    title: 'My Space',
    pageTitle: 'Book a Test Drive',
    intro: 'Fill out this form to book a road test at our Mercedes-Benz showroom.',
    name: 'Full Name',
    namePlaceholder: 'Enter your full name',
    phone: 'Phone',
    phonePlaceholder: 'Enter your phone number',
    model: 'Model',
    modelPlaceholder: 'Select a model',
    date: 'Preferred Date',
    submit: 'Submit Request',
  },
}

/* Form state */
const form = ref({ name: '', phone: '', model: '', date: '' })
const ids = { name: 'name-input', phone: 'phone-input', model: 'model-select', date: 'date-input' }
const today = new Date().toISOString().slice(0, 10)
const isPhoneValid = computed(() => /^[+]?[\d\s()-]{8,}$/.test(form.value.phone || ''))

/* Submit */
const submitForm = () => {
  if (!form.value.name || !isPhoneValid.value || !form.value.model || !form.value.date) {
    alert(props.language === 'FR'
      ? 'Veuillez remplir correctement tous les champs.'
      : 'Please complete all fields correctly.')
    return
  }
  alert(`${texts[props.language].submit}: ${JSON.stringify(form.value)}`)
}
</script>
