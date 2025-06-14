import {createRouter, createWebHistory} from 'vue-router'
import Main from "@/views/Main.vue";
import Login from "@/views/Login.vue";
import Room from "@/components/Room.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {path: '/', name: 'home', redirect: '/chat'},
        {path: '/agent', name: 'agent', redirect: '/login' },
        {path: '/login',  name: 'login', component: Login },
        {
            path: '/chat',
            component: Main,
            children: [
                {
                    path: '',
                    name: 'chat_main',
                    component: Room,
                }
            ]
        }
    ],
})

export default router