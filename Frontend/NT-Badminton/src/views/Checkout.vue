<script setup lang="ts">
    import { ref, onMounted, watch } from 'vue';
    import {  useRouter } from 'vue-router';
    import OrderItems from '@/components/OrderItems.vue';
    import ContactListModal from '@/components/ContactListModal.vue';
    import { getUserContacts } from '@/api/user';
    import { createOrder } from '@/api/order';

    const router = useRouter();
    const token = localStorage.getItem("token");
    const contacts = ref([]);
    const connectAddress = (contact) => {
        if (!contact || !contact.streetAddress || !contact.ward || !contact.district || !contact.city) return null;
        return contact.streetAddress + ", " + contact.ward.name + ", " + contact.district.name + ", " + contact.city.name;
    }
    const fetchContacs = async () => {
        if (token) {
            try {
                contacts.value = await getUserContacts(token);
                contacts.value = contacts.value.map(contact => ({
                    ...contact,
                    fullAddress: connectAddress(contact)
                }));
            } catch (error) {
                console.error("Failed to fetch user contacts:", error);
            }
        }
    }
    onMounted(async () => {
        await fetchContacs();
        currentContact.value = getMainContact();
    });
    const getMainContact = () => {
        return contacts.value.find((c: any) => c.type === 'MAIN') || {};
    }
    const currentContact = ref(null);
    watch(contacts, (newVal) => {
        if(currentContact.value === null) currentContact.value = getMainContact();
    }, { immediate: true });

    const products = ref([]);
    products.value = JSON.parse(localStorage.getItem("OrderItems") || '[]');
    const paymentMethods = ref([
        { id: 0, name: 'Thanh toán khi nhận hàng (COD)', icon: 'university' },
        { id: 1, name: 'VNPay', icon: 'credit-card' },
    ]);
    const selectedPaymentMethod = ref(paymentMethods.value[0].id);

    const selectPaymentMethod = (id: number) => {
        selectedPaymentMethod.value = id;
    };
    const showContactListModal = ref(false);
    const handleCloseContactListModel = () => {
        showContactListModal.value = false;
    };
    const handleUpdateContactListModel = () => {
        fetchContacs();
    };

    const handleChooseContact = (contact) => {
        currentContact.value = contact;
    }

    const handleCheckout = async () => {
        if (!currentContact.value || !currentContact.value.fullAddress || !currentContact.value.phone) {
            alert("Vui lòng cung cấp đầy đủ thông tin địa chỉ nhận hàng.");
            return;
        }

        if (products.value.length === 0) {
            alert("Giỏ hàng của bạn đang trống.");
            return;
        }

        const orderData = {
            orderItems: products.value.map(product => ({
                productVariantId: product.productVariantId,
                quantity: product.quantity
            })),
            contactId: currentContact.value.id,
            paymentMethod: selectedPaymentMethod.value
        };

        try {
            const url = await createOrder(token, orderData);
            console.log("Order created successfully:", url);
            localStorage.removeItem("OrderItems");
            if (selectedPaymentMethod.value == 1) {
                window.open(url, "_blank");
            }
            router.push('/purchase');
            
        } catch (error) {
            alert(error);
            console.error("Failed to create order:", error);
        }
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
                    <p class="user-name"><span class="bold-text">Họ tên:</span> {{ currentContact.firstName + " " + currentContact.lastName }}</p>
                    <p class="user-phone"><span class="bold-text">Số điện thoại:</span> {{ currentContact.phone }}</p>
                    <p class="user-address"><span class="bold-text">Địa chỉ:</span> {{ currentContact.fullAddress || '(Chưa có địa chỉ !)' }}</p>
                    <button class="edit-button" @click="showContactListModal = true">Thay đổi</button>
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
                <button class="confirm-button" @click="handleCheckout">Xác nhận đơn hàng</button>
            </div>
        </div>
    </div>
    <ContactListModal 
        :show="showContactListModal"
        :contacts="contacts"
        @close="handleCloseContactListModel" 
        @update="handleUpdateContactListModel"
        @choose="handleChooseContact"
    />
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