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
                    <h3>{{ item.name }}</h3>
                </a>
            </div>
            <div class="item-quantity-choice">
                <button class="reduce-quantity">-</button>
                <input type="number" class="item-quantity" v-model="item.quantity" min="1" />
                <button class="increase-quantity">+</button>
            </div>
            <h3 class="cart-item-price">Giá: <span class="price">{{ item.price }} <span class="underline price">đ</span></span></h3>
            <font-awesome-icon class="cart-item-details-icon" :icon="['fas', 'trash']" />
        </div>
    </div>
</template>
<style scoped>
    .cart-item{
        width: 100%;
        display: flex;
        flex-direction: row;
        align-items: center;
        padding: 10px;
    }
    .cart-item-details{
        flex: 1;
        display: flex;
        align-self: stretch;
        align-items: center;
        justify-content: space-around;
    }
    .cart-item-name{
        display: flex;
        margin-left: 20px;
    }
    .item-quantity-choice {
      display: flex;
      gap: 3px;
      height: 30px;
    }

    .item-quantity-choice input {
      width: 70px;
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
    width: 30px;
    }
    a{
        text-decoration: none;
        color: #000000;
        &:hover {
          color: var(--main-color);
        };
    }
    .price {
      font-weight: bold;
      color: var(--main-color);
    }
    .underline {
      text-decoration: underline;
    }

</style>