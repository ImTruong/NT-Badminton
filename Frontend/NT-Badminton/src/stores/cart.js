import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { getCart, addToCart, removeProductFromCart, updateCartItemQuantity } from '@/api/cart.js';

export const useCartStore = defineStore('cart', () => {
  // State
  const cartItems = ref([]);
  const isLoading = ref(false);
  const error = ref(null);
  const currentPage = ref(1);
  const itemsPerPage = ref(5);
  const token = localStorage.getItem('token');

  // Getters
  const totalCartAmount = computed(() => {
    return cartItems.value.reduce((total, item) => {
      return total + item.price;
    }, 0);
  });

  const cartItemCount = computed(() => {
    return cartItems.value.length;
  });

  // Actions
  async function fetchCartItems() {
    const token = localStorage.getItem('token');
    if (!token) {
      cartItems.value = [];
      return;
    }
    
    isLoading.value = true;
    error.value = null;
    
    try {
      const response = await getCart(token, currentPage.value, itemsPerPage.value);
      cartItems.value = response.content;
    } catch (err) {
      error.value = err.message || 'Failed to fetch cart items';
      console.error('Error fetching cart:', err);
    } finally {
      isLoading.value = false;
    }
  }

  async function addItemToCart(productVariantId, quantity) {
    if (!token) {
      // Handle not logged in case
      return { success: false, message: 'Please log in to add items to cart' };
    }
    
    isLoading.value = true;
    error.value = null;
    
    try {
      await addToCart(token, productVariantId, quantity);
      // Refresh cart items after adding
      await fetchCartItems();
      return { success: true };
    } catch (err) {
      error.value = err.message || 'Failed to add item to cart';
      console.error('Error adding to cart:', err);
      return { success: false, message: error.value };
    } finally {
      isLoading.value = false;
    }
  }

  async function removeItemFromCart(cartItemId) {
    try {
      await removeProductFromCart(token, cartItemId);
      // Refresh cart items after removing
      await fetchCartItems();
      return { success: true };
    } catch (err) {
      error.value = err.message || 'Failed to remove item from cart';
      console.error('Error removing from cart:', err);
      return { success: false, message: error.value };
    }
  }

  async function updateItemQuantity(cartItemId, quantity) {
    try {
      await updateCartItemQuantity(token, cartItemId, quantity);
      await fetchCartItems();
      return { success: true };
    } catch (err) {
      error.value = err.message || 'Failed to update item quantity';
      console.error('Error updating item quantity:', err);
      return { success: false, message: error.value };
    }
  }

  return {
    cartItems,
    isLoading,
    error,
    totalCartAmount,
    cartItemCount,
    currentPage,
    itemsPerPage,
    fetchCartItems,
    addItemToCart,
    removeItemFromCart,
    updateItemQuantity
  };
});