import axios from "axios";
 
export const getCart = async (token,page,size) => {
  try {
    const response = await axios.get("/cart", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      params: {
        page: page,
        size: size
      }
    });
    const { success, data, message } = response.data;
    if (!success) {
      throw new Error(message || 'Lấy danh sách giỏ hàng thất bại');
    }
    return data;
  }catch(error) {
    console.log("Error:", error.response?.status || "No status available");
    if (error.response?.data && error.response.data.message) {
      throw new Error(error.response.data.message);
    }else {
      throw new Error("Lỗi không xác định khi lấy danh sách giỏ hàng");
    }
  }
};
