import axios from "axios";

export const register = async (registerRequest) => {
    try{
        const response = await axios.post('/user/register', registerRequest);
        const { success, message } = response.data;
        if (!success) {
            throw new Error(message);
        }
    }catch(error) {
        console.log("Error:", error.response.status);   // mã lỗi, ví dụ 400
        if (error.response.data && error.response.data.message) {
            throw("Message:", error.response.data.message);   // dữ liệu trả về từ server
        }else {
            throw("Message: Lỗi không xác định khi đăng ký", error);
        }
    }
}

export const login = async (loginRequest) => {
    try {
        const response = await axios.post('/user/login', loginRequest);
        const { success, message, data } = response.data;
        if (!success) {
            throw new Error(message);
        }
        return data;
    } catch (error) {
        console.log("Error:", error.response?.status);
        if (error.response?.data?.message) {
            throw new Error(error.response.data.message);
        } else {
            throw new Error("Lỗi không xác định khi đăng nhập");
        }
    }
}

export const getUserContacts = async (token) => {
    try{
        const response = await axios.get('/user/contacts', {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        const { success, message, data } = response.data;
        if (!success) {
            throw new Error(message);
        }
        return data;
    } catch (error) {
        console.log("Error:", error.response?.status);
        if (error.response?.data?.message) {
            throw new Error(error.response.data.message);
        } else {
            throw new Error("Lỗi không xác định khi lấy danh bạ");
        }
    }
}

export const updateUserContact = async (token, updateRequest) => {
    try {
        const response = await axios.put(`/user/contact`, updateRequest, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        const { success, message } = response.data;
        if (!success) {
            throw new Error(message);
        }
    } catch (error) {
        console.log("Error:", error.response?.status);
        if (error.response?.data?.message) {
            throw new Error(error.response.data.message);
        } else {
            throw new Error("Lỗi không xác định khi cập nhật danh bạ");
        }
    }
}

export const addUserContact = async (token, addRequest) => {
    try {
        const response = await axios.post(`/user/contact`, addRequest, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        console.log(response)
        const { success, message } = response.data;
        if (!success) {
            throw new Error(message);
        }
    } catch (error) {
        console.log("Error:", error.response?.status);
        if (error.response?.data?.message) {
            throw new Error(error.response.data.message);
        } else {
            throw new Error("Lỗi không xác định khi thêm danh bạ");
        }
    }
}
