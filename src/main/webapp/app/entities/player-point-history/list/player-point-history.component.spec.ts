import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PlayerPointHistoryService } from '../service/player-point-history.service';

import { PlayerPointHistoryComponent } from './player-point-history.component';

describe('PlayerPointHistory Management Component', () => {
  let comp: PlayerPointHistoryComponent;
  let fixture: ComponentFixture<PlayerPointHistoryComponent>;
  let service: PlayerPointHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PlayerPointHistoryComponent],
    })
      .overrideTemplate(PlayerPointHistoryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlayerPointHistoryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PlayerPointHistoryService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.playerPointHistories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
