import axios from "axios";

export const getLocations = async () => {
    try {
        const response = await axios.get("/contact/locations");
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
            throw new Error("Lỗi không xác định khi lấy địa điểm");
        }
    }
};
