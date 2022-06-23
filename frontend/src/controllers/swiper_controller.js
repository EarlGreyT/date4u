import Swiper from "swiper";
import {Controller} from "@hotwired/stimulus";

export default class extends Controller {
    static targets = ["container", "next", "prev", "pagination"];
    static values = {
        perview: {type: Number, default: 1},
        spacebetween: {type: Number, default: 15},
        loop: {type: Boolean, default: true},
        lazy: {type: Boolean, default: true},
        starthash: {type: String, default:""}
    }

    connect() {
        this.swiper = new Swiper(this.containerTarget, {
            lazy: this.lazyValue,
            slidesPerView: this.perviewValue,
            spaceBetween: this.spacebetweenValue,
            loop: this.loopValue,
            breakpoints: {
              '1400':{
                slidesPerView: this.perviewValue
              },
              '1000':{
                slidesPerView: Math.min(Math.ceil(this.perviewValue/2),2)
              },
              '1':{
                slidesPerView: 1
              }
            },
            hashNavigation: {
                watchState: true,
            },
            pagination: {
                el: this.pagination,
                clickable: true,
            },
            navigation: {
                nextEl: this.nextTarget,
                prevEl: this.prevTarget,
            },
        });
        if (this.starthashValue !== "") {
            let startIndex = this.swiper.slides.findIndex(slide => slide.children[0].dataset.hash === this.starthashValue);
            this.swiper.slideTo(startIndex,0)
        }
        this.swiper.on('slideChange', event => this.dispatch('picUpdate',
        {detail:
            {content:
                this.swiper.slides[event.activeIndex].children[0].dataset.hash
            }
        }))
    }
}