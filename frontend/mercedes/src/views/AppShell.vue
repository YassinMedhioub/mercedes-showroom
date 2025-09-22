
<template>
  <HeaderBar
    v-if="$route.name !== 'login'"
    :is-admin="isAdminView"
    @logout="handleLogout"
  />

  <router-view
    v-if="ready || $route.name !== 'login'"
    :car-id="selectedCarId"
    :adminName="currentUser.fullName"
    :isDark="isDark"
    :language="lang"
    v-bind="$attrs"
    @login="handleLogin"
    @logout="handleLogout"
  />

  <ChatWidget
    v-if="$route.name !== 'login'"
    :language="lang"
    :isDark="isDark"
    :cars="carsArray"
    @navigate="p => router.push(p)"
  />

  <FooterBar v-if="$route.name !== 'login'" />
</template>
<script setup>
import { computed, nextTick, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import ChatWidget from '../components/ChatWidget.vue'
import FooterBar from '../components/FooterBar.vue'
import HeaderBar from '../components/HeaderBar.vue'
import { usePrefs } from '../components/usePrefs'
import { fetchCars } from '../services/api'
import { clearAuth, setAuth, verifyUser } from '../services/auth'

const router = useRouter()
const route = useRoute()
const { lang, isDark } = usePrefs()

const ready = ref(true) // Start with true - no loading screen
const carsArray = ref([])
const selectedCarId = ref(null)
const currentUser = ref({
  fullName: localStorage.getItem('adminName') || 'Administrateur'
})

const isAdminView = computed(() => route.meta.requiresAdmin === true)

// Update selectedCarId when route changes (e.g., for /car/:id)
watch(
  () => route.params.id,
  (newId) => {
    selectedCarId.value = newId ? Number(newId) : null
  }
)

// Enhanced handleLogin with better timing
async function handleLogin(eventData) {
  console.log('🔍 APP HANDLE LOGIN:', eventData)
  
  const { role, fullName } = eventData || {}
  
  if (!role) {
    console.warn('🔶 NO ROLE IN LOGIN EVENT - USING LOCALSTORAGE')
    // Fallback to localStorage if event data is incomplete
    const storedRole = localStorage.getItem('role')
    if (storedRole) role = storedRole
  }
  
  try {
    // Set auth state
    setAuth({ role, fullName })
    currentUser.value.fullName = fullName || localStorage.getItem('adminName') || 'Administrateur'
    
    console.log('🔍 APP AUTH STATE SET:', { role, fullName: currentUser.value.fullName })
    
    // Ensure DOM is updated before navigation
    await nextTick()
    
    // Determine landing page
    const landing = role === 'ADMIN' ? '/admin' : '/dashboard'
    console.log('🔍 APP NAVIGATING TO:', landing)
    
    // Navigate with error handling
    try {
      await router.push(landing)
      console.log('🟢 APP NAVIGATION SUCCESSFUL')
    } catch (navError) {
      console.error('🔴 APP NAVIGATION FAILED:', navError)
      // Fallback navigation
      window.location.href = landing
    }
    
  } catch (error) {
    console.error('🔴 APP LOGIN FAILED:', error)
    await router.push('/login')
  }
}

async function handleLogout() {
  try {
    console.log('🔍 APP LOGOUT STARTED')
    clearAuth()
    currentUser.value.fullName = 'Administrateur'
    await router.push('/login')
    console.log('🟢 APP LOGOUT SUCCESSFUL')
  } catch (error) {
    console.error('🔴 APP LOGOUT FAILED:', error)
    window.location.href = '/login'
  }
}

// Simplified initialization - no blocking operations
onMounted(async () => {
  console.log('🔍 APP ONMOUNTED - ROUTE:', route.name)
  
  // Set initial state based on current route
  if (route.name === 'login') {
    ready.value = true
    return
  }
  
  try {
    // Quick user verification for current session
    const user = await verifyUser()
    if (user) {
      currentUser.value.fullName = user.fullName || user.username || 'Administrateur'
    }
    
    // Background car fetch - don't block UI
    fetchCars()
      .then(cars => {
        carsArray.value = cars
        console.log('🟢 APP CARS LOADED:', cars.length)
      })
      .catch(error => {
        console.error('Failed to fetch cars:', error)
        carsArray.value = []
      })
      
  } catch (error) {
    console.error('App initialization error:', error)
    // Don't redirect - let router guard handle it
  }
  
  ready.value = true
  console.log('🟢 APP READY')
})

// Watch for route changes to update UI state
watch(() => route.name, async (newName) => {
  console.log('🔍 APP ROUTE CHANGED:', newName)
  
  if (newName === 'login') {
    // Clear sensitive data on login page
    currentUser.value.fullName = 'Administrateur'
  } else {
    // Refresh user data on authenticated pages
    try {
      const user = await verifyUser()
      if (user) {
        currentUser.value.fullName = user.fullName || user.username || 'Administrateur'
      }
    } catch (error) {
      console.error('Route change user verification failed:', error)
    }
  }
})
</script>