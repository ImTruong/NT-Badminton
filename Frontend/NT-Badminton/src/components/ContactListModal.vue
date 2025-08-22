<script setup lang="ts">
    import { ref, defineProps, defineEmits } from 'vue'
    import ContactDetailModal from './ContactDetailModal.vue'

    const props = defineProps({
    show: { type: Boolean, required: true }
    })

    const emit = defineEmits(['close'])

    const close = () => {
        emit('close')
    }
    const contacts = ref([
        { id:1, name: 'Nguyễn Văn A', phone: '0123456789', address: '123 Đường ABC, Quận 1, TP.HCM', mainContact: false },
        { id:2, name: 'Trần Thị B', phone: '0987654321', address: '456 Đường DEF, Quận 2, TP.HCM', mainContact: true }
    ])
    const selectedContactId = ref<number | null>(null)
    const showContactModal = ref(false)
    const handleOpenContactModal = () => {
        showContactModal.value = true
    }
    const handleCloseContactModel = () => {
        showContactModal.value = false
    }
</script>

<template>
    <div class="wrapper" v-if="props.show">
        <div class="modal-overlay"></div>
        <div class="modal-content" v-if="!showContactModal">
            <div class="header-title">
                <p>Địa chỉ của tôi</p>
            </div>
            <div class="contacts">
                <div class="contact" v-for="contact in contacts" :key="contact.id">
                    <label :for="'contact-' + contact.id">
                        <div class="choosen-btn">
                        <input type="radio"
                            :value="contact.id" 
                            v-model="selectedContactId" 
                            :id="'contact-' + contact.id"/>
                        </div>
                    </label>
                    <label :for="'contact-' + contact.id" class="contact-info-wrap">
                        <div class="contact-info">
                            <div class="upper-info">
                                <p class="info-name">{{ contact.name }}</p>
                                <p class="sub-info">{{ contact.phone }}</p>
                            </div>
                            <div class="lower-info">
                                <p class="sub-info">{{ contact.address }}</p>
                            </div>
                        </div>
                        <div class="main-contact" v-if="contact.mainContact">
                            <div class="main-contact-label">Mặc định</div>
                        </div>
                    </label>    
                    
                    <div class="modify-contact">
                        <span class="edit-contact" @click="handleOpenContactModal">Cập nhật</span>
                    </div>
                </div>
            </div>
            <div class="footer">
                <button class="add-contact" @click="close">Thêm địa chỉ</button>
                <button class="close-modal" @click="close">Đóng</button>
            </div>
        </div>
        <ContactDetailModal 
            :show="showContactModal"
            @close="handleCloseContactModel" />
    </div>
  
    
</template>

<style scoped>
    .wrapper {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: center;  /* căn ngang giữa */
        align-items: center;      /* căn dọc giữa */
    }
    .modal-overlay {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0,0,0,0.5);
    }
    .modal-content {
        background-color: white;
        border-radius: 8px;
        font-size: 17px;
        width: 500px;
        z-index: 1;
    }
    .header-title {
        font-size: 20px;
        font-weight: bold;
        border-bottom: 1px solid #e0e0e0;
        padding: 16px 24px;
    }
    .contacts {
        padding: 0 24px;
    }
    .contact {
        padding: 16px 0;
        display: flex;
        position: relative;
    }
    .contact:not(:last-child){
        border-bottom: 1px solid #e0e0e0;
    }
    .upper-info{
        display: flex;
    }
    .upper-info > *:not(:last-child) {
        padding-right: 10px;
        border-right: 1px solid #8d8d8d;
    }
    .upper-info > *:not(:first-child) {
        padding-left: 10px;
    }
    .info-name {
        font-size: 18px;
        font-weight: 500;
    }
    .sub-info {
        font-size: 16px;
        color: #666;
        text-align: center;
    }
    .edit-contact {
        cursor: pointer;
        color: #007bff;
    }
    .choosen-btn {
        margin-right: 16px;
        display: flex;
        align-items: center;
    }
    .choosen-btn input {
        width: 20px;
        height: 20px;
    }
    .modify-contact{
        right: 0;
        position: absolute;
    }
    .footer {
        display: flex;
        padding: 20px 24px;
        border-top: 1px solid #e0e0e0;
        gap: 10px;
        justify-content: end;
    }
    .footer button {
        padding: 10px;
        cursor: pointer;
        width: 13 0px;
        font-size: 15px;
    }
    .add-contact {
        background-color: var(--main-color);
        border: none;
        color: white;
        transition: background-color 0.3s;
        &:hover {
            filter: brightness(90%); /* làm tối đi 10% */
        }
    }
    .close-modal {
        background-color: transparent;
        border: 1px solid #e0e0e0;
        &:hover {
            background-color: #f0f0f0; /* làm sáng lên khi hover */
        }
    }
    .contact-info-wrap{
        display: flex;
        flex-direction: column;
        flex: 1;
        gap: 8px;
    }
    .lower-info {
        display: flex;
        justify-content: left;
    }
    .main-contact{
        color: var(--main-color);
        border: 1px solid var(--main-color);
        width: fit-content;
        padding: 2px 8px;
    }

</style>