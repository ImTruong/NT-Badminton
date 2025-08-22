<script setup>
    import { ref, reactive, defineProps, defineEmits } from 'vue'

    const props = defineProps({
        show: { type: Boolean, required: true }
    })

    const emit = defineEmits(['close'])

    const close = () => {
        emit('close')
    }

    const contact = reactive({
        name: 'Nguyễn Trường',
        phone: '0123456789',
        areaAddress: 'Quận 1, TP.HCM',
        streetAddress: '123 Đường ABC'
    })

    const isMainContact = ref(false);
</script>

<template>
    <div class="modal" v-if="props.show">
        <div class="modal-content">
            <h2>Cập nhật địa chỉ</h2>
            <div class="input-group">
                <div class="input-box">
                    <div class="label">Họ và tên</div>
                    <input type="text" name="name" id="name" v-model="contact.name">
                </div>
                <div class="input-box">
                    <div class="label">Số điện thoại</div>
                    <input type="text" name="phone" id="phone" v-model="contact.phone">
                </div>
            </div>
            <div class="input-group">
                <div class="input-box">
                    <div class="label">Tỉnh/Thành phố, Quận/Huyện, Phường/xã</div>
                    <input type="text" name="areaAddress" id="areaAddress" v-model="contact.areaAddress">
                </div>
            </div>
            <div class="input-group">
                <div class="input-box">
                    <div class="label">Địa chỉ cụ thể</div>
                    <textarea name="streetAddress" id="streetAddress" v-model="contact.streetAddress"></textarea>
                </div>
            </div>
            <div class="main-contact">
                <div class="main-contact-label">Liên hệ chính</div>
                <input type="checkbox" v-model="isMainContact">
            </div>
            <div class="button-group">
                <button @click="close" class="close-btn">Đóng</button>
                <button @click="updateContact" class="update-btn">Cập nhật</button>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .modal {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .modal-content {
        background-color: white;
        padding: 24px;
        border-radius: 5px;
        width: 450px;
        display: flex;
        flex-direction: column;
        gap: 16px;
    }
    .close {
        cursor: pointer;
        float: right;
        font-size: 20px;
    }
    .input-group {
        margin-bottom: 16px;
        display: flex;
        gap: 16px;
    }
    .input-box{
        flex: 1;
        position: relative;
        display: flex;
        justify-content: center;
    }
    .input-box input,
    .input-box textarea {
        border: 1px solid rgba(0, 0, 0, .2);
        border-radius: 4px;
        padding: 12px 12px;
        width: 100%;
        box-sizing: border-box;
    }
    .label{
        font-size: 12px;
        color: rgba(0, 0, 0, .4);
        position: absolute;
        top: -10px;
        left: 12px;
        background-color: #ffffff;
        padding: 0 4px;
    }
    .button-group {
        display: flex;
        justify-content: flex-end;
        gap: 8px;
    }
    .button-group button {
        padding: 8px 16px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }
    .update-btn {
        background-color: var(--main-color);
        border: none;
        color: white;
        transition: background-color 0.3s;
        &:hover {
            filter: brightness(90%); /* làm tối đi 10% */
        }
    }
    .close-btn {
        background-color: #d8d8d8;
        border: 1px solid #e0e0e0;
        &:hover {
            background-color: #c0c0c0;
        }
    }
    .main-contact {
        display: flex;
        align-items: center;
        gap: 8px;
    }
    .main-contact-label {
        font-size: 14px;
        color: rgba(0, 0, 0, .6);
    }
    .main-contact input {
        width: 20px;
        height: 20px;
    }

</style>
