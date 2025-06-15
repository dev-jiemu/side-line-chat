import {defineStore} from "pinia";
import {ref} from "vue";
import roomApi from '@/api/room';

export const useChatStore = defineStore('chat', () => {

    const roomInfo = ref({
        roomId: '',
        sender: '',
        target: '',
        roomType: '',
    })

    const messageHistory = ref([])

    let socket = null
    const isConnected = ref(false)

    const createChatRoom = (userId, scb, fcb) => {
        let param = {
            target: 'contactor', // TODO : 일단 상담원 fix
            sender: userId,
            room_type: 'main'
        }
        
        roomApi.createRoom(param, (result) => {
            let {room_id} = result
            roomInfo.value.roomId = room_id
            roomInfo.value.sender = userId
            roomInfo.value.target = 'contactor'
            roomInfo.value.roomType = param.room_type
            if (scb) {
                scb(room_id)
            }
        }, (error) => {
            if (fcb) {
                fcb(error)
            }
        })
    }

    const createSideChatRoom = (userId, roomNo, scb, fcb) => {
        let param = {
            target: 'contactor', // TODO : 일단 상담원 fix
            sender: userId,
            main_room_id: roomNo,
            room_type: 'side'
        }

        roomApi.createSideChatRoom(param, (result) => {
            let {room_id} = result
            roomInfo.value.roomId = room_id
            roomInfo.value.sender = userId
            roomInfo.value.target = 'contactor'
            roomInfo.value.roomType = param.room_type
            if (scb) {
                scb(room_id)
            }
        }, (error) => {
            if (fcb) {
                fcb(error)
            }
        })
    }

    const connectWebSocket = async (roomId, userId) => {
        messageHistory.value = []

        roomApi.getLogList(roomId, (logData) => {
            messageHistory.value = logData.map(log => ({
                type: log.sender === userId ? 'send' : 'receive',
                message: log.message,
                sender: log.sender,
                timestamp: log.send_at,
                log_seqno: log.log_seqno,
            }))
        }, () => {})

        socket = new WebSocket(`ws://localhost:8080/ws/chat?room_id=${roomId}&user_id=${userId}`)

        socket.onopen = () => {
            console.log('websocket connected')
            isConnected.value = true
        }

        socket.onmessage = (event) => {
            console.log('receive message : ', event.data)
            const data = JSON.parse(event.data)

            let obj = {
                type: 'receive',
                message: data.message,
            }

            messageHistory.value.push(obj)
        }

        socket.onerror = (error) => {
            console.error('WebSocket error:', error)
            isConnected.value = false
        }

        socket.onclose = () => {
            console.log('websocket closed')
            isConnected.value = false
        }
    }

    const sendMessage = (message, fcb) => {
        if (isConnected.value) {
            socket.send(JSON.stringify({ message }))

            let obj = {
                type: 'send',
                message: message,
            }
            messageHistory.value.push(obj)
        } else {
            if (fcb) {
                fcb()
            }
        }
    }

    const disconnect = () => {
        if (socket) {
            socket.close()
            socket = null
            isConnected.value = false
        }
    }

    return {
        roomInfo,
        messageHistory,
        createChatRoom,
        createSideChatRoom,
        connectWebSocket,
        sendMessage,
        disconnect,
    }
})