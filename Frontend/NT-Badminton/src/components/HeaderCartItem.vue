<script setup>
    import { defineProps } from 'vue';
    const props = defineProps({
        item: {
            type: Object,
            required: true
        }
    });
    const item = props.item;
</script>

<template>
    <div class="cart-item">
        <img class="item-image" :src="item.img" alt="Product Image"/>
        <div class="cart-item-details">
        <div class="cart-item-name">
            <a class="item-name" href="">
                {{ item.name }}
            </a>
            <font-awesome-icon class="cart-item-details-icon" :icon="['fas', 'trash']" />
        </div>
        <div class="cart-item-control">
            <div class="item-quantity-choice">
            <button class="reduce-quantity">-</button>
            <input type="number" class="item-quantity" v-model="item.quantity" min="1" />
            <button class="increase-quantity">+</button>
            </div>
            <span class="cart-item-price">Giá: {{ item.price }}đ</span>
        </div>
        </div>
    </div>
</template>
<style scoped>
    .cart-item {
    min-width: 320px;
    display: flex;
    align-items: center;
    background-color: #ffffff;
    padding: 10px;
    border-bottom: 1px solid #ccc;
  }
  .cart-item-details {
    display: flex;
    flex-direction: column;
    width: 100%;
  }

  .cart-item-name {
    display: flex;
    align-items: center;
    justify-content: space-around;
    margin-bottom: 10px;
  }

  .cart-item-control {
    display: flex;
    flex-direction: row;
    width: 100%;
  }
  .item-quantity-choice {
    display: flex;
    gap: 3px;
  }

  .item-quantity-choice input {
    width: 50px;
    text-align: center;
    border: 1px solid var(--main-color);
    border-radius: 7px;
    gap: 10px;
  }

  .item-quantity-choice button {
    border: none;
    background-color: var(--main-color);
    color: #ffffff;
    cursor: pointer;
    border-radius: 20px;
  }
  .cart-item-price {
    margin-left: auto;
    font-weight: bold;
    color: var(--main-color);
  }
  .item-name:hover {
    color: var(--main-color);
    background-color: #ffffff;
  }
  a.item-name{
    &:hover{
      background-color: transparent;
    }
  }
  a{
    text-decoration: none;
    color: #000000;
  }
</style>