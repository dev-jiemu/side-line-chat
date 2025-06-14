import {createRouter, createWebHistory} from 'vue-router'
import Main from "@/views/Main.vue";
import Login from "@/views/Login.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {path: '/', name: 'home', redirect: '/main'},
        {path: '/agent', name: 'agent', redirect: '/login' },
        {path: '/main', name: 'main', component: Main },
        {path: '/login',  name: 'login', component: Login },
    ],
})

export default router