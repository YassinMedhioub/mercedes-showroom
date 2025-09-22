
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

        <!-- Subtitle -->
        <p class="mb-8 max-w-3xl" :class="props.isDark ? 'text-zinc-300' : 'text-zinc-700'">
          {{ t('intro') }}
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
            {{ t('yourInfo') }}
          </h2>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-6">
            <div class="md:col-span-2">
              <label class="block mb-1 font-medium">{{ t('name') }}</label>
              <input
                v-model="form.name"
                type="text"
                required
                autocomplete="name"
                :placeholder="t('namePlaceholder')"
                class="w-full px-3 py-2 rounded-lg border focus:outline-none focus:ring focus:ring-zinc-400/40"
                :class="props.isDark
                  ? 'bg-black/40 border-zinc-700 text-white placeholder:text-zinc-400'
                  : 'bg-white border-zinc-300 text-black placeholder:text-zinc-500'"
              />
            </div>

            <div class="md:col-span-2">
              <label class="block mb-1 font-medium">{{ t('email') }}</label>
              <input
                v-model="form.email"
                type="email"
                required
                autocomplete="email"
                :placeholder="t('emailPlaceholder')"
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
            {{ t('appointmentDetails') }}
          </h2>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-6">
            <div>
              <label class="block mb-1 font-medium">{{ t('advisorType') }}</label>
              <select
                v-model="form.advisor"
                required
                class="w-full px-3 py-2 rounded-lg border focus:outline-none focus:ring focus:ring-zinc-400/40"
                :class="props.isDark
                  ? 'bg-black/40 border-zinc-700 text-white'
                  : 'bg-white border-zinc-300 text-black'"
              >
                <option value="">{{ t('advisorPlaceholder') }}</option>
                <option value="technical">{{ t('technicalService') }}</option>
                <option value="manager">{{ t('manager') }}</option>
                <option value="it">{{ t('itManager') }}</option>
                <option value="receptionist">{{ t('receptionist') }}</option>
              </select>
            </div>

            <div>
              <label class="block mb-1 font-medium">{{ t('meetingReason') }}</label>
              <select
                v-model="form.reason"
                required
                class="w-full px-3 py-2 rounded-lg border focus:outline-none focus:ring focus:ring-zinc-400/40"
                :class="props.isDark
                  ? 'bg-black/40 border-zinc-700 text-white'
                  : 'bg-white border-zinc-300 text-black'"
              >
                <option value="">{{ t('reasonPlaceholder') }}</option>
                <option value="buy">{{ t('buyCar') }}</option>
                <option value="cv">{{ t('submitCV') }}</option>
                <option value="service">{{ t('serviceInquiry') }}</option>
                <option value="partnership">{{ t('partnership') }}</option>
              </select>
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
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from '../composables/useI18n'
import { submitAdvisorMeeting } from '../services/api'

/* Props */
const props = defineProps({
  isDark: { type: Boolean, default: false },
  language: { type: String, default: 'FR' },
})

const router = useRouter()
const { t } = useI18n()

const form = ref({ 
  name: '', 
  email: '', 
  advisor: '', 
  reason: '' 
})
const isSubmitting = ref(false)

/* Form validation */
const isFormValid = () => {
  return form.value.name.trim() && 
         form.value.email.trim() && 
         form.value.advisor && 
         form.value.reason
}

/* Submit handler */
const submitForm = async () => {
  if (!isFormValid()) {
    alert(t('formError'))
    return
  }

  isSubmitting.value = true
  try {
    await submitAdvisorMeeting(form.value)
    alert(t('submitSuccess'))
    router.push('/dashboard')
  } catch (error) {
    console.error('Failed to submit advisor meeting:', error)
    alert(t('submitError'))
  } finally {
    isSubmitting.value = false
  }
}
</script>