<template>
    <v-list density="compact" v-for="(item, idx) in roomList">
        <v-list-item color="primary" @click="getChat(item.room_id)">
            <v-list-item-title>{{item.room_id}}</v-list-item-title>
        </v-list-item>
    </v-list>
</template>
<script setup>
import {useUserStore} from "@/stores/user.js";
import {storeToRefs} from "pinia";

const user = useUserStore();
const { roomList } = storeToRefs(user);

const getChat = (room_id) => {

}
</script>