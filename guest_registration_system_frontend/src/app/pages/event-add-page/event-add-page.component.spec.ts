import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventAddPageComponent } from './event-add-page.component';

describe('EventAddPageComponent', () => {
  let component: EventAddPageComponent;
  let fixture: ComponentFixture<EventAddPageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EventAddPageComponent]
    });
    fixture = TestBed.createComponent(EventAddPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
