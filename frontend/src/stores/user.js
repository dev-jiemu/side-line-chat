import {defineStore} from "pinia";
import {computed, ref} from "vue";
import userApi from '@/api/user.js'

export const useUserStore = defineStore('user', () => {
    const userInfo = ref({
        userId: '',
        authType: ''
    })

    const login = (userId, password, scb, fcb) => {
        let loginRequest = {
            user_id: userId,
            password: password,
        }

        userApi.login(loginRequest, (data) => {
            let { auth_type } = data;

            userInfo.value.userId = userId;
            userInfo.value.authType = auth_type;

            if (scb) {
                scb()
            }
        }, (err) => {
            console.error('login error : ', err)
            if (fcb) {
                fcb()
            }
        })

    }

    const isContact = computed(() => {
        return userInfo.value.authType === 'contact'
    })

    const isObserver = computed(() => {
        return userInfo.value.authType === 'observer'
    })

    const randomUserIdForCustomer = () => {
        const characters = 'abcdefghijklmnopqrstuvwxyz0123456789';
        let result = '';

        for (let i = 0; i < 12; i++) {
            result += characters.charAt(Math.floor(Math.random() * characters.length));
        }

        return result
    }

    return {
        userInfo, login, isContact, isObserver,
        randomUserIdForCustomer,
    }
})