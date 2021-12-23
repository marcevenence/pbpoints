import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TeamDetailPointService } from '../service/team-detail-point.service';

import { TeamDetailPointComponent } from './team-detail-point.component';

describe('Component Tests', () => {
  describe('TeamDetailPoint Management Component', () => {
    let comp: TeamDetailPointComponent;
    let fixture: ComponentFixture<TeamDetailPointComponent>;
    let service: TeamDetailPointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TeamDetailPointComponent],
      })
        .overrideTemplate(TeamDetailPointComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TeamDetailPointComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TeamDetailPointService);

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
      expect(comp.teamDetailPoints?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
