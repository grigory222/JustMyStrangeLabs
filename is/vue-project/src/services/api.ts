import axios from 'axios';
import type { AxiosInstance } from 'axios';
import type { Page, Route, RouteQueryParams, GroupByNameItem, Location, Coordinates } from '@/types/models';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

class ApiService {
  private http: AxiosInstance;

  constructor() {
    this.http = axios.create({
      baseURL: API_BASE_URL,
      headers: { 'Content-Type': 'application/json' },
    });
  }

  public async getRoutes(params: RouteQueryParams = {}): Promise<Page<Route>> {
    const response = await this.http.get<Page<Route>>('/routes', { params });
    return response.data;
  }

  public async getRouteById(id: number): Promise<Route> {
    const response = await this.http.get<Route>(`/routes/${id}`);
    return response.data;
  }

  public async createRoute(payload: Omit<Route, 'id' | 'creationDate'>): Promise<Route> {
    const response = await this.http.post<Route>('/routes', payload);
    return response.data;
  }

  public async updateRoute(id: number, payload: Partial<Omit<Route, 'id' | 'creationDate'>>): Promise<Route> {
    const response = await this.http.patch<Route>(`/routes/${id}`, payload);
    return response.data;
  }

  public async deleteRoute(id: number): Promise<void> {
    await this.http.delete(`/routes/${id}`);
  }

  public async getLocations(): Promise<Location[]> {
    const response = await this.http.get<Location[]>('/locations');
    return response.data;
  }

  public async getCoordinates(): Promise<Coordinates[]> {
    const response = await this.http.get<Coordinates[]>('/coordinates');
    return response.data;
  }

  // Special operations
  public async deleteAllByRating(rating: number): Promise<number> {
    const response = await this.http.delete<{ deleted: number }>(`/routes/by-rating`, { params: { rating } });
    return response.data.deleted; // number of deleted
  }

  public async deleteAnyByRating(rating: number): Promise<Route | null> {
    const response = await this.http.delete<Route | null>(`/routes/one-by-rating`, { params: { rating } });
    return response.data;
  }

  public async groupByName(): Promise<GroupByNameItem[]> {
    const response = await this.http.get<GroupByNameItem[]>('/routes/group-by-name');
    return response.data;
  }

  public async findRoutesBetween(params: { fromId?: number; toId?: number; sort?: string; order?: 'asc' | 'desc' }): Promise<Route[]> {
    const response = await this.http.get<Page<Route>>('/routes/between', { params });
    return response.data.content;
  }

  public async addRouteBetween(payload: {
    name: string;
    fromId: number;
    toId: number;
    distance: number;
    rating?: number | null;
    coordinates: { x: number; y: number };
  }): Promise<Route> {
    const response = await this.http.post<Route>('/routes/add-between', payload);
    return response.data;
  }
}

export const api = new ApiService();


