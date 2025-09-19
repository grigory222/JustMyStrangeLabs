export interface Coordinates {
  id?: number;
  x: number; // long in backend
  y: number; // Float (> -845)
}

export interface Location {
  id?: number; // if backend exposes IDs for linking existing locations
  x: number; // double
  y: number; // Integer (not null)
  z: number; // Long (not null)
  name: string; // not null
}

export interface Route {
  id: number;
  name: string;
  coordinates: Coordinates;
  creationDate: string; // ISO string
  from: Location;
  to: Location | null;
  distance: number; // > 1
  rating: number | null; // > 0 or null
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number; // current page index (0-based)
}

export type SortDirection = 'asc' | 'desc';

export interface RouteQueryParams {
  page?: number; // 0-based
  size?: number;
  sort?: string; // field name for sorting, e.g. "name"
  order?: SortDirection; // "asc" or "desc"
  nameEquals?: string; // exact match filter (OpenAPI: nameEquals)
  rating?: number; // exact match filter
}

export interface GroupByNameItem {
  name: string;
  routesCount: number; // long in backend
}


