
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
            {{ t('pageTitle') }}
          </h1>

          <button
            type="button"
            @click="router.push('/dashboard')"
            class="inline-flex items-center justify-center px-3 py-2 rounded-lg text-sm font-medium transition-colors shrink-0"
            :class="props.isDark
              ? 'bg-zinc-800 text-white hover:bg-zinc-700'
              : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'"
          >
            ← {{ t('back') }}
          </button>
        </div>

        <!-- Intro -->
        <p class="mb-8 max-w-3xl" :class="props.isDark ? 'text-zinc-300' : 'text-zinc-700'">
          {{ t('intro') }}
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
            <!-- Name -->
            <div class="md:col-span-2">
              <label :for="ids.name" class="block mb-1 font-medium">
                {{ t('name') }}
              </label>
              <input
                :id="ids.name"
                v-model.trim="form.name"
                type="text"
                required
                autocomplete="name"
                :placeholder="t('namePlaceholder')"
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
                {{ t('phone') }}
              </label>
              <input
                :id="ids.phone"
                v-model.trim="form.phone"
                type="tel"
                inputmode="tel"
                required
                :placeholder="t('phonePlaceholder')"
                pattern="^[+]?[\d\s()-]{8,}$"
                autocomplete="tel"
                class="w-full px-3 py-2 rounded-lg border focus:outline-none focus:ring focus:ring-zinc-400/40"
                :class="props.isDark
                  ? 'bg-black/40 border-zinc-700 text-white placeholder:text-zinc-400'
                  : 'bg-white border-zinc-300 text-black placeholder:text-zinc-400'"
                :aria-invalid="!isPhoneValid ? 'true' : 'false'"
              />
              <p class="mt-1 text-xs" :class="props.isDark ? 'text-zinc-400' : 'text-zinc-500'">
                {{ t('phoneExample') }}
              </p>
            </div>

            <!-- Model -->
            <div>
              <label :for="ids.model" class="block mb-1 font-medium">
                {{ t('model') }}
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
                <option value="">{{ t('modelPlaceholder') }}</option>
                <option v-for="model in carModels" :key="model" :value="model">{{ model }}</option>
              </select>
            </div>

            <!-- Date -->
            <div class="md:col-span-2 md:col-start-2">
              <label :for="ids.date" class="block mb-1 font-medium">
                {{ t('date') }}
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
              @click="router.push('/dashboard')"
              class="w-full sm:w-auto px-4 py-2 rounded-lg font-medium transition-colors"
              :class="props.isDark
                ? 'bg-zinc-800 text-white hover:bg-zinc-700'
                : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'"
            >
              {{ t('cancel') }}
            </button>

            <button
              type="submit"
              :disabled="isSubmitting"
              class="w-full sm:w-auto px-5 py-2.5 rounded-lg font-semibold transition-colors disabled:opacity-50"
              :class="props.isDark
                ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
                : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'"
            >
              {{ isSubmitting ? t('submitting') : t('submit') }}
            </button>
          </div>
        </form>
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from '../composables/useI18n'
import { fetchCarModels, submitTestDrive } from '../services/api'

/* Props */
const props = defineProps({
  isDark: { type: Boolean, default: false },
  language: { type: String, default: 'FR' },
})

const router = useRouter()
const { t } = useI18n()

/* Form state */
const form = ref({ name: '', phone: '', model: '', date: '' })
const carModels = ref(['Classe S', 'Classe A', 'GLE', 'AMG GT', 'Classe C', 'GLA'])
const isSubmitting = ref(false)
const ids = { 
  name: 'name-input', 
  phone: 'phone-input', 
  model: 'model-select', 
  date: 'date-input' 
}
const today = new Date().toISOString().slice(0, 10)
const isPhoneValid = computed(() => /^[+]?[\d\s()-]{8,}$/.test(form.value.phone || ''))

/* Fetch car models */
onMounted(async () => {
  try {
    carModels.value = await fetchCarModels()
  } catch (err) {
    console.error('Failed to fetch car models:', err)
    // Fallback to default models if API fails
  }
})

/* Submit handler */
const submitForm = async () => {
  if (!form.value.name || !isPhoneValid.value || !form.value.model || !form.value.date) {
    alert(t('formError'))
    return
  }
  
  isSubmitting.value = true
  try {
    await submitTestDrive(form.value)
    alert(t('submitSuccess'))
    router.push('/dashboard')
  } catch (err) {
    console.error('Failed to submit test drive:', err)
    alert(t('submitError'))
  } finally {
    isSubmitting.value = false
  }
}
</script>