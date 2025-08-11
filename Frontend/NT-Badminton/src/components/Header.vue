<script setup>
  import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
  import {getCategories} from "@/api/category.js";
  import { ref, onMounted } from 'vue';
  import HeaderCartItem from "@/components/HeaderCartItem.vue";

  const cartItems = ref([
    {
      id: 1,
      name: "Racket Yonex Voltric Z Force II",
      img: "https://cdn.shopvnb.com/img/64x64/uploads/san_pham/vot-cau-long-vnb-v200i-hong-3.webp",
      price: 100000,
      quantity: 1
    }
  ]);

  const categories = ref(null);

  onMounted(() => {
    (async () => {
      try {
        const fetchedCategories = await getCategories();
        categories.value = fetchedCategories;
        console.log("Categories fetched successfully:", categories.value);
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    })();
  });
</script>

<template>
  <div class="container">
    <div class="header-top">
      <div class="header-logo">
        <router-link to="/" class="logo-link">
          <img class="web-logo" src="@/assets/icons/logo.jpg" alt="Logo" />
        </router-link>
      </div>
      <div class="header-middle">
        <ul class="menu middle-top-menu">
          <li class="middle-menu-item">
            <a class="store-location" href="">
              <font-awesome-icon :icon="['fas', 'location-dot']" />
              <span>Địa điểm cửa hàng</span>
            </a>
          </li>
          <li class="middle-menu-item search-bar">
            <input type="text" placeholder="Tìm kiếm"/>
            <font-awesome-icon class="search-icon" :icon="['fas', 'search']" />
          </li>
        </ul>
      </div>
      <div class="top-right-menu">
        <ul class="menu">
          <li class="header-item">
            <a class="top-right-item" href="">
              <font-awesome-icon :icon="['fas', 'user']" />
              <span class="item-text">Tài khoản</span>
            </a>
            <div class="user-choice dropdown">
              <router-link to="/register" class="dropdown-item">Đăng ký</router-link>
              <router-link to="/login" class="dropdown-item">Đăng nhập</router-link>
            </div>
          </li>
          <li class="header-item">
            <a class="top-right-item" href="">
              <font-awesome-icon :icon="['fas', 'cart-shopping']" />
              <span class="item-text">Giỏ hàng</span>
            </a>
            <div class="cart-dropdown dropdown">
              <div class="cart-title">Giỏ hàng</div>
              <div class="cart-items">
                <HeaderCartItem
                  v-for="item in cartItems" :key="item.id"
                  :item="item"
                />
              </div>
              <div class="cart-end">
                <div class="total">
                  <span class="total-word">Tổng tiền:</span>
                  <span class="total-price">100.000đ</span>
                </div>
                <button class="cart-btn">Thanh toán</button>
              </div>
            </div>
          </li>
          <li class="header-item">
            <a class="top-right-item" href="">
              <font-awesome-icon :icon="['fas', 'box-open']" />
              <span class="item-text">Đơn hàng</span>
            </a>

          </li>

        </ul>
      </div>
    </div>
    <ul class="header-bottom menu">
      <li class="nav-item">
        <router-link to="/" class="highlight">
          <span class="item-text highlight">Trang chủ</span>
        </router-link>
      </li>
      <li class="nav-item product-menu">
        <a href="#" class="highlight">
          Sản phẩm <font-awesome-icon class="product-icon" :icon="['fas', 'down-long']" />
        </a>
        <ul class="product-category">
          <li
              v-for="category in categories" :key="category.id"
              class="category-item">
            <a href="">{{category.name}}</a>
            <ul class="subcategory-list">
              <li
                  v-for="subCategory in category.children"
                  class="subcategory-item">
                <a href="#">{{subCategory.name}}</a>
              </li>
            </ul>
          </li>
        </ul>
      </li>
    </ul>
  </div>

</template>

<style scoped>
  /* === LAYOUT === */
  .container {
    width: 100%;
    height: 120px;
    top: 0;
    z-index: 1000;
    display: grid;
    grid-template: 66% 1fr / 100%;
  }

  .menu, .header-bottom, .header-top {
    display: flex;
    flex-direction: row;
  }

  .header-top {
    justify-content: center;
    width: 100%;
    height: 100%;
    align-items: center;
    margin: 0 auto;
  }

  .header-bottom {
    width: 100%;
    height: 100%;
    background-color: var(--main-color);
    justify-content: center;
    align-items: center;
    gap: 30%;
    list-style-type: none;
    position: relative;
  }

  /* === HEADER MIDDLE === */
  .header-middle {
    width: 50vw;
    padding: 10px;
  }

  .store-location, .middle-menu-item {
    display: flex;
    align-items: center;
  }

  .store-location {
    margin-right: 40px;
    gap: 8px;
    text-decoration: none;
    color: #000;
  }

  .store-location:hover {
    color: var(--main-color);
  }

  .search-bar {
    width: 70%;
    background-color: #e6e5e5;
    padding: 10px;
    border-radius: 25px;
    position: relative;
  }

  .search-bar input {
    width: 100%;
    height: 100%;
    padding: 0 20px;
    border-radius: 25px;
    font-size: 16px;
    color: #000;
    background-color: transparent;
    border: none;
  }

  .search-bar input:focus {
    outline: none;
  }

  .search-icon {
    position: absolute;
    right: 20px;
    top: 50%;
    transform: translateY(-50%);
    color: #000;
  }

  /* === HEADER RIGHT === */
  .header-top li {
    display: flex;
    flex-direction: column;
  }

  .header-item {
    position: relative;
  }

  .header-item:not(:last-child) {
    margin-right: 50px;
  }

  .header-item a {
    height: 100%;
    text-decoration: none;
    color: #000;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-end;
  }

  .header-item a:hover {
    color: var(--main-color);
  }

  .header-top .middle-menu-item {
    flex-direction: row;
  }

  /* === DROPDOWN === */
  .header-item:hover .dropdown {
    display: block;
  }

  .dropdown {
    display: none;
    position: absolute;
    top: 100%;
    min-width: 150px;
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 6px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    left: 50%;
    transform: translateX(-50%);
    z-index: 1000;
  }

  .dropdown a {
    display: block;
    text-align: center;
    padding: 5px 0;
  }

  .dropdown a:hover {
    background-color: #f0f0f0;
  }

  /* === CART DROPDOWN === */
  .cart-dropdown {
    display: none;
    position: absolute;
    top: 100%;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    overflow: hidden;
    left: 50%;
    transform: translateX(-50%);
  }

  .cart-title {
    font-weight: bold;
    padding: 10px;
    background-color: var(--main-color);
    color: #fff;
    text-align: center;
  }

  .item-quantity-choice input {
    width: 50px;
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
  }

  .cart-end {
    display: flex;
    flex-direction: column;
    background-color: #e6e5e5;
    padding: 10px;
  }

  .total {
    display: flex;
    margin-bottom: 10px;
  }

  .total-word, .total-price {
    font-weight: bold;
  }

  .total-price {
    margin-left: auto;
    color: #e24b4b;
  }

  .cart-btn {
    width: 100%;
    padding: 10px;
    background-color: var(--main-color);
    border: none;
    color: #ffffff;
  }

  .cart-btn:hover {
    background-color: #ffffff;
    color: var(--main-color);
    cursor: pointer;
    border: 1px solid var(--main-color);
  }
  .product-category{
    display: none;
    position: absolute;
    width: 100%;
    left: 0;
    top: 100%;
    z-index: 10;
    grid-template-columns: repeat(5, 1fr);
    max-height: 200px;
    overflow-x: scroll;
    background-color: #ffffff;
  }
  .header-bottom a{
    text-decoration: none;
    color: #000000;
  }
  .nav-item > a{
    color: #ffffff;
  }
  .category-item{
    list-style: none;
    text-align: left;
    padding: 15px;
  }
  .subcategory-list{
    padding: 0;
  }
  .subcategory-item{
    padding: 5px 0;
    list-style: none;
    text-align: left;
  }
  .subcategory-item a{
    color: #5b5b5b;
    text-decoration: none;
  }
  .category-item > a {
    color: var(--main-color);
    display: block;
    padding-bottom: 2px;
    border-bottom: 2px solid #adadad;
    width: 100%;
    box-sizing: border-box;
  }
  .product-menu:hover .product-category{
    display: grid;
  }
  .product-menu{
    height: 100%;
  }
  .product-menu > a{
    height: 100%;
    display: flex;
    align-items: center;
    z-index: 999;
  }
  ul{
    padding: 0;
  }
  .highlight{
    font-weight: bold;
  }
  .product-icon{
    margin-left: 4px;
    font-size: 7px;
    transform: translateY(2px);
    display: inline-block;
    transition: transform 0.3s ease;
  }
  .product-menu:hover .product-icon {
    transform: translateY(2px) rotate(180deg);
    color: #000000;
  }
  /* Chrome, Safari, Edge (WebKit/Blink) */
  input[type=number]::-webkit-inner-spin-button,
  input[type=number]::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }

  /* Firefox */
  input[type=number] {
    -moz-appearance: textfield;
  }

  /* (tùy chọn) giữ padding/size ổn */
  input[type=number] {
    /* nếu muốn, đảm bảo vẫn rõ ràng là số */
    -webkit-appearance: none;
    appearance: none;
  }
</style>