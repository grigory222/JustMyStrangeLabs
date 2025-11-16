import axios from 'axios';
import type { AxiosInstance } from 'axios';
import type { Page, Route, RouteQueryParams, GroupByNameItem, Location, Coordinates } from '@/types/models';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

class ApiService {
  public http: AxiosInstance;

  constructor() {
    this.http = axios.create({
      baseURL: API_BASE_URL,
      headers: { 'Content-Type': 'application/json' },
      withCredentials: true, // Включаем отправку cookies для сессий
    });

    // Добавляем перехватчик для обработки 403 ошибок
    this.http.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 403) {
          // Сессия истекла или пользователь не авторизован
          console.warn('Session expired or unauthorized. Redirecting to login...');
          
          // Очищаем localStorage от сохраненного состояния
          localStorage.removeItem('auth');
          
          // Используем window.location для редиректа, чтобы избежать проблем с циклическими импортами
          window.location.href = '/auth';
        }
        return Promise.reject(error);
      }
    );
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

  // Import operations
  public async importRoutes(file: File): Promise<{ id: number; addedCount: number }> {
    const formData = new FormData();
    formData.append('file', file);
    const response = await this.http.post<{ id: number; addedCount: number }>('/import', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
  }

  public async getImportHistory(): Promise<any[]> {
    const response = await this.http.get<any[]>('/import/history');
    return response.data;
  }

  public async downloadImportFile(importId: number): Promise<any> {
    const response = await this.http.get(`/import/${importId}/download`, {
      responseType: 'blob'
    });
    return response;
  }

  // Admin operations
  public async getAdminStatus(): Promise<{ minioFailureSimulation: boolean; databaseFailureSimulation: boolean; businessErrorSimulation: boolean }> {
    const response = await this.http.get('/admin/status');
    return response.data;
  }

  public async toggleMinio(simulate: boolean): Promise<void> {
    await this.http.post(`/admin/minio/toggle?simulate=${simulate}`);
  }

  public async toggleDatabase(simulate: boolean): Promise<void> {
    await this.http.post(`/admin/database/toggle?simulate=${simulate}`);
  }

  public async toggleBusinessError(simulate: boolean): Promise<void> {
    await this.http.post(`/admin/business-error/toggle?simulate=${simulate}`);
  }
}

export const api = new ApiService();


