<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { routesState, routes, totalPages, fetchRoutes, deleteRoute } from '@/store/routesStore';
import RouteDialog from '@/components/RouteDialog.vue';

const showDialog = ref(false);
const editId = ref<number | null>(null);

const filterName = ref<string>('');
// ...existing code...
const sortField = ref<string>('');
const sortDir = ref<'asc' | 'desc'>('asc');

function openCreate() {
  editId.value = null;
  showDialog.value = true;
}
function openEdit(id: number) {
  editId.value = id;
  showDialog.value = true;
}

function applyFilters() {
  const sort = sortField.value || undefined;
  const order = sortField.value ? sortDir.value : undefined;
  void fetchRoutes({
    page: 0,
    nameEquals: filterName.value || undefined,
  // ...existing code...
    sort,
    order
  });
}

function toggleSort(field: string) {
  if (sortField.value === field) {
    sortDir.value = sortDir.value === 'asc' ? 'desc' : 'asc';
  } else {
    sortField.value = field;
    sortDir.value = 'asc';
  }
  const sort = sortField.value || undefined;
  const order = sortField.value ? sortDir.value : undefined;
  void fetchRoutes({ page: 0, sort, order });
}

function changePage(p: number) {
  void fetchRoutes({ page: p });
}

onMounted(() => {
  void fetchRoutes();
});

watch([() => routesState.query.size], () => {
  void fetchRoutes({ page: 0 });
});
</script>

<template>
  <div class="routes-view">
    <div class="toolbar">
      <div class="filters">
        <div class="field">
          <label>name</label>
          <input class="input" v-model="filterName" placeholder="exact match" />
        </div>
        <!-- rating filter removed -->
        <button class="btn" @click="applyFilters">Apply</button>
      </div>
      <button class="btn primary add" @click="openCreate">Add Route</button>
    </div>

    <div v-if="routesState.error" class="error">{{ routesState.error }}</div>
    <div v-if="routesState.loading">Загрузка...</div>

    <table v-if="!routesState.loading">
      <thead>
        <tr>
          <th @click="toggleSort('id')" :class="{ sortable: true, active: sortField==='id' }">ID <span class="carret" v-if="sortField==='id'">{{ sortDir==='asc' ? '▲' : '▼' }}</span></th>
          <th @click="toggleSort('name')" :class="{ sortable: true, active: sortField==='name' }">name <span class="carret" v-if="sortField==='name'">{{ sortDir==='asc' ? '▲' : '▼' }}</span></th>
          <th>coordinates.x</th>
          <th>coordinates.y</th>
          <th @click="toggleSort('creationDate')" :class="{ sortable: true, active: sortField==='creationDate' }">creationDate <span class="carret" v-if="sortField==='creationDate'">{{ sortDir==='asc' ? '▲' : '▼' }}</span></th>
          <th>from.name</th>
          <th>to.name</th>
          <th @click="toggleSort('distance')" :class="{ sortable: true, active: sortField==='distance' }">distance <span class="carret" v-if="sortField==='distance'">{{ sortDir==='asc' ? '▲' : '▼' }}</span></th>
          <th @click="toggleSort('rating')" :class="{ sortable: true, active: sortField==='rating' }">rating <span class="carret" v-if="sortField==='rating'">{{ sortDir==='asc' ? '▲' : '▼' }}</span></th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="r in routes" :key="r.id">
          <td>{{ r.id }}</td>
          <td>{{ r.name }}</td>
          <td>{{ r.coordinates.x }}</td>
          <td>{{ r.coordinates.y }}</td>
          <td>{{ new Date(r.creationDate).toLocaleString() }}</td>
          <td>{{ r.from?.name }}</td>
          <td>{{ r.to?.name }}</td>
          <td>{{ r.distance }}</td>
          <td>{{ r.rating ?? '—' }}</td>
          <td>
            <button class="btn" @click="openEdit(r.id)">Edit</button>
            <button class="btn danger" @click="deleteRoute(r.id)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div class="pagination" v-if="totalPages > 1">
      <button class="btn" :disabled="routesState.query.page === 0" @click="changePage(0)">«</button>
      <button class="btn" :disabled="routesState.query.page === 0" @click="changePage(routesState.query.page - 1)">‹</button>
      <span>Стр. {{ (routesState.query.page ?? 0) + 1 }} из {{ totalPages }}</span>
      <button class="btn" :disabled="routesState.query.page >= totalPages - 1" @click="changePage(routesState.query.page + 1)">›</button>
      <button class="btn" :disabled="routesState.query.page >= totalPages - 1" @click="changePage(totalPages - 1)">»</button>
      <select class="input" v-model.number="routesState.query.size">
        <option :value="5">5</option>
        <option :value="10">10</option>
        <option :value="20">20</option>
        <option :value="50">50</option>
      </select>
    </div>

    <RouteDialog v-if="showDialog" :id="editId" @close="showDialog = false" @saved="() => { showDialog = false; }" />
  </div>
  
</template>

<style scoped>
.routes-view { display: grid; gap: 12px; }
.toolbar { display: flex; gap: 12px; align-items: center; justify-content: space-between; }
.filters { display: flex; gap: 12px; align-items: end; }
.field { display: grid; gap: 4px; }
.input { padding: 6px 8px; border: 1px solid var(--color-border); border-radius: 6px; background: var(--color-background); color: var(--color-text); }
.btn { padding: 6px 10px; border: 1px solid var(--color-border); background: var(--color-background-soft); border-radius: 6px; cursor: pointer; }
.btn.primary { background: hsla(160, 100%, 37%, 1); border-color: hsla(160, 100%, 37%, 0.9); color: white; }
.btn.danger { background: #b00020; border-color: rgba(176, 0, 32, 0.9); color: white; }
.add { align-self: flex-end; }

table { width: 100%; border-collapse: separate; border-spacing: 0; border: 1px solid var(--color-border); border-radius: 8px; overflow: hidden; }
thead th { background: var(--color-background-soft); text-align: left; }
th, td { padding: 8px 10px; border-bottom: 1px solid var(--color-border); }
tbody tr:hover { background: var(--color-background-soft); }
th.sortable { cursor: pointer; user-select: none; }
th.active { color: hsla(160, 100%, 37%, 1); }
.carret { margin-left: 4px; }

.pagination { display: flex; gap: 8px; align-items: center; }
.error { color: #b00020; }
</style>


