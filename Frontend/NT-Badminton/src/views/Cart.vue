<script setup>
    import { ref, onMounted } from 'vue';
    import CartItem from "@/components/CartItem.vue";
    import { getCart } from '@/api/cart';

    const cartItems = ref([]);
    

    onMounted(async () => {
        const token = localStorage.getItem("token");
        try{
            const fetchedCartItems = await getCart(token);
            cartItems.value = fetchedCartItems.content;
            console.log(cartItems.value);
        }catch (error){
            console.error("Error fetching cart items:", error);
        }
    });
</script>

<template>
    
    <Header class="header"></Header> 
    <div class="content">
        <div class="title-box">
            <h3 class="title bold-text">Giỏ hàng</h3>
        </div>
        <div class="item-list-box">
            <CartItem
                class = "cart-item"
                v-for="item in cartItems" :key="item.id"
                :item="item"
            />
        </div>
        <div class="total-box">
            <div class="upper-box">
                <h3 class="total-title">Tổng cộng</h3>
                <h2 class="total-price price">{{ cartItems.reduce((acc, item) => acc + item.salePrice * item.quantity, 0).toLocaleString() }} <span class="underline price">đ</span></h2>
            </div>
            <div class="lower-box">
                <button class="checkout-button">Thanh toán</button>
            </div>
        </div>
    </div>
    
</template>

<style scoped>
    .header{
        margin-bottom: 30px;
    }
    .content{
        width: 85vw;
        margin: 0 auto;
    }
    .title-box{
        background-color: var(--main-color);
        color: #ffffff;
        padding: 7px 16px;
    }
    .bold-text{
        font-weight: bold;
    }
    .item-list-box{
        display: flex;
        flex-direction: column;
        gap: 10px;
        border: 1px solid #e0e0e0;
        border-radius: 5px;
    }
    .cart-item:not(:last-child){
        border-bottom: 1px solid #e0e0e0;
    }
    .price{
        color: var(--main-color);
    }
    .underline{
        text-decoration: underline;
    }
    .total-box{
        margin-top: 20px;
        padding: 12px 16px;
        border-top: 1px solid #e0e0e0;
        border-radius: 5px;
    }
    .upper-box{
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .lower-box button{
        margin-top: 10px;
        width: 100%;
        color: #ffffff;
        background-color: var(--main-color);
        border: none;
        padding: 12px 16px;
        border-radius: 4px;
        cursor: pointer;
        &:hover{
            color: var(--main-color);
            background-color: #ffffff;
            border: 1px solid var(--main-color);
        }
    }

</style>