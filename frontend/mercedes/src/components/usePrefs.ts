// src/components/usePrefs.ts
import { getCurrentInstance, onMounted, ref, watch } from 'vue'

const isDark = ref(localStorage.getItem('darkMode') === 'true')
const lang = ref(localStorage.getItem('lang') || 'FR')

function applyDark(v: boolean) {
  if (typeof document === 'undefined') return
  document.documentElement.classList.toggle('dark', v)
}

function toggleDark() {
  isDark.value = !isDark.value
}

let initialized = false

export function usePrefs() {
  // Only attach lifecycle once, from inside a component
  if (getCurrentInstance() && !initialized) {
    initialized = true

    onMounted(() => {
      // ensure correct class on first mount
      applyDark(isDark.value)
    })

    // Run after DOM updates to avoid mid-patch mutations
    watch(
      isDark,
      (v) => {
        localStorage.setItem('darkMode', String(v))
        applyDark(v)
      },
      { immediate: true, flush: 'post' }
    )

    watch(lang, (v) => localStorage.setItem('lang', v))
  }

  return { isDark, lang, toggleDark }
}
