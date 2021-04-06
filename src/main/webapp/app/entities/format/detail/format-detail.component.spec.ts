import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormatDetailComponent } from './format-detail.component';

describe('Component Tests', () => {
  describe('Format Management Detail Component', () => {
    let comp: FormatDetailComponent;
    let fixture: ComponentFixture<FormatDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FormatDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ format: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FormatDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FormatDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load format on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.format).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
