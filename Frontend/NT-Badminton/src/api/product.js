import axios from "axios";
import qs from "qs";

export const searchProducts = async (filters, page, size) => {
  try {
    const params = {
      name: filters.name,
      brands: filters.brands?.length ? filters.brands : null,
      categoryIds: filters.categoryIds?.length ? filters.categoryIds : null,
      minPrice: filters.minPrice,
      maxPrice: filters.maxPrice,
      rating: filters.rating,
      page,
      size
    };

    const response = await axios.get('/product', {
      params,
      paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' })
    });
    const { success, data, message } = response.data;
    if (success) {
      return data;
    } else {
      console.error("Error searching products:", message);
      throw new Error(message);
    }
  } catch (error) {
    console.error("Error searching products:", error);
    throw error;
  }
};

export const getAllProductBrands = async () => {
  try {
    const response = await axios.get('/product/brands');
    const {success, data, message} = response.data;
    if (success) {
      return data;
    } else {
      console.error("Error fetching product brands:", message);
      throw new Error(message);
    }
  } catch (error) {
    console.error("Error fetching product brands:", error);
    throw error;
  }
};

export const getProductDetail = async (productId) => {
  try {
    const response = await axios.get(`/product/${productId}`);
    const { success, data, message } = response.data;
    if (success) {
      return data;
    } else {
      console.error("Error fetching product detail:", message);
      throw new Error(message);
    }
  } catch (error) {
    console.error("Error fetching product detail:", error);
    throw error;
  }
};
