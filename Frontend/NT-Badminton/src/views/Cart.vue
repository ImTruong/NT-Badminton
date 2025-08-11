<script setup>
    import { ref } from 'vue';
    import CartItem from "@/components/CartItem.vue";

    const cartItems = ref([
        {
            id: 1,
            name: "Racket Yonex Voltric Z Force II",
            img: "https://cdn.shopvnb.com/img/180x180/uploads/san_pham/vot-cau-long-vnb-v200i-hong-3.webp",
            price: 100000,
            quantity: 1
        },
        {
            id: 2,
            name: "Vợt Cầu Lông Victor Brave Sword 12",
            img: "https://cdn.shopvnb.com/img/180x180/uploads/san_pham/vot-cau-long-victor-brave-sword-12.webp",
            price: 120000,
            quantity: 1
        }
    ]);
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
                <h2 class="total-price price">{{ cartItems.reduce((acc, item) => acc + item.price * item.quantity, 0) }} <span class="underline price">đ</span></h2>
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