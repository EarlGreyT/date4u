import {Controller} from "@hotwired/stimulus";
export default class extends Controller{
    static targets = ["pic","picname"];

    updateProfilepic(event){
        this.picnameTarget.value=event.detail.content;
    }
}