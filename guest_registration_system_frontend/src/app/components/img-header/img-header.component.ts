import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-img-header',
  templateUrl: './img-header.component.html',
  styleUrls: ['./img-header.component.scss']
})
export class ImgHeaderComponent {
  @Input() name: string = '';
}
