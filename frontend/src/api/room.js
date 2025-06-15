import axios from 'axios'

const baseUrl = import.meta.env.VITE_API_BASE_URL

export default {
    createRoom(req, scb, fcb) {
        let reqUrl = '/room'

        axios.post(baseUrl + reqUrl, req, {
            headers: {
                "Content-Type": "application/json",
            }
        }).then((response) => {
            console.log(response.data.data)
            if (response.status === 200) {
                if (scb) {
                    scb(response.data.data) // room id 반환됨
                }
            } else {
                if (fcb) {
                    fcb(response.data)
                }
            }
        }).catch((error) => {
            console.error('axios error : ', error)
        })
    },
    getRoomList(userId, scb, fcb) {
        let reqUrl = `/room/${userId}/list`

        axios.get(baseUrl + reqUrl, {
            headers: {
                "Content-Type": "application/json",
            }
        }).then((response) => {
            console.log(response.data.data)
            if (response.status === 200) {
                if (scb) {
                    scb(response.data.data)
                }
            } else {
                if (fcb) {
                    fcb(response.data)
                }
            }
        }).catch((error) => {
            console.error('axios error : ', error)
        })
    },
    getRoom(seqno, scb, fcb) {
        let reqUrl = `/room/${seqno}`

        axios.get(baseUrl + reqUrl, {
            headers: {
                "Content-Type": "application/json",
            }
        }).then((response) => {
            console.log(response.data.data)
            if (response.status === 200) {
                if (scb) {
                    scb(response.data.data)
                }
            } else {
                if (fcb) {
                    fcb(response.data)
                }
            }
        }).catch((error) => {
            console.error('axios error : ', error)
        })
    },
    deleteRoom(seqno, scb, fcb) {
        let reqUrl = `/room/${seqno}`

        axios.delete(baseUrl + reqUrl, {
            headers: {
                "Content-Type": "application/json",
            }
        }).then((response) => {
            console.log(response.data.data)
            if (response.status === 200) {
                if (scb) {
                    scb(response.data.data)
                }
            } else  {
                if (fcb) {
                    fcb(response.data)
                }
            }
        }).catch((error) => {
            console.error('axios error : ', error)
        })
    },
    getLogList(roomId, scb, fcb) {
        let reqUrl = `/logs/${roomId}`

        axios.get(baseUrl + reqUrl, {
            headers: {
                "Content-Type": "application/json",
            }
        }).then((response) => {
            console.log(response.data.data)
            if (response.status === 200) {
                if (scb) {
                    scb(response.data.data)
                }
            } else  {
                if (fcb) {
                    fcb(response.data)
                }
            }
        }).catch((error) => {
            console.error('axios error : ', error)
        })
    },
    createSideChatRoom(req, scb, fcb) {
        let reqUrl = `/room/${req.main_room_id}`

        axios.post(baseUrl + reqUrl, req, {
            headers: {
                "Content-Type": "application/json",
            }
        }).then((response) => {
            // console.log(response.data.data)
            if (response.status === 200) {
                if (scb) {
                    scb(response.data.data) // room id 반환됨
                }
            } else {
                if (fcb) {
                    fcb(response.data)
                }
            }
        }).catch((error) => {
            console.error('axios error : ', error)
        })
    }
}