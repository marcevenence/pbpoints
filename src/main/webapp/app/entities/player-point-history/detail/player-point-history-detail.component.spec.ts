import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlayerPointHistoryDetailComponent } from './player-point-history-detail.component';

describe('PlayerPointHistory Management Detail Component', () => {
  let comp: PlayerPointHistoryDetailComponent;
  let fixture: ComponentFixture<PlayerPointHistoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlayerPointHistoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ playerPointHistory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PlayerPointHistoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PlayerPointHistoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load playerPointHistory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.playerPointHistory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
