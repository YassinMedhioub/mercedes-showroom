// src/components/usePrefs.ts
import { ref, watch } from 'vue'

const g = globalThis as any

if (!g.__PREFS__) {
  const lang = ref(localStorage.getItem('lang') || 'FR')
  const isDark = ref(localStorage.getItem('darkMode') === 'true')

  // apply on load
  document.documentElement.classList.toggle('dark', isDark.value)

  // DEBUG: prove this singleton is created once
  console.log('[usePrefs] created singleton at', import.meta.url)

  watch(lang, v => {
    console.log('[usePrefs] lang ->', v)                 // DEBUG on change
    localStorage.setItem('lang', v)
  })

  watch(isDark, v => {
    console.log('[usePrefs] isDark ->', v)               // DEBUG on change
    localStorage.setItem('darkMode', String(v))
    document.documentElement.classList.toggle('dark', v)
  })

  const toggleDark = () => (isDark.value = !isDark.value)

  g.__PREFS__ = { lang, isDark, toggleDark }
} else {
  // If your app imports the module twice via different paths,
  // you’ll still reuse the same singleton:
  console.log('[usePrefs] reusing singleton at', import.meta.url)
}

export function usePrefs() {
  return (globalThis as any).__PREFS__ as {
    lang: ReturnType<typeof ref<string>>
    isDark: ReturnType<typeof ref<boolean>>
    toggleDark: () => void
  }
}
