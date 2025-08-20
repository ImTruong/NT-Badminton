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
        <img class="item-image" :src="item.productCoverImage" alt="Product Image"/>
        <div class="cart-item-details">
            <div class="cart-item-name">
                <router-link :to="`/product/${item.productId}`">
                    <h3>{{ item.productName }}</h3>
                </router-link>
            </div>
            <div class="item-choice-wrap">
                <div class="item-choices">
                    <h4><strong>Tuỳ chọn:</strong></h4>
                    <p
                    v-for="
                        (value, key) in item.productOptionalValue"
                        :key="key">
                        {{ key }}: {{ value }}
                    </p>
                </div>
            </div>
            <div class="item-quantity-choice">
                <button class="reduce-quantity">-</button>
                <input type="number" class="item-quantity" v-model="item.quantity" min="1" />
                <button class="increase-quantity">+</button>
            </div>
            <h3 class="cart-item-price">Giá: <span class="price">{{ item.salePrice.toLocaleString() }} <span class="underline price">đ</span></span></h3>
            <font-awesome-icon class="cart-item-details-icon" :icon="['fas', 'trash']" />
        </div>
    </div>
</template>
<style scoped>
    .cart-item{
        width: 100%;
        display: flex;
        flex-direction: row;
        padding: 10px;
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
    .item-choice-wrap {
        width: 200px;
        height: 100%;
        position: relative;
        display: flex;
        justify-content: center;
    }
    .item-choices {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
    }
    p ,h4{
        text-align: center;
    }

</style>