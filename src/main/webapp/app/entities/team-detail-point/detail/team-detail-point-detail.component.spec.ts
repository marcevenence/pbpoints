import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TeamDetailPointDetailComponent } from './team-detail-point-detail.component';

describe('Component Tests', () => {
  describe('TeamDetailPoint Management Detail Component', () => {
    let comp: TeamDetailPointDetailComponent;
    let fixture: ComponentFixture<TeamDetailPointDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TeamDetailPointDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ teamDetailPoint: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TeamDetailPointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TeamDetailPointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load teamDetailPoint on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.teamDetailPoint).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
