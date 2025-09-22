
<template>
  <div :class="['min-h-svh bg-gradient-to-br', props.isDark ? 'from-[#0b0b0e] via-[#0f1115] to-[#1a1d22] text-zinc-100' : 'from-[#f6f7fb] via-[#f1f2f6] to-[#e7eaef] text-zinc-900']">
    <main class="mx-auto w-full px-4 sm:px-6 py-8 max-w-[90rem] pt-[var(--appbar)] sm:pt-[calc(var(--appbar)+0.5rem)]">
      
      <!-- HEADER -->
      <div class="flex items-center justify-between mb-6">
        <div>
          <h1 class="text-2xl sm:text-3xl font-extrabold">{{ t('title') }}</h1>
          <p class="text-sm opacity-70">{{ t('subtitle') }}</p>
        </div>

        <div class="flex gap-2">
          <button @click="router.push('/admin')" 
                  class="inline-flex items-center justify-center px-3 py-2 rounded-lg text-sm font-medium transition-colors"
                  :class="props.isDark ? 'bg-zinc-800 text-white hover:bg-zinc-700' : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
            ← {{ t('back') }}
          </button>
          <button @click="openCreate" 
                  class="px-3 py-2 rounded-lg text-sm font-semibold"
                  :class="props.isDark ? 'bg-[#5d737e] text-white hover:bg-zinc-700' : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'">
            ＋ {{ t('newCar') }}
          </button>
        </div>
      </div>

      <!-- TOOLBAR -->
      <div class="flex gap-2 mb-4">
        <input v-model="q" @input="onDebouncedSearch" :placeholder="t('searchPH')" 
               class="flex-1 px-3 py-2 rounded-lg text-sm"
               :class="props.isDark ? 'bg-zinc-900 border border-zinc-800' : 'bg-white border border-zinc-300'"/>
        <button @click="fetchCars" class="px-3 py-2 rounded-lg text-sm">{{ t('search') }}</button>
      </div>

      <!-- TABLE -->
      <section :class="['rounded-2xl shadow-xl overflow-x-auto', props.isDark ? 'bg-zinc-900 text-white' : 'bg-white border border-zinc-200']">
        <div class="grid grid-cols-11 gap-2 px-4 py-3 font-medium text-sm" :class="props.isDark ? 'bg-zinc-800 text-white' : 'bg-zinc-100 text-zinc-900'">
          <div>{{ t('model') }}</div>
          <div>{{ t('type') }}</div>
          <div>{{ t('price') }}</div>
          <div>{{ t('max_speed') }}</div>
          <div>{{ t('torque') }}</div>
          <div>{{ t('km') }}</div>
          <div>{{ t('horsepower') }}</div>
          <div>{{ t('fuel_type') }}</div>
          <div>{{ t('transmission') }}</div>
          <div class="col-span-2 text-right">{{ t('actions') }}</div>
        </div>

        <div v-for="c in cars" :key="c.id" class="grid grid-cols-11 gap-2 px-4 py-3 border-t"
             :class="props.isDark ? 'border-zinc-800' : 'border-zinc-200'">
          <div>{{ c.model }}</div>
          <div>{{ c.type }}</div>
          <div>{{ c.price }} TND</div>
          <div>{{ c.maxSpeed }} km/h</div>
          <div>{{ c.torque }}</div>
          <div>{{ c.km }}</div>
          <div>{{ c.horsepower }} ch</div>
          <div>{{ c.fuelType }}</div>
          <div>{{ c.transmission }}</div>
          <div class="col-span-2 flex gap-2 justify-end">
            <button @click="openEdit(c)" class="px-2 py-1 rounded border text-xs">✏️</button>
            <button @click="confirmDelete(c)" class="px-2 py-1 rounded border text-xs text-red-500">🗑</button>
          </div>
        </div>
      </section>
    </main>

    <!-- FORM MODAL -->
    <transition name="fade">
      <div v-if="showForm" class="fixed inset-0 bg-black/50 grid place-items-center">
        <div class="w-full max-w-2xl rounded-2xl shadow-2xl p-6" :class="props.isDark ? 'bg-zinc-900 text-white' : 'bg-white text-zinc-800'">
          <h2 class="text-lg font-semibold mb-3">{{ form.id ? t('editCar') : t('createCar') }}</h2>
          <form @submit.prevent="saveCar" class="grid grid-cols-2 gap-3">
            <input v-model="form.model" :placeholder="t('model')" required 
                   :class="props.isDark ? 'bg-zinc-800 border-zinc-600' : 'bg-white border-zinc-300'"/>
            <input v-model="form.type" :placeholder="t('type')" required 
                   :class="props.isDark ? 'bg-zinc-800 border-zinc-600' : 'bg-white border-zinc-300'"/>
            <input v-model.number="form.price" type="number" :placeholder="t('price')" 
                   :class="props.isDark ? 'bg-zinc-800 border-zinc-600' : 'bg-white border-zinc-300'"/>
            <input v-model.number="form.maxSpeed" type="number" :placeholder="t('max_speed')" 
                   :class="props.isDark ? 'bg-zinc-800 border-zinc-600' : 'bg-white border-zinc-300'"/>
            <input v-model="form.torque" :placeholder="t('torque')" 
                   :class="props.isDark ? 'bg-zinc-800 border-zinc-600' : 'bg-white border-zinc-300'"/>
            <input v-model.number="form.km" type="number" :placeholder="t('km')" 
                   :class="props.isDark ? 'bg-zinc-800 border-zinc-600' : 'bg-white border-zinc-300'"/>
            <input v-model.number="form.horsepower" type="number" :placeholder="t('horsepower')" 
                   :class="props.isDark ? 'bg-zinc-800 border-zinc-600' : 'bg-white border-zinc-300'"/>
            <input v-model="form.fuelType" :placeholder="t('fuel_type')" 
                   :class="props.isDark ? 'bg-zinc-800 border-zinc-600' : 'bg-white border-zinc-300'"/>
            <input v-model="form.transmission" :placeholder="t('transmission')" 
                   :class="props.isDark ? 'bg-zinc-800 border-zinc-600' : 'bg-white border-zinc-300'"/>
            
            <div class="col-span-2 flex justify-end gap-2 pt-3">
              <button type="button" @click="closeForm" class="px-3 py-2 border rounded-lg">{{ t('cancel') }}</button>
              <button type="submit" :disabled="saving" 
                      class="px-3 py-2 rounded-lg font-semibold bg-emerald-600 text-white disabled:opacity-50">
                {{ saving ? t('saving') : (form.id ? t('save') : t('create')) }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>

    <!-- DELETE CONFIRM MODAL -->
    <transition name="fade">
      <div v-if="toDelete" class="fixed inset-0 bg-black/50 grid place-items-center">
        <div class="w-full max-w-md rounded-2xl shadow-2xl p-6" :class="props.isDark ? 'bg-zinc-900 text-white' : 'bg-white text-zinc-800'">
          <h2 class="text-lg font-semibold mb-3">{{ t('confirmTitle') }}</h2>
          <p class="text-sm mb-4">{{ t('confirmText') }} <b>{{ toDelete.model }}</b> ?</p>
          <div class="flex justify-end gap-2">
            <button @click="toDelete=null" class="px-3 py-2 border rounded-lg">{{ t('cancel') }}</button>
            <button @click="removeCar" :disabled="deleting" 
                    class="px-3 py-2 rounded-lg font-semibold bg-red-600 text-white disabled:opacity-50">
              {{ deleting ? t('deleting') : t('delete') }}
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from '../composables/useI18n'
import { createCar, deleteCar, fetchCars as fetchCarsApi, updateCar } from '../services/api'

const emit = defineEmits(['navigate'])

/* Props */
const props = defineProps({
  isDark: Boolean,
  language: { type: String, default: 'FR' }
})

/* Services */
const router = useRouter()
const { t } = useI18n()

/* STATE */
const q = ref('')
const cars = ref([])
const showForm = ref(false)
const saving = ref(false)
const deleting = ref(false)
const form = ref({ 
  id: null, 
  model: '', 
  type: '', 
  price: null, 
  maxSpeed: null, 
  torque: '', 
  km: null, 
  horsepower: null, 
  fuelType: '', 
  transmission: '' 
})
const toDelete = ref(null)

/* CRUD Operations */
async function fetchCars() {
  try {
    cars.value = await fetchCarsApi({ search: q.value || undefined })
  } catch (error) {
    console.error('Failed to fetch cars:', error)
  }
}

function openCreate() {
  form.value = { 
    id: null, 
    model: '', 
    type: '', 
    price: null, 
    maxSpeed: null, 
    torque: '', 
    km: null, 
    horsepower: null, 
    fuelType: '', 
    transmission: '' 
  }
  showForm.value = true
}

function openEdit(car) { 
  form.value = { ...car } 
  showForm.value = true 
}

function closeForm() { 
  showForm.value = false 
  form.value = { 
    id: null, 
    model: '', 
    type: '', 
    price: null, 
    maxSpeed: null, 
    torque: '', 
    km: null, 
    horsepower: null, 
    fuelType: '', 
    transmission: '' 
  }
}

async function saveCar() {
  if (!form.value.model || !form.value.type) {
    alert(t('formError'))
    return
  }
  
  saving.value = true
  try {
    if (form.value.id) {
      await updateCar(form.value.id, form.value)
    } else {
      await createCar(form.value)
    }
    showForm.value = false
    await fetchCars()
  } catch (error) {
    console.error('Failed to save car:', error)
    alert(t('saveError'))
  } finally {
    saving.value = false
  }
}

function confirmDelete(car) { 
  toDelete.value = car 
}

async function removeCar() {
  if (!toDelete.value) return
  
  deleting.value = true
  try {
    await deleteCar(toDelete.value.id)
    toDelete.value = null
    await fetchCars()
  } catch (error) {
    console.error('Failed to delete car:', error)
    alert(t('deleteError'))
  } finally {
    deleting.value = false
  }
}

/* DEBOUNCED SEARCH */
let timer = null
function onDebouncedSearch() {
  clearTimeout(timer)
  timer = setTimeout(fetchCars, 350)
}

onMounted(fetchCars)
</script>

<style>
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>