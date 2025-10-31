/* import { computed, ref } from 'vue'

const prefs = ref({
  lang: localStorage.getItem('lang') || 'EN',
  dark: localStorage.getItem('isDark') === 'true'
})

export function usePrefs() {
  const isDark = computed(() => prefs.value.dark)

  const lang = computed({
    get: () => prefs.value.lang,
    set: (v) => {
      prefs.value.lang = v
      localStorage.setItem('lang', v)
    }
  })

  function toggleDark() {
    prefs.value.dark = !prefs.value.dark
    localStorage.setItem('isDark', prefs.value.dark)
  }

  return {
    prefs,
    isDark,
    lang,
    toggleDark
  }
}
 */


import { computed, ref } from 'vue'

const prefs = ref({
  lang: localStorage.getItem('lang') || 'EN',
  dark: localStorage.getItem('isDark') === 'true'
})

export function usePrefs() {
  const isDark = computed({
    get: () => prefs.value.dark,
    set: (v) => {
      prefs.value.dark = v
      localStorage.setItem('isDark', v)
      console.log('🌙 Dark mode persisted:', v)  // Debug optionnel (retirez en prod)
    }
  })

  const lang = computed({
    get: () => prefs.value.lang,
    set: (v) => {
      prefs.value.lang = v
      localStorage.setItem('lang', v)
    }
  })

  // Optionnel: Gardez toggleDark pour une API simple (utilise le setter)
  function toggleDark() {
    isDark.value = !isDark.value  // Utilise le setter - pas de warning
  }

  return {
    prefs,
    isDark,
    lang,
    toggleDark
  }
}