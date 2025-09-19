<script setup lang="ts">
import { ref } from 'vue';
import { api } from '@/services/api';
import type { GroupByNameItem, Route, SortDirection, Location } from '@/types/models';
import LocationDialog from '@/components/LocationDialog.vue';

const rating = ref<number | null>(null);
const deletedCount = ref<number | null>(null);
const deletedAny = ref<Route | null>(null);

const groups = ref<GroupByNameItem[] | null>(null);

const fromLocation = ref<Location | null>(null);
const toLocation = ref<Location | null>(null);
const sortParam = ref<string>('');
const sortOrder = ref<SortDirection>('asc');
const found = ref<Route[] | null>(null);

const newBetween = ref({ name: '', fromId: null as number | null, toId: null as number | null, distance: 2, rating: null as number | null, coordinates: { x: 0, y: 0 } });
const createdBetween = ref<Route | null>(null);

const showLocationDialog = ref(false);
const locationTarget = ref<'from' | 'to' | 'newFrom' | 'newTo' | null>(null);

function openLocationDialog(target: 'from' | 'to' | 'newFrom' | 'newTo') {
  locationTarget.value = target;
  showLocationDialog.value = true;
}

function selectLocation(location: Location) {
  if (locationTarget.value === 'from') {
    fromLocation.value = location;
  } else if (locationTarget.value === 'to') {
    toLocation.value = location;
  } else if (locationTarget.value === 'newFrom') {
    newBetween.value.fromId = location.id ?? null;
  } else if (locationTarget.value === 'newTo') {
    newBetween.value.toId = location.id ?? null;
  }
}

async function onDeleteAllByRating() {
  if (rating.value == null) return;
  deletedCount.value = await api.deleteAllByRating(rating.value);
}

async function onDeleteAnyByRating() {
  if (rating.value == null) return;
  deletedAny.value = await api.deleteAnyByRating(rating.value);
}

async function onGroupByName() {
  groups.value = await api.groupByName();
}

async function onFindBetween() {
  found.value = await api.findRoutesBetween({ fromId: fromLocation.value?.id, toId: toLocation.value?.id, sort: sortParam.value || undefined, order: sortOrder.value });
}

async function onAddBetween() {
  if (newBetween.value.fromId == null || newBetween.value.toId == null) return;
  createdBetween.value = await api.addRouteBetween({
    name: newBetween.value.name,
    fromId: newBetween.value.fromId,
    toId: newBetween.value.toId,
    distance: newBetween.value.distance,
    rating: newBetween.value.rating,
    coordinates: { ...newBetween.value.coordinates },
  });
}
</script>

<template>
  <div class="ops">
    <section>
      <h3>Удалить по рейтингу</h3>
      <label class="field">Рейтинг <input class="input" type="number" v-model.number="rating" /></label>
      <div class="row">
        <button class="btn danger" @click="onDeleteAllByRating">Удалить все</button>
        <button class="btn" @click="onDeleteAnyByRating">Удалить один</button>
      </div>
      <div v-if="deletedCount != null">Удалено: {{ deletedCount }}</div>
      <div v-if="deletedAny">Удален маршрут ID {{ deletedAny.id }}</div>
    </section>

    <section>
      <h3>Группировка по имени</h3>
      <button class="btn" @click="onGroupByName">Выполнить</button>
      <ul v-if="groups">
        <li v-for="g in groups" :key="g.name">{{ g.name }} — {{ g.routesCount }}</li>
      </ul>
    </section>

    <section>
      <h3>Найти маршруты между локациями</h3>
      <div class="row">
        <div class="field">
          <button class="btn" @click="openLocationDialog('from')">Select From</button>
          <span v-if="fromLocation"> {{ fromLocation.name }}</span>
        </div>
        <div class="field">
          <button class="btn" @click="openLocationDialog('to')">Select To</button>
          <span v-if="toLocation"> {{ toLocation.name }}</span>
        </div>
        <label class="field">Сортировка
          <select class="input" v-model="sortParam">
            <option value="">—</option>
            <option value="name">name</option>
            <option value="distance">distance</option>
          </select>
        </label>
        <label class="field">Порядок
          <select class="input" v-model="sortOrder">
            <option value="asc">asc</option>
            <option value="desc">desc</option>
          </select>
        </label>
        <button class="btn" @click="onFindBetween">Найти</button>
      </div>
      <ul v-if="found && found.length > 0">
        <li v-for="r in found" :key="r.id">#{{ r.id }} {{ r.name }} ({{ r.distance }})</li>
      </ul>
      <div v-else-if="found">
        <p>Маршруты не найдены.</p>
      </div>
    </section>

    <section>
      <h3>Добавить маршрут между локациями</h3>
      <div class="grid">
        <label class="field">name<input class="input" v-model="newBetween.name" /></label>
        <div class="field">
          <button class="btn" @click="openLocationDialog('newFrom')">Select From</button>
          <span v-if="newBetween.fromId"> ID: {{ newBetween.fromId }}</span>
        </div>
        <div class="field">
          <button class="btn" @click="openLocationDialog('newTo')">Select To</button>
          <span v-if="newBetween.toId"> ID: {{ newBetween.toId }}</span>
        </div>
        <label class="field">distance<input class="input" type="number" min="2" v-model.number="newBetween.distance" /></label>
        <label class="field">rating<input class="input" type="number" min="1" v-model.number="(newBetween.rating as any)" /></label>
        <label class="field">coordinates.x<input class="input" type="number" v-model.number="newBetween.coordinates.x" /></label>
        <label class="field">coordinates.y<input class="input" type="number" v-model.number="newBetween.coordinates.y" /></label>
      </div>
      <button class="btn primary" @click="onAddBetween">Добавить</button>
      <div v-if="createdBetween">Создан маршрут #{{ createdBetween.id }}</div>
    </section>
  </div>
  <LocationDialog v-if="showLocationDialog" @close="showLocationDialog = false" @select="selectLocation" />
</template>

<style scoped>
.ops { display: grid; gap: 16px; }
section { border: 1px solid var(--color-border); padding: 12px; border-radius: 12px; background: var(--color-background); }
.row { display: flex; gap: 8px; align-items: center; }
.grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 8px; }
label { display: grid; gap: 4px; }
.field input.input, .field select.input { width: 100%; }
.input { padding: 8px 10px; border: 1px solid var(--color-border); border-radius: 8px; background: var(--color-background); color: var(--color-text); }
.btn { padding: 6px 10px; border: 1px solid var(--color-border); background: var(--color-background-soft); border-radius: 8px; cursor: pointer; }
.btn.primary { background: hsla(160, 100%, 37%, 0.15); border-color: hsla(160, 100%, 37%, 0.4); }
.btn.danger { background: rgba(176, 0, 32, 0.1); border-color: rgba(176, 0, 32, 0.25); }
</style>


