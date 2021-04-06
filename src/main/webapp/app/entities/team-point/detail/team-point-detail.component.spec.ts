import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TeamPointDetailComponent } from './team-point-detail.component';

describe('Component Tests', () => {
  describe('TeamPoint Management Detail Component', () => {
    let comp: TeamPointDetailComponent;
    let fixture: ComponentFixture<TeamPointDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TeamPointDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ teamPoint: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TeamPointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TeamPointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load teamPoint on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.teamPoint).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
