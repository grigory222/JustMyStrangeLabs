<template>
  <div class="dialog-backdrop" @click.self="$emit('close')">
    <div class="dialog">
      <header>
        <h3>Select Location</h3>
        <button class="btn" @click="$emit('close')">×</button>
      </header>
      <ul>
        <li v-for="location in locations" :key="location.id" @click="selectLocation(location)">
          {{ location.name }} ({{ location.x }}, {{ location.y }}, {{ location.z }})
        </li>
      </ul>
      <footer>
        <button class="btn" @click="$emit('close')">Close</button>
      </footer>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { api } from '@/services/api';
import type { Location } from '@/types/models';

const locations = ref<Location[]>([]);

const emit = defineEmits(['select', 'close']);

onMounted(async () => {
  locations.value = await api.getLocations();
});

function selectLocation(location: Location) {
  emit('select', location);
  emit('close');
}
</script>

<style scoped>
.dialog-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, .3);
  display: grid;
  place-items: center;
  z-index: 100;
}

.dialog {
  background: var(--color-background);
  color: var(--color-text);
  min-width: 400px;
  max-width: 90vw;
  border-radius: 12px;
  padding: 16px;
  border: 1px solid var(--color-border);
  box-shadow: 0 6px 24px rgba(0, 0, 0, .2);
}

header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

ul {
  list-style-type: none;
  padding: 0;
  max-height: 300px;
  overflow-y: auto;
}

li {
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
}

li:hover {
  background-color: var(--color-background-soft);
}

footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 12px;
}

.btn {
  padding: 6px 10px;
  border: 1px solid var(--color-border);
  background: var(--color-background-soft);
  border-radius: 8px;
  cursor: pointer;
}
</style>
