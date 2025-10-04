<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/store/authStore';
import { api } from '@/services/api';
import type { Route } from '@/types/models';

const authStore = useAuthStore();
const router = useRouter();
const stats = ref({ totalRoutes: 0, recentRoutes: [] as Route[] });

onMounted(async () => {
  try {
    const data = await api.getRoutes({ page: 0, size: 5, sort: 'creationDate', order: 'desc' });
    stats.value.totalRoutes = data.totalElements;
    stats.value.recentRoutes = data.content;
  } catch (error) {
    console.error('Failed to load stats:', error);
  }
});

function goToRoutes() {
  router.push('/routes');
}

function goToSpecial() {
  router.push('/special');
}
</script>

<template>
  <main class="home">
    <div class="hero">
      <h1>🗺️ Управление Маршрутами</h1>
      <p class="subtitle">Добро пожаловать, {{ authStore.user?.username }}!</p>
    </div>

    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-number">{{ stats.totalRoutes }}</div>
        <div class="stat-label">Всего маршрутов</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ authStore.roles.length }}</div>
        <div class="stat-label">Ролей</div>
      </div>
    </div>

    <div class="actions">
      <button class="action-btn primary" @click="goToRoutes">
        <span class="icon">📋</span>
        <span>Просмотр маршрутов</span>
      </button>
      <button 
        class="action-btn secondary" 
        @click="goToSpecial"
      >
        <span class="icon">⚙️</span>
        <span>Специальные операции</span>
      </button>
    </div>

    <div v-if="stats.recentRoutes.length > 0" class="recent">
      <h2>Последние маршруты</h2>
      <div class="route-list">
        <div v-for="route in stats.recentRoutes" :key="route.id" class="route-item">
          <div class="route-name">{{ route.name }}</div>
          <div class="route-meta">
            Дистанция: {{ route.distance }} | 
            Рейтинг: {{ route.rating ?? 'N/A' }}
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<style scoped>
.home {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.hero {
  text-align: center;
  padding: 40px 20px;
}

.hero h1 {
  font-size: 48px;
  margin-bottom: 12px;
  background: linear-gradient(135deg, var(--color-heading), #42b983);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.subtitle {
  font-size: 20px;
  color: var(--color-text);
  opacity: 0.8;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.stat-card {
  background: var(--color-background-soft);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  padding: 24px;
  text-align: center;
}

.stat-number {
  font-size: 48px;
  font-weight: bold;
  color: var(--color-heading);
}

.stat-label {
  font-size: 14px;
  color: var(--color-text);
  opacity: 0.7;
  margin-top: 8px;
}

.actions {
  display: flex;
  gap: 16px;
  justify-content: center;
  flex-wrap: wrap;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 32px;
  font-size: 18px;
  border-radius: 12px;
  border: none;
  cursor: pointer;
  transition: all 0.3s;
}

.action-btn.primary {
  background: #42b983;
  color: white;
}

.action-btn.primary:hover {
  background: #359268;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(66, 185, 131, 0.3);
}

.action-btn.secondary {
  background: var(--color-background-soft);
  color: var(--color-heading);
  border: 2px solid var(--color-border);
}

.action-btn.secondary:hover {
  background: var(--color-background-mute);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.icon {
  font-size: 24px;
}

.recent {
  margin-top: 20px;
}

.recent h2 {
  font-size: 24px;
  margin-bottom: 16px;
}

.route-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.route-item {
  background: var(--color-background-soft);
  border: 1px solid var(--color-border);
  border-radius: 8px;
  padding: 16px;
}

.route-name {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-heading);
  margin-bottom: 8px;
}

.route-meta {
  font-size: 14px;
  color: var(--color-text);
  opacity: 0.7;
}
</style>
