import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/products' },
  { path: '/login', component: () => import('@/views/Login.vue') },
  { path: '/register', component: () => import('@/views/Register.vue') },
  { path: '/products', component: () => import('@/views/Products.vue') },
  { path: '/product/:id', component: () => import('@/views/ProductDetail.vue') },
  { path: '/cart', component: () => import('@/views/Cart.vue') },
  { path: '/orders', component: () => import('@/views/Orders.vue') },
  { path: '/order/:id', component: () => import('@/views/OrderDetail.vue') },
  { path: '/address', component: () => import('@/views/Address.vue') },
  { path: '/admin/products', component: () => import('@/views/admin/ProductManage.vue') },
  { path: '/admin/categories', component: () => import('@/views/admin/CategoryManage.vue') },
  { path: '/admin/orders', component: () => import('@/views/admin/OrderManage.vue') }
]

export default createRouter({ history: createWebHistory(), routes })
