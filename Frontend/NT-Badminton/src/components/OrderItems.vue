<script setup lang="ts">
    import { defineProps } from 'vue';
    const props = defineProps<{
        products: Array<{
            productName: string;
            productOptionalValue: object;
            price: number;
            quantity: number;
            image: string;
        }>
    }>();
    const formatPrice = (price: number) => {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(price);
    };
</script>
<template>
  <div class="table-wrapper">
    <table class="cart-table">
      <thead>
      <tr>
        <th class="bold-text"><h2>Sản phẩm</h2></th>
        <th class="blur-text"></th>
        <th class="blur-text">Đơn giá</th>
        <th class="blur-text">Số lượng</th>
        <th class="blur-text">Thành tiền</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(product, index) in products" :key="index">
        <td>
          <div class="product-info">
            <img :src="product.image" alt="Product image" class="product-image" />
            <div class="product-name-ellipsis" :title="product.productName">
              {{ product.productName.length > 30 ? product.productName.slice(0, 30) + '...' : product.productName }}
            </div>
          </div>
        </td>
        <td class="blur-text">
          <div v-for="(value, key) in product.productOptionalValue" :key="key">
            <span class="bold-text">{{ key }}:</span> {{ value }}
          </div>
        </td>
        <td>{{ formatPrice(product.price) }}</td>
        <td>{{ product.quantity }}</td>
        <td>{{ formatPrice(product.price * product.quantity) }}</td>
      </tr>
      <tr>
        <td colspan="4" class="bold-text no-border">Tổng cộng</td>
        <td class="bold-text highlight-text no-border">{{ formatPrice(products.reduce((total, product) => total + product.price * product.quantity, 0)) }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>
<style scoped>
  .table-wrapper {
    width: 100%;
    padding: 20px;
  }
  .cart-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 16px;
  }
  .cart-table h2{
    margin: 0;
    font-size: 18px;
    font-weight: bold;
  }
  .cart-table th, .cart-table td {
    text-align: center;
    padding: 8px;
    border-bottom: 1px solid #e0e0e0;
  }
  .product-info {
    display: flex;
    align-items: center;
    gap: 10px;
    justify-content: center;
  }
  .product-name-ellipsis {
    max-width: 180px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .product-image {
    object-fit: cover;
  }
  .blur-text{
    color: #888888;
  }
  .highlight-text {
    color: var(--main-color);
  }
  td.no-border {
    border-bottom: none;
  }
  .bold-text{
    font-weight: bold;
  }
</style>
