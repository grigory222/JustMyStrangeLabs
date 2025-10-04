import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import * as authService from '@/services/auth';
import type { LoginRequest, RegisterRequest, AuthResponse } from '@/types/models';

export const useAuthStore = defineStore('auth', () => {
  const user = ref<AuthResponse | null>(null);
  const isAuthenticated = computed(() => !!user.value);
  const roles = computed(() => user.value?.roles || []);

  async function login(username: LoginRequest['username'], password: LoginRequest['password']) {
    try {
      const response = await authService.login({ username, password });
      user.value = response.data;
    } catch (error: any) {
      user.value = null;
      throw new Error(error.response?.data?.message || 'Login failed');
    }
  }

  async function register(username: RegisterRequest['username'], password: RegisterRequest['password']) {
    try {
      await authService.register({ username, password });
      // Optionally log in the user directly after registration
      await login(username, password);
    } catch (error: any) {
      user.value = null;
      throw new Error(error.response?.data?.message || 'Registration failed');
    }
  }

  async function logout() {
    try {
      await authService.logout();
    } finally {
      user.value = null;
    }
  }

  async function checkAuth() {
    try {
      const response = await authService.getMe();
      user.value = response.data;
    } catch (error) {
      user.value = null;
    }
  }

  return {
    user,
    isAuthenticated,
    roles,
    login,
    register,
    logout,
    checkAuth,
  };
}, {
  persist: true, // Using pinia-plugin-persistedstate
});
