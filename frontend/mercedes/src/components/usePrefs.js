import { computed, ref } from 'vue'

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
