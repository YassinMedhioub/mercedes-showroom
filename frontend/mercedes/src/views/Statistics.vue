
<template>
  <div
    :class="[
      'min-h-svh bg-gradient-to-br',
      props.isDark
        ? 'from-[#0b0b0e] via-[#0f1115] to-[#1a1d22] text-zinc-100'
        : 'from-[#f6f7fb] via-[#f1f2f6] to-[#e7eaef] text-zinc-900'
    ]"
  >
  <br>
    <main
      class="mx-auto w-full px-4 sm:px-6 py-8 max-w-[80rem] pt-[var(--appbar)] sm:pt-[calc(var(--appbar)+0.5rem)]"
    >
      <!-- HEADER -->
      <div class="flex items-center justify-between mb-6">
        <div>
          <h1 class="text-2xl sm:text-3xl font-extrabold">{{ t('title') }}</h1>
          <p class="text-sm opacity-70">{{ t('subtitle') }}</p>
        </div>

        <button
          @click="router.push('/admin')"
          class="inline-flex items-center justify-center px-3 py-2 rounded-lg text-sm font-medium transition-colors"
          :class="props.isDark
            ? 'bg-zinc-800 text-white hover:bg-zinc-700'
            : 'bg-white border border-zinc-300 text-zinc-700 hover:bg-zinc-200'"
        >
          ← {{ t('back') }}
        </button>
      </div>

      <!-- KPI CARDS -->
      <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
        <div class="rounded-2xl p-5 shadow"
          :class="props.isDark ? 'bg-zinc-900 border border-zinc-700' : 'bg-white border border-zinc-200'">
          <p class="text-sm opacity-70">{{ t('totalUsers') }}</p>
          <p class="text-2xl font-bold">{{ stats.users }}</p>
        </div>
        <div class="rounded-2xl p-5 shadow"
          :class="props.isDark ? 'bg-zinc-900 border border-zinc-700' : 'bg-white border border-zinc-200'">
          <p class="text-sm opacity-70">{{ t('totalCars') }}</p>
          <p class="text-2xl font-bold">{{ stats.cars }}</p>
        </div>
        <div class="rounded-2xl p-5 shadow"
          :class="props.isDark ? 'bg-zinc-900 border border-zinc-700' : 'bg-white border border-zinc-200'">
          <p class="text-sm opacity-70">{{ t('totalSales') }}</p>
          <p class="text-2xl font-bold">{{ stats.sales }} TND</p>
        </div>
      </div>

      <!-- SALES CHART -->
      <section class="rounded-2xl shadow p-5"
        :class="props.isDark ? 'bg-zinc-900 border border-zinc-700' : 'bg-white border border-zinc-200'">
        <h2 class="text-lg font-semibold mb-3">{{ t('salesChart') }}</h2>
        <div class="h-[350px]">
          <Line :data="salesData" :options="chartOptions" />
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from '../composables/useI18n'
import { fetchStatistics as fetchStatsApi } from '../services/api'

// Chart.js + vue-chartjs
import {
  CategoryScale,
  Chart as ChartJS,
  Legend,
  LinearScale,
  LineElement,
  PointElement,
  Title,
  Tooltip
} from 'chart.js'
import { Line } from 'vue-chartjs'

// Register Chart.js components
ChartJS.register(Title, Tooltip, Legend, LineElement, CategoryScale, LinearScale, PointElement)

const emit = defineEmits(['navigate'])

/* Props */
const props = defineProps({
  isDark: Boolean,
  language: { type: String, default: 'FR' }
})

/* Services */
const router = useRouter()
const { t } = useI18n()

/* state */
const stats = ref({ users: 0, cars: 0, sales: 0 })
const salesData = ref({
  labels: [],
  datasets: []
})

const chartOptions = computed(() => ({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: { 
      labels: { 
        color: props.isDark ? '#d1d5db' : '#6b7280' 
      } 
    },
    tooltip: { 
      mode: 'index', 
      intersect: false,
      backgroundColor: props.isDark ? 'rgba(17, 24, 39, 0.8)' : 'rgba(255, 255, 255, 0.8)',
      titleColor: props.isDark ? '#f9fafb' : '#111827',
      bodyColor: props.isDark ? '#d1d5db' : '#374151'
    }
  },
  scales: {
    x: { 
      ticks: { color: props.isDark ? '#d1d5db' : '#6b7280' },
      grid: { 
        color: props.isDark ? 'rgba(55, 65, 81, 0.2)' : 'rgba(229, 231, 235, 0.2)' 
      }
    },
    y: { 
      ticks: { color: props.isDark ? '#d1d5db' : '#6b7280' },
      grid: { 
        color: props.isDark ? 'rgba(55, 65, 81, 0.2)' : 'rgba(229, 231, 235, 0.2)' 
      }
    }
  }
}))

/* fetch stats */
async function fetchStatistics() {
  try {
    const data = await fetchStatsApi()
    stats.value = data.overview || { users: 0, cars: 0, sales: 0 }

    salesData.value = {
      labels: data.salesByMonth?.map(e => e.month) || [],
      datasets: [
        {
          label: t('salesTND'),
          data: data.salesByMonth?.map(e => e.sales) || [],
          borderColor: '#10b981',
          backgroundColor: 'rgba(16,185,129,0.2)',
          tension: 0.3
        },
        {
          label: t('carsSold'),
          data: data.salesByMonth?.map(e => e.cars) || [],
          borderColor: '#3b82f6',
          backgroundColor: 'rgba(59,130,246,0.2)',
          tension: 0.3
        }
      ]
    }
  } catch (e) {
    console.error('fetchStatistics error', e)
    // Fallback data
    stats.value = { users: 42, cars: 128, sales: 2450000 }
    salesData.value = {
      labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
      datasets: [
        {
          label: t('salesTND'),
          data: [200000, 250000, 300000, 280000, 320000, 350000],
          borderColor: '#10b981',
          backgroundColor: 'rgba(16,185,129,0.2)',
          tension: 0.3
        },
        {
          label: t('carsSold'),
          data: [8, 12, 15, 14, 18, 20],
          borderColor: '#3b82f6',
          backgroundColor: 'rgba(59,130,246,0.2)',
          tension: 0.3
        }
      ]
    }
  }
}

onMounted(fetchStatistics)
</script>