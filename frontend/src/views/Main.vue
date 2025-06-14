<template>
    <v-app-bar height="70">
        <v-app-bar-nav-icon v-model="layout" @click="updateLayout" variant="text" style="opacity:0.5;"></v-app-bar-nav-icon>
        <v-toolbar-title>
            <h3 @click="router.push('/')">One on One Chatting</h3>
        </v-toolbar-title>
        <!-- TODO : check -->
        <v-navigation-drawer v-if="isContact">
            <chatting-list/>
        </v-navigation-drawer>
    </v-app-bar>
    <v-main class="mt-2 ml-5 mr-5 mb-10">
        <v-container class="text-center align-center d-flex fill-height justify-center">
            <router-view/>
        </v-container>
    </v-main>
</template>
<script setup>
import {ref} from "vue";
import {useRouter} from "vue-router";
import {useUserStore} from "@/stores/user.js";
import {storeToRefs} from "pinia";
import ChattingList from "@/components/ChattingList.vue";

const user = useUserStore();
const { isContact } = storeToRefs(user);

const layout = ref(true)
const router = useRouter();

const updateLayout = () => {
    layout.value = !layout.value;
}
</script>