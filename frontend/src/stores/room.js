import {defineStore} from "pinia";
import {ref} from "vue";
import roomApi from '@/api/room.js'

export const useRoomStore = defineStore('rooms', () => {
    const roomHierarchy = ref([])
    const selectRoom = ref() // 선택한 room info (log delete or room delete request?)

    const getRoomList = (userId) => {
        roomApi.getRoomList(userId, (data) => {
            roomHierarchy.value = []

            for (let room of data) {
                roomHierarchy.value.push(room)
            }
        }, (error) => {
            console.error(error);
            roomHierarchy.value = []
        })
    }

    const getRoom = (roomId, scb) => {
        roomApi.getRoom(roomId, (roomInfo) => {
            selectRoom.value = Object.assign(roomInfo, {})
            console.log(selectRoom.value)
            if (scb) {
                scb()
            }
        }, (error) => {
            console.error(error);
            selectRoom.value = {}
        })
    }

    const deleteRoom = (roomId, scb, fcb) => {
        roomApi.deleteRoom(roomId, () => {
            if(scb) {
                scb()
            }
        }, () => {
            if (fcb) {
                fcb()
            }
        })
    }

    return {
        roomHierarchy, selectRoom,
        getRoomList, getRoom, deleteRoom
    }
})