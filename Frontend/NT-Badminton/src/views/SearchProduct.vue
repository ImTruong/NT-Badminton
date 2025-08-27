<script setup>
  import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
  import { ref, computed, onMounted, watch, reactive } from "vue";
  import { getRootCategories } from "@/api/category";
  import { searchProducts, getAllProductBrands } from "@/api/product";
  import { useRoute, useRouter } from "vue-router";
  const route = useRoute();
  const router = useRouter();

  const categories = ref(null);

  const brands = ref(null);

  const ratings = [5, 4, 3, 2, 1, 0];


  const searchQuery = computed(() => route.query.productName || " ");

  const filters = reactive({
    name: ref(''),
    categoryIds: ref([]),
    brands: ref([]),
    minPrice: ref(null),
    maxPrice: ref(null),
    rating: ref(null),
  });

  const pageSize = ref(20);
  const currentPage = ref(0);
  const totalPages = ref(8);

  watch(
    [() => route.query, currentPage, pageSize],
    ([newQuery]) => {
      filters.name = newQuery.productName || '';
      filters.categoryIds = newQuery.categoryIds
        ? newQuery.categoryIds.split(',').map(id => parseInt(id))
        : [];
      filters.brands = newQuery.brands ? newQuery.brands.split(',') : [];
      filters.minPrice = newQuery.minPrice ? parseFloat(newQuery.minPrice) : null;
      filters.maxPrice = newQuery.maxPrice ? parseFloat(newQuery.maxPrice) : null;
      filters.rating = newQuery.rating ? parseInt(newQuery.rating) : null;
      console.log(filters);
      fetchProducts();
    },
    { immediate: true, deep: true }
  );
  watch(searchQuery, (newValue) => {
    filters.name = newValue;
  });
  watch(filters, () => {
    currentPage.value = 0; 
    updateRoute();
  }, { deep: true });

  const updateRoute = () => {
    const query = {
      productName: filters.name || undefined,
      categoryIds: filters.categoryIds.length > 0 ? filters.categoryIds.join(',') : undefined,
      brands: filters.brands.length > 0 ? filters.brands.join(',') : undefined,
      minPrice: filters.minPrice || undefined,
      maxPrice: filters.maxPrice || undefined,
      rating: filters.rating || undefined,
    };
    router.push({ path: '/search', query });
  };

  async function fetchProducts() {
    try {
      const fetchedProducts = await searchProducts({ ...filters, page: currentPage.value, size: pageSize.value });
      products.value = fetchedProducts.content;
      totalPages.value = fetchedProducts.totalPages;
    } catch (error) {
      console.error("Error fetching products:", error);
    }
  }

  const sortOptions = [
    {value: "Mặc định"},
    {value: "Giá tăng dần"},
    {value: "Giá giảm dần"},
    {value: "Đánh giá cao nhất"},
  ];

  const sortMode = ref("Mặc định");

  const products = ref([]);

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

  onMounted(async () => {

    await fetchProducts();

    try {
      const fetchedCategories = await getRootCategories();
      categories.value = fetchedCategories;
    } catch (error) {
      console.error("Error fetching categories:", error);
    }

    try {
      const fetchedBrands = await getAllProductBrands();
      brands.value = fetchedBrands;
    } catch (error) {
      console.error("Error fetching brands:", error);
    }
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
              <input type="checkbox" 
                :id="cat.id" name="category" 
                :value="cat.id" 
                v-model="filters.categoryIds" />
              <label :for="cat.id">{{ cat.name }}</label>
            </li>
          </ul>
        </div>
        <div class="filter-option price-filter">
          <label for="price-range">Khoảng giá:</label>
          <div class="price-input-container">
            <input type="text" class="bottom-price" name="price-range-bottom" id="price-range-bottom" autocomplete="off" placeholder="đ&#x0332 Từ" v-model="filters.minPrice">
            <span>-</span>
            <input type="text" class="top-price" name="price-range-top" id="price-range-top" autocomplete="off" placeholder="đ&#x0332 Đến" v-model="filters.maxPrice">
          </div>
        </div>
        <div class="filter-option">
          <h3 class="header-option">
            Thương hiệu:
          </h3>
          <ul class="options scroll-limit">
            <li class="option-item" v-for="brand in brands" :key="brand">
              <input type="checkbox" :id="brand" name="brand" :value="brand" v-model="filters.brands" />
              <label :for="brand">{{ brand }}</label>
            </li>
          </ul>
        </div>
        <div class="filter-option">
          <h3 class="header-option">
            Đánh giá:
          </h3>
          <ul class="options">
            <li class="option-item" v-for="star in ratings" :key="star">
              <input class="hidden" type="radio" :id="'rating-' + star" name="rating" :value="star" v-model="filters.rating" />
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
            <img class="product-image" src="https://cdn.shopvnb.com/img/300x300/uploads/san_pham/giay-cau-long-taro-tr024-1_1732240510.webp" alt="Product Image" />
            <h3>{{ product.name }}</h3>
          </router-link>
          <div class="price-wrap">
            <span class="price">{{ product.price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }) }}</span>
            <span class="old-price">{{ product.priceBeforeDiscount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }) }}</span>
          </div>
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
  .price-wrap {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    gap: 10px;
  }
  .old-price{
    text-decoration: line-through;
    color: #999;
  }


</style>