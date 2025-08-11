import axios from "axios";

export const register = async (userInformation) => {
    try{
        const response = await axios.post('/user/register', userInformation);
        const { success, message } = response.data;
        if (!success) {
            throw new Error(message);
        }
    }catch(error) {
        throw new Error(message);
    }
}

