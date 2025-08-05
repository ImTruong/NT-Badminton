import './assets/main.css'

import { createApp } from 'vue'
import Header from '@/components/Header.vue'
import App from '@/App.vue'
import router from '@/router/route'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faUser, faLocationDot, faSearch, faCartShopping, faBoxOpen, faTrash, faMoneyBill, faRotate, faCar, faStar, faDownLong, faChevronCircleLeft, faChevronCircleRight, faFilter, faSort, faCartPlus } from '@fortawesome/free-solid-svg-icons'
import { faStar as faStarSolid, faStarHalfStroke } from '@fortawesome/free-solid-svg-icons'
import { faStar as faStarRegular } from '@fortawesome/free-regular-svg-icons'

library.add(faUser, faLocationDot, faSearch, faCartShopping, faBoxOpen, faTrash, faMoneyBill, faRotate, faCar, faStar, faDownLong, faChevronCircleLeft, faChevronCircleRight, faFilter, faStarSolid, faStarRegular, faStarHalfStroke, faSort, faCartPlus)

const app = createApp(App)
app.use(router)
app.component('font-awesome-icon', FontAwesomeIcon)
app.component('Header',Header)
app.mount('#app')
