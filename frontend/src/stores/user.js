import {defineStore} from "pinia";
import {computed, ref} from "vue";
import userApi from '@/api/user.js'

export const useUserStore = defineStore('user', () => {
    const roomList = ref([])

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
            // TODO : Auth type setting
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
        return userInfo.value.authType === 'observer' || userInfo.value.authType === 'contact'
    })

    const randomUserIdForCustomer = () => {
        const characters = 'abcdefghijklmnopqrstuvwxyz0123456789';
        let result = '';

        for (let i = 0; i < length; i++) {
            result += characters.charAt(Math.floor(Math.random() * characters.length));
        }

        return result
    }

    return {
        roomList, userInfo, login, isContact,
        randomUserIdForCustomer,
    }
})