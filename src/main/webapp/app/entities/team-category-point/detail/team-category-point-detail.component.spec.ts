import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TeamCategoryPointDetailComponent } from './team-category-point-detail.component';

describe('Component Tests', () => {
  describe('TeamCategoryPoint Management Detail Component', () => {
    let comp: TeamCategoryPointDetailComponent;
    let fixture: ComponentFixture<TeamCategoryPointDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TeamCategoryPointDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ teamCategoryPoint: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TeamCategoryPointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TeamCategoryPointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load teamCategoryPoint on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.teamCategoryPoint).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
