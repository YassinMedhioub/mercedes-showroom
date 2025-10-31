import vue from '@vitejs/plugin-vue'
import { defineConfig } from 'vite'

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '^/api/chat': {
        target: 'http://localhost:8787',
        changeOrigin: true,
        secure: false,
      },
      '^/api/ai': {
        target: 'http://localhost:8787',
        changeOrigin: true,
        secure: false,
      },
      '^/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/api/, '/api')
      }
    },
  },

})
