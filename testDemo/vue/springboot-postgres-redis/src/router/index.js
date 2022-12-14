import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/Layout.vue'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect:"/login",
    children:[
      {
        path: 'home',
        name: 'Home',
        component: () => import ("@/views/Home")
      },
      {
        path: 'add',
        name: 'Add',
        component: () => import ("@/views/Add")
      },
      {
        path: 'search',
        name: 'Search',
        component: () => import ("@/views/Search")
      },
      {
        path: 'dml',
        name: 'DML',
        component: () => import ("@/views/DML")
      },
      {
        path: 'advance',
        name: 'Advance',
        component: () => import ("@/views/Advance")
      },
      {
        path: 'fileIO',
        name: 'FileIO',
        component: () => import ("@/views/FileIO")
      },
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import("@/views/Login")
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
