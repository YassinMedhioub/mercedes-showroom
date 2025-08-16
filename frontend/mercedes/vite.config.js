// vite.config.js
import vue from '@vitejs/plugin-vue'
import { defineConfig } from 'vite'

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8787', // MUST match your AI server
        changeOrigin: true,
        secure: false,
      },
    },
  },
})
