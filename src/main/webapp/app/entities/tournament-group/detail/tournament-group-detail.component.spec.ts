import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TournamentGroupDetailComponent } from './tournament-group-detail.component';

describe('Component Tests', () => {
  describe('TournamentGroup Management Detail Component', () => {
    let comp: TournamentGroupDetailComponent;
    let fixture: ComponentFixture<TournamentGroupDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TournamentGroupDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tournamentGroup: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TournamentGroupDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TournamentGroupDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tournamentGroup on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tournamentGroup).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
