import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NavpointComponent } from './navpoint.component';

describe('NavpointComponent', () => {
  let component: NavpointComponent;
  let fixture: ComponentFixture<NavpointComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NavpointComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavpointComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
