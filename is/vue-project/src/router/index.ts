import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import { useAuthStore } from '@/store/authStore';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: true }
    },
    {
      path: '/routes',
      name: 'routes',
      component: () => import('../views/RoutesView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/auth',
      name: 'auth',
      component: () => import('../views/AuthView.vue'),
      meta: { guest: true }
    },
    {
      path: '/special',
      name: 'special',
      component: () => import('../views/SpecialOpsView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/import',
      name: 'import',
      component: () => import('../views/ImportView.vue'),
      meta: { requiresAuth: true }
    }
  ]
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  if (!authStore.isAuthenticated && authStore.user === null) {
    authStore.checkAuth().then(() => {
      handleNavigation(to, from, next);
    });
  } else {
    handleNavigation(to, from, next);
  }
});

function handleNavigation(to: any, from: any, next: any) {
  const authStore = useAuthStore();
  const requiresAuth = to.matched.some((record: any) => record.meta.requiresAuth);
  const guest = to.matched.some((record: any) => record.meta.guest);
  const requiredRoles = to.meta.roles as string[] | undefined;

  if (requiresAuth && !authStore.isAuthenticated) {
    next('/auth');
  } else if (guest && authStore.isAuthenticated) {
    next('/');
  } else if (requiredRoles && !requiredRoles.some(role => authStore.roles.includes(role))) {
    next('/'); // Access denied - redirect to home
  }
  else {
    next();
  }
}

export default router
