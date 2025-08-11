import axios from "axios";

export const getCategories = async () => {
  try {
    const response = await axios.get('/category/all');
    const { success, data, message } = response.data;

    if (!success) {
      throw new Error(message || 'Lấy danh sách categories thất bại');
    }

    return data;
  } catch (error) {
    console.error('Lỗi khi gọi API getCategories:', error);
    throw error;
  }
};