<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import { ref, onMounted } from 'vue'

const navRef = ref<HTMLElement | null>(null)
const glowX = ref(0)
const glowW = ref(0)
const glowVisible = ref(false)

function moveGlowTo(el: HTMLElement) {
  const nav = navRef.value
  if (!nav) return
  const navRect = nav.getBoundingClientRect()
  const r = el.getBoundingClientRect()
  const centerX = r.left - navRect.left + r.width / 2
  glowX.value = centerX
  glowW.value = Math.max(80, Math.min(220, r.width + 40))
  glowVisible.value = true
  nav.style.setProperty('--glow-x', `${glowX.value}px`)
  nav.style.setProperty('--glow-w', `${glowW.value}px`)
  nav.classList.add('glow-on')
}

function onLeaveNav() {
  const nav = navRef.value
  if (!nav) return
  glowVisible.value = false
  nav.classList.remove('glow-on')
}

onMounted(() => {
  // initialize center
  const nav = navRef.value
  if (!nav) return
  nav.style.setProperty('--glow-x', `0px`)
  nav.style.setProperty('--glow-w', `120px`)
})
</script>

<template>
  <header class="app-header">
    <div class="brand">
      <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="40" height="40" />
      <span class="title">Routes</span>
    </div>
    <nav ref="navRef" class="top-nav" @mouseleave="onLeaveNav">
      <span class="nav-glow" aria-hidden="true"></span>
      <RouterLink to="/" @mouseenter="(e:any)=>moveGlowTo(e.currentTarget)">Главная</RouterLink>
      <RouterLink to="/routes" @mouseenter="(e:any)=>moveGlowTo(e.currentTarget)">Маршруты</RouterLink>
      <RouterLink to="/ops" @mouseenter="(e:any)=>moveGlowTo(e.currentTarget)">Операции</RouterLink>
      <RouterLink to="/about" @mouseenter="(e:any)=>moveGlowTo(e.currentTarget)">О проекте</RouterLink>
    </nav>
  </header>

  <RouterView />
</template>

<style scoped>
.app-header {
  position: sticky;
  top: 0;
  z-index: 20;
  line-height: 1.5;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: var(--color-background);
  border-bottom: 1px solid var(--color-border);
}

.brand { display: flex; align-items: center; gap: 10px; }
.title { font-size: 20px; font-weight: 600; }

.top-nav { font-size: 16px; display: flex; gap: 12px; }
.top-nav { position: relative; align-items: center; }
.top-nav .nav-glow {
  position: absolute;
  top: 50%;
  left: 0;
  width: var(--glow-w, 120px);
  height: 52px;
  transform: translateX(calc(var(--glow-x, 0px) - var(--glow-w, 120px) / 2)) translateY(-50%);
  pointer-events: none;
  border-radius: 999px;
  background: radial-gradient(ellipse at center,
    rgba(100, 200, 180, 0.46) 0%,
    rgba(100, 200, 180, 0.34) 30%,
    rgba(100, 200, 180, 0.24) 55%,
    rgba(100, 200, 180, 0.12) 78%,
    rgba(100, 200, 180, 0) 95%);
  filter: blur(14px);
  opacity: 0;
  will-change: transform, width, opacity;
  transition:
    transform 350ms ease,
    width 350ms ease,
    opacity 250ms ease;
}
.top-nav.glow-on .nav-glow { opacity: 1; }

.top-nav a.router-link-exact-active { color: hsla(160, 100%, 37%, 1); }

.top-nav a.router-link-exact-active:hover { background-color: transparent; }

.top-nav a {
  position: relative;
  display: inline-block;
  padding: 8px 12px;
  border-radius: 10px;
  background: transparent;
  transition: text-shadow 1.6s cubic-bezier(.22,.61,.36,1), color 1.2s ease, background 1.2s ease;
}
.top-nav a:hover {
  background: transparent !important;
  text-shadow:
    0 0 8px rgba(100, 200, 180, 0.85),
    0 0 28px rgba(100, 200, 180, 0.70),
    0 0 80px rgba(100, 200, 180, 0.55),
    0 0 160px rgba(100, 200, 180, 0.40),
    0 0 260px rgba(100, 200, 180, 0.30);
}

/* Active route indicator */
.top-nav a.router-link-exact-active {
  color: hsla(160, 100%, 37%, 1);
  text-shadow:
    0 0 6px rgba(100, 200, 180, 0.55),
    0 0 18px rgba(100, 200, 180, 0.35);
}
.top-nav a.router-link-exact-active::after {
  content: '';
  position: absolute;
  left: 50%;
  bottom: -6px;
  transform: translateX(-50%);
  width: 60%;
  height: 3px;
  border-radius: 999px;
  background: radial-gradient(ellipse at center, rgba(100, 200, 180, 0.7), rgba(100, 200, 180, 0) 70%);
  filter: blur(2px);
}

.top-nav a:first-of-type { border: 0; }

@media (min-width: 1024px) {
  .app-header { padding-right: calc(var(--section-gap) / 2); }
}
</style>
