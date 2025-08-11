<script setup>
  import Header from "@/components/Header.vue";
  import { ref, onMounted, onUnmounted } from "vue";
  import ChoiceSlider from "@/components/ChoiceSlider.vue";

  const sliderIdPrefix = 'slider-image-';
  const sliderImages = ref([
    new URL('@/assets/icons/1920x640p2.avif', import.meta.url).href,
    'https://cdn.shopvnb.com/img/1920x640/uploads/slider/astrox88-sd-key-visual-2880x1120-_1718650445.webp',
    'https://cdn.shopvnb.com/img/1920x640/uploads/slider/thiet-ke-chua-co-ten-12_1727137763.webp'
  ]);
  const sliderIndex = ref(0);
  let sliderInterval = null;

  const categories = [
    { id: 0, name: 'Tất Cả Sản Phẩm' },
    { id: 1, name: 'Vợt Cầu Lông' },
    { id: 2, name: 'Giày Cầu Lông' },
    { id: 3, name: 'Quần Áo Cầu Lông' },
    { id: 4, name: 'Phụ Kiện Cầu Lông' },
    { id: 5, name: 'Ao Cầu Lông' },
    { id: 6, name: 'Khác' }
  ];
  const activeCategory = ref(categories[0].id);
  const changeCategory = (categoryId) => {
    activeCategory.value = categoryId;
  }

  const productListRef = ref(null);
  const scrollProductSaleOff = (direction) => {
    const container = productListRef.value;
    if (!container) return;

    const product = container.querySelector('.product');
    if (!product) return;

    const style = getComputedStyle(product);
    const marginRight = parseInt(style.marginRight) || 0;

    const scrollAmount = product.offsetWidth + marginRight;
    if (direction === 'right') {
      container.scrollBy({ left: scrollAmount, behavior: 'smooth' });
    } else {
      container.scrollBy({ left: -scrollAmount, behavior: 'smooth' });
    }
  }

  onMounted(() => {
    sliderInterval = setInterval(() => {
      const slider = document.querySelector('.slider');
      if (slider) {
        sliderIndex.value = (sliderIndex.value + 1) % sliderImages.value.length;
        if (sliderIndex.value != 0)
          slider.scrollLeft += slider.offsetWidth;
        else
          slider.scrollLeft = 0;
      }
    }, 4000);
  })
  onUnmounted(() => {
    clearInterval(sliderInterval);
  });
</script>

<template>
  <Header />
  <div class="slider" ref="slider">
    <img
        v-for="(image, index) in sliderImages"
        :key="index"
        :src="image"
        :id="`${sliderIdPrefix}-${index}`"
        alt="Slider Image"
    />
  </div>
  <div class="policies">
    <div class="policy">
      <font-awesome-icon class="policy-icon" :icon="['fas', 'car']" />
      <p>
        Vận chuyển <span class="highlight">TOÀN QUỐC</span>
        <br>
        Thanh toán khi nhận hàng
      </p>
    </div>
    <div class="policy">
      <font-awesome-icon class="policy-icon" :icon="['fas', 'star']" />
      <p>
        <span class="highlight">Bảo đảm chất lượng</span>
        <br>
        Sản phẩm bảo đảm chất lượng.
      </p>
    </div>
    <div class="policy">
      <font-awesome-icon class="policy-icon" :icon="['fas', 'money-bill']" />
      <p>
        Tiến hành <span class="highlight">THANH TOÁN</span>
        <br>
        Với nhiều <span class="highlight">PHƯƠNG THỨC</span>
      </p>
    </div>
    <div class="policy">
      <font-awesome-icon class="policy-icon" :icon="['fas', 'rotate']" />
      <p>
        <span class="highlight">Đổi sản phẩm mới</span>
        <br>
        nếu sản phẩm lỗi
      </p>
    </div>
  </div>
  <div class="sale-off">
    <div class = "sale-off-container">
      <h2>Sale Off</h2>
    </div>
    <div class="sale-off-product">
      <ChoiceSlider 
        :categories="categories"
        :activeCategory="activeCategory"
        @changeCategory="changeCategory"
      />
      <div class="products">
        <div class="product-scroll">
          <div class="prev-scroll scroll-btn" @click="scrollProductSaleOff('left')">
            <font-awesome-icon class="btn" icon="fa-solid fa-circle-chevron-left" />
          </div>
          <div class="next-scroll scroll-btn" @click="scrollProductSaleOff('right')">
            <font-awesome-icon class="btn" icon="fa-solid fa-circle-chevron-right" />
          </div>
        </div>
        <div class="product-list" ref="productListRef">
          <div class="product">
            <img src="https://cdn.shopvnb.com/img/300x300/uploads/san_pham/vot-cau-long-vnb-v200-do-2.webp" alt="racket">
            <a>Vợt Cầu Lông VNB V200 Đỏ</a>
            <span class="price">1.200.000 <span class="underline">đ</span></span>
          </div>
          <div class="product">
            <img src="https://cdn.shopvnb.com/img/300x300/uploads/san_pham/vot-cau-long-vnb-v200-do-2.webp" alt="racket">
            <a>Vợt Cầu Lông VNB V200 Đỏ</a>
            <span class="price">1.200.000 <span class="underline">đ</span></span>
          </div>
          <div class="product">
            <img src="https://cdn.shopvnb.com/img/300x300/uploads/san_pham/vot-cau-long-vnb-v200-do-2.webp" alt="racket">
            <a>Vợt Cầu Lông VNB V200 Đỏ</a>
            <span class="price">1.200.000 <span class="underline">đ</span></span>
          </div>
          <div class="product">
            <img src="https://cdn.shopvnb.com/img/300x300/uploads/san_pham/vot-cau-long-vnb-v200-do-2.webp" alt="racket">
            <a>Vợt Cầu Lông VNB V200 Đỏ</a>
            <span class="price">1.200.000 <span class="underline">đ</span></span>
          </div>
          <div class="product">
            <img src="https://cdn.shopvnb.com/img/300x300/uploads/san_pham/vot-cau-long-vnb-v200-do-2.webp" alt="racket">
            <a>Vợt Cầu Lông VNB V200 Đỏ</a>
            <span class="price">1.200.000 <span class="underline">đ</span></span>
          </div>
          <div class="product">
            <img src="https://cdn.shopvnb.com/img/300x300/uploads/san_pham/vot-cau-long-vnb-v200-do-2.webp" alt="racket">
            <a>Vợt Cầu Lông VNB V200 Đỏ</a>
            <span class="price">1.600.000 <span class="underline">đ</span></span>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<style scoped>
  .slider {
    width: 100%;
    max-width: 100vw;
    overflow-x: hidden;
    display: flex;
    flex-direction: row;
    scroll-behavior: smooth;
    position: relative;
    aspect-ratio: 1920 / 640;
    margin-bottom: 30px;
  }

  .slider img {
    width: 100%;
    height: auto;
    display: block;
    flex-shrink: 0;
    object-fit: fill;
  }
  .policies{
    width: 85vw;
    margin: 0 auto;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
  }
  .policy{
    text-align: left;
    display: flex;
    justify-content: center;
    align-items: center;
    color: var(--main-color);
    border: 1px solid #cccccc;
    padding: 7px 10px;
    border-radius: 10px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
    width:23%;
    margin-bottom: 30px;
  }
  .highlight{
    font-weight: bold;
  }
  .policy-icon{
    font-size: 36px;
    margin-right: 10px;
  }
  .sale-off{
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 85vw;
    margin: 0 auto;
  }
  .sale-off-container{
    color: var(--main-color);
    margin-bottom: 30px;
    position: relative;
    text-align: center;
    display: inline-block;
    height: 65px;
    width: fit-content;
    &:before {
      content: '';
      position: absolute;
      width: 50px;
      height: 5px;
      background-color: var(--main-color);
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      border-radius: 10px;
    }
    &:after {
      content: '';
      position: absolute;
      width: 140%;
      height: 5px;
      background-color: #cfcbcb;
      z-index: -2;
      bottom: 0;
      right: 50%;
      transform: translateX(50%);
      border-radius: 10px;
    }
  }
  .sale-off-container h2 {
    font-size: 2rem;
  }
  .sale-off-product{
    width: 100%;
  }
  .products{
    margin-bottom: 60px;
    position: relative;
  }
  .product-list{
    display: flex;
    background-color: #eda44c;
    align-items: center;
    margin: 0 auto;
    padding: 25px 20px 15px 20px;
    overflow-x: auto;
    scroll-behavior: smooth;
    width: 85vw;
  }
  .product-list::-webkit-scrollbar {
    display: none;
  }
  .product{
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: space-between;
    background: #ffffff;
    padding: 15px;
    &:not(:last-child){
      margin-right: 15px;
    }
  }
  .product img{
    width: 212px;
    height: 212px;
  }
  .price{
    color: #e24b4b;
    font-weight: bold;
  }
  .underline{
    text-decoration: underline;
    font-weight: inherit;
    color: inherit;
  }
  .product-scroll {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    pointer-events: none; /* Quan trọng: không chặn sự kiện lên .product-list */
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .scroll-btn{
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    cursor: pointer;
    z-index: 1;
    background: none;
    pointer-events: auto;
  }
  .next-scroll{
    right: 5px;
  }
  .prev-scroll{
    left: 5px;
  }
  .scroll-btn .btn {
    font-size: 36px;
  }

</style>
