import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RosterDetailComponent } from './roster-detail.component';

describe('Component Tests', () => {
  describe('Roster Management Detail Component', () => {
    let comp: RosterDetailComponent;
    let fixture: ComponentFixture<RosterDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RosterDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ roster: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RosterDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RosterDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load roster on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.roster).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
