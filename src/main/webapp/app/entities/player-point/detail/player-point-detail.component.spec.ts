import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlayerPointDetailComponent } from './player-point-detail.component';

describe('Component Tests', () => {
  describe('PlayerPoint Management Detail Component', () => {
    let comp: PlayerPointDetailComponent;
    let fixture: ComponentFixture<PlayerPointDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PlayerPointDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ playerPoint: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PlayerPointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlayerPointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load playerPoint on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.playerPoint).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
