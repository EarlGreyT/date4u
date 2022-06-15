import { Application } from "@hotwired/stimulus"
import { definitionsFromContext } from "@hotwired/stimulus-webpack-helpers"
import * as Turbo from "@hotwired/turbo"
import Swiper, {Navigation, Pagination, HashNavigation, Lazy} from "swiper";
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/hash-navigation';
import 'swiper/css/lazy';

Swiper.use([Navigation, Pagination, HashNavigation, Lazy]);
window.Stimulus = Application.start()
const context = require.context("./controllers", true, /\.js$/)
Stimulus.load(definitionsFromContext(context))