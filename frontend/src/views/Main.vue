<template>
    <v-app-bar height="70">
        <v-app-bar-nav-icon variant="text" style="opacity:0.5;"></v-app-bar-nav-icon>
        <v-toolbar-title>
            <h3 @click="router.push('/')">One on One Chatting
                <span v-if="isContact || isObserver">[ {{userInfo.userId}} ]</span>
            </h3>
        </v-toolbar-title>
    </v-app-bar>

    <v-navigation-drawer v-if="isContact || isObserver" permanent>
        <chatting-list/>
    </v-navigation-drawer>

    <v-main class="mt-2 ml-5 mr-5 mb-10">
        <v-container class="text-center align-center d-flex fill-height justify-center">
            <router-view/>
        </v-container>
    </v-main>
</template>
<script setup>
import {useUserStore} from "@/stores/user.js";
import {storeToRefs} from "pinia";
import ChattingList from "@/components/ChattingList.vue";
import {useRouter} from "vue-router";

const user = useUserStore();
const { userInfo, isContact, isObserver } = storeToRefs(user);

const router = useRouter();
</script>