<template>
    <!-- 상담사는 채팅생성 버튼 안보여도 됨 -->
    <template v-if="roomInfo.roomId === '' && !isContact && !isObserver">
        <v-btn @click="createChat">
            New Chat
        </v-btn>
    </template>
    <template v-else-if="roomInfo.roomId !== ''">
        <v-card width="50%" class="ma-5 pa-5">
            <v-card-title>
                <v-row class="align-center fill-height">
                    <v-col cols="10">
                        <v-icon icon="mdi-account-outline" size="small"></v-icon>
                        [ {{ roomInfo.sender || roomInfo.sender_id }} ] is Chat Room
                    </v-col>
                    <v-col cols="2" class="text-center">
                        <v-btn v-if="roomInfo.roomType || roomInfo.room_type === 'side' && isContact " icon="mdi-trash-can-outline" variant="text" size="x-small"
                               @click="deleteSideChat(roomInfo.roomId || roomInfo.room_id)"/>
                        <v-btn icon="mdi-close" variant="text" size="x-small" @click="closeChatting"/>
                    </v-col>
                </v-row>

            </v-card-title>
            <v-divider class="pb-3 mt-1"/>
            <v-card-text class="pa-2 mt-2" style="height: 450px; overflow-y: auto;">
                <div v-for="(msg, index) in messageHistory" :key="index"
                     :class="msg.type === 'send' ? 'text-right mb-2' : 'text-left mb-2'">
                    <v-chip
                            :color="msg.type === 'send' ? 'primary' : 'success'"
                            :class="msg.type === 'send' ? 'ml-auto' : 'mr-auto'"
                            style="max-width: 70%; word-wrap: break-word;">
                        {{ msg.message }}
                        <v-btn v-if="isContact" variant="text" size="x-small" icon="mdi-close" @click="logDelete(msg.log_seqno)"/>
                    </v-chip>
                </div>
            </v-card-text>
            <v-divider/>
            <v-row class="pt-2 pb-1 mt-1 mr-2 align-center text-center">
                <v-col cols="10">
                    <v-text-field
                            prepend-icon="mdi-pencil-outline"
                            density="compact"
                            hide-details="auto"
                            variant="outlined"
                            @keyup.enter="sendMessage"
                            v-model="message"/>
                </v-col>
                <v-col cols="2" class="text-center">
                    <v-btn color="success" variant="outlined" @click="sendMessage">SEND</v-btn>
                </v-col>
            </v-row>
        </v-card>
    </template>
</template>
<script setup>

import {useUserStore} from "@/stores/user.js";
import {storeToRefs} from "pinia";
import {useChatStore} from "@/stores/chat.js";
import {onMounted, onUnmounted, ref} from "vue";
import {useRoomStore} from "@/stores/room.js";

const user = useUserStore();
const { userInfo, isContact, isObserver } = storeToRefs(user);

const chat = useChatStore();
const { roomInfo, messageHistory } = storeToRefs(chat);

const room = useRoomStore();

const message = ref('')

const createChat = () => {
    // console.log(userInfo.value)

    // userInfo 정보가 없으면 일반 고객으로 간주함 (상담사, 옵저버는 로그인 해야함)
    if (!userInfo.value.userId) {
        userInfo.value.userId = user.randomUserIdForCustomer()
    }

    chat.createChatRoom(userInfo.value.userId, (roomId) => {
        // 방 생성 완료되면 websocket connnect
        chat.connectWebSocket(roomId, userInfo.value.userId)
    }, (error) => {
        alert('채팅 생성중 오류가 발생했습니다. : ', error)
    })
}

const sendMessage = () => {
    if (!message.value.trim()) return; // 빈 메시지 방지
    
    console.log('sendMessage : ', message.value)
    chat.sendMessage(message.value)
    message.value = '' // 메시지 전송 후 초기화
}

const closeChatting = () => {
    chat.disconnect()
    messageHistory.value = []

    // 화면에서 제거
    roomInfo.value.roomId = ""
}

const logDelete = (logSeqno) => {
    // TODO : 채팅 데이터 삭제도 필요하려나...?
}

const deleteSideChat = (roomId) => {
    console.log(roomId)
    room.deleteRoom(roomId, () =>{
        // TODO : next job?
    }, () => {})
}

onMounted(() => {
    // console.log(JSON.stringify(userInfo.value))
})

onUnmounted(() => {
    chat.disconnect()
})
</script>