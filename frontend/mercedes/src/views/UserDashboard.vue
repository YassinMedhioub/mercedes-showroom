<template>
  <div class="min-h-screen bg-gradient-to-br from-zinc-900 via-black to-gray-800 flex flex-col">
    <header class="flex items-center justify-between bg-black/80 p-5">
      <div class="flex items-center gap-3">
        <img src="../assets/mercedes-logo-simple.png" class="w-10 h-10" />
        <div>
          <div class="text-lg text-white font-bold">Mon espace</div>
          <div class="text-xs text-zinc-300 tracking-widest">Mercedes-Benz</div>
        </div>
      </div>
      <button @click="logout" class="px-4 py-2 rounded-lg bg-[#5d737e] text-white font-semibold hover:bg-zinc-700">Déconnexion</button>
    </header>
    <main class="flex-1 flex flex-col items-center justify-center">
      <h1 class="text-3xl text-white mb-4 font-bold">Bienvenue, Cher Client</h1>
      <p class="text-zinc-300 mb-8">Retrouvez ici vos informations et services personnalisés.</p>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-8 w-full max-w-3xl">
        <div class="rounded-xl bg-zinc-900/80 p-6 shadow-xl text-center text-white">
          <div class="text-4xl mb-2">🚘</div>
          <div class="font-semibold mb-1">Mes véhicules</div>
          <div class="text-xs text-zinc-400">Voir historique et entretien</div>
        </div>
        <div class="rounded-xl bg-zinc-900/80 p-6 shadow-xl text-center text-white">
          <div class="text-4xl mb-2">🛠️</div>
          <div class="font-semibold mb-1">Prendre un rendez-vous</div>
          <div class="text-xs text-zinc-400">Service, maintenance, etc.</div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter()
const logout = () => router.push('/')
</script>
