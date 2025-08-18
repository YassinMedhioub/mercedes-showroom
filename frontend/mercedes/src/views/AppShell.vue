<template>
  <!-- Render only after we decide the landing page to avoid flicker -->
   <HeaderBar
    v-if="ready && currentPage !== 'login'"
    :is-admin="isAdminView"
    @logout="handleLogout"
    />
  <component
    v-if="ready"
    :is="pageComponents[currentPage]"
    :key="currentPage"               
    :car-id="selectedCarId"
    :adminName="currentUser.fullName"
    @login="handleLogin"
    @logout="handleLogout"
    @navigate="handleNavigate"
    :isDark="isDark"
    :language="lang"
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
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import ChatWidget from '../components/ChatWidget.vue'
import FooterBar from '../components/FooterBar.vue'
import HeaderBar from '../components/HeaderBar.vue'
import { usePrefs } from '../components/usePrefs'

/* Pages */
import AdminDashboard from './AdminDashboard.vue'
import AvailableCars from './AvailableCars.vue'
import CarDetails from './CarDetails.vue'
import DriveBook from './DriveBook.vue'
import Login from './Login.vue'
import ManageUsers from './ManageUsers.vue'
import MeetAdvisor from './MeetAdvisor.vue'
import MercedesTechs from './MercedesTech.vue'
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
  techs: MercedesTechs,
  driveBook : DriveBook,
}

/* State */
const isLoggedIn = ref(false)
const currentPage = ref('login')     // start at login
const selectedCarId = ref(null)
const ready = ref(false)
const { lang, isDark } = usePrefs()
const ADMIN_PAGES = new Set(['adminDashboard', 'ManageUsers'])
const isAdminView = computed(() => ADMIN_PAGES.has(currentPage.value))

const language = lang 
const isDarkMode = isDark  

const carsArray = [
  { id:1, model:"Classe S 2012",            family:"Classe S", type:"Berline de Luxe", price:"110 900",  aliases:["classe s","class s","s-class","w221","mercedes s"] },
  { id:2, model:"Classe A 200 d AMG Line",  family:"Classe A", type:"Citadine Compacte", price:"159 200",  aliases:["classe a","class a","a-class","a 200","a200"] },
  { id:3, model:"Mercedes Benz GLE S 63 4MATIC Coupé", family:"GLE Coupé", type:"SUV", price:"240 500", aliases:["gle","gle coupe","gle coupé","gle s63"] },
  { id:4, model:"Mercedes AMG GT",          family:"AMG GT",  type:"Sports Car", price:"170 000", aliases:["amg gt","gt amg"] },
  { id:5, model:"Classe C 200 Avantgarde",  family:"Classe C", type:"Break", price:"110 000",  aliases:["classe c","class c","c-class","c200"] },
  { id:6, model:"GLA 200 D",                family:"GLA",      type:"SUV Coupé", price:"92 400", aliases:["gla","gla 200","gla200"] },
];

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

function handleLogin({ role, fullName } = {}) {
  localStorage.setItem('isLoggedIn', 'true')
  if (role) localStorage.setItem('role', role)
  if (fullName) {
    localStorage.setItem('adminName', fullName)
    currentUser.value.fullName = fullName
  }
  const landing = role === 'ADMIN' ? 'adminDashboard' : 'dashboard'
  currentPage.value = landing
  localStorage.setItem('currentPage', landing)
}

function handleLogout() {
  localStorage.removeItem('isLoggedIn')
  localStorage.removeItem('role')
  localStorage.removeItem('currentPage')
  localStorage.removeItem('selectedCarId')
  localStorage.removeItem('adminName')                // ✅ clear
  selectedCarId.value = null
  currentUser.value.fullName = 'Administrateur'       // ✅ reset
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

const currentUser = ref({
  fullName: localStorage.getItem('adminName') || 'Administrateur'
})


window.addEventListener('chat-open-details', e => handleNavigate({ page: 'car', id: e.detail }))
window.addEventListener('chat-open-drive',   () => handleNavigate('driveBook'))
window.addEventListener('chat-open-techs',   () => handleNavigate('techs'))
window.addEventListener('chat-open-offers',  () => handleNavigate('specials'))
window.addEventListener('chat-open-advisor', () => handleNavigate('meetAdvisor'))
</script>
