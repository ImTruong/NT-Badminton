<script setup>
    import { ref } from 'vue';
    import { useRoute } from 'vue-router';
    import ChoiceSlider from '@/components/ChoiceSlider.vue';
    import OrderItems from '@/components/OrderItems.vue';

    const route = useRoute();
    const orders = ref([
        {
            orderId: 1,
            orderItemList: [
                {
                    id: 1,
                    name: 'Vợt Cầu Lông',
                    quantity: 2,
                    price: 200000,
                    type: 'Racket',
                    image: "https://cdn.shopvnb.com/img/40x40/uploads/san_pham/vot-cau-long-vnb-v200i-hong-3.webp"
                },
                {
                    id: 2,
                    name: 'Bóng Cầu Lông',
                    quantity: 5,
                    price: 50000,
                    type: 'Ball',
                    image: "https://cdn.shopvnb.com/img/40x40/uploads/san_pham/vot-cau-long-vnb-v200i-hong-3.webp"
                }
            ],
            totalPrice: 300000,
            totalPriceAfterDiscount: 270000,
            deliveryStatus: 'Đang giao',
            paymentStatus: 'Đã thanh toán',
            isCanceled: false,
            contact: {
                name: 'Nguyễn Văn A',
                phone: '0123456789',
                address: '123 Đường ABC, Quận 1, TP.HCM'
            }
        },
        {
            orderId: 2,
            orderItemList: [
                {
                    id: 3,
                    name: 'Giày Cầu Lông',
                    quantity: 1,
                    price: 850000,
                    type: 'Shoes',
                    image: "https://cdn.shopvnb.com/img/40x40/uploads/san_pham/giay-cau-long-lining-ayat003-1-trang.webp"
                },
                {
                    id: 4,
                    name: 'Áo Cầu Lông',
                    quantity: 2,
                    price: 180000,
                    type: 'Clothes',
                    image: "https://cdn.shopvnb.com/img/40x40/uploads/san_pham/ao-cau-long-vnb-a220-nam-do-1.webp"
                }
            ],
            totalPrice: 1210000,
            totalPriceAfterDiscount: 1210000,
            deliveryStatus: 'Đã giao',
            paymentStatus: 'Đã thanh toán',
            isCanceled: false,
            contact: {
                name: 'Trần Thị B',
                phone: '0987654321',
                address: '456 Đường XYZ, Quận 7, TP.HCM'
            }
            },
            {
            orderId: 3,
            orderItemList: [
                {
                    id: 5,
                    name: 'Túi Vợt Cầu Lông',
                    quantity: 1,
                    price: 420000,
                    type: 'Accessory',
                    image: "https://cdn.shopvnb.com/img/40x40/uploads/san_pham/balo-cau-long-vnb-vb912-den-1.webp"
                }
            ],
            totalPrice: 420000,
            totalPriceAfterDiscount: 378000,
            deliveryStatus: 'Chưa giao',
            paymentStatus: 'Chưa thanh toán',
            isCanceled: true,
            contact: {
                name: 'Lê Văn C',
                phone: '0909123456',
                address: '789 Đường KLM, Quận 3, TP.HCM'
            }
        }
    ]);
    const categories = [
        { id: 0, name: 'Tất cả đơn hàng' },
        { id: 1, name: 'Đơn hàng đã thanh toán' },
        { id: 2, name: 'Đơn hàng chưa thanh toán' },
        { id: 3, name: 'Đơn hàng đang giao' },
        { id: 4, name: 'Đơn hàng đã giao' },
        { id: 5, name: 'Đơn hàng đã hủy' }
    ];
    const activeCategory = ref(categories[0].id);
    const changeCategory = (categoryId) => {
        activeCategory.value = categoryId;
    }
</script>

<template>
    <Header></Header>
    <div class="wrapper">
        <div class="order-choice">
            <ChoiceSlider
                class="order-choice-slider"
                :categories="categories"
                :activeCategory="activeCategory"
                @changeCategory="changeCategory"
            />
        </div>
        <div class="orders-box">
            <div v-for="order in orders" :key="order.orderId" class="order-item">
                <h2 class="order-title">Đơn hàng #{{ order.orderId }}</h2>
                <div class="order-content-wrap">
                    <div 
                        class="status-box" 
                        :class="{ canceled: order.isCanceled }"
                    >
                        <div class="status-item">
                            <span class="status-label">Trạng thái giao hàng:</span>
                            <span class="highlight">{{ order.deliveryStatus }}</span>
                        </div>
                        <div class="status-item">
                            <span class="status-label">Trạng thái thanh toán:</span>
                            <span class="highlight">{{ order.paymentStatus }}</span>
                        </div>
                        <div 
                            class="status-item canceled-text" 
                            v-if="order.isCanceled"
                        >
                            <span>Đơn hàng đã hủy</span>
                        </div>
                    </div>
                    <div class="contact-box">
                        <h4>Thông tin liên hệ</h4>
                        <div class="contact-item">
                            <span class="contact-label">Tên:</span>
                            <span>{{ order.contact.name }}</span>
                        </div>
                        <div class="contact-item">
                            <span class="contact-label">Điện thoại:</span>
                            <span>{{ order.contact.phone }}</span>
                        </div>
                        <div class="contact-item">
                            <span class="contact-label">Địa chỉ:</span>
                            <span>{{ order.contact.address }}</span>
                        </div>
                    </div>
                    <div class="order-detail-box">
                        <OrderItems :products="order.orderItemList" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .wrapper {
        width: 65vw;
        margin: 0 auto;
    }
    .order-choice {
        margin: 20px 0;
        box-sizing: border-box;
        height: 40px;
    }
    .order-title{
        text-align: center;
        font-size: 20px;
        font-weight: bold;
        border-bottom: 1px solid #eee;
        padding-bottom: 10px;
    }
    .order-item {
        margin-top: 40px;
        padding: 10px;
        border: 1px solid #eee;
        border-radius: 5px;
        background-color: #fff;
        font-size: 15px;
    }
    .status-box {
        margin-top: 10px;
        padding: 15px;
        border-radius: 8px;
        background-color: #f9f9f9;
        border: 1px solid #eee;
        font-size: 14px;
        line-height: 1.6;
    }

    .status-box.canceled {
        background-color: #ffe5e5;
        border-color: #ffb3b3;
    }

    .status-item {
        display: flex;
        gap: 8px;
        margin-bottom: 6px;
    }

    .status-item:last-child {
        margin-bottom: 0;
    }

    .status-label {
        font-weight: 600;
        color: #555;
    }

    .highlight {
        color: var(--main-color);
        font-weight: 600;
    }

    .canceled-text {
        font-weight: 600;
        color: #c00;
    }
    .highlight {
        color: var(--main-color);
    }
    .order-content-wrap {
        padding: 10px;
    }
    .contact-box {
        margin-top: 15px;
        padding: 15px;
        border-radius: 8px;
        background-color: #f9f9f9;
        border: 1px solid #eee;
        font-size: 14px;
        line-height: 1.6;
    }

    .contact-box h4 {
        font-size: 16px;
        font-weight: bold;
        color: var(--main-color);
        margin-bottom: 10px;
    }

    .contact-item {
        display: flex;
        gap: 8px;
        align-items: center;
        margin-bottom: 6px;
    }

    .contact-label {
        font-weight: 600;
        color: #555;
    }
</style>