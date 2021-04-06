import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlayerDetailPointDetailComponent } from './player-detail-point-detail.component';

describe('Component Tests', () => {
  describe('PlayerDetailPoint Management Detail Component', () => {
    let comp: PlayerDetailPointDetailComponent;
    let fixture: ComponentFixture<PlayerDetailPointDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PlayerDetailPointDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ playerDetailPoint: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PlayerDetailPointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlayerDetailPointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load playerDetailPoint on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.playerDetailPoint).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
