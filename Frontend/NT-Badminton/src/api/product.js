import axios from "axios";
import qs from "qs";

export const searchProducts = async (filters, page, size) => {
  try {
    const params = {
      name: filters.name,
      brand: filters.brands?.[0] || null,
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
    console.log(response);
    return response.data;
  } catch (error) {
    console.error("Error searching products:", error);
    throw error;
  }
};
