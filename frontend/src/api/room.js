import axios from 'axios'

const baseUrl = import.meta.env.VITE_API_BASE_URL

export default {
    createRoom(req, scb, fcb) {
        let reqUrl = '/room'

        console.log(baseUrl)

        axios.post(baseUrl + reqUrl, req, {
            headers: {
                "Content-Type": "application/json",
            }
        }).then((response) => {
            if (response.status === 200) { // room id 반환됨
                if(scb) {
                    scb(response.data)
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