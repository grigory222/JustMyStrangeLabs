import { api } from './api';
import type { LoginRequest, RegisterRequest } from '@/types/models';

export const login = (data: LoginRequest) => {
  return api.http.post('/auth/login', data);
};

export const register = (data: RegisterRequest) => {
  return api.http.post('/auth/register', data);
};

export const logout = () => {
  return api.http.post('/auth/logout');
};

export const getMe = () => {
  return api.http.get('/auth/me');
};
