<script setup lang="ts">
    import { ref } from 'vue';
    import OrderItems from '@/components/OrderItems.vue';

    const user = ref({
        name: 'Nguyễn Văn A',
        phone: '0123456789',
        address: '123 Đường ABC, Quận 1, TP.HCM'
    });
    const products = ref([
        {
            name: 'Vợt Cầu Lông',
            type: 'Thể thao',
            price: 200000,
            quantity: 2,
            image: "https://cdn.shopvnb.com/img/40x40/uploads/san_pham/vot-cau-long-vnb-v200i-hong-3.webp"
        },
        {
            name: 'Bóng Cầu Lông',
            type: 'Thể thao',
            price: 50000,
            quantity: 5,
            image: "https://cdn.shopvnb.com/img/40x40/uploads/san_pham/vot-cau-long-vnb-v200i-hong-3.webp"
        }
    ]);
    
    const paymentMethods = ref([
        { id: 1, name: 'Thanh toán khi nhận hàng (COD)', icon: 'credit-card' },
        { id: 2, name: 'VNPay', icon: 'university' },
    ]);
    const selectedPaymentMethod = ref(paymentMethods.value[0].id);

    const selectPaymentMethod = (id: number) => {
        console.log(`Selected payment method: ${id}`);
        selectedPaymentMethod.value = id;
    };
</script>

<template>
    <Header></Header>
    <div class="wrap">
        <div class="content">
            <div class="address-box">
                <div class="address-box-decor"></div>
                <div class="title">
                    <font-awesome-icon :icon="['fas', 'location-dot']" /> Địa chỉ nhận hàng
                </div>
                <div class="address">
                    <p class="user-name"><span class="bold-text">Họ tên:</span> {{ user.name }}</p>
                    <p class="user-phone"><span class="bold-text">Số điện thoại:</span> {{ user.phone }}</p>
                    <p class="user-address"><span class="bold-text">Địa chỉ:</span> {{ user.address }}</p>
                    <button class="edit-button">Thay đổi</button>
                </div>
            </div>
            <div class="split-box"></div>
            <OrderItems :products="products" />
            <div class="split-box"></div>
            <div class="payment-box">
                <h2>Phương thức thanh toán</h2>
                <div v-for="method in paymentMethods" :key="method.id" class="payment-method">
                    <input :id="'payment-method-' + method.id" class="hidden" type="radio" :value="method.id" v-model="selectedPaymentMethod" />
                    <label 
                    class="radio-label" 
                    :class="{ active: selectedPaymentMethod == method.id }" 
                    :for="'payment-method-' + method.id"
                    @click="selectPaymentMethod(method.id)">
                        <font-awesome-icon :icon="['fas', method.icon]" />
                        <span>{{ method.name }}</span>
                    </label>
                </div>
            </div>
            <div class="split-box"></div>
            <div class="confirm-box">
                <button class="confirm-button">Xác nhận đơn hàng</button>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .wrap {
        background-color: #f1f1f1;
        height: 100vh;
    }
    .split-box {
        height: 15px;
        width: 100%;
        margin: 0 auto;
        background-color: #f1f1f1;
    }
    .content {
        position: relative;
        top: 10px;
        width: 80vw;
        margin: 0 auto;
        background-color: #ffffff;
        --payment-box-color: #434343;
    }
    .address-box {
        padding: 25px 30px;
        display: flex;
        flex-direction: column;
        gap: 20px;
        font-size: 17px;
        position: relative;
    }
    .title {
        display: flex;
        align-items: center;
        gap: 10px;
        color: var(--main-color);
    }
    .address {
        display: flex;
        align-items: center;
        gap: 15px;
    }
    .bold-text{
        font-weight: bold;
    }
    .address p:not(:last-child){
        padding: 0 15px 0 0;
        border-right: 2px solid #e0e0e0;
    }
    .edit-button {
        background-color: #ffffff;
        color: #2607f4;
        border: 1px solid #2607f4;
        padding: 10px 15px;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.3s, color 0.3s;
        &:hover {
            background-color: #2607f4;
            color: #ffffff;
        }
    }
    .address-box-decor {
        height: 3px;
        width: 100%;
        background-image: repeating-linear-gradient(45deg, #6fa6d6, #6fa6d6 33px, transparent 0, transparent 41px, #f18d9b 0, #f18d9b 74px, transparent 0, transparent 82px);
    }
    .payment-box {
        padding: 25px 30px;
        display: flex;
        flex-direction: column;
        gap: 20px;
        font-size: 17px;
        justify-content: center;
        align-items: center;
    }
    .hidden{
        display: none;
    /* CSS variable moved to .content selector */
    }
    .radio-label{
        display: flex;
        align-items: center;
        gap: 10px;
        cursor: pointer;
        padding: 10px;
        transition: background-color 0.3s, color 0.3s;
        color: var(--payment-box-color);
        border: var(--payment-box-color) 1px solid;
        width: 30%;
    }
    .radio-label:hover {
        background-color: #f0f0f0;
    }
    .active {
        color: var(--main-color);
        border-color: var(--main-color);
    }
    .payment-method{
        display: flex;
        align-items: center;
        gap: 10px;
        justify-content: center;
        width: 100%;
    }
    .confirm-box {
        display: flex;
        justify-content: flex-end;
        padding: 20px;
    }
    .confirm-box button{
        padding: 15px 50px;
        color: #ffffff;
        background-color: var(--main-color);
        border: none;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.3s;
    }
    .confirm-box button:hover {
        background-color: darken(var(--main-color), 5%);
        color: var(--main-color);
        border: 1px solid var(--main-color);
    }
</style>