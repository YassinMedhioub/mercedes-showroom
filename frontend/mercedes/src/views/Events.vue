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
          {{ t('events') }}
        </h1>

        <!-- Back -->
        <button
          @click="router.push('/dashboard')"
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
        {{ t('eventsSubs') }}
      </p>
    </section>

    <!-- EVENTS GRID -->
    <section class="w-full max-w-6xl mx-auto px-3 sm:px-6 pb-14">
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="event in events"
          :key="event.id"
          @click="handleEventClick(event)"
          class="rounded-2xl p-4 sm:p-6 shadow-xl flex flex-col items-center transition-all duration-200 group cursor-pointer hover:scale-[1.02]"
          :class="props.isDark
            ? 'bg-zinc-900/90 text-white border border-zinc-800 hover:bg-gray-300 hover:text-black'
            : 'bg-white text-zinc-800 border border-zinc-200 hover:bg-zinc-100'"
        >
          <!-- Main event image -->
          <img
            :src="getMainImage(event)"
            :alt="event.title"
            class="mb-4 rounded-xl w-full h-44 object-cover shadow-md bg-white"
          />

          <div class="font-semibold text-lg sm:text-xl mb-1">{{ event.title }}</div>
          <div class="text-sm mb-2">
            {{ formatDate(event.date) }} • {{ event.location }}
          </div>

          <div class="flex flex-row gap-3 w-full justify-center">
            <button
              @click.stop="handleParticipateClick(event)"
              class="px-4 py-2 rounded-lg text-sm font-semibold transition-colors"
              :class="[
                getButtonClass(event.id)
              ]"
              :disabled="!isAuthenticated || hasParticipated(event.id)"
            >
              {{ getButtonText(event.id) }}
            </button>
            <button
              @click.stop="handleEventClick(event)"
              class="px-4 py-2 rounded-lg text-sm font-semibold transition-colors"
              :class="props.isDark
                ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
                : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'"
            >
              {{ t('details') }}
            </button>
          </div>
        </div>
      </div>
    </section>

  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useI18n } from '../composables/useI18n';
import { fetchEvents, fetchUserParticipations, participateInEvent } from '../services/api'; // NEW: Import fetchUserParticipations

/* Props */
const props = defineProps({
  isDark: { type: Boolean, default: false },
  language: { type: String, default: 'FR' },
})

const router = useRouter()
const { t } = useI18n()

/* Auth State */
const token = computed(() => {
  const tok = localStorage.getItem('authToken')
  console.log("tok: ",tok);
  console.log('🔍 Token check:', tok ? 'Present (' + tok.substring(0, 10) + '...' + ')' : 'MISSING')
  return tok
})
const isAuthenticated = computed(() => {
  const auth = !!token.value
  console.log('🛡️ isAuthenticated:', auth)
  return auth
})

// NEW: User's participated event IDs (Set for fast lookup)
const participatedEvents = ref(new Set())

// Watch for token changes (e.g., if login happens in another tab or post-navigation)
watch(token, (newToken) => {
  console.log('📱 Token changed:', newToken ? 'Updated' : 'Cleared')
  if (newToken) {
    // Refetch participations if authenticated
    loadUserParticipations()
  } else {
    participatedEvents.value.clear()
  }
})

/* Events */
const events = ref([])

onMounted(async () => {
  console.log('🚀 Events component mounted')
  console.log('🔑 Initial token:', localStorage.getItem('authToken'))
  console.log('🛡️ Initial auth:', isAuthenticated.value)
  try {
    events.value = await fetchEvents()
    console.log('🎉 Events loaded:', events.value.map(event => ({ id: event.id, title: event.title })))
  } catch (err) {
    console.error('Failed to fetch events:', err)
  }

  // NEW: Load user's participations if authenticated
  if (isAuthenticated.value) {
    await loadUserParticipations()
  }
})

// NEW: Load user's participated events
async function loadUserParticipations() {
  if (!isAuthenticated.value) return
  try {
    const userParticipations = await fetchUserParticipations()  // Assume returns array of event IDs
    participatedEvents.value = new Set(userParticipations)
    console.log('📋 Loaded participations:', Array.from(participatedEvents.value))
  } catch (err) {
    console.error('Failed to load participations:', err)
  }
}

/* Participation Handler */
async function handleParticipateClick(event) {
  console.log('🖱️ Participate clicked for event:', event.id)
  console.log('🛡️ Current auth state:', isAuthenticated.value)
  
  if (!isAuthenticated.value) {
    console.log('🚫 Not authenticated, redirecting to login')
    localStorage.setItem('pendingEventId', event.id.toString())
    router.push('/login')
    return
  }

  if (hasParticipated(event.id)) {
    console.log('ℹ️ Already participated in event:', event.id)
    return  // Or show alert: alert(t('alreadyParticipated'))
  }

  try {
    console.log('✅ Authenticating and participating...')
    await participateInEvent(event.id)
    alert(t('participationSuccess'))
    console.log('🎉 Participated in event:', event.id)
    // NEW: Refresh participations after success
    await loadUserParticipations()
  } catch (err) {
    console.error('❌ Participation failed:', err)
    const errorMessage = err.response?.data?.error || err.response?.data?.message || err.message || t('participationError')
    if (errorMessage.toLowerCase().includes('already') || errorMessage.toLowerCase().includes('participate')) {
      alert(t('alreadyParticipated'))
      // NEW: Refresh on "already" error
      await loadUserParticipations()
    } else {
      alert(errorMessage)
    }
  }
}

// NEW: Check if user has participated in event
function hasParticipated(eventId) {
  return participatedEvents.value.has(eventId)
}

// NEW: Get button text based on state
function getButtonText(eventId) {
  if (!isAuthenticated.value) return t('loginToParticipate')
  if (hasParticipated(eventId)) return t('participated')
  return t('participate')
}

// NEW: Get button class based on state
function getButtonClass(eventId) {
  const baseClass = props.isDark
    ? 'bg-green-700 text-white hover:bg-green-800'
    : 'bg-green-600 text-white hover:bg-green-700'
  
  if (!isAuthenticated.value) {
    return baseClass + ' opacity-50 hover:opacity-75'
  }
  
  if (hasParticipated(eventId)) {
    return props.isDark
      ? 'bg-zinc-700 text-white'
      : 'bg-zinc-400 text-zinc-700 border border-zinc-300'
  }
  
  return baseClass
}

/* Navigation handler */
function handleEventClick(event) {
  console.log('🎉 Event clicked:', event.id, event.title)
  console.log('📍 Current route:', router.currentRoute.value.path)
  console.log('🔗 Router instance:', router)
  
  try {
    console.log('➡️ Attempting navigation to:', `/event/${event.id}`)
    router.push(`/event/${event.id}`)
  } catch (error) {
    console.error('❌ Navigation error:', error)
  }
}

/* Helpers */
function getMainImage(event) {
  return event.images && event.images.length ? 'http://localhost:8080' + event.images[0] : '/fallback.jpg';
}

function formatDate(date) {
  const d = new Date(date)
  return props.language === 'FR'
    ? d.toLocaleDateString('fr-FR', { day: 'numeric', month: 'long', year: 'numeric' })
    : d.toLocaleDateString('en-US', { day: 'numeric', month: 'long', year: 'numeric' })
}
</script>