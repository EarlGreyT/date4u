import {Controller} from "@hotwired/stimulus";
export default class extends Controller{
    static targets = ["pic"];

    updateProfilepic(event){
        console.log(event.target.dataset.hash);
    }
}