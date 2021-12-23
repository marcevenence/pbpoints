import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TeamCategoryPointService } from '../service/team-category-point.service';

import { TeamCategoryPointComponent } from './team-category-point.component';

describe('Component Tests', () => {
  describe('TeamCategoryPoint Management Component', () => {
    let comp: TeamCategoryPointComponent;
    let fixture: ComponentFixture<TeamCategoryPointComponent>;
    let service: TeamCategoryPointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TeamCategoryPointComponent],
      })
        .overrideTemplate(TeamCategoryPointComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TeamCategoryPointComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TeamCategoryPointService);

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
      expect(comp.teamCategoryPoints?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
