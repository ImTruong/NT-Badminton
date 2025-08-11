import {createRouter, createWebHistory} from "vue-router";
import Home from "@/views/Home.vue";
import Register from "@/views/Register.vue";
import Login from "@/views/Login.vue";
import SearchProduct from "@/views/SearchProduct.vue";
import ProductDetail from "@/views/ProductDetail.vue";
import Cart from "@/views/Cart.vue";
import Checkout from "@/views/Checkout.vue";
import Purchase from "@/views/Purchase.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {
            path: "/",
            name: "home",
            component: Home
        },
        {
            path: "/register",
            name: "register",
            component: Register
        },
        {
            path: "/login",
            name: "login",
            component: Login
        },
        {
            path: "/search",
            name: "searchProduct",
            component: SearchProduct
        },
        {
            path: "/product/:id",
            name: "productDetail",
            component: ProductDetail
        },
        {
            path: "/cart",
            name: "cart",
            component: Cart
        },
        {
            path: "/checkout",
            name: "checkout",
            component: Checkout
        },
        {
            path: "/purchase",
            name: "purchase",
            component: Purchase
        }
    ]
})

export default router