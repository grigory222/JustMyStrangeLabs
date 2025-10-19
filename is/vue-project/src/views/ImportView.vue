<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { api } from '@/services/api';
import { useAuthStore } from '@/store/authStore';

interface ImportOperation {
  id: number;
  status: 'SUCCESS' | 'FAILED' | 'IN_PROGRESS';
  username: string;
  addedCount: number | null;
  createdAt?: string; // может приходить с сервера
  localCreatedAt?: string; // локальная дата/время
  errorMessage: string | null;
}

const authStore = useAuthStore();
const file = ref<File | null>(null);
const uploading = ref(false);
const uploadMessage = ref('');
const history = ref<ImportOperation[]>([]);

const fileInput = ref<HTMLInputElement | null>(null);
const isAdmin = computed(() => {
  const roles = authStore.roles as unknown as string[];
  return roles && roles.includes('ROLE_ADMIN');
});

function onFileChange(event: Event) {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    file.value = target.files[0];
  }
}

async function uploadFile() {
  if (!file.value) {
    uploadMessage.value = 'Выберите файл для загрузки';
    return;
  }

  uploading.value = true;
  uploadMessage.value = '';

  try {
    const response = await api.importRoutes(file.value);
    uploadMessage.value = `✅ Импорт успешен! Добавлено объектов: ${response.addedCount}`;
    file.value = null;
    if (fileInput.value) {
      fileInput.value.value = '';
    }
    await loadHistory();
  } catch (error: any) {
    uploadMessage.value = `❌ Ошибка импорта: ${error.response?.data?.message || error.message}`;
  } finally {
    uploading.value = false;
  }
}

async function loadHistory() {
  try {
    const data = await api.getImportHistory();
    // Добавляем локальную дату/время для каждой операции
    history.value = data.map(op => ({
      ...op,
      localCreatedAt: op.createdAt || new Date().toISOString()
    }));
  } catch (error) {
    console.error('Failed to load import history:', error);
  }
}

function getStatusBadge(status: string) {
  const badges: Record<string, string> = {
    'SUCCESS': '✅ Успешно',
    'FAILED': '❌ Ошибка',
    'IN_PROGRESS': '⏳ В процессе'
  };
  return badges[status] || status;
}

function formatDate(dateString: string | undefined) {
  if (!dateString) return new Date().toLocaleString('ru-RU');
  try {
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return new Date().toLocaleString('ru-RU');
    return date.toLocaleString('ru-RU');
  } catch {
    return new Date().toLocaleString('ru-RU');
  }
}

onMounted(() => {
  loadHistory();
});
</script>

<template>
  <div class="import-container">
    <h1>📤 Импорт маршрутов</h1>

    <section class="upload-section">
      <h2>Загрузить файл</h2>
      <p class="help-text">
        Поддерживаемые форматы: JSON.<br>
        Файл должен содержать массив объектов Route с полями: name, coordinates, from, to, distance, rating.
      </p>

      <div class="file-upload">
        <input
          ref="fileInput"
          type="file"
          accept=".json"
          @change="onFileChange"
          class="file-input"
        />
        <button
          class="btn primary"
          @click="uploadFile"
          :disabled="uploading || !file"
        >
          {{ uploading ? 'Загрузка...' : 'Загрузить' }}
        </button>
      </div>

      <div v-if="file" class="file-info">
        📄 Выбран файл: <strong>{{ file.name }}</strong> ({{ (file.size / 1024).toFixed(2) }} KB)
      </div>

      <div v-if="uploadMessage" class="upload-message" :class="{ error: uploadMessage.includes('❌') }">
        {{ uploadMessage }}
      </div>
    </section>

    <section class="history-section">
      <h2>История импорта</h2>
      <p class="help-text" v-if="isAdmin">
        Вы видите все операции импорта (режим администратора)
      </p>
      <p class="help-text" v-else>
        Отображаются только ваши операции импорта
      </p>

      <div v-if="history.length === 0" class="no-data">
        Нет операций импорта
      </div>

      <div v-else class="history-table">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Статус</th>
              <th>Пользователь</th>
              <th>Добавлено объектов</th>
              <th>Дата</th>
              <th>Ошибка</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="op in history" :key="op.id">
              <td>{{ op.id }}</td>
              <td>
                <span class="status-badge" :class="op.status.toLowerCase()">
                  {{ getStatusBadge(op.status) }}
                </span>
              </td>
              <td>{{ op.username }}</td>
              <td>{{ op.addedCount ?? '—' }}</td>
              <td>{{ formatDate(op.localCreatedAt) }}</td>
              <td class="error-cell">{{ op.errorMessage ?? '—' }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </div>
</template>

<style scoped>
.import-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 32px;
}

h1 {
  font-size: 32px;
  margin-bottom: 8px;
}

h2 {
  font-size: 24px;
  margin-bottom: 12px;
}

.help-text {
  color: var(--color-text);
  opacity: 0.7;
  font-size: 14px;
  margin-bottom: 16px;
}

section {
  background: var(--color-background-soft);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  padding: 24px;
}

.file-upload {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 16px;
}

.file-input {
  flex: 1;
  padding: 8px;
  border: 1px solid var(--color-border);
  border-radius: 6px;
  background: var(--color-background);
}

.btn {
  padding: 10px 24px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s;
}

.btn.primary {
  background: #42b983;
  color: white;
}

.btn.primary:hover:not(:disabled) {
  background: #359268;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.file-info {
  padding: 12px;
  background: var(--color-background);
  border: 1px solid var(--color-border);
  border-radius: 6px;
  margin-bottom: 12px;
}

.upload-message {
  padding: 12px;
  border-radius: 6px;
  background: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.upload-message.error {
  background: #f8d7da;
  color: #721c24;
  border-color: #f5c6cb;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: var(--color-text);
  opacity: 0.5;
}

.history-table {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background: var(--color-background-mute);
}

th {
  padding: 12px;
  text-align: left;
  font-weight: 600;
  border-bottom: 2px solid var(--color-border);
}

td {
  padding: 12px;
  border-bottom: 1px solid var(--color-border);
}

.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.success {
  background: #d4edda;
  color: #155724;
}

.status-badge.failed {
  background: #f8d7da;
  color: #721c24;
}

.status-badge.in_progress {
  background: #fff3cd;
  color: #856404;
}

.error-cell {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 12px;
  color: #721c24;
}
</style>
