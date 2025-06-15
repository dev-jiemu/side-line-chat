<template>
    <v-container class="justify-center align-center fill-height d-flex">
        <v-card
                width="400px"
                flat
                elevation="2"
        >
            <v-card-title class="text-center justify-center align-center mt-5">
                Agent Login
            </v-card-title>
            <v-card-text class="text-center">
                <v-form class="mt-5 ml-6 mr-6">
                    <v-text-field
                            v-model="userId"
                            type="text"
                            variant="outlined"
                            placeholder="id"
                            prepend-inner-icon="mdi-account-outline"
                            density="compact"
                    >
                    </v-text-field>
                    <v-text-field
                            type="password"
                            v-model="password"
                            density="compact"
                            placeholder="password"
                            prepend-inner-icon="mdi-lock-outline"
                            variant="outlined"
                            @keydown.enter="doLogin"
                    ></v-text-field>
                </v-form>
            </v-card-text>
            <v-card-text class="text-center">
                <v-btn
                        width="85%"
                        size="large"
                        class="mb-4"
                        variant="outlined"
                        color="indigo-darken-2"
                        @click="doLogin"
                >
                    LOGIN
                </v-btn>
            </v-card-text>
        </v-card>
    </v-container>
</template>
<script setup>
import {useUserStore} from "@/stores/user.js";
import {ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();

const user = useUserStore();

const userId = ref('')
const password = ref('')

const doLogin = () => {
    if (userId.value === '') {
        alert('아이디를 입력해주세요')
        return
    }

    if (password.value === '') {
        alert('패스워드를 입력해주세요')
        return
    }

    user.login(userId.value, password.value, () => {
        console.log('Login Success')
        router.push('/')
    }, () => {
        alert('아이디 또는 비밀번호를 확인해주세요.')
    })
}
</script>
