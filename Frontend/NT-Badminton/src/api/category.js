import axios from "axios";

export const getCategories = async () => {
  try {
    const response = await axios.get('/category/all');
    const { success, data, message } = response.data;

    if (!success) {
      throw new Error(message || 'Lấy danh sách categories thất bại');
    }

    return data;
  }catch(error) {
    console.log("Error:", error.response.status);
    if (error.response.data && error.response.data.message) {
      throw("Message:", error.response.data.message);
    }else {
      throw("Message: Lỗi không xác định khi lấy danh sách categories", error);
    }
  }
};

export const getRootCategories = async () => {
  try {
    const response = await axios.get('/category/root');
    const { success, data, message } = response.data;

    if (!success) {
      throw new Error(message || 'Lấy danh sách categories thất bại');
    }

    return data;
  }catch(error) {
    console.log("Error:", error.response.status);
    if (error.response.data && error.response.data.message) {
      throw("Message:", error.response.data.message);
    }else {
      throw("Message: Lỗi không xác định khi lấy danh sách categories", error);
    }
  }
};