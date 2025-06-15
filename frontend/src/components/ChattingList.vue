<template>
    <div class="chatting-list">
        <!-- 헤더 -->
        <v-list-subheader class="px-4 py-3 font-weight-bold">
            <v-icon icon="mdi-chat" class="mr-2"></v-icon>
            채팅방 목록
            <v-btn
                    icon="mdi-refresh"
                    size="x-small"
                    variant="text"
                    @click="refreshRoomList">
            </v-btn>
        </v-list-subheader>
        
        <v-divider></v-divider>

        <!-- MAIN 룸들 순회 -->
        <v-list density="compact">
            <div v-for="mainRoom in roomHierarchy" :key="mainRoom.room_id">
                <!-- MAIN 룸 렌더링 -->
                <v-list-item 
                    @click="getChat(mainRoom.room_id)"
                    class="room-item"
                    :class="{ 'selected': selectRoom?.room_id === mainRoom.room_id }"
                >
                    <template v-slot:prepend>
                        <v-icon 
                            icon="mdi-account-circle" 
                            color="primary" 
                            size="small"
                            class="mr-3"
                        ></v-icon>
                    </template>
                    
                    <v-list-item-title class="font-weight-medium">
                        Room {{ mainRoom.room_id }}
                    </v-list-item-title>
                    
                    <v-list-item-subtitle class="text-caption">
                        {{ mainRoom.sender_id }}
                    </v-list-item-subtitle>

                    <template v-slot:append>
                        <div class="d-flex align-center gap-2">
                            <v-chip
                                    size="x-small"
                                    color="primary"
                                    variant="outlined"
                            >
                                MAIN
                            </v-chip>
                            <!-- 옵저버일 때만 사이드 채팅 생성 버튼 표시 -->
                            <v-btn
                                    v-if="isObserver"
                                    icon="mdi-plus"
                                    size="x-small"
                                    variant="text"
                                    color="orange-darken-2"
                                    @click.stop="createSideChat(mainRoom.room_id)"
                            >
                            </v-btn>
                        </div>
                    </template>
                </v-list-item>

                <!-- 이 MAIN 룸의 SIDE 룸들 렌더링 -->
                <div v-for="sideRoom in (mainRoom.side_rooms || [])" :key="sideRoom.room_id" class="side-room">
                    <v-list-item 
                        @click="getChat(sideRoom.room_id)"
                        class="room-item"
                        :class="{ 'selected': selectRoom?.room_id === sideRoom.room_id }"
                    >
                        <template v-slot:prepend>
                            <div class="d-flex align-center">
                                <div class="side-connector"></div>
                                <v-icon 
                                    icon="mdi-eye" 
                                    color="orange-darken-2"
                                    size="small"
                                    class="mr-3"
                                ></v-icon>
                            </div>
                        </template>
                        
                        <v-list-item-title class="font-weight-medium">
                            Room {{ sideRoom.room_id }}
                        </v-list-item-title>
                        
                        <v-list-item-subtitle class="text-caption">
                            {{ sideRoom.sender_id }}
                        </v-list-item-subtitle>
                        
                        <template v-slot:append>
                            <v-chip 
                                size="x-small" 
                                color="orange-darken-2" 
                                variant="outlined"
                            >
                                SIDE
                            </v-chip>
                        </template>
                    </v-list-item>
                </div>
            </div>
        </v-list>
        
        <!-- 빈 상태 -->
        <div v-if="roomHierarchy.length === 0" class="empty-state pa-4 text-center">
            <v-icon icon="mdi-chat-outline" size="48" color="grey-lighten-1" class="mb-2"></v-icon>
            <div class="text-body-2 text-grey-darken-1">
                아직 채팅방이 없습니다
            </div>
        </div>
    </div>
</template>

<script setup>
import {storeToRefs} from "pinia";
import {useRoomStore} from "@/stores/room.js";
import {onMounted} from "vue";
import {useChatStore} from "@/stores/chat.js";
import {useUserStore} from "@/stores/user.js";

const room = useRoomStore()
const { roomHierarchy, selectRoom } = storeToRefs(room);

const chat = useChatStore();
const { roomInfo } = storeToRefs(chat)

const user = useUserStore()
const { userInfo, isObserver } = storeToRefs(user);

const getChat = (room_id) => {
    try {
        console.log('get chat : ', room_id);
        
        if (!room_id) {
            console.error('room_id is required');
            return;
        }
        
        room.getRoom(room_id, () => {
            if (selectRoom.value && selectRoom.value.room_id) {
                chat.connectWebSocket(room_id, userInfo.value.userId);
                roomInfo.value = Object.assign(selectRoom.value, {})
            }
        });
        
    } catch (error) {
        console.error('Error in getChat:', error);
    }
}

const createSideChat = (mainRoomId) => {
    chat.createSideChatRoom(userInfo.value.userId, mainRoomId, (roomId) => {
        console.log('sideChat new roomId : ', roomId)
        chat.connectWebSocket(roomId, userInfo.value.userId)
    }, (error) => {
        alert('사이드 채팅 생성 중 오류가 발생했습니다 : ', error)
    })
}

onMounted(() => {
    console.log('chatting List userId : ', userInfo.value.userId)

    if (roomHierarchy.value.length === 0) {
        room.getRoomList(userInfo.value.userId)
    }
})

// TODO : 사실 SSE 로 서버에서 클라이언트한테 내려주는게 더 깔끔할텐데..ㅠ
const refreshRoomList = () => {
    room.getRoomList(userInfo.value.userId)
}
</script>

<style scoped>
.chatting-list {
    height: 100%;
    overflow-y: auto;
}

.room-item {
    cursor: pointer;
    transition: background-color 0.2s;
    border-radius: 8px;
    margin: 2px 8px;
}

.room-item:hover {
    background-color: rgba(0, 0, 0, 0.04);
}

.room-item.selected {
    background-color: rgba(25, 118, 210, 0.08);
    border-left: 3px solid #1976d2;
}

.side-room {
    margin-left: 16px;
    position: relative;
}

.side-connector {
    width: 20px;
    height: 1px;
    background-color: #ccc;
    margin-right: 8px;
    position: relative;
}

.side-connector::before {
    content: '';
    position: absolute;
    left: 0;
    top: -10px;
    width: 1px;
    height: 20px;
    background-color: #ccc;
}

.empty-state {
    margin-top: 40px;
}
</style>