// src/nav.js
import { computed, reactive } from 'vue'

// Import your "pages" as components (not as routes)
import AdminDashboard from '@/views/AdminDashboard.vue'
import AvailableCars from '@/views/AvailableCars.vue'
import DriveBook from '@/views/DriveBook.vue'
import MeetAdvisor from '@/views/MeetAdvisor.vue'
import MercedesTech from '@/views/MercedesTech.vue'
import SpecialOffers from '@/views/SpecialOffers.vue'
import UserDashboard from '@/views/UserDashboard.vue'

export const views = {
    userDashboard: UserDashboard,
    cars: AvailableCars,
    techs: MercedesTech,
    driveBook: DriveBook,
    meetAdvisor: MeetAdvisor,
    specials: SpecialOffers,
    adminDashboard: AdminDashboard
}

export const nav = reactive({
    current: 'userDashboard' // default screen
})

export const currentComponent = computed(() => views[nav.current] || UserDashboard)
