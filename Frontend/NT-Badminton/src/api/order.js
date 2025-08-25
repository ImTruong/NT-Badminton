import axios from "axios";

export const createOrder = async (token, orderData) => {
    try {
        const response = await axios.post('/order', orderData, {
            headers: {
                Authorization: `Bearer ${token}`
            },
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
            throw new Error("Lỗi không xác định khi tạo đơn hàng");
        }
    }
}

export const getOrders = async (token) => {
    try {
        const response = await axios.get('/order/all', {
            headers: {
                Authorization: `Bearer ${token}`
            },
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
            throw new Error("Lỗi không xác định khi lấy danh sách đơn hàng");
        }
    }
}
