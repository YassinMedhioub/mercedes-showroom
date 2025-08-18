<template>
  <!-- Render only after we decide the landing page to avoid flicker -->
   <HeaderBar v-if="ready && currentPage !== 'login'" @logout="handleLogout" />
  <component
    v-if="ready"
    :is="pageComponents[currentPage]"
    :key="currentPage"               
    :car-id="selectedCarId"
    @login="handleLogin"
    @logout="handleLogout"
    @navigate="handleNavigate"
  />

  <ChatWidget
    v-if="currentPage !== 'login'"
    :language="language"
    :isDark="isDarkMode"
    :cars="carsArray"
    @navigate="handleNavigate"
  />
  <FooterBar v-if="ready && currentPage !== 'login'" />
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue'
import ChatWidget from '../components/ChatWidget.vue'
import FooterBar from '../components/FooterBar.vue'
import HeaderBar from '../components/HeaderBar.vue'
import { usePrefs } from '../components/usePrefs'

/* Pages */
import AdminDashboard from './AdminDashboard.vue'
import AvailableCars from './AvailableCars.vue'
import CarDetails from './CarDetails.vue'
import Login from './Login.vue'
import ManageUsers from './ManageUsers.vue'
import MeetAdvisor from './MeetAdvisor.vue'
import SpecialOffers from './SpecialOffers.vue'
import UserDashboard from './UserDashboard.vue'

/* Page map (manual pager, no URL changes) */
const pageComponents = {
  login: Login,
  dashboard: UserDashboard,
  adminDashboard: AdminDashboard, 
  ManageUsers: ManageUsers,
  cars: AvailableCars,
  meetAdvisor: MeetAdvisor,
  offers: SpecialOffers,
  car: CarDetails,
}

/* State */
const isLoggedIn = ref(false)
const currentPage = ref('login')     // start at login
const selectedCarId = ref(null)
const ready = ref(false)
const { lang, isDark } = usePrefs()

/* Decide where to land on load */
function pickLandingPage() {
  if (!isLoggedIn.value) { currentPage.value = 'login'; return }
  const saved = localStorage.getItem('currentPage')
  if (saved && pageComponents[saved]) {
    if (saved === 'car') {
      const savedId = localStorage.getItem('selectedCarId')
      if (savedId) selectedCarId.value = savedId
      else { currentPage.value = 'cars'; localStorage.setItem('currentPage','cars'); return }
    }
    currentPage.value = saved
  } else {
    const role = localStorage.getItem('role')
    const landing = role === 'ADMIN' ? 'adminDashboard' : 'dashboard'
    currentPage.value = landing
    localStorage.setItem('currentPage', landing)
  }
}

onMounted(() => {
  isLoggedIn.value = localStorage.getItem('isLoggedIn') === 'true'
  const savedId = localStorage.getItem('selectedCarId')
  if (savedId) selectedCarId.value = savedId
  pickLandingPage()
  window.addEventListener('storage', onStorage)
  ready.value = true
})
onBeforeUnmount(() => window.removeEventListener('storage', onStorage))

function onStorage(e) {
  if (e.key === 'isLoggedIn') { isLoggedIn.value = e.newValue === 'true'; pickLandingPage() }
  if (e.key === 'currentPage') {
    const cp = e.newValue
    if (cp && pageComponents[cp]) {
      if (cp === 'car') {
        const sid = localStorage.getItem('selectedCarId')
        if (!sid) return
        selectedCarId.value = sid
      }
      currentPage.value = cp
    }
  }
  if (e.key === 'selectedCarId') selectedCarId.value = e.newValue
}

function handleLogin({ role } = {}) {
  localStorage.setItem('isLoggedIn', 'true')
  if (role) localStorage.setItem('role', role)
  const landing = role === 'ADMIN' ? 'adminDashboard' : 'dashboard'
  currentPage.value = landing                      // ← no URL change
  localStorage.setItem('currentPage', landing)
}

function handleLogout() {
  localStorage.removeItem('isLoggedIn')
  localStorage.removeItem('role')
  localStorage.removeItem('currentPage')
  localStorage.removeItem('selectedCarId')
  selectedCarId.value = null
  currentPage.value = 'login'
}

function go(page, extras = {}) {
  if (!isLoggedIn.value && page !== 'login') { currentPage.value = 'login'; return }
  if (!pageComponents[page]) return
  if (page === 'car') {
    const id = extras.id ?? selectedCarId.value ?? localStorage.getItem('selectedCarId')
    if (!id) { currentPage.value = 'cars'; localStorage.setItem('currentPage','cars'); return }
    selectedCarId.value = id
    localStorage.setItem('selectedCarId', String(id))
  } else if (currentPage.value === 'car') {
    selectedCarId.value = null
    localStorage.removeItem('selectedCarId')
  }
  currentPage.value = page                         // ← internal pager only
  localStorage.setItem('currentPage', page)
}

function handleNavigate(payload) {
  if (typeof payload === 'string') go(payload)
  else if (payload?.page) go(payload.page, payload)
}

window.addEventListener('chat-open-details', e => handleNavigate({ page: 'car', id: e.detail }))
window.addEventListener('chat-open-drive',   () => handleNavigate('driveBook'))
window.addEventListener('chat-open-techs',   () => handleNavigate('techs'))
window.addEventListener('chat-open-offers',  () => handleNavigate('specials'))
window.addEventListener('chat-open-advisor', () => handleNavigate('meetAdvisor'))
</script>
