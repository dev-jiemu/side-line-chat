import axios from 'axios'

const baseUrl = import.meta.env.VITE_API_BASE_URL

export default {
    login(userInfo, scb, fcb) {
        let reqUrl = '/user/login'

        axios.post(baseUrl + reqUrl, userInfo,{
            headers: {
                "Content-Type": "application/json",
            }
        }).then(response => {
            console.log('axios result : ', response)
            if (response.status === 200) {
                if(scb) {
                    scb(response.data.data)
                }
            } else {
                if (fcb) {
                    fcb(response.data)
                }
            }
        }).catch(error => {
            console.error('axios error : ', error)
        })
    }
}