<template>
  <div class="w-full max-w-6xl mx-auto px-4 sm:px-6 py-8">
    <!-- Header row -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 mb-6">
      <div>
        <h1 :class="[
              'font-extrabold leading-tight',
              isDark ? 'text-white' : 'text-zinc-900',
              'text-2xl sm:text-3xl'
            ]">
          {{ t('title') }}
        </h1>
        <p class="mt-1 text-sm" :class="isDark ? 'text-zinc-300' : 'text-zinc-600'">{{ t('subtitle') }}</p>
      </div>

      <div class="flex items-center gap-2">
        <button
          @click="emit('navigate','adminDashboard')"
          class="inline-flex items-center justify-center px-3 py-2 rounded-lg text-sm font-medium transition-colors"
          :class="isDark
            ? 'bg-zinc-800 text-white hover:bg-zinc-700'
            : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
          ← {{ t('back') }}
        </button>

        <button
          @click="openCreate"
          class="inline-flex items-center gap-2 px-3 py-2 rounded-lg text-sm font-semibold transition-colors"
          :class="isDark
            ? 'bg-[#5d737e] text-white hover:bg-zinc-700'
            : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'">
          <span class="text-lg">＋</span> {{ t('newUser') }}
        </button>
      </div>
    </div>

    <!-- Toolbar -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 mb-4">
      <div class="flex-1 flex items-center gap-2">
        <div class="relative flex-1">
          <span class="absolute left-3 top-1/2 -translate-y-1/2 opacity-70">🔎</span>
          <input
            v-model="q"
            @input="onDebouncedSearch"
            :placeholder="t('searchPH')"
            class="w-full pl-9 pr-3 py-2 rounded-lg text-sm focus:outline-none"
            :class="isDark
              ? 'bg-zinc-900 text-white placeholder:text-zinc-400 border border-zinc-800'
              : 'bg-white border border-zinc-300 text-zinc-800'"
          />
        </div>

        <button @click="fetchUsers(1)"
          class="px-3 py-2 rounded-lg text-sm"
          :class="isDark ? 'bg-zinc-800 text-white hover:bg-zinc-700' : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
          {{ t('search') }}
        </button>

        <button @click="resetFilters"
          class="px-3 py-2 rounded-lg text-sm"
          :class="isDark ? 'bg-zinc-800 text-white hover:bg-zinc-700' : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
          {{ t('reset') }}
        </button>
      </div>

      <div class="flex items-center gap-2">
        <label class="text-xs sm:text-sm" :class="isDark ? 'text-zinc-300' : 'text-zinc-600'">{{ t('pageSize') }}</label>
        <select v-model.number="size" @change="fetchUsers(1)"
          class="px-2 py-1 rounded-lg text-sm"
          :class="isDark ? 'bg-zinc-900 text-white border border-zinc-800' : 'bg-white border border-zinc-300 text-zinc-800'">
          <option :value="5">5</option>
          <option :value="10">10</option>
          <option :value="20">20</option>
        </select>

        <button @click="fetchUsers(page)"
          class="px-3 py-2 rounded-lg text-sm"
          :class="isDark ? 'bg-zinc-800 text-white hover:bg-zinc-700' : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
          ⟳ {{ t('refresh') }}
        </button>
      </div>
    </div>

    <!-- Card -->
    <section
      :class="[
        'rounded-2xl shadow-xl overflow-hidden',
        isDark ? 'bg-zinc-900/90 text-white' : 'bg-white text-zinc-800 border border-zinc-200'
      ]"
    >
      <!-- Table header -->
      <div :class="isDark ? 'bg-zinc-900/50' : 'bg-zinc-50'">
        <div class="grid grid-cols-12 gap-2 px-4 py-3 text-sm font-medium">
          <div class="col-span-3">{{ t('user') }}</div>
          <div class="col-span-3">{{ t('email') }}</div>
          <div class="col-span-2">{{ t('role') }}</div>
          <div class="col-span-2">{{ t('status') }}</div>
          <div class="col-span-2 text-right">{{ t('created') }}</div>
        </div>
      </div>

      <!-- Rows -->
      <div>
        <!-- Loading skeleton -->
        <div v-if="loading" class="divide-y" :class="isDark ? 'divide-zinc-800' : 'divide-zinc-200'">
          <div v-for="i in 5" :key="i" class="px-4 py-3 grid grid-cols-12 gap-2">
            <div class="col-span-3 h-5 rounded animate-pulse" :class="isDark?'bg-zinc-800':'bg-zinc-200'"></div>
            <div class="col-span-3 h-5 rounded animate-pulse" :class="isDark?'bg-zinc-800':'bg-zinc-200'"></div>
            <div class="col-span-2 h-5 rounded animate-pulse" :class="isDark?'bg-zinc-800':'bg-zinc-200'"></div>
            <div class="col-span-2 h-5 rounded animate-pulse" :class="isDark?'bg-zinc-800':'bg-zinc-200'"></div>
            <div class="col-span-2 h-5 rounded animate-pulse ml-auto w-24" :class="isDark?'bg-zinc-800':'bg-zinc-200'"></div>
          </div>
        </div>

        <!-- Empty state -->
        <div v-else-if="users.length === 0" class="px-6 py-10 text-center">
          <div class="text-4xl mb-2">⭐</div>
          <div class="font-semibold mb-1">{{ t('empty') }}</div>
          <div class="text-sm opacity-70">{{ t('emptyHint') }}</div>
          <button @click="openCreate"
            class="mt-4 px-4 py-2 rounded-lg text-sm font-semibold transition-colors"
            :class="isDark ? 'bg-[#5d737e] text-white hover:bg-zinc-700' : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'">
            ＋ {{ t('newUser') }}
          </button>
        </div>

        <!-- Data rows -->
        <div v-else class="divide-y" :class="isDark ? 'divide-zinc-800' : 'divide-zinc-200'">
          <div
            v-for="u in users"
            :key="u.id"
            class="px-4 py-3 grid grid-cols-12 gap-2 items-center hover:brightness-110 transition"
          >
            <!-- user -->
            <div class="col-span-3 flex items-center gap-3 min-w-0">
              <div class="w-9 h-9 rounded-full grid place-items-center font-bold shrink-0"
                   :class="isDark ? 'bg-zinc-800 text-white' : 'bg-zinc-200 text-zinc-800'">
                {{ initials(u.username || u.name) }}
              </div>
              <div class="min-w-0">
                <div class="font-medium truncate">{{ u.username || u.name }}</div>
                <div class="text-xs opacity-70 truncate">#{{ u.id }}</div>
              </div>
            </div>

            <!-- email -->
            <div class="col-span-3 truncate">{{ u.email }}</div>

            <!-- role -->
            <div class="col-span-2">
              <span class="inline-flex items-center px-2 py-1 rounded-full text-xs border"
                    :class="u.role==='ADMIN'
                      ? (isDark ? 'bg-purple-900/30 text-purple-200 border-purple-700'
                                : 'bg-purple-100 text-purple-800 border-purple-300')
                      : (isDark ? 'bg-zinc-800 text-zinc-200 border-zinc-700'
                                : 'bg-zinc-100 text-zinc-800 border-zinc-300')">
                {{ u.role }}
              </span>
            </div>

            <!-- active -->
            <div class="col-span-2">
              <button
                @click="toggleActive(u)"
                class="relative inline-flex h-6 w-11 items-center rounded-full transition"
                :class="u.active
                  ? (isDark ? 'bg-emerald-600' : 'bg-emerald-500')
                  : (isDark ? 'bg-zinc-700' : 'bg-zinc-300')">
                <span class="sr-only">{{ t('status') }}</span>
                <span
                  class="inline-block h-5 w-5 transform rounded-full bg-white transition"
                  :class="u.active ? 'translate-x-5' : 'translate-x-1'"></span>
              </button>
            </div>

            <!-- created + actions -->
            <div class="col-span-2 flex items-center justify-end gap-2">
              <span class="text-xs opacity-70 mr-1">{{ formatDate(u.createdAt || u.created_at) }}</span>

              <button @click="openEdit(u)"
                class="px-2 py-1 rounded border text-xs"
                :class="isDark ? 'border-zinc-700 hover:bg-zinc-800' : 'border-zinc-300 hover:bg-zinc-100'">
                ✏️ {{ t('edit') }}
              </button>

              <button @click="resetPassword(u)"
                class="px-2 py-1 rounded border text-xs"
                :class="isDark ? 'border-zinc-700 hover:bg-zinc-800' : 'border-zinc-300 hover:bg-zinc-100'">
                🔒 {{ t('resetPwd') }}
              </button>

              <button @click="confirmDelete(u)"
                class="px-2 py-1 rounded border text-xs"
                :class="isDark ? 'border-red-800 bg-red-900/30 text-red-200 hover:bg-red-900/50' : 'border-red-300 bg-red-100 text-red-700 hover:bg-red-200'">
                🗑 {{ t('delete') }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Footer / pagination -->
      <div class="flex items-center justify-between px-4 py-3 border-t"
           :class="isDark ? 'border-zinc-800' : 'border-zinc-200'">
        <div class="text-xs" :class="isDark ? 'text-zinc-300' : 'text-zinc-600'">
          {{ t('showing') }} {{ from }}–{{ to }} {{ t('of') }} {{ total }}
        </div>
        <div class="flex items-center gap-2">
          <button :disabled="page<=1" @click="fetchUsers(page-1)"
                  class="px-3 py-1.5 rounded text-sm disabled:opacity-40"
                  :class="isDark ? 'bg-zinc-800 text-white hover:bg-zinc-700' : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
            ‹
          </button>
          <span class="text-sm" :class="isDark ? 'text-zinc-200' : 'text-zinc-800'">{{ page }}</span>
          <button :disabled="to>=total" @click="fetchUsers(page+1)"
                  class="px-3 py-1.5 rounded text-sm disabled:opacity-40"
                  :class="isDark ? 'bg-zinc-800 text-white hover:bg-zinc-700' : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
            ›
          </button>
        </div>
      </div>
    </section>

    <!-- Form modal -->
    <transition name="fade">
      <div v-if="showForm" class="fixed inset-0 z-[1000] grid place-items-center bg-black/50 p-4">
        <div class="w-full max-w-lg rounded-2xl shadow-2xl p-5 sm:p-6"
             :class="isDark ? 'bg-zinc-900/95 text-white border border-zinc-800' : 'bg-white text-zinc-800'">
          <div class="flex items-center justify-between mb-3">
            <div class="text-lg font-semibold">{{ form.id ? t('editUser') : t('createUser') }}</div>
            <button @click="closeForm" class="text-sm opacity-70 hover:opacity-100">✕</button>
          </div>

          <form @submit.prevent="saveUser" class="space-y-3">
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
              <div>
                <label class="block text-xs mb-1 opacity-70">{{ t('username') }}</label>
                <input v-model.trim="form.username" required
                       class="w-full px-3 py-2 rounded-lg text-sm focus:outline-none"
                       :class="isDark ? 'bg-zinc-800 border border-zinc-700' : 'bg-white border border-zinc-300'"/>
              </div>
              <div>
                <label class="block text-xs mb-1 opacity-70">{{ t('email') }}</label>
                <input v-model.trim="form.email" type="email" required
                       class="w-full px-3 py-2 rounded-lg text-sm focus:outline-none"
                       :class="isDark ? 'bg-zinc-800 border border-zinc-700' : 'bg-white border border-zinc-300'"/>
              </div>

              <div>
                <label class="block text-xs mb-1 opacity-70">{{ t('role') }}</label>
                <select v-model="form.role" class="w-full px-3 py-2 rounded-lg text-sm"
                        :class="isDark ? 'bg-zinc-800 border border-zinc-700' : 'bg-white border border-zinc-300'">
                  <option value="USER">USER</option>
                  <option value="ADMIN">ADMIN</option>
                </select>
              </div>

              <label class="flex items-center gap-2 text-sm mt-6 sm:mt-0">
                <input type="checkbox" v-model="form.active" />
                <span>{{ t('active') }}</span>
              </label>

              <div v-if="!form.id" class="sm:col-span-2">
                <label class="block text-xs mb-1 opacity-70">{{ t('password') }}</label>
                <input v-model.trim="form.password" :placeholder="t('passwordPH')" required
                       class="w-full px-3 py-2 rounded-lg text-sm focus:outline-none"
                       :class="isDark ? 'bg-zinc-800 border border-zinc-700' : 'bg-white border border-zinc-300'"/>
              </div>
            </div>

            <div class="flex items-center justify-end gap-2 pt-2">
              <button type="button" @click="closeForm"
                      class="px-3 py-2 rounded-lg text-sm"
                      :class="isDark ? 'bg-zinc-800 text-white hover:bg-zinc-700' : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
                {{ t('cancel') }}
              </button>
              <button type="submit" :disabled="saving"
                      class="px-3 py-2 rounded-lg text-sm font-semibold disabled:opacity-60"
                      :class="isDark ? 'bg-[#5d737e] text-white hover:bg-zinc-700' : 'bg-[#e6eaf1] text-zinc-700 hover:bg-zinc-300 border border-zinc-200'">
                {{ form.id ? t('save') : t('create') }}
              </button>
            </div>

            <p v-if="formErr" class="text-red-400 text-sm mt-1">{{ formErr }}</p>
          </form>
        </div>
      </div>
    </transition>

    <!-- Delete confirm -->
    <transition name="fade">
      <div v-if="toDelete" class="fixed inset-0 z-[1000] grid place-items-center bg-black/50 p-4">
        <div class="w-full max-w-md rounded-2xl shadow-2xl p-5"
             :class="isDark ? 'bg-zinc-900/95 text-white border border-zinc-800' : 'bg-white text-zinc-800'">
          <div class="font-semibold mb-2">{{ t('confirmTitle') }}</div>
          <p class="text-sm opacity-80 mb-4">
            {{ t('confirmText') }} <b>{{ toDelete.username || toDelete.name }}</b>?
          </p>
          <div class="flex items-center justify-end gap-2">
            <button @click="toDelete=null"
                    class="px-3 py-2 rounded-lg text-sm"
                    :class="isDark ? 'bg-zinc-800 text-white hover:bg-zinc-700' : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'">
              {{ t('cancel') }}
            </button>
            <button @click="removeUser" :disabled="saving"
                    class="px-3 py-2 rounded-lg text-sm font-semibold disabled:opacity-60"
                    :class="isDark ? 'bg-red-900/40 text-red-200 border border-red-800' : 'bg-red-100 text-red-700 border border-red-300'">
              {{ t('delete') }}
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import axios from 'axios'
import { computed, onMounted, ref } from 'vue'

const emit = defineEmits(['navigate'])

/* Theme & language (kept consistent with your other pages) */
const isDark = ref(localStorage.getItem('darkMode') === 'true')
const language = ref(localStorage.getItem('lang') || 'FR')

/* i18n */
const i18n = {
  FR: {
    title: 'Gestion Utilisateurs',
    subtitle: 'Ajoutez, modifiez, désactivez des comptes.',
    back: 'Retour',
    newUser: 'Nouvel utilisateur',
    searchPH: 'Rechercher (nom, email)…',
    search: 'Rechercher',
    reset: 'Réinitialiser',
    pageSize: 'Par page',
    refresh: 'Actualiser',
    user: 'Utilisateur',
    email: 'Email',
    role: 'Rôle',
    status: 'Statut',
    created: 'Créé le',
    showing: 'Affichage',
    of: 'sur',
    loading: 'Chargement…',
    empty: 'Aucun utilisateur',
    emptyHint: 'Ajoutez votre premier utilisateur pour commencer.',
    active: 'Actif',
    inactive: 'Inactif',
    edit: 'Modifier',
    resetPwd: 'Réinit. MDP',
    delete: 'Supprimer',
    editUser: 'Modifier l’utilisateur',
    createUser: 'Créer un utilisateur',
    username: "Nom d'utilisateur",
    password: 'Mot de passe',
    passwordPH: 'Choisir un mot de passe',
    cancel: 'Annuler',
    save: 'Enregistrer',
    create: 'Créer',
    confirmTitle: 'Confirmation',
    confirmText: 'Supprimer définitivement',
  },
  EN: {
    title: 'User Management',
    subtitle: 'Create, edit and disable accounts.',
    back: 'Back',
    newUser: 'New user',
    searchPH: 'Search (name, email)…',
    search: 'Search',
    reset: 'Reset',
    pageSize: 'Per page',
    refresh: 'Refresh',
    user: 'User',
    email: 'Email',
    role: 'Role',
    status: 'Status',
    created: 'Created',
    showing: 'Showing',
    of: 'of',
    loading: 'Loading…',
    empty: 'No users',
    emptyHint: 'Add your first user to get started.',
    active: 'Active',
    inactive: 'Inactive',
    edit: 'Edit',
    resetPwd: 'Reset PW',
    delete: 'Delete',
    editUser: 'Edit user',
    createUser: 'Create user',
    username: 'Username',
    password: 'Password',
    passwordPH: 'Choose a password',
    cancel: 'Cancel',
    save: 'Save',
    create: 'Create',
    confirmTitle: 'Confirmation',
    confirmText: 'Permanently delete',
  }
}
const t = (k) => i18n[language.value]?.[k] ?? i18n.FR[k]

/* API base */
const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080'

/* list state */
const q = ref('')
const page = ref(1)
const size = ref(10)
const total = ref(0)
const users = ref([])
const loading = ref(false)

/* computed */
const from = computed(() => users.value.length ? (page.value - 1) * size.value + 1 : 0)
const to   = computed(() => Math.min(page.value * size.value, total.value))

/* form state */
const showForm = ref(false)
const form = ref({ id: null, username: '', email: '', role: 'USER', active: true, password: '' })
const formErr = ref('')
const saving = ref(false)
const toDelete = ref(null)

/* helpers */
function initials(name = '') {
  const parts = name.trim().split(/\s+/).slice(0, 2)
  return parts.map(s => s[0]?.toUpperCase() ?? '').join('') || 'U'
}
function formatDate(d) {
  if (!d) return '—'
  const dd = new Date(d)
  return dd.toLocaleDateString(language.value === 'FR' ? 'fr-FR' : 'en-GB', {
    year: 'numeric', month: 'short', day: '2-digit'
  })
}

/* CRUD */
async function fetchUsers(target = page.value) {
  try {
    loading.value = true
    page.value = target
    const { data } = await axios.get(`${API_BASE}/api/users`, {
      params: { q: q.value || undefined, page: page.value, size: size.value },
      headers: authHeader()
    })
    // Expect: { items: [], total: N } OR a plain array fallback
    users.value = Array.isArray(data?.items) ? data.items : (Array.isArray(data) ? data : [])
    total.value = Number.isFinite(data?.total) ? data.total : users.value.length
  } catch (e) {
    console.error('fetchUsers error', e)
    users.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

function resetFilters() {
  q.value = ''
  fetchUsers(1)
}

/* create/edit */
function openCreate() {
  form.value = { id: null, username: '', email: '', role: 'USER', active: true, password: '' }
  formErr.value = ''
  showForm.value = true
}
function openEdit(u) {
  form.value = { id: u.id, username: u.username || u.name || '', email: u.email || '', role: u.role || 'USER', active: !!u.active, password: '' }
  formErr.value = ''
  showForm.value = true
}
function closeForm() {
  showForm.value = false
  formErr.value = ''
}

async function saveUser() {
  try {
    saving.value = true
    formErr.value = ''
    const body = { username: form.value.username, email: form.value.email, role: form.value.role, active: form.value.active }
    if (form.value.id) {
      await axios.put(`${API_BASE}/api/users/${form.value.id}`, body, { headers: authHeader(true) })
    } else {
      await axios.post(`${API_BASE}/api/users`, { ...body, password: form.value.password }, { headers: authHeader(true) })
    }
    showForm.value = false
    await fetchUsers(page.value)
  } catch (e) {
    console.error('saveUser error', e)
    formErr.value = language.value === 'FR' ? "Échec de l’enregistrement." : 'Failed to save.'
  } finally {
    saving.value = false
  }
}

async function toggleActive(u) {
  try {
    await axios.patch(`${API_BASE}/api/users/${u.id}/toggle`, {}, { headers: authHeader(true) })
    u.active = !u.active
  } catch (e) {
    console.error('toggleActive error', e)
    fetchUsers(page.value)
  }
}

async function resetPassword(u) {
  try {
    await axios.post(`${API_BASE}/api/users/${u.id}/reset-password`, {}, { headers: authHeader(true) })
    alert(language.value === 'FR' ? 'Mot de passe réinitialisé (si email configuré).' : 'Password reset (if email configured).')
  } catch (e) {
    console.error('resetPassword error', e)
    alert(language.value === 'FR' ? 'Échec de la réinitialisation.' : 'Reset failed.')
  }
}

function confirmDelete(u) { toDelete.value = u }
async function removeUser() {
  if (!toDelete.value) return
  try {
    saving.value = true
    await axios.delete(`${API_BASE}/api/users/${toDelete.value.id}`, { headers: authHeader(true) })
    toDelete.value = null
    await fetchUsers(page.value)
  } catch (e) {
    console.error('removeUser error', e)
    alert(language.value === 'FR' ? 'Suppression impossible.' : 'Delete failed.')
  } finally {
    saving.value = false
  }
}

/* utils */
function authHeader(json=false) {
  const h = {}
  const tok = localStorage.getItem('authToken')
  if (tok) h.Authorization = `Bearer ${tok}`
  if (json) h['Content-Type'] = 'application/json'
  return h
}

let searchTimer = null
function onDebouncedSearch() {
  clearTimeout(searchTimer)
  searchTimer = setTimeout(() => fetchUsers(1), 350)
}

/* init */
onMounted(() => fetchUsers(1))
</script>

<style scoped>
.fade-enter-active, .fade-leave-active { transition: opacity .15s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>
