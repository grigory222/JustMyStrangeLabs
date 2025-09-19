import { reactive, computed } from 'vue';
import { api } from '@/services/api';
import { wsService } from '@/services/ws';
import type { Page, Route, RouteQueryParams } from '@/types/models';

interface RoutesState {
  page: Page<Route> | null;
  loading: boolean;
  error: string | null;
  query: Required<Pick<RouteQueryParams, 'page' | 'size'>> & Omit<RouteQueryParams, 'page' | 'size'>;
}

const state = reactive<RoutesState>({
  page: null,
  loading: false,
  error: null,
  query: { page: 0, size: 10 },
});

function parseWsMessage(data: any): { type: 'created' | 'updated' | 'deleted'; id: number } | null {
  try {
    // The backend sends: { type: "created"|"updated"|"deleted", id: number }
    if (data && data.type && data.id) {
      return data;
    }
  } catch {
    // ignore
  }
  return null;
}

wsService.connect();
wsService.subscribe((data) => {
  const msg = parseWsMessage(data);
  if (!msg) return;
  // Refresh current page to reflect live updates
  void fetchRoutes();
});

export async function fetchRoutes(extra?: Partial<RouteQueryParams>): Promise<void> {
  state.loading = true;
  state.error = null;
  try {
    if (extra) {
      state.query = { ...state.query, ...extra } as any;
    }
    // Преобразуем rating в число, если оно строка
    const query = { ...state.query };
    if (typeof query.rating === 'string') {
      query.rating = query.rating ? Number(query.rating) : undefined;
    }
    const page = await api.getRoutes(query);
    state.page = page;
  } catch (e: any) {
    state.error = e?.message || 'Failed to load routes';
  } finally {
    state.loading = false;
  }
}

export async function createRoute(payload: Omit<Route, 'id' | 'creationDate'>): Promise<Route> {
  // Валидация: name у from и to
  if (!payload.from?.name || payload.from.name.trim() === '') {
    throw new Error('Location "from" name must not be empty');
  }
  if (payload.to && (!payload.to.name || payload.to.name.trim() === '')) {
    throw new Error('Location "to" name must not be empty');
  }
  const created = await api.createRoute(payload);
  await fetchRoutes();
  return created;
}

export async function updateRoute(id: number, payload: Partial<Omit<Route, 'id' | 'creationDate'>>): Promise<Route> {
  // Валидация: name у from и to, если они есть в payload
  if (payload.from && (!payload.from.name || payload.from.name.trim() === '')) {
    throw new Error('Location "from" name must not be empty');
  }
  if (payload.to && (!payload.to.name || payload.to.name.trim() === '')) {
    throw new Error('Location "to" name must not be empty');
  }
  const updated = await api.updateRoute(id, payload);
  await fetchRoutes();
  return updated;
}

export async function deleteRoute(id: number): Promise<void> {
  await api.deleteRoute(id);
  await fetchRoutes();
}

export const routesState = state;
export const routes = computed(() => state.page?.content ?? []);
export const totalPages = computed(() => state.page?.totalPages ?? 0);


