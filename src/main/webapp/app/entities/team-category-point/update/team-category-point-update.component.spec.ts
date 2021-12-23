jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TeamCategoryPointService } from '../service/team-category-point.service';
import { ITeamCategoryPoint, TeamCategoryPoint } from '../team-category-point.model';
import { ITeamDetailPoint } from 'app/entities/team-detail-point/team-detail-point.model';
import { TeamDetailPointService } from 'app/entities/team-detail-point/service/team-detail-point.service';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/service/event-category.service';

import { TeamCategoryPointUpdateComponent } from './team-category-point-update.component';

describe('Component Tests', () => {
  describe('TeamCategoryPoint Management Update Component', () => {
    let comp: TeamCategoryPointUpdateComponent;
    let fixture: ComponentFixture<TeamCategoryPointUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let teamCategoryPointService: TeamCategoryPointService;
    let teamDetailPointService: TeamDetailPointService;
    let eventCategoryService: EventCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TeamCategoryPointUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TeamCategoryPointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TeamCategoryPointUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      teamCategoryPointService = TestBed.inject(TeamCategoryPointService);
      teamDetailPointService = TestBed.inject(TeamDetailPointService);
      eventCategoryService = TestBed.inject(EventCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call TeamDetailPoint query and add missing value', () => {
        const teamCategoryPoint: ITeamCategoryPoint = { id: 456 };
        const teamDetailPoint: ITeamDetailPoint = { id: 55557 };
        teamCategoryPoint.teamDetailPoint = teamDetailPoint;

        const teamDetailPointCollection: ITeamDetailPoint[] = [{ id: 55588 }];
        spyOn(teamDetailPointService, 'query').and.returnValue(of(new HttpResponse({ body: teamDetailPointCollection })));
        const additionalTeamDetailPoints = [teamDetailPoint];
        const expectedCollection: ITeamDetailPoint[] = [...additionalTeamDetailPoints, ...teamDetailPointCollection];
        spyOn(teamDetailPointService, 'addTeamDetailPointToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ teamCategoryPoint });
        comp.ngOnInit();

        expect(teamDetailPointService.query).toHaveBeenCalled();
        expect(teamDetailPointService.addTeamDetailPointToCollectionIfMissing).toHaveBeenCalledWith(
          teamDetailPointCollection,
          ...additionalTeamDetailPoints
        );
        expect(comp.teamDetailPointsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call EventCategory query and add missing value', () => {
        const teamCategoryPoint: ITeamCategoryPoint = { id: 456 };
        const eventCategory: IEventCategory = { id: 99043 };
        teamCategoryPoint.eventCategory = eventCategory;

        const eventCategoryCollection: IEventCategory[] = [{ id: 71329 }];
        spyOn(eventCategoryService, 'query').and.returnValue(of(new HttpResponse({ body: eventCategoryCollection })));
        const additionalEventCategories = [eventCategory];
        const expectedCollection: IEventCategory[] = [...additionalEventCategories, ...eventCategoryCollection];
        spyOn(eventCategoryService, 'addEventCategoryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ teamCategoryPoint });
        comp.ngOnInit();

        expect(eventCategoryService.query).toHaveBeenCalled();
        expect(eventCategoryService.addEventCategoryToCollectionIfMissing).toHaveBeenCalledWith(
          eventCategoryCollection,
          ...additionalEventCategories
        );
        expect(comp.eventCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const teamCategoryPoint: ITeamCategoryPoint = { id: 456 };
        const teamDetailPoint: ITeamDetailPoint = { id: 83372 };
        teamCategoryPoint.teamDetailPoint = teamDetailPoint;
        const eventCategory: IEventCategory = { id: 95340 };
        teamCategoryPoint.eventCategory = eventCategory;

        activatedRoute.data = of({ teamCategoryPoint });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(teamCategoryPoint));
        expect(comp.teamDetailPointsSharedCollection).toContain(teamDetailPoint);
        expect(comp.eventCategoriesSharedCollection).toContain(eventCategory);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const teamCategoryPoint = { id: 123 };
        spyOn(teamCategoryPointService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ teamCategoryPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: teamCategoryPoint }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(teamCategoryPointService.update).toHaveBeenCalledWith(teamCategoryPoint);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const teamCategoryPoint = new TeamCategoryPoint();
        spyOn(teamCategoryPointService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ teamCategoryPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: teamCategoryPoint }));
        saveSubject.complete();

        // THEN
        expect(teamCategoryPointService.create).toHaveBeenCalledWith(teamCategoryPoint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const teamCategoryPoint = { id: 123 };
        spyOn(teamCategoryPointService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ teamCategoryPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(teamCategoryPointService.update).toHaveBeenCalledWith(teamCategoryPoint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTeamDetailPointById', () => {
        it('Should return tracked TeamDetailPoint primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTeamDetailPointById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackEventCategoryById', () => {
        it('Should return tracked EventCategory primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEventCategoryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
