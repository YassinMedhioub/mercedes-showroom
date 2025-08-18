<!-- src/components/FooterBar.vue -->
<template>
  <footer class="w-full bg-black mt-4">
    <img v-if="isDark" src="../assets/footer.png" alt="Mercedes-Benz Brands" class="block w-full object-contain" />
    <img v-else   src="../assets/footer-inverted.png" alt="Mercedes-Benz Brands" class="block w-full object-contain" />
    <hr>
    <div
      :class="[
        isDark ? 'bg-black text-zinc-300' : 'bg-white text-black',
        'flex flex-col sm:flex-row items-center justify-center gap-1 sm:gap-2 text-xs sm:text-sm md:text-base py-2 px-2 w-full text-center'
      ]">
      &copy; 2025 Mercedes-Benz. All rights reserved.
    </div>
  </footer>
</template>

<script setup>
import { usePrefs } from './usePrefs'
const { isDark } = usePrefs()
</script>
