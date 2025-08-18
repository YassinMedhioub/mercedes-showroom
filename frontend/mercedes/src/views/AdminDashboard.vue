<template>
  <div class="min-h-screen bg-gradient-to-tr from-black via-gray-900 to-zinc-800 flex flex-col">
    <header class="flex items-center justify-between bg-black/80 p-5">
      <div class="flex items-center gap-3">
        <img src="../assets/mercedes-logo.png" class="w-10 h-10" />
        <div>
          <div class="text-lg text-white font-bold">Admin Dashboard</div>
          <div class="text-xs text-zinc-300 tracking-widest">Mercedes-Benz</div>
        </div>
      </div>
      <button @click="logout" class="px-4 py-2 rounded-lg bg-[#5d737e] text-white font-semibold hover:bg-zinc-700">Déconnexion</button>
    </header>
    <main class="flex-1 flex flex-col items-center justify-center">
      <h1 class="text-3xl text-white mb-4 font-bold">Bienvenue, Administrateur</h1>
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
import { useRouter } from 'vue-router';

/* Emit to shell for logout */
const emit = defineEmits(['logout', 'navigate'])
const router = useRouter()
function logout() {
  emit('logout')
}
</script>
