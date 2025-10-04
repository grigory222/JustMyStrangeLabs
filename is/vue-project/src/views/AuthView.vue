<template>
  <div class="auth-container">
    <div class="auth-form">
      <h2>{{ isLogin ? 'Login' : 'Register' }}</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="username">Username</label>
          <input type="text" id="username" v-model="username" required />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" id="password" v-model="password" required />
        </div>
        <div v-if="error" class="error-message">{{ error }}</div>
        <button type="submit" class="submit-btn">{{ isLogin ? 'Login' : 'Register' }}</button>
      </form>
      <p class="toggle-form">
        {{ isLogin ? "Don't have an account?" : "Already have an account?" }}
        <a href="#" @click.prevent="isLogin = !isLogin">{{ isLogin ? 'Register' : 'Login' }}</a>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useAuthStore } from '@/store/authStore';
import { useRouter } from 'vue-router';

const isLogin = ref(true);
const username = ref('');
const password = ref('');
const authStore = useAuthStore();
const router = useRouter();
const error = ref<string | null>(null);

const handleSubmit = async () => {
  error.value = null;
  try {
    if (isLogin.value) {
      await authStore.login(username.value, password.value);
    } else {
      await authStore.register(username.value, password.value);
    }
    router.push('/');
  } catch (err: any) {
    error.value = err.message || 'An error occurred';
  }
};
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: var(--color-background);
}

.auth-form {
  background: var(--color-background-soft);
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
  text-align: center;
}

h2 {
  margin-bottom: 1.5rem;
  color: var(--color-heading);
}

.form-group {
  margin-bottom: 1rem;
  text-align: left;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: var(--color-text);
}

input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  box-sizing: border-box;
  background-color: var(--color-background-mute);
  color: var(--color-text);
}

.submit-btn {
  width: 100%;
  padding: 0.75rem;
  background-color: hsla(160, 100%, 37%, 1);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s;
}

.submit-btn:hover {
  background-color: hsla(160, 100%, 37%, 0.8);
}

.toggle-form {
  margin-top: 1rem;
  color: var(--color-text);
}

.toggle-form a {
  color: hsla(160, 100%, 37%, 1);
  text-decoration: none;
}

.toggle-form a:hover {
  text-decoration: underline;
}

.error-message {
  color: red;
  margin-bottom: 1rem;
}
</style>
