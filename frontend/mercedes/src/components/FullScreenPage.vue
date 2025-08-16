<template>
  <transition name="fade">
    <div
      v-if="modelValue"
      class="fixed inset-0 z-[9999]"
      aria-modal="true"
      role="dialog"
    >
      <!-- Backdrop -->
      <div class="absolute inset-0 bg-black/70 backdrop-blur-sm"></div>

      <!-- Content -->
      <div
        class="absolute inset-0 flex flex-col"
        :class="isDark ? 'bg-zinc-900' : 'bg-white'"
      >
        <!-- Top bar -->
        <div
          class="flex items-center justify-between px-4 sm:px-6 py-3 border-b"
          :class="isDark ? 'border-zinc-800 text-white' : 'border-zinc-200 text-zinc-800'"
        >
          <slot name="title">
            <h2 class="text-lg sm:text-xl font-semibold">{{ title }}</h2>
          </slot>
        <div class="max-w-6xl mx-auto mb-4">

          <button
            @click="goTo('home')"
            class="px-3 py-1.5 rounded-lg text-sm font-medium transition-colors"
            :class="isDark
              ? 'bg-zinc-800 text-white hover:bg-zinc-700'
              : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'"
          >
            {{ closeText }} 
          </button>
        </div>

        <!-- Page body -->
        <div class="flex-1 overflow-auto">
          <slot />
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { onMounted, onUnmounted, watch } from 'vue'

const props = defineProps({
  modelValue: { type: Boolean, required: true },
  title: { type: String, default: '' },
  closeText: { type: String, default: 'Close' },
  isDark: { type: Boolean, default: false },
})

const emit = defineEmits(['update:modelValue'])

function close() {
  emit('update:modelValue', false)
}

function onKey(e) {
  if (e.key === 'Escape') close()
}

// lock scroll on open
watch(() => props.modelValue, val => {
  document.documentElement.classList.toggle('overflow-hidden', val)
})

onMounted(() => window.addEventListener('keydown', onKey))
onUnmounted(() => {
  window.removeEventListener('keydown', onKey)
  document.documentElement.classList.remove('overflow-hidden')
})
</script>

<style>
.fade-enter-active, .fade-leave-active { transition: opacity .18s ease; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>
