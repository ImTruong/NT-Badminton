<script setup lang="ts">
  import { ref, onMounted, defineProps, defineEmits } from "vue";

  const props = defineProps({
    categories: {
      type: Array,
      required: true
    },
    activeCategory: {
      type: Number,
      required: true
    }
  });

  const emit = defineEmits(["changeCategory"]);

  const categoryList = ref(null);
  const atLeftEdge = ref(true);
  const atRightEdge = ref(false);
  function checkCategoryScroll() {
    if (categoryList.value) {
      // Check if at left edge (scrollLeft === 0)
      atLeftEdge.value = categoryList.value.scrollLeft <= 0;
      // Check if at right edge (scrolled to the max possible)
      atRightEdge.value = Math.ceil(categoryList.value.scrollLeft + categoryList.value.clientWidth) >=
          categoryList.value.scrollWidth;
    }
  }
  const changeCategory = (categoryId) => {
    emit("changeCategory", categoryId);
  }
  onMounted(() => {
    checkCategoryScroll();
    window.addEventListener('resize', checkCategoryScroll);
  });
</script>

<template>
  <div class="categories" :class="{ 'hide-left-fade': atLeftEdge, 'hide-right-fade': atRightEdge }">
    <ul class="category-list" ref="categoryList">
      <li v-for="(category, index) in categories"
          :key="category.id" class="category-item"
          :class="{ active: activeCategory === category.id }"
          @click="changeCategory(category.id)"
      >
        <a href="#" @click.prevent>{{ category.name }}</a>
      </li>
    </ul>
  </div>
</template>

<style scoped>
  .categories{
    display: flex;
    justify-content: center;
    align-items: center;
    border: 1px solid #eaeaea;
    border-radius: 10px;
    width: 100%;
    height: 100%;
    position: relative; 
    overflow: hidden;
  }
  .categories::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    width: 20px;
    height: 100%;
    background: linear-gradient(to right, rgba(255,255,255,0.95), rgba(255,255,255,0));
    z-index: 2;
    pointer-events: none; 
  }

  /* Right fade */
  .categories::after {
    content: "";
    position: absolute;
    top: 0;
    right: 0;
    width: 40px;
    height: 100%;
    background: linear-gradient(to left, rgba(255,255,255,0.95), rgba(255,255,255,0));
    z-index: 2;
    pointer-events: none;
  }
  .categories.hide-right-fade::after {
    opacity: 0;
  }
  .categories.hide-left-fade::before {
    opacity: 0;
  }
  .categories::before, .categories::after {
    transition: opacity 0.3s ease;
  }
  .category-list{
    list-style: none;
    display: flex;
    flex-direction: row;
    padding: 0;
    overflow-x: auto;
    scroll-behavior: smooth;
    background: #fbfbfb;
  }
  .category-item{
    flex: 0 0 220px;
    font-size: 16px;
    text-align: center;
    text-decoration: none;
    padding: 5px;
    border-right: #eaeaea 1px solid;
    margin: 5px 0;
  }
  .category-item.active {
    background-color: var(--main-color);
  }
  .category-item.active a {
    color: #ffffff;
  }
  .category-item a {
    text-decoration: none;
    color: #545454;
    font-weight: bold;
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .category-list::-webkit-scrollbar {
    display: none;
  }
  .category-item a {
    text-decoration: none;
    color: #545454;
    font-weight: bold;
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
</style>