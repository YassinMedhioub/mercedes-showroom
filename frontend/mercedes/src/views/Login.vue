<template>
  <div :class="['flex flex-col min-h-screen', isDark ? 'dark' : '']">
    <!-- HEADER -->
    <header
      class="flex flex-wrap items-center justify-between w-full px-4 md:px-8 py-2 bg-black/80 backdrop-blur sticky top-0 z-50 shadow-md gap-y-2"
    >
      <div class="flex items-center gap-2 md:gap-4 min-w-0">
        <img src="../assets/mercedes-logo.png" alt="Mercedes-Benz Logo"
          class="w-10 h-10 sm:w-12 sm:h-12 object-contain shrink-0"
        />
        <div class="w-[2px] h-8 sm:h-10 bg-zinc-700 mx-2 sm:mx-3 shrink-0"></div>
        <div class="flex flex-col min-w-0">
          <span class="text-base sm:text-lg font-bold text-white tracking-wide truncate">Star Silver Sfax</span>
          <span class="text-[10px] sm:text-xs text-zinc-300 tracking-widest truncate">Mercedes-Benz</span>
        </div>
      </div>
      <div class="flex items-center gap-2 sm:gap-4 w-auto">
        <!-- Language selector with custom arrow -->
        <div class="relative">
          <select
            v-model="language"
            class="appearance-none bg-black/40 border border-zinc-700 rounded-lg text-xs sm:text-sm px-2 py-1 text-white focus:outline-none pr-6"
          >
            <option value="EN">EN</option>
            <option value="FR">FR</option>
          </select>
          <span class="pointer-events-none absolute right-2 top-1/2 -translate-y-1/2 text-zinc-300">
            <!-- Chevron Down SVG -->
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/>
            </svg>
          </span>
        </div>
        <!-- Dark/Light toggle -->
        <button @click="toggleDark" :aria-label="isDark ? 'Light mode' : 'Dark mode'"
          class="ml-1 sm:ml-2 bg-zinc-800 hover:bg-zinc-600 p-2 rounded-lg text-white transition"
        >
          <svg v-if="!isDark" class="w-4 h-4 sm:w-5 sm:h-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <!-- moon icon -->
            <path stroke-linecap="round" stroke-linejoin="round"
                  d="M21 12.79A9 9 0 1111.21 3a7 7 0 009.79 9.79z"/>
          </svg>
          <svg v-else class="w-4 h-4 sm:w-5 sm:h-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <!-- sun icon -->
            <circle cx="12" cy="12" r="5" />
            <path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"/>
          </svg>
        </button>
      </div>


    </header>

    <!-- MAIN -->
    <main class="flex-1 flex flex-col items-center justify-center px-2 sm:px-0 py-4 relative">
      <img
        src="../assets/mercedes-logo.png"
        alt="Mercedes-Benz Logo"
        class="w-16 h-16 sm:w-20 sm:h-20 mb-4 sm:mb-6 opacity-90 drop-shadow-lg mx-auto"
      />
      <div
        class="w-full max-w-xs sm:max-w-md bg-white/10 dark:bg-zinc-900/80 backdrop-blur-lg rounded-2xl shadow-2xl p-4 sm:p-8 mx-auto"
      >
        <h1 class="text-2xl sm:text-3xl font-extrabold text-white tracking-wider mb-1 font-sans text-center drop-shadow-xl">
          {{ t('title') }}
        </h1>
        <p class="text-zinc-300 text-sm sm:text-base mb-4 tracking-tight text-center">{{ t('desc') }}</p>
        <form @submit.prevent="handleLogin" class="space-y-4 sm:space-y-6">
          <div>
            <label class="block text-zinc-200 mb-1 font-medium text-xs sm:text-sm">{{ t('username') }}</label>
            <input
              v-model="username"
              type="text"
              required
              :placeholder="t('usernamePlaceholder')"
              class="w-full px-3 py-2.5 sm:px-4 sm:py-2.5 rounded-xl bg-zinc-900/80 text-white placeholder:text-zinc-400 border border-white/20 focus:border-[#5d737e] focus:ring-2 focus:ring-[#5d737e] outline-none transition text-sm"
            />
          </div>
          <div>
            <label class="block text-zinc-200 mb-1 font-medium text-xs sm:text-sm">{{ t('password') }}</label>
            <input
              v-model="password"
              type="password"
              required
              :placeholder="t('passwordPlaceholder')"
              class="w-full px-3 py-2.5 sm:px-4 sm:py-2.5 rounded-xl bg-zinc-900/80 text-white placeholder:text-zinc-400 border border-white/20 focus:border-[#5d737e] focus:ring-2 focus:ring-[#5d737e] outline-none transition text-sm"
            />
          </div>
          <button
            type="submit"
            class="w-full bg-gradient-to-r from-[#5d737e] via-gray-900 to-[#5d737e] hover:from-gray-800 hover:to-gray-900 text-white font-bold py-2.5 rounded-xl shadow-lg transition-all duration-300 focus:ring-2 focus:ring-[#5d737e] text-sm sm:text-base"
          >
            {{ t('signIn') }}
          </button>
          <div class="text-right mt-1">
            <a href="#" class="text-xs sm:text-sm text-[#5d737e] hover:text-white transition">{{ t('forgot') }}</a>
          </div>
        </form>
        <div v-if="error" class="text-red-400 text-center mt-3 text-xs sm:text-base">{{ error }}</div>
      </div>
    </main>
    <footer class="w-full bg-black p-0 m-0">
      <img
        src="../assets/footer.png"
        alt="Mercedes-Benz Brands"
        class="block w-full object-contain"
        style="margin-bottom: 0; padding-bottom: 0;"
      />
      <div class="flex flex-col sm:flex-row items-center justify-center gap-1 sm:gap-2 bg-black text-zinc-300 text-xs sm:text-sm md:text-base py-2 px-2 w-full text-center">
        &copy; 2025 Mercedes-Benz. All rights reserved.
      </div>
    </footer>
  </div>
</template>

<script setup>
import axios from "axios";
import { onMounted, ref, watch } from 'vue';

const username = ref('')
const password = ref('')
const error = ref('')
const language = ref('EN')
const isDark = ref(false)

// Simple translations
const translations = {
  EN: {
    title: 'Mercedes Login',
    desc: 'Sign in to your luxury experience',
    username: 'Username',
    usernamePlaceholder: 'Enter your username',
    password: 'Password',
    passwordPlaceholder: 'Enter your password',
    signIn: 'Sign In',
    forgot: 'Forgot password?',
  },
  FR: {
    title: 'Connexion Mercedes',
    desc: 'Connectez-vous à votre expérience de luxe',
    username: "Nom d'utilisateur",
    usernamePlaceholder: "Entrez votre nom d'utilisateur",
    password: 'Mot de passe',
    passwordPlaceholder: 'Entrez votre mot de passe',
    signIn: 'Se connecter',
    forgot: 'Mot de passe oublié ?',
  },
}

const t = (key) => translations[language.value][key] || ''

async function handleLogin() {
  error.value = "";
  if (!username.value || !password.value) {
    error.value = language.value === "FR"
      ? "Veuillez entrer vos informations"
      : "Please enter your credentials";
    return;
  }
  try {
    const response = await axios.post("http://localhost:8080/api/auth/login", {
      username: username.value,
      password: password.value,
    });
    const role = response.data.role;
    // Redirect based on role
    if (role === "ADMIN") {
      window.location.href = "/adminDashboard"; // Or use Vue Router
    } else if (role === "USER") {
      window.location.href = "/userDashboard";
    } else {
      error.value = "Role inconnu";
    }
  } catch (err) {
    error.value = language.value === "FR"
      ? "Identifiants incorrects"
      : "Invalid credentials";
  }
}

// DARK/LIGHT MODE: Toggle 'dark' class on <html>
const toggleDark = () => {
  isDark.value = !isDark.value
  updateHtmlClass()
}

function updateHtmlClass() {
  const html = document.documentElement
  if (isDark.value) html.classList.add('dark')
  else html.classList.remove('dark')
}

// Make sure it's in sync on mount and on changes
onMounted(updateHtmlClass)
watch(isDark, updateHtmlClass)
</script>