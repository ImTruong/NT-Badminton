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

export const addToCart = async (token, variantId, quantity) => {
  try {
    const addProductToCartRequest = {
      productVariantId: variantId,
      quantity: quantity
    };
    const response = await axios.post("/cart", addProductToCartRequest, {
      headers: {
        Authorization: `Bearer ${token}`,
      }
    });
    const { success, message } = response.data;
    if (!success) {
      throw new Error(message || 'Thêm sản phẩm vào giỏ hàng thất bại');
    }
  } catch (error) {
    console.log("Error:", error.response?.status || "No status available");
    if (error.response?.data && error.response.data.message) {
      throw new Error(error.response.data.message);
    } else {
      throw new Error("Lỗi không xác định khi thêm sản phẩm vào giỏ hàng");
    }
  }
};

export const removeProductFromCart = async (token, cartItemId) => {
  try {
    const response = await axios.delete(`/cart`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      params: {
        productVariantId: cartItemId
      }
    });
    const { success, message } = response.data;
    if (!success) {
      throw new Error(message || 'Xóa sản phẩm khỏi giỏ hàng thất bại');
    }
  } catch (error) {
    console.log("Error:", error.response?.status || "No status available");
    if (error.response?.data && error.response.data.message) {
      throw new Error(error.response.data.message);
    } else {
      throw new Error("Lỗi không xác định khi xóa sản phẩm khỏi giỏ hàng");
    }
  }
};

export const updateCartItemQuantity = async (token, cartItemId, quantity) => {
  try {
    const response = await axios.put(`/cart/quantity`, 
      {
        productVariantId: cartItemId,
        quantity: quantity
      }, 
      {
      headers: {
        Authorization: `Bearer ${token}`,
      }
      }
    );
    const { success, message } = response.data;
    if (!success) {
      throw new Error(message || 'Cập nhật số lượng sản phẩm trong giỏ hàng thất bại');
    }
  } catch (error) {
    console.log("Error:", error.response?.status || "No status available");
    if (error.response?.data && error.response.data.message) {
      throw new Error(error.response.data.message);
    } else {
      throw new Error("Lỗi không xác định khi cập nhật số lượng sản phẩm trong giỏ hàng");
    }
  }
};
