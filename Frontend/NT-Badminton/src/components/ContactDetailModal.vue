<script setup>
    import { ref, reactive, defineProps, defineEmits, onMounted, computed } from 'vue'
    import { getLocations } from '@/api/contact.js'
    import { updateUserContact, addUserContact } from '@/api/user.js'

    const props = defineProps({
        contact: { type: Object},
        mode: { type: String}
    })

    const contact = props.contact ? reactive(JSON.parse(JSON.stringify(props.contact))) : reactive({})

    const emit = defineEmits(['close', 'update'])

    const close = () => {
        emit('close')
    }

    const update = () => {
        emit('update')
    }

    const isMainContact = ref(props.contact ? props.contact.type === 'MAIN' : false);

    const dropdownHeader = ref([
        { label: 'Tỉnh/Thành phố', value: 'city' },
        { label: 'Quận/Huyện', value: 'district' },
        { label: 'Phường/Xã', value: 'ward' }
    ])
    const activeDropdown = ref(dropdownHeader.value[0].value);

    const locations = ref();

    const selected = reactive({
        city: props.contact?.city || null,
        district: props.contact?.district || null,
        ward: props.contact?.ward || null,
        cityId: props.contact?.city?.id || null,
        districtId: props.contact?.district?.id || null,
        wardId: props.contact?.ward?.id || null
    })

    // Computed để hiển thị địa chỉ đã chọn
    const administrativeAddress = computed(() => {
        if (selected.city && selected.district && selected.ward) {
            return `${selected.ward.name}, ${selected.district.name}, ${selected.city.name}`
        }
        if (selected.city && selected.district) {
            return `${selected.district.name}, ${selected.city.name}`
        }
        if (selected.city) {
            return selected.city.name
        }
        return ""
    })

    const chooseCity = (city) => {
        selected.city = city
        selected.cityId = city.id
        selected.district = null
        selected.districtId = null
        selected.ward = null
        selected.wardId = null
        activeDropdown.value = "district"
    }

    const chooseDistrict = (district) => {
        selected.district = district
        selected.districtId = district.id
        selected.ward = null
        selected.wardId = null
        activeDropdown.value = "ward"
    }

    const chooseWard = (ward) => {
        selected.ward = ward
        selected.wardId = ward.id
        isDropdown.value = false;
    }

    const switchDropdown = (dropdown) => {
        if (!selected.district && dropdown === 'ward') return;
        if (!selected.city && (dropdown === 'district' || dropdown === 'ward')) return;
        activeDropdown.value = dropdown;
    }

    const isDropdown = ref(false);

    const errors = ref({})

    const validateForm = () => {
        errors.value = {}

        if (!contact.firstName) errors.value.firstName = "Họ là bắt buộc"
        if (!contact.lastName) errors.value.lastName = "Tên là bắt buộc"
        if (!contact.phone) errors.value.phone = "Số điện thoại là bắt buộc"
        if (!selected.city || !selected.district || !selected.ward) {
            errors.value.address = "Bạn phải chọn đủ Tỉnh/Thành phố, Quận/Huyện, Phường/Xã"
        }
        if (!contact.streetAddress) errors.value.streetAddress = "Địa chỉ cụ thể là bắt buộc"

        return Object.keys(errors.value).length === 0
    }

    const handleUpdateContact = async () => {
        if (!validateForm()) {
            return
        }
        try {
            const token = localStorage.getItem("token")
            const updatedContact = {
                contactId: props.mode == 'update' ? contact.id : null,
                firstName: contact.firstName,
                lastName: contact.lastName,
                phone: contact.phone,
                type: isMainContact.value ? 'MAIN' : 'SUB',
                cityId: selected.cityId,
                districtId: selected.districtId,
                wardId: selected.wardId,
                streetAddress: contact.streetAddress
            }
            if (props.mode == 'update') await updateUserContact(token, updatedContact)
            if (props.mode == 'create') await addUserContact(token, updatedContact)
            alert("Cập nhật địa chỉ thành công!")
            update();
        } catch (error) {
            console.error("Failed to update contact:", error)
            alert("Cập nhật địa chỉ thất bại. Vui lòng thử lại.")
        }
    }

    onMounted(async () => {
        try {
            locations.value = await getLocations();
        } catch (error) {
            console.error("Error fetching locations:", error);
        }
    });

</script>

<template>
    <div class="modal">
        <div class="modal-content">
            <h2>Cập nhật địa chỉ</h2>
            <div class="input-group">
                <div class="input-box">
                    <div class="label">Họ</div>
                    <input type="text" name="name" id="name" v-model="contact.firstName">
                    <div v-if="errors.firstName" class="error">{{ errors.firstName }}</div>
                </div>
                <div class="input-box">
                    <div class="label">Tên</div>
                    <input type="text" name="name" id="name" v-model="contact.lastName">
                    <div v-if="errors.lastName" class="error">{{ errors.lastName }}</div>
                </div>
            </div>
            <div class="input-group">
                <div class="input-box">
                    <div class="label">Số điện thoại</div>
                    <input type="text" name="phone" id="phone" v-model="contact.phone">
                    <div v-if="errors.phone" class="error">{{ errors.phone }}</div>
                </div>
            </div>
            <div class="input-group">
                <div class="input-box">
                    <div class="label">Tỉnh/Thành phố, Quận/Huyện, Phường/xã</div>
                    <div class="location-wrap">
                        <input type="text" name="areaAddress" id="areaAddress" v-model="administrativeAddress" readonly
                            @click="isDropdown = !isDropdown"/>
                        <div v-if="errors.address" class="error">{{ errors.address }}</div>
                        <div class="dropdown-wrap" v-if="isDropdown">
                            <div class="dropdown-header">
                                <div
                                    v-for="item in dropdownHeader"
                                    :key="item.value"
                                    class="dropdown-header-item"
                                    :class="{ active: activeDropdown == item.value }"
                                    @click="switchDropdown(item.value)"
                                >
                                    {{ item.label }}
                                </div>
                            </div>
                            <div class="dropdown-body">
                                <div v-if="activeDropdown === 'city'">
                                    <div 
                                        v-for="city in locations?.cities" 
                                        :key="city.id" 
                                        class="dropdown-item"
                                        @click="chooseCity(city)">
                                        {{ city.name }}
                                    </div>
                                </div>
                                <div v-if="activeDropdown === 'district' && selected.city">
                                    <div 
                                        v-for="district in selected.city.districts" 
                                        :key="district.id" 
                                        class="dropdown-item"
                                        @click="chooseDistrict(district)">
                                        {{ district.name }}
                                    </div>
                                </div>
                                <div v-if="activeDropdown === 'ward' && selected.district">
                                    <div 
                                        v-for="ward in selected.district.wards" 
                                        :key="ward.id" 
                                        class="dropdown-item"
                                        @click="chooseWard(ward)">
                                        {{ ward.name }}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="input-group">
                <div class="input-box">
                    <div class="label">Địa chỉ cụ thể</div>
                    <textarea name="streetAddress" id="streetAddress" v-model="contact.streetAddress"></textarea>
                    <div v-if="errors.streetAddress" class="error">{{ errors.streetAddress }}</div>
                </div>
            </div>
            <div class="main-contact">
                <div class="main-contact-label">Liên hệ chính</div>
                <input type="checkbox" v-model="isMainContact">
            </div>
            <div class="button-group">
                <button @click="close" class="close-btn">Đóng</button>
                <button @click="handleUpdateContact" class="update-btn">Cập nhật</button>
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
        flex-direction: column;
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
    .dropdown-wrap{
        position: absolute;
        top: 49px;
        z-index: 10;
        background-color: #ffffff;
        width: 100%;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        border: 1px solid rgba(0, 0, 0, .2);
        border-radius: 4px;
    }
    .dropdown-header{
        display: flex;
        box-sizing: border-box;
        border-bottom: 1px solid rgba(0, 0, 0, .2);
    }
    .dropdown-header-item{
        flex: 1;
        display: flex;
        align-items: center;
        padding: 12px 12px;
        justify-content: center;
        cursor: pointer;
    }
    .active{
        border-bottom: 2px solid var(--main-color);
        color: var(--main-color);
    }
    .dropdown-item{
        padding: 10px 12px;
        cursor: pointer;
        &:hover {
            background-color: #f0f0f0;
        }
    }
    .error {
        color: red;
        font-size: 13px;
        margin-top: 4px;
    }
</style>
