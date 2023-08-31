import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImgHeaderComponent } from './img-header.component';

describe('ImgHeaderComponent', () => {
  let component: ImgHeaderComponent;
  let fixture: ComponentFixture<ImgHeaderComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ImgHeaderComponent]
    });
    fixture = TestBed.createComponent(ImgHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
