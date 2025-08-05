<script setup>
  import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
  import { ref, computed } from "vue";
  const categories = [
    { id: "shoes", label: "Giày", value: "shoes" },
    { id: "clothing", label: "Quần áo", value: "clothing" },
    { id: "accessories", label: "Phụ kiện", value: "accessories" },
    { id: "equipment", label: "Thiết bị", value: "equipment" },
    { id: "bags", label: "Túi xách", value: "bags" },
    { id: "rackets", label: "Vợt cầu lông", value: "rackets" },
    { id: "shuttlecocks", label: "Quả cầu lông", value: "shuttlecocks" },
    { id: "strings", label: "Dây vợt", value: "strings" },
    { id: "grips", label: "Grip vợt", value: "grips" },
  ];

  const brands = [
    { id: "nike", label: "Nike", value: "nike" },
    { id: "adidas", label: "Adidas", value: "adidas" },
    { id: "puma", label: "Puma", value: "puma" },
  ];

  const ratings = [5, 4, 3, 2, 1];

  const searchQuery = ref("t");

  const sortOptions = [
    {value: "Mặc định"},
    {value: "Giá tăng dần"},
    {value: "Giá giảm dần"},
    {value: "Đánh giá cao nhất"},
  ];

  const sortMode = ref("Mặc định");

  const products = [
    {id: 3, name: "Vợt cầu lông Yonex Astrox 99", price: 1500000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 4, name: "Giày cầu lông Yonex Power Cushion Aerus 3", price: 2500000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 5, name: "Quả cầu lông Yonex AS-50", price: 500000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 6, name: "Dây vợt cầu lông Yonex BG66 Ultimax", price: 200000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 7, name: "Grip vợt cầu lông Yonex Super Grap", price: 100000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 8, name: "Túi vợt cầu lông Yonex Pro Bag", price: 800000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 9, name: "Quần áo cầu lông Yonex Team", price: 600000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 10, name: "Vợt cầu lông Victor Thruster K", price: 1800000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 11, name: "Giày cầu lông Li-Ning Windstorm 72", price: 2200000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 12, name: "Quả cầu lông RSL Classic Tourney", price: 600000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 13, name: "Dây vợt cầu lông Ashaway ZyMax 62", price: 180000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 14, name: "Grip vợt cầu lông Babolat VS Original", price: 120000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 15, name: "Túi vợt cầu lông Victor Bag 9226", price: 900000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 16, name: "Quần áo cầu lông Li-Ning Super Series", price: 700000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 17, name: "Vợt cầu lông Wilson Blade 98", price: 2000000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 18, name: "Giày cầu lông Mizuno Wave Fang", price: 2400000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 19, name: "Quả cầu lông Carlton GT1", price: 550000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 20, name: "Dây vợt cầu lông Tecnifibre 305", price: 150000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 21, name: "Grip vợt cầu lông Head Hydrosorb", price: 110000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 22, name: "Túi vợt cầu lông Yonex 9826", price: 950000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 23, name: "Quần áo cầu lông Victor Team", price: 650000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
    {id: 24, name: "Vợt cầu lông Yonex Nanoflare 800", price: 1700000, image: "https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp"},
  ]

  const pageSize = 20;
  const currentPage = ref(0); // Trang backend bắt đầu từ 0
  const totalPages = ref(8);

  function goToPage(page) {
    if (page >= 0 && page < totalPages.value) {
      currentPage.value = page;
      fetchProducts();
    }
  }
  const visiblePages = computed(() => {
    const pages = [];
    const start = Math.max(0, currentPage.value - 1);
    const end = Math.min(start + 3, totalPages.value - 1);
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    return pages;
  });

</script>

<template>
  <Header class="header"></Header>
  <div class="content">
    <div class="filter-container">
      <div class="filter-title">
        <font-awesome-icon :icon="['fas', 'filter']"/>
        <h2>Bộ lọc sản phẩm</h2>
      </div>
      <div class="filter-options">
        <div class="filter-option">
          <h3 class="header-option">
            Danh mục sản phẩm:
          </h3>
          <ul class="options scroll-limit">
            <li class="option-item" v-for="cat in categories" :key="cat.id">
              <input type="checkbox" :id="cat.id" name="category" :value="cat.value" />
              <label :for="cat.id">{{ cat.label }}</label>
            </li>
          </ul>
        </div>
        <div class="filter-option price-filter">
          <label for="price-range">Khoảng giá:</label>
          <div class="price-input-container">
            <input type="text" class="bottom-price" name="price-range-bottom" id="price-range-bottom" autocomplete="off" placeholder="đ&#x0332 Từ">
            <span>-</span>
            <input type="text" class="top-price" name="price-range-top" id="price-range-top" autocomplete="off" placeholder="đ&#x0332 Đến">
          </div>

        </div>
        <div class="filter-option">
          <h3 class="header-option">
            Thương hiệu:
          </h3>
          <ul class="options scroll-limit">
            <li class="option-item" v-for="brand in brands" :key="brand.id">
              <input type="checkbox" :id="brand.id" name="brand" :value="brand.value" />
              <label :for="brand.id">{{ brand.label }}</label>
            </li>
          </ul>
        </div>
        <div class="filter-option">
          <h3 class="header-option">
            Đánh giá:
          </h3>
          <ul class="options">
            <li class="option-item" v-for="star in ratings" :key="star">
              <input class="hidden" type="radio" :id="'rating-' + star" name="rating" :value="star" />
              <label :for="'rating-' + star" class="star-label">
                <span class="stars">
                  <font-awesome-icon
                      v-for="i in 5"
                      :key="i"
                      :icon="i <= star ? ['fas', 'star'] : ['far', 'star']"
                      class="star-icon"
                      size="sm"
                  />
                </span>
                <span v-if="star < 5" class="suffix"> trở lên</span>
              </label>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <div class="product-list-container">
      <div class="product-list-header">
        <div class="search-title">
          <h2 v-if="searchQuery">
            Kết quả tìm kiếm sản phẩm theo:
            <span class="search-query">{{ searchQuery }}</span>
          </h2>
          <h2 v-else>
            Tất cả sản phẩm
          </h2>
        </div>
        <div class="sort-container">
            <span class="highlight">
              <font-awesome-icon :icon="['fas', 'sort']" />
              Sắp xếp:
            </span>
          <div class="dropdown-sort-menu">
            <span>{{sortMode}}</span>
            <ul class="sort-choice">
              <li v-for="option in sortOptions" :key="option.value">
                <button class="sort-btn" @click="sortMode = option.value">{{ option.value }}</button>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="product-list">
        <div class="product-item" v-for="product in products" :key="product.id">
          <router-link :to="`/product/${product.id}`" class="product-link">
            <img class="product-image" :src="product.image" alt="Product Image" />
            <h3>{{ product.name }}</h3>
          </router-link>
          <p class="price">{{ product.price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }) }}</p>
        </div>
      </div>
      <div class="pagination">
        <button @click="goToPage(currentPage - 1)" :disabled="currentPage === 0">←</button>

        <button
            v-for="page in visiblePages"
            :key="page"
            :class="{ active: currentPage === page }"
            @click="goToPage(page)"
        >
          {{ page + 1 }}
        </button>

        <button @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPages - 1">→</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
  body{
    background-color: #f9f9f9;
    font-family: Arial, sans-serif;
  }
  .content{
    display: flex;
    justify-content: center;
    width: 85vw;
    margin: 0 auto;
  }
  .product-list-container{
    flex: 1;
    margin-left: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  }
  .header{
    margin-bottom: 20px;
  }
  .filter-container{
    width: 270px;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  }
  .filter-title{
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 15px;
  }
  h2{
    font-weight: bold;
    margin: 0;
  }
  .filter-option{
    display: flex;
    flex-direction: column;
    margin-bottom: 5px;
    padding-bottom: 20px;
    gap: 10px;
  }
  .filter-option:not(:last-child){
    border-bottom: 1px solid #ccc;
  }
  .options{
    width: 100%;
    padding: 0;
    display: flex;
    flex-direction: column;
    list-style-type: none;
    gap: 10px;
    overflow-y: auto;
    margin-bottom: 10px;

  }
  .scroll-limit{
    max-height: calc((1.5rem + 10px) * 4);
  }
  .option-item{
    display: flex;
    align-items: center;
    gap: 9px;
    width: 100%;
    line-height: 1.5rem;
    height: 1.5rem;
  }
  .price-input-container{
    width: 100%;
    display: flex;
    gap: 10px;
    flex-wrap: nowrap;
    align-items: center;
    justify-content: space-between;
  }
  .price-input-container input {
    flex: 1;
    padding: 3px;
    max-width: 43%;
    box-sizing: border-box;
  }
  .stars {
    display: inline-flex;
    gap: 2px;
    color: #faca51;
  }
  .star-label{
    display: flex;
    align-items: center;
    cursor: pointer;
  }
  .star-icon {
    width: 16px;
    height: 16px;
  }

  .suffix {
    margin-left: 6px;
    color: #555;
    font-size: 14px;
  }
  .hidden{
    display: none;
  }
  .highlight{
    font-weight: bold;
  }
  .product-list-header{
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    background: #f4f4f4;
    padding: 10px;
    border-radius: 8px 8px 0 0;
  }
  .dropdown-sort-menu{
    position: relative;
    display: inline-block;
    cursor: pointer;
  }
  .dropdown-sort-menu span{
    margin-left: 5px;
  }
  .sort-choice{
    display: none;
    list-style: none;
    padding: 5px;
    position: absolute;
    top: 100%;
    right: 0;
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    z-index: 10;
    flex-direction: column;
    width: 180px;
  }
  .sort-btn{
    background: none;
    border: none;
    padding: 10px;
    text-align: left;
    width: 100%;
    cursor: pointer;
    color: #333;
  }
  .sort-btn:hover{
    background-color: var(--main-color);
    color: #fff;
  }
  .dropdown-sort-menu:hover .sort-choice{
    display: flex;
  }
  .product-list {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(230px, 1fr));
    gap: 20px;
  }
  .product-item{
    width: 230px;
    padding: 15px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
  .product-image{
    width: 100%;
    object-fit: cover;
    border-radius: 8px;
  }
  .product-link{
    text-decoration: none;
    color: inherit;
  }
  .price {
    font-weight: bold;
    color: var(--main-color);
    margin-top: 10px;
  }
  .pagination{
    display: flex;
    justify-content: right;
    align-items: center;
    padding: 10px;
    gap: 10px;
    margin: 10px;
  }
  .pagination button{
    background-color: #ffffff;
    border: 1px solid var(--main-color);
    color: var(--main-color);
    border-radius: 4px;
    padding: 5px 10px;
    cursor: pointer;
  }
  .pagination button:disabled{
    background-color: #f0f0f0;
    color: #ccc;
    cursor: not-allowed;
    transition: background-color 0.3s ease;
  }
  .pagination button.active{
    background-color: var(--main-color);
    color: #ffffff;
    transition: background-color 0.3s ease;
  }


</style>