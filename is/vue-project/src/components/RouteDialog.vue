<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { api } from '@/services/api';
import { createRoute, updateRoute } from '@/store/routesStore';
import type { Route, Location, Coordinates } from '@/types/models';
import LocationDialog from './LocationDialog.vue';
import CoordinatesDialog from './CoordinatesDialog.vue';

const props = defineProps<{ id: number | null }>();
const emit = defineEmits<{ (e: 'close'): void; (e: 'saved', route: Route): void }>();

const loading = ref(false);
const error = ref<string | null>(null);

const showLocationDialog = ref(false);
const locationTarget = ref<'from' | 'to' | null>(null);
const showCoordinatesDialog = ref(false);

const form = reactive({
  name: '',
  coordinates: { x: 0, y: 0 } as Coordinates,
  from: { x: 0, y: 0, z: 0, name: '' } as Location,
  to: { x: 0, y: 0, z: 0, name: '' } as Location,
  toEnabled: true,
  distance: 2,
  rating: null as number | null,
});

onMounted(async () => {
  if (props.id != null) {
    loading.value = true;
    try {
      const data = await api.getRouteById(props.id);
      form.name = data.name;
      form.coordinates = { ...data.coordinates };
      form.from = { ...data.from };
      form.to = data.to ? { ...data.to } : ({ x: 0, y: 0, z: 0, name: '' } as Location);
      form.toEnabled = !!data.to;
      form.distance = data.distance;
      form.rating = data.rating ?? null;
    } catch (e: any) {
      error.value = e?.message || 'Не удалось загрузить маршрут';
    } finally {
      loading.value = false;
    }
  }
});

function openLocationDialog(target: 'from' | 'to') {
  locationTarget.value = target;
  showLocationDialog.value = true;
}

function selectLocation(location: Location) {
  if (locationTarget.value) {
    form[locationTarget.value] = location;
  }
}

function openCoordinatesDialog() {
  showCoordinatesDialog.value = true;
}

function selectCoordinates(coords: Coordinates) {
  form.coordinates = coords;
}

async function onSubmit() {
  error.value = null;
  try {
    const payload: any = {
      name: form.name,
      coordinates: { ...form.coordinates },
      from: { ...form.from },
      to: form.toEnabled ? { ...form.to } : null,
      distance: Number(form.distance),
      rating: form.rating === null || form.rating === undefined || form.rating === ('' as any) ? null : Number(form.rating),
    };
    let saved: Route;
    if (props.id == null) saved = await createRoute(payload);
    else saved = await updateRoute(props.id, payload);
    emit('saved', saved);
  } catch (e: any) {
    error.value = e?.response?.data?.message || e?.message || 'Ошибка при сохранении';
  }
}

</script>

<template>
  <div class="dialog-backdrop" @click.self="emit('close')">
    <div class="dialog">
      <header>
        <h3>{{ props.id == null ? 'Add Route' : 'Edit Route' }}</h3>
        <button class="btn" @click="emit('close')">×</button>
      </header>
      <form @submit.prevent="onSubmit">
        <div class="grid">
          <label class="field">name
            <input class="input" v-model.trim="form.name" required />
          </label>
          <div class="field">
            <label>coordinates.x
              <input class="input" type="number" v-model.number="form.coordinates.x" />
            </label>
            <button type="button" class="btn" @click="openCoordinatesDialog">Select</button>
          </div>
          <label class="field">coordinates.y (>-845)
            <input class="input" type="number" step="0.01" v-model.number="form.coordinates.y" />
          </label>

          <fieldset class="col-span-2">
            <legend>from</legend>
            <button type="button" class="btn" @click="openLocationDialog('from')">Select From</button>
            <div class="grid">
              <label class="field">x<input class="input" type="number" step="0.01" v-model.number="form.from.x" /></label>
              <label class="field">y<input class="input" type="number" v-model.number="form.from.y" /></label>
              <label class="field">z<input class="input" type="number" v-model.number="form.from.z" /></label>
              <label class="field">name<input class="input" v-model="form.from.name" /></label>
            </div>
          </fieldset>

          <fieldset class="col-span-2">
            <legend>to</legend>
            <button type="button" class="btn" @click="openLocationDialog('to')">Select To</button>
            <label class="field"><input type="checkbox" v-model="form.toEnabled" /> Specify "to" location</label>
            <div class="grid" v-if="form.toEnabled">
              <label class="field">x<input class="input" type="number" step="0.01" v-model.number="form.to.x" /></label>
              <label class="field">y<input class="input" type="number" v-model.number="form.to.y" /></label>
              <label class="field">z<input class="input" type="number" v-model.number="form.to.z" /></label>
              <label class="field">name<input class="input" v-model="form.to.name" /></label>
            </div>
          </fieldset>

          <label class="field">distance (>1)
            <input class="input" type="number" min="2" v-model.number="form.distance" />
          </label>
          <label class="field">rating (>0 or empty)
            <input class="input" type="number" min="1" v-model.number="form.rating" />
          </label>
        </div>
        <div v-if="error" class="error">{{ error }}</div>
        <footer>
          <button class="btn" type="button" @click="emit('close')">Cancel</button>
          <button class="btn primary" type="submit" :disabled="loading">Save</button>
        </footer>
      </form>
    </div>
  </div>
  <LocationDialog v-if="showLocationDialog" :show="showLocationDialog" @close="showLocationDialog = false" @select="selectLocation" />
  <CoordinatesDialog v-if="showCoordinatesDialog" @close="showCoordinatesDialog = false" @select="selectCoordinates" />
</template>

<style scoped>
.dialog-backdrop { position: fixed; inset: 0; background: rgba(0,0,0,.3); display: grid; place-items: center; }
.dialog { background: var(--color-background); color: var(--color-text); min-width: 720px; max-width: 90vw; border-radius: 12px; padding: 16px; border: 1px solid var(--color-border); box-shadow: 0 6px 24px rgba(0,0,0,.2); }
header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
.grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 10px; }
.col-span-2 { grid-column: span 2; }
fieldset { border: 1px solid #ddd; padding: 8px; border-radius: 6px; }
label { display: grid; gap: 4px; font-size: 14px; }
.field input.input { width: 100%; }
.input { padding: 8px 10px; border: 1px solid var(--color-border); border-radius: 8px; background: var(--color-background); color: var(--color-text); }
.btn { padding: 6px 10px; border: 1px solid var(--color-border); background: var(--color-background-soft); border-radius: 8px; cursor: pointer; }
.btn.primary { background: hsla(160, 100%, 37%, 0.15); border-color: hsla(160, 100%, 37%, 0.4); }
footer { display: flex; justify-content: flex-end; gap: 8px; margin-top: 12px; }
.error { color: #b00020; margin-top: 8px; }
</style>


