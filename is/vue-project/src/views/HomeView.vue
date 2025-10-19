<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/store/authStore';
import { api } from '@/services/api';

const authStore = useAuthStore();
const router = useRouter();
const totalRoutes = ref(0);
const username = computed(() => {
  const user = authStore.user as unknown as { username: string; roles: string[]; message: string; } | null;
  return user?.username || '';
});
const userRole = computed(() => {
  const roles = authStore.roles as unknown as string[];
  return roles && roles.length > 0 ? roles[0] : '';
});

onMounted(async () => {
  try {
    const data = await api.getRoutes({ page: 0, size: 1 });
    totalRoutes.value = data.totalElements;
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

function goToImport() {
  router.push('/import');
}

function getRoleDisplayName(role: string): string {
  const roleMap: Record<string, string> = {
    'ROLE_USER': 'Пользователь',
    'ROLE_ADMIN': 'Администратор'
  };
  return roleMap[role] || role;
}
</script>

<template>
  <main class="home">
    <div class="hero">
      <h1>🗺️ Управление Маршрутами</h1>
      <p class="subtitle">Добро пожаловать, {{ username }}!</p>
      <div class="user-info">
        <span class="user-role">{{ getRoleDisplayName(userRole) }}</span>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-number">{{ totalRoutes }}</div>
        <div class="stat-label">Всего маршрутов</div>
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
      <button 
        class="action-btn secondary" 
        @click="goToImport"
      >
        <span class="icon">📤</span>
        <span>Импорт маршрутов</span>
      </button>
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

.user-info {
  margin-top: 12px;
  display: flex;
  justify-content: center;
  gap: 12px;
}

.user-role {
  display: inline-block;
  padding: 6px 16px;
  background: var(--color-background-soft);
  border: 1px solid var(--color-border);
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  color: var(--color-heading);
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
</style>
