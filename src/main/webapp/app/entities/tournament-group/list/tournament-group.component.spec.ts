import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TournamentGroupService } from '../service/tournament-group.service';

import { TournamentGroupComponent } from './tournament-group.component';

describe('Component Tests', () => {
  describe('TournamentGroup Management Component', () => {
    let comp: TournamentGroupComponent;
    let fixture: ComponentFixture<TournamentGroupComponent>;
    let service: TournamentGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TournamentGroupComponent],
      })
        .overrideTemplate(TournamentGroupComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TournamentGroupComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TournamentGroupService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
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
      expect(comp.tournamentGroups?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
