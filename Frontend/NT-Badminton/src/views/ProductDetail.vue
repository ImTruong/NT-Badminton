<script setup>
  import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
  import {ref, onMounted, nextTick, reactive, onBeforeMount} from 'vue';
  import { useRoute } from "vue-router";
  import { getProductDetail } from "@/api/product";

  const route = useRoute();

  const product = ref(null);

  const tmpHeadImg = 'https://cdn.shopvnb.com';
  const tmpMiddleImgLink = '/img/300x300';
  const headContentImages = [
    '/uploads/san_pham/vot-cau-long-vnb-carbon-training-150g-5.webp',
    '/uploads/san_pham/vot-cau-long-vnb-carbon-training-150g-3.webp',
    '/uploads/san_pham/vot-cau-long-vnb-carbon-training-150g-2.webp'
  ];
  const quantity = ref(1);
  const tabMode = ref('description'); // Mặc định là mô tả
  // Refs
  const mainList = ref(null);
  const thumbnails = ref(null);
  const currentIndex = ref(0);

  // Drag state
  let isDown = false;
  let startX = 0;
  let scrollLeftStart = 0;
  let snapTimeout = null;

  const slideWidth = () => {
    if (!mainList.value) return 0;
    const first = mainList.value.querySelector('.img-item-main');
    if (!first) return 0;
    const style = getComputedStyle(first);
    const marginRight = parseFloat(style.marginRight || 0);
    return first.getBoundingClientRect().width + marginRight;
  };

  const clamp = (v, min, max) => Math.min(Math.max(v, min), max);

  const updateCurrentIndexFromScroll = () => {
    const sw = slideWidth();
    if (sw === 0) return;
    const raw = mainList.value.scrollLeft / sw;
    currentIndex.value = clamp(Math.round(raw), 0, headContentImages.length - 1);
  };

  const snapToIndex = idx => {
    const target = clamp(idx, 0, headContentImages.length - 1);
    const sw = slideWidth();
    if (sw === 0) return;
    mainList.value.scrollTo({
      left: target * sw,
      behavior: 'smooth'
    });
    currentIndex.value = target;
  };

  const onPointerDown = e => {
    isDown = true;
    mainList.value.setPointerCapture?.(e.pointerId);
    startX = e.clientX;
    scrollLeftStart = mainList.value.scrollLeft;
    mainList.value.classList.add('grabbing');
    if (snapTimeout) {
      clearTimeout(snapTimeout);
      snapTimeout = null;
    }
  };

  const onPointerMove = e => {
    if (!isDown) return;
    const dx = e.clientX - startX;
    mainList.value.scrollLeft = scrollLeftStart - dx;
    updateCurrentIndexFromScroll();
  };

  const onPointerUp = () => {
    if (!isDown) return;
    isDown = false;
    mainList.value.classList.remove('grabbing');
    // Snap sau khi thả, delay nhỏ để tránh xung đột với pointer events
    snapTimeout = setTimeout(() => {
      snapToIndex(currentIndex.value);
    }, 50);
  };

  // Khi click thumbnail
  const onThumbClick = idx => {
    snapToIndex(idx);
  };

  const choosenOptions = ref([
  ]);
  const chooseOption = (optionId, valueId) => {
    const idx = choosenOptions.value.findIndex(o => o.optionId === optionId);
    if (idx !== -1) {
      // cập nhật valueId
      choosenOptions.value[idx].valueId = valueId;
    } else {
      // thêm mới
      choosenOptions.value.push({ optionId, valueId });
    }
  };
  const isSelected = (optionId, valueId) => {
    return choosenOptions.value.some(o => o.optionId === optionId && o.valueId === valueId);
  };

  const calAverageRating = () => {
    if (product.ratings.length === 0) return 0;
    const total = product.ratings.reduce((sum, r) => sum + r.rating, 0);
    return (total / product.ratings.length).toFixed(1);
  };

  const starAverageDetail = (star) => {
    const count = product.ratings.filter(r => r.rating === star).length;
    return {
      count,
      percentage: ((count / product.ratings.length) * 100).toFixed(1)
    };
  }

  const getFirstLetter = (name) => {
    if (!name || name.length === 0) return '';
    return name.charAt(0).toUpperCase();
  };

  const hoverRating = ref(0)          
  const selectedRating = ref(0)   

  const getStarIcon = (i) => {
    const active = hoverRating.value ? hoverRating.value : selectedRating.value
    return i <= active ? ['fas', 'star'] : ['far', 'star']
  }

  const getPriceForSelectedOption = () => {
    const selectedOption = choosenOptions.value.find(o => o.optionId === product.options[0].id);
    if (selectedOption) {
      const option = product.options.find(o => o.id === selectedOption.optionId);
      if (option) {
        const value = option.values.find(v => v.id === selectedOption.valueId);
        if (value) {
          return value.price;
        }
      }
    }
    return product.priceAfterDiscount;
  };

  onMounted(() => {
    window.addEventListener('resize', () => {
      snapToIndex(currentIndex.value);
    });

    mainList.value.addEventListener('scroll', () => {
      updateCurrentIndexFromScroll();
      if (snapTimeout) clearTimeout(snapTimeout);
      snapTimeout = setTimeout(() => {
        snapToIndex(currentIndex.value);
      }, 100);
    });

  });

  onBeforeMount(async () => {
    const productId = route.params.id;
    product.value = await getProductDetail(productId);
    console.log(product.value);
  });
</script>

<template>
  <Header></Header>
  <div class="content">
    <div class="product-cart">
      <div class="product-img">
        <!-- ảnh lớn (slider) -->
        <div
            class="img-main-list"
            ref="mainList"
            @pointerdown="onPointerDown"
            @pointermove="onPointerMove"
            @pointerup="onPointerUp"
            @pointerleave="onPointerUp"
            @pointercancel="onPointerUp"
        >
          <div
              class="slide-wrapper"
              v-for="(image, index) in headContentImages"
              :key="index"
          >
            <img
                :src="tmpHeadImg + image"
                alt="main"
                class="img-item-main"
                draggable="false"
                @dragstart.prevent
            />
          </div>
        </div>

        <!-- thumbnails -->
        <div class="img-sub-list" ref="thumbnails">
          <div
              class="thumb-wrapper"
              v-for="(image, index) in headContentImages"
              :key="index"
              :class="{ active: currentIndex === index }"
              @click="onThumbClick(index)"
          >
            <img
                :src="tmpHeadImg + tmpMiddleImgLink + image"
                alt="thumb"
                class="img-item-sub"
                draggable="false"
                @dragstart.prevent
            />
          </div>
        </div>
      </div>
      <div class="side-content">
        <h1 class="product-name">{{ product.name }}</h1>
        <div class="brand-stock">
          <span class="brand">Thương hiệu: <span class="highlight-text">{{ product.brand }}</span></span>
          <span class="line">&nbsp;&nbsp;|&nbsp;&nbsp;</span>
          <span class="stock">Tình trạng: <span class="stock highlight-text">Còn hàng</span></span>
        </div>
        <div class="price-container">
          <h2 class="highlight-text price">{{product.priceAfterDiscount}}<span class="underline price">đ</span> </h2>
          <span class="original-price"><del>Giá gốc: {{product.price}}<span class="underline">đ</span></del></span>
        </div>
        <div class="options">
          <div class="option" v-for="option in product.options" :key="option.id">
            <div class="option-name-container">
              <b class="option-name">{{ option.name }}:</b>
            </div>
            <div class="option-values">
              <div class="option-values">
                <div
                  v-for="(val, key) in option.values"
                  :key="key"
                  class="option-value-box"
                  :class="{ selected: isSelected(option.id, val) }"
                  @click="chooseOption(option.id, val)"
                >
                  {{ key }}
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- <div class="quantity-wrap">
          <span>
            Số lượng:
          </span>
          <div class="item-quantity-choice">
            <button class="btn reduce-quantity"
              @click="quantity = Math.max(1, quantity - 1)"
            >-</button>
            <input type="number" class="item-quantity" :value="quantity" min="1" />
            <button class="btn increase-quantity"
              @click="quantity++"
            >+</button>
          </div>
        </div>
        <button class="btn cart-btn">
          <FontAwesomeIcon icon="fa-solid fa-cart-plus" />
          Thêm vào giỏ hàng</button>
      </div>
    </div>
    <div class="tab-container">
      <div class="tabs" role="tablist" aria-label="Product information">
        <div
            class="desc-btn tab"
            role="tab"
            aria-selected="true"
            aria-controls="description-panel"
            id="description-tab"
            :class = "{ activeTab: tabMode == 'description' }"
            data-target="description-panel"
            @click="tabMode = 'description'"
        >
          Mô tả sản phẩm
        </div>
        <div
            class="review-btn tab"
            :class = "{ activeTab: tabMode == 'reviews' }"
            @click="tabMode = 'reviews'"
            role="tab"
            aria-selected="false"
            aria-controls="reviews-panel"
            id="reviews-tab"
            data-target="reviews-panel"
        >
          Đánh giá người dùng
        </div>
      </div>

      <div class="tab-panels">
        <div
            role="tabpanel"
            id="description-panel"
            aria-labelledby="description-tab"
            class="panel"
            :hidden="tabMode != 'description'"
        >
          <p>
            {{ product.description }}
          </p>
        </div>
        <div
            role="tabpanel"
            id="reviews-panel"
            aria-labelledby="reviews-tab"
            class="panel"
            :hidden="tabMode != 'reviews'"
        >
          <div class="rating-overview">
            <div class="average-rating">
              <span class="average-num">{{ calAverageRating() }} / 5</span>
              <div class="av-rating-star">
                <font-awesome-icon
                    v-for="i in 5"
                    :key="i"
                    :icon="i <= calAverageRating() ? ['fas', 'star'] : ['far', 'star']"
                    class="star-icon"
                    size="sm"
                />
              </div>
            </div>
            <div class="star-rating">
              <div class="progress-bar-container">
                <span>5 <FontAwesomeIcon icon="fa-solid fa-star" class="star-icon" /></span>
                <progress
                    :value="starAverageDetail(5).count"
                    :max="product.ratings.length"
                    class="progress-bar">
                </progress>
                <span>{{ starAverageDetail(5).count }} Đánh giá</span>
              </div>
              <div class="progress-bar-container">
                <span>4 <FontAwesomeIcon icon="fa-solid fa-star" class="star-icon" /></span>
                <progress
                    :value="starAverageDetail(4).count"
                    :max="product.ratings.length"
                    class="progress-bar">
                </progress>
                <span>{{ starAverageDetail(4).count }} Đánh giá</span>
              </div>
              <div class="progress-bar-container">
                <span>3 <FontAwesomeIcon icon="fa-solid fa-star" class="star-icon" /></span>
                <progress
                    :value="starAverageDetail(3).count"
                    :max="product.ratings.length"
                    class="progress-bar">
                </progress>
                <span>{{ starAverageDetail(3).count }} Đánh giá</span>
              </div>
              <div class="progress-bar-container">
                <span>2 <FontAwesomeIcon icon="fa-solid fa-star" class="star-icon" /></span>
                <progress
                    :value="starAverageDetail(2).count"
                    :max="product.ratings.length"
                    class="progress-bar">
                </progress>
                <span>{{ starAverageDetail(2).count }} Đánh giá</span>
                </div>
              <div class="progress-bar-container">
                <span>1 <FontAwesomeIcon icon="fa-solid fa-star" class="star-icon" /></span>
                <progress
                    :value="starAverageDetail(1).count"
                    :max="product.ratings.length"
                    class="progress-bar">
                </progress>
                <span>{{ starAverageDetail(1).count }} Đánh giá</span>
              </div>
            </div>
          </div>
          <div class="rating-list">
            <div class="rating-item" v-for="(rating, index) in product.ratings" :key="index">
              <div class="left-rating-wrap">
                <div class="rating-user-avatar">
                  <span class="user-avatar">{{ getFirstLetter(rating.name) }}</span>
                </div>
              </div>
              <div class="right-rating-wrap">
                <div class="upper-rating-box">
                  <div class="rating-user bold-text">{{ rating.name }}</div>
                  <div class="rating-stars star-icon">
                    <font-awesome-icon
                        v-for="i in 5"
                        :key="i"
                        :icon="i <= rating.rating ? ['fas', 'star'] : ['far', 'star']"
                        class="star-icon"
                        size="sm"
                    />
                  </div>
                </div>
                <div class="rating-comment">{{ rating.comment }}</div>
                <div class="time-comments">
                  <span class="time bold-text">{{ new Date(rating.timeCreated).toLocaleDateString() }}</span>
                </div>
              </div>
            </div>
          </div>
          <div class="write-review-box">
            <form @submit.prevent class="form-review">
              <h2 class="title">Đánh giá sản phẩm</h2>
              <span class="sub-title">Hãy chia sẻ những điều bạn nghĩ về sản phẩm này với những người mua khác nhé.</span>
              <div class="rating-stars">
                <span class="bold-text">Đánh giá của bạn:</span>
                <font-awesome-icon
                    v-for="i in 5"
                    :key="i"
                    :icon="getStarIcon(i)"
                    class="star-icon choosable-star"
                    size="lg"
                    @mouseover="hoverRating = i"
                    @mouseleave="hoverRating = 0"
                    @click="selectedRating = i"
                />
              </div>
              <div class="desc-box">
                <textarea
                    class="review-textarea"
                    placeholder="Nhập đánh giá của bạn tại đây..."
                    rows="4"
                    required
                ></textarea>
              </div>
              <div class="btn-review">
                <button type="submit" class="btn">Gửi đánh giá</button>
              </div>
            </form>
          </div>
        </div> -->
      </div>
    </div>
  </div>
</template>

<style scoped>
  .content {
    width: 75vw;
    margin: 0 auto;
  }
  .product-cart {
    display: flex;
    justify-content: center;
    align-items: stretch;
    margin: 20px 0 60px 0;
  }
  .product-img {
    width: 30%;
    flex: 1;
  }
  .img-main-list {
    display: flex;
    overflow-x: auto;
    scroll-behavior: smooth;
    touch-action: pan-x;
    cursor: grab;
    gap: 8px;
    padding-bottom: 4px;
    /* ẩn scrollbar nếu muốn */
    scrollbar-width: none;
  }
  .img-main-list::-webkit-scrollbar {
    display: none;
  }
  .img-main-list.grabbing {
    cursor: grabbing;
  }
  .slide-wrapper {
    flex: 0 0 100%;
    scroll-snap-align: start;
    position: relative;
  }
  .img-item-main {
    width: 100%;
    height: auto;
    display: block;
    border-radius: 8px;
    object-fit: contain;
  }

  /* thumbnails */
  .img-sub-list {
    margin-top: 12px;
    display: flex;
    gap: 8px;
    justify-content: center;
  }
  .thumb-wrapper {
    flex: 0 0 auto;
    padding: 2px;
    border-radius: 6px;
    cursor: pointer;
    transition: box-shadow .2s, transform .2s;
    position: relative;
  }
  .thumb-wrapper.active {
    box-shadow: 0 0 0 3px #007bff;
    transform: scale(1.05);
  }
  .img-item-sub {
    display: block;
    width: 60px;
    height: 60px;
    object-fit: cover;
    border-radius: 4px;
  }
  .side-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding-left: 20px;
    box-sizing: border-box;
    align-self: stretch;
    gap: 20px;
  }
  .highlight-text{
    color: var(--main-color);
  }
  .option-values{
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  }
  .option-value-box{
    padding: 6px 12px;
    border: 1px solid #ccc;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color .2s, color .2s, border-color .2s;
    margin-top: 8px;
  }
  .selected{
    background-color: var(--main-color);
    color: white;
    border-color: var(--main-color);
  }
  .item-quantity-choice {
    height: 1.8rem;
    width: 20%;
    display: flex;
    gap: 3px;
  }
  .item-quantity-choice > * {
    flex: 1; /* mỗi phần chiếm cùng tỉ lệ */
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .item-quantity-choice input {
    flex: 1;
    width: 50px;
    text-align: center;
    border: 1px solid var(--main-color);
    border-radius: 7px;
    gap: 10px;
  }

  .item-quantity-choice button {
    flex: 0 0 20%;
    border: none;
    background-color: var(--main-color);
    color: #ffffff;
    cursor: pointer;
    border-radius: 20px;
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
  .quantity-wrap{
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
  }
  .cart-btn {
    width: 200px;
    padding: 10px;
    border-radius: 10px;
  }

  .btn:hover {
    background-color: #ffffff;
    color: var(--main-color);
    cursor: pointer;
    border: 1px solid var(--main-color);
  }
  .product-name{
    font-weight: bold;
  }
  .btn{
    background-color: var(--main-color);
    border: none;
    color: #ffffff;
    transition: background-color .2s, color .2s, border-color .2s;
  }
  .option:not(:last-child){
    margin-bottom: 10px;
  }
  .price-container{
    display: flex;
    flex-direction: row;
    gap: 7px;
    align-items: center;
  }
  .underline{
    text-decoration: underline;
  }
  .price{
    font-weight: bold;
  }
  .tabs{
    display: flex;
    gap: 30px;
  }
  .tab{
    padding: 10px 15px;
    cursor: pointer;
    font-weight: bold;
    font-size: 25px;
    transition: background-color .2s , color .2s, border-color .2s;
  }
  .activeTab, .tab:hover{
    background: #feefe8;
    color: #f66315;
    border-radius: 10px 10px 0 0;
  }
  .activeTab{
    border-bottom: #f66315 solid 4px;
  }
  .tab-panels{
    background: #feefe8;
    padding: 30px 10px;
  }
  .star-icon{
    color: #faca51;
  }
  .rating-overview{
    display: flex;
    flex-direction: row;
    align-items: center;
    border: 1px solid #fae0b8;
    border-radius: 20px;
  }
  .average-rating{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 40%;
    color: #f89f2a;
    font-size: 30px;
    border-right: 1px solid #fae0b8;
  }
  .average-rating span{
    font-weight: bold;
  }
  progress.progress-bar {
    width: 50%;
    height: 12px;
    border: none;
    border-radius: 10px;
    appearance: none;
  }
  /* Chrome, Safari, Edge (WebKit) */
  progress.progress-bar::-webkit-progress-bar {
    background-color: #d1d1d1;
    border-radius: 10px;
  }

  progress.progress-bar::-webkit-progress-value {
    background-color: orange;
    border-radius: 10px;
  }
  .star-rating{
    width: 60%;
  }
  .progress-bar-container{
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 20px;
    margin: 10px 0;
  }
  .rating-list{
    font-size: 15px;
  }
  .upper-rating-box{
    display: flex;
    gap: 5px;
    font-size: 18px;
  }
  .rating-item{
    display: flex;
    flex-direction: row;
    align-items: center;
    margin: 10px 0;
    padding: 20px;
    border-bottom: 1px solid #dbdbdb;
  }
  .rating-user-avatar{
    width: 80px;
    height: 80px;
    border-radius: 50%;
    background-color: #8d8c8c;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 20px;
    font-weight: bold;
    color: #000000;
    margin-right: 20px;
  }
  .right-rating-wrap{
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  .bold-text{
    font-weight: bold;
  }
  .time{
    font-size: 13px;
  }
  .form-review{
    margin-left: 20px;
    display: flex;
    flex-direction: column;
    gap: 15px;
  }
  .title{
    font-size: 25px;
    font-weight: bold;
    color: #f66315;
  }
  .sub-title{
    font-size: 15px;
    color: #8d8c8c;
  }
  .review-textarea{
    width: 100%;
    padding: 10px;
    border-radius: 10px;
    border: 1px solid #ccc;
    resize: vertical;
  }
  .btn-review{
    display: flex;
    justify-content: flex-end;
  }
  .btn-review button{
    padding: 10px 40px;
    border-radius: 10px;
  }
  .rating-stars{
    display: flex;
    gap: 5px;
    align-items: center;
    font-size: 15px;
  }
  .choosable-star{
    cursor: pointer;
    transition: color .2s;
  }


</style>
