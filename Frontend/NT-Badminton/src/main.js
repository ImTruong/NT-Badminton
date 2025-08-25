import './assets/main.css'

import { createApp } from 'vue'
import Header from '@/components/Header.vue'
import App from '@/App.vue'
import router from '@/router/route'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faUser, faLocationDot, faSearch, faCartShopping, faBoxOpen, faTrash, faMoneyBill, faRotate, faCar, faStar, faDownLong, faChevronCircleLeft, faChevronCircleRight, faFilter, faSort, faCartPlus, faCreditCard, faUniversity } from '@fortawesome/free-solid-svg-icons'
import { faStar as faStarSolid, faStarHalfStroke } from '@fortawesome/free-solid-svg-icons'
import { faStar as faStarRegular } from '@fortawesome/free-regular-svg-icons'
import { createPinia } from 'pinia'

library.add(faUser, faLocationDot, faSearch, faCartShopping, faBoxOpen, faTrash, faMoneyBill, faRotate, faCar, faStar, faDownLong, faChevronCircleLeft, faChevronCircleRight, faFilter, faStarSolid, faStarRegular, faStarHalfStroke, faSort, faCartPlus, faCreditCard, faUniversity)

const app = createApp(App)
app.use(router)
app.use(createPinia())
app.component('font-awesome-icon', FontAwesomeIcon)
app.component('Header',Header)
app.mount('#app')
