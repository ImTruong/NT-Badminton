import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      '/category': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      },
      '/contact': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      },
      '/user': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      },
      '/cart': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      },
      '/product': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      },
      '/order': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      }
    }
  }
})
