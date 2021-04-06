import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EventCategoryDetailComponent } from './event-category-detail.component';

describe('Component Tests', () => {
  describe('EventCategory Management Detail Component', () => {
    let comp: EventCategoryDetailComponent;
    let fixture: ComponentFixture<EventCategoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EventCategoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ eventCategory: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EventCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EventCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load eventCategory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.eventCategory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
