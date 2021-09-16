import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SuspensionDetailComponent } from './suspension-detail.component';

describe('Component Tests', () => {
  describe('Suspension Management Detail Component', () => {
    let comp: SuspensionDetailComponent;
    let fixture: ComponentFixture<SuspensionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SuspensionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ suspension: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SuspensionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SuspensionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load suspension on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.suspension).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
