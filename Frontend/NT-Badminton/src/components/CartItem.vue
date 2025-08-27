<script setup>
    import { defineProps, defineEmits } from 'vue';
    const props = defineProps({
        item: {
            type: Object,
            required: true
        }
    });
    const item = props.item;

    const emit = defineEmits(["updateQuantity", "removeItem"]);

    const increaseQuantity = () => {
        emit("updateQuantity", { id: props.item.id, quantity: props.item.quantity + 1 });
    };

    const decreaseQuantity = () => {
        if (props.item.quantity > 1) {
            emit("updateQuantity", { id: props.item.id, quantity: props.item.quantity - 1 });
        }
    };

    const removeItem = () => {
        emit("removeItem", props.item.id);
    };
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
                <button class="reduce-quantity" @click="decreaseQuantity">-</button>
                <input type="number" class="item-quantity" v-model="item.quantity" min="1" 
                    @input="emit('updateQuantity', { id: item.id, quantity: +$event.target.value })"/>
                <button class="increase-quantity" @click="increaseQuantity">+</button>
            </div>
            <h3 class="cart-item-price">Giá: <span class="price">{{ item.price.toLocaleString() }} <span class="underline price">đ</span></span></h3>
            <div class="cart-item-details-icon">
                <font-awesome-icon class="icon" :icon="['fas', 'trash']" @click="removeItem" />
            </div>
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
        height: fit-content;
    }
    .cart-item-details{
        display: flex;
        justify-content: space-around;
        flex: 1;
    }
    .cart-item-name{
        display: flex;
        margin-left: 20px;
        align-items: center;
    }
    .item-quantity-choice {
        display: flex;
        gap: 3px;
        align-items: center;
    }

    .item-quantity-choice input {
        width: 70px;
        height: 30px;
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
        height: 30px;
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
        display: flex;
        justify-content: center;
    }
    .item-choices {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
    }
    p ,h4{
        text-align: center;
    }
    .cart-item-price {
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .cart-item-details-icon{
        display: flex;
        justify-content: center;
        align-items: center;
        cursor: pointer;
    }
    .icon{
        height: 20px;
    }

</style>