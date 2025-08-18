<template>
  <div class="min-h-[calc(100vh-180px)] bg-gradient-to-tr from-black via-gray-900 to-zinc-800 flex flex-col">
    <main class="flex-1 flex flex-col items-center justify-start px-6 pt-30 pb-12">
      <h1 class="text-3xl text-white mb-4 font-bold">
        Bienvenue, {{ displayName }}
      </h1>

      <p class="text-zinc-300 mb-8">Gérez les utilisateurs, les véhicules, et plus encore.</p>

      <div class="grid grid-cols-1 md:grid-cols-3 gap-6 w-full max-w-4xl">
        <button @click="$emit('navigate', 'ManageUsers')">
          <div class="rounded-xl bg-zinc-900/80 p-6 shadow-xl text-center text-white">
            <div class="text-4xl mb-2">👤</div>
            Gestion Utilisateurs
            <div class="text-xs text-zinc-400">Ajouter, modifier, supprimer...</div>
          </div>
        </button>

        <div class="rounded-xl bg-zinc-900/80 p-6 shadow-xl text-center text-white">
          <div class="text-4xl mb-2">🚗</div>
          <div class="font-semibold mb-1">Gestion Véhicules</div>
          <div class="text-xs text-zinc-400">Ajouter, stock, modèles...</div>
        </div>

        <div class="rounded-xl bg-zinc-900/80 p-6 shadow-xl text-center text-white">
          <div class="text-4xl mb-2">📊</div>
          <div class="font-semibold mb-1">Statistiques</div>
          <div class="text-xs text-zinc-400">Commandes, ventes, etc.</div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const emit = defineEmits(['logout', 'navigate'])
const router = useRouter()

/* Passe le nom depuis le parent : <AdminHome :adminName="user.fullName" /> */
const props = defineProps({
  adminName: { type: String, default: '' }
})
const displayName = computed(() => (props.adminName || '').trim() || 'Administrateur')
function logout () {
  emit('logout')
}
</script>
