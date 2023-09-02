import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyAddFormComponent } from './company-add-form.component';

describe('CompanyAddFormComponent', () => {
  let component: CompanyAddFormComponent;
  let fixture: ComponentFixture<CompanyAddFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompanyAddFormComponent]
    });
    fixture = TestBed.createComponent(CompanyAddFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
