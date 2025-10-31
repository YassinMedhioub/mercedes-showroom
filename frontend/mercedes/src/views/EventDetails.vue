<template>
  <div
    :class="[
      'min-h-screen flex flex-col bg-gradient-to-br',
      props.isDark
        ? 'from-[#0b0b0e] via-[#0f1115] to-[#1a1d22] text-zinc-100'
        : 'from-[#f6f7fb] via-[#f1f2f6] to-[#e7eaef] text-zinc-900'
    ]"
  >
    <!-- HERO -->
    <section class="w-full max-w-6xl mx-auto px-3 sm:px-6 pt-8 sm:pt-12">
      <div class="w-full max-w-6xl mx-auto px-3 sm:px-6 mb-2 flex items-center justify-between">
        <!-- Title -->
        <h1 class="font-bold text-2xl sm:text-3xl text-current">
          {{ t('eventDetails') }}
        </h1>

        <!-- Back -->
        <button
          @click="router.push('/events')"
          class="px-3 py-2 rounded-lg text-sm font-medium transition-colors"
          :class="props.isDark
            ? 'bg-zinc-800 text-white hover:bg-zinc-700'
            : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'"
        >
          ← {{ props.language === 'FR' ? 'Retour' : 'Back' }}
        </button>
      </div>

      <!-- Subtitle -->
      <p
        :class="[
          'max-w-3xl mb-6 sm:mb-8',
          props.isDark ? 'text-zinc-300' : 'text-zinc-700'
        ]"
      >
        {{ t('eventDetailsSubs') }}
      </p>
    </section>

    <!-- EVENT DETAILS -->
    <section v-if="event" class="w-full max-w-6xl mx-auto px-3 sm:px-6 pb-14">
      <!-- Hero Image -->
      <div class="rounded-2xl overflow-hidden shadow-xl mb-6">
        <img
          :src="getMainImage(event)"
          :alt="event.title"
          class="w-full h-64 sm:h-96 object-cover"
        />
      </div>

      <!-- Event Info -->
      <div class="space-y-4 sm:space-y-6">
        <h2 class="font-bold text-3xl sm:text-4xl">{{ event.title }}</h2>
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 text-sm sm:text-base">
          <div class="flex items-center gap-2">
            <span class="text-lg">📅</span>
            <span>{{ formatDate(event.date) }}</span>
          </div>
          <div class="flex items-center gap-2">
            <span class="text-lg">📍</span>
            <span>{{ event.location }}</span>
          </div>
        </div>

        <!-- Description -->
        <p class="text-lg leading-relaxed whitespace-pre-line">
          {{ event.description }}
        </p>

        <!-- Images Gallery (if multiple images) -->
        <div v-if="event.images && event.images.length > 1" class="grid grid-cols-2 sm:grid-cols-3 gap-4">
          <img
            v-for="(image, index) in event.images.slice(1)"
            :key="index"
            :src="'http://localhost:8080' + image"
            :alt="event.title + ' image ' + (index + 1)"
            class="rounded-xl object-cover h-32 sm:h-48"
          />
        </div>

        <!-- Participate Button -->
        <div class="flex justify-center mt-8">
          <button
            @click="handleParticipateClick(event)"
            class="px-6 py-3 rounded-lg text-base font-semibold transition-colors"
            :class="getButtonClass(event.id)"
            :disabled="!isAuthenticated || hasParticipated(event.id)"
          >
            {{ getButtonText(event.id) }}
          </button>
        </div>
      </div>
    </section>

    <!-- Loading/Error States -->
    <section v-else class="w-full max-w-6xl mx-auto px-3 sm:px-6 pb-14 flex flex-col items-center justify-center">
      <div v-if="loading" class="text-center">
        <p>{{ t('loading') }}</p>
      </div>
      <div v-else-if="error" class="text-center text-red-500">
        <p>{{ error }}</p>
        <button @click="loadEvent" class="mt-4 px-4 py-2 rounded-lg bg-blue-500 text-white">
          {{ t('retry') }}
        </button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useI18n } from '../composables/useI18n'
import { fetchEventById, fetchUserParticipations, participateInEvent } from '../services/api'

/* Props */
const props = defineProps({
  isDark: { type: Boolean, default: false },
  language: { type: String, default: 'FR' },
})

const route = useRoute()
const router = useRouter()
const { t } = useI18n()

const eventId = computed(() => parseInt(route.params.id))

/* Auth State */
const token = computed(() => localStorage.getItem('authToken'))
const isAuthenticated = computed(() => !!token.value)

/* Event State */
const event = ref(null)
const loading = ref(true)
const error = ref('')

// User's participated event IDs
const participatedEvents = ref(new Set())

onMounted(async () => {
  if (isAuthenticated.value) {
    await loadUserParticipations()
  }
  await loadEvent()
})

/* Load Event Details */
async function loadEvent() {
  loading.value = true
  error.value = ''
  try {
    event.value = await fetchEventById(eventId.value)
    console.log('🎉 Event loaded:', event.value)
  } catch (err) {
    console.error('Failed to load event:', err)
    error.value = t('eventLoadError')
  } finally {
    loading.value = false
  }
}

/* Load User's Participations */
async function loadUserParticipations() {
  if (!isAuthenticated.value) return
  try {
    const userParticipations = await fetchUserParticipations()
    participatedEvents.value = new Set(userParticipations)
    console.log('📋 Loaded participations:', Array.from(participatedEvents.value))
  } catch (err) {
    console.error('Failed to load participations:', err)
  }
}

/* Participation Handler */
async function handleParticipateClick(event) {
  if (!isAuthenticated.value) {
    localStorage.setItem('pendingEventId', event.id.toString())
    router.push('/login')
    return
  }

  if (hasParticipated(event.id)) {
    console.log('ℹ️ Already participated in event:', event.id)
    return
  }

  try {
    await participateInEvent(event.id)
    alert(t('participationSuccess'))
    await loadUserParticipations()  // Refresh
  } catch (err) {
    console.error('❌ Participation failed:', err)
    const errorMessage = err.response?.data?.error || err.message || t('participationError')
    if (errorMessage.toLowerCase().includes('already')) {
      alert(t('alreadyParticipated'))
      await loadUserParticipations()  // Refresh on error
    } else {
      alert(errorMessage)
    }
  }
}

// Check if participated
function hasParticipated(eventId) {
  return participatedEvents.value.has(eventId)
}

// Button Text
function getButtonText(eventId) {
  if (!isAuthenticated.value) return t('loginToParticipate')
  if (hasParticipated(eventId)) return t('participated')
  return t('participate')
}

// Button Class
function getButtonClass(eventId) {
  const baseClass = props.isDark
    ? 'bg-green-700 text-white hover:bg-green-800'
    : 'bg-green-600 text-white hover:bg-green-700'
  
  if (!isAuthenticated.value) {
    return baseClass + ' opacity-50 cursor-not-allowed hover:opacity-75'
  }
  
  if (hasParticipated(eventId)) {
    return props.isDark
      ? 'bg-zinc-700 text-white cursor-not-allowed'
      : 'bg-zinc-400 text-zinc-700 cursor-not-allowed border border-zinc-300'
  }
  
  return baseClass
}

/* Helpers */
function getMainImage(event) {
  return event.images && event.images.length ? 'http://localhost:8080' + event.images[0] : '/fallback.jpg'
}

function formatDate(date) {
  const d = new Date(date)
  return props.language === 'FR'
    ? d.toLocaleDateString('fr-FR', { day: 'numeric', month: 'long', year: 'numeric' })
    : d.toLocaleDateString('en-US', { day: 'numeric', month: 'long', year: 'numeric' })
}
</script>