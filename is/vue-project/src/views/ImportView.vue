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
  fileKey: string | null;
  fileName: string | null;
  fileSize: number | null;
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

// Admin controls
const adminStatus = ref({
  minioFailureSimulation: false,
  databaseFailureSimulation: false,
  businessErrorSimulation: false
});
const loadingAdminStatus = ref(false);

async function loadAdminStatus() {
  if (!isAdmin.value) return;
  try {
    loadingAdminStatus.value = true;
    adminStatus.value = await api.getAdminStatus();
  } catch (error) {
    console.error('Failed to load admin status:', error);
  } finally {
    loadingAdminStatus.value = false;
  }
}

async function toggleMinio() {
  try {
    await api.toggleMinio(!adminStatus.value.minioFailureSimulation);
    await loadAdminStatus();
  } catch (error: any) {
    alert(`Ошибка: ${error.response?.data?.message || error.message}`);
  }
}

async function toggleDatabase() {
  try {
    await api.toggleDatabase(!adminStatus.value.databaseFailureSimulation);
    await loadAdminStatus();
  } catch (error: any) {
    alert(`Ошибка: ${error.response?.data?.message || error.message}`);
  }
}

async function toggleBusinessError() {
  try {
    await api.toggleBusinessError(!adminStatus.value.businessErrorSimulation);
    await loadAdminStatus();
  } catch (error: any) {
    alert(`Ошибка: ${error.response?.data?.message || error.message}`);
  }
}

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

async function downloadFile(op: ImportOperation) {
  if (!op.fileKey) {
    alert('Файл недоступен для этой операции импорта');
    return;
  }

  try {
    const response = await api.downloadImportFile(op.id);
    
    // Create a blob from the response
    const blob = new Blob([response.data], { type: response.headers['content-type'] || 'application/octet-stream' });
    
    // Create download link
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = op.fileName || `import-${op.id}.json`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  } catch (error: any) {
    alert(`Ошибка скачивания файла: ${error.response?.data?.message || error.message}`);
  }
}

function formatFileSize(bytes: number | null): string {
  if (!bytes) return '—';
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(2)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(2)} MB`;
}

onMounted(() => {
  loadHistory();
  loadAdminStatus();
});
</script>

<template>
  <div class="import-container">
    <h1>📤 Импорт маршрутов</h1>

    <!-- Admin Controls Panel -->
    <section v-if="isAdmin" class="admin-panel">
      <div class="admin-controls">
        <div class="control-item">
          <label class="switch">
            <input type="checkbox" :checked="adminStatus.minioFailureSimulation" @change="toggleMinio" :disabled="loadingAdminStatus">
            <span class="slider"></span>
          </label>
          <span class="control-label">❌ MinIO Failure</span>
        </div>
        
        <div class="control-item">
          <label class="switch">
            <input type="checkbox" :checked="adminStatus.databaseFailureSimulation" @change="toggleDatabase" :disabled="loadingAdminStatus">
            <span class="slider"></span>
          </label>
          <span class="control-label">❌ DB Failure</span>
        </div>
        
        <div class="control-item">
          <label class="switch">
            <input type="checkbox" :checked="adminStatus.businessErrorSimulation" @change="toggleBusinessError" :disabled="loadingAdminStatus">
            <span class="slider"></span>
          </label>
          <span class="control-label">❌ Logic Error</span>
        </div>
      </div>
    </section>

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
              <th>Файл</th>
              <th>Размер</th>
              <th>Дата</th>
              <th>Ошибка</th>
              <th>Действия</th>
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
              <td>{{ op.fileName ?? '—' }}</td>
              <td>{{ formatFileSize(op.fileSize) }}</td>
              <td>{{ formatDate(op.localCreatedAt) }}</td>
              <td class="error-cell">{{ op.errorMessage ?? '—' }}</td>
              <td>
                <button
                  v-if="op.fileKey"
                  class="btn download"
                  @click="downloadFile(op)"
                  title="Скачать файл импорта"
                >
                  📥 Скачать
                </button>
                <span v-else class="no-file">—</span>
              </td>
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

.btn.download {
  background: #007bff;
  color: white;
  padding: 6px 12px;
  font-size: 14px;
}

.btn.download:hover:not(:disabled) {
  background: #0056b3;
}

.no-file {
  color: var(--color-text);
  opacity: 0.5;
}

/* Admin Panel */
.admin-panel {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 15px 20px;
  border-radius: 8px;
  margin-bottom: 30px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.admin-controls {
  display: flex;
  gap: 30px;
  justify-content: center;
  align-items: center;
}

.control-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.control-label {
  color: white;
  font-weight: 500;
  font-size: 14px;
}

/* Toggle Switch */
.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 24px;
  cursor: pointer;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.3);
  transition: 0.3s;
  border-radius: 24px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 18px;
  width: 18px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: 0.3s;
  border-radius: 50%;
}

input:checked + .slider {
  background-color: rgba(76, 175, 80, 0.8);
}

input:checked + .slider:before {
  transform: translateX(26px);
}

input:disabled + .slider {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>

