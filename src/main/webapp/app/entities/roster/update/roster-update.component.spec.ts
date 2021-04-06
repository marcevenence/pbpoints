jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RosterService } from '../service/roster.service';
import { IRoster, Roster } from '../roster.model';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/service/event-category.service';

import { RosterUpdateComponent } from './roster-update.component';

describe('Component Tests', () => {
  describe('Roster Management Update Component', () => {
    let comp: RosterUpdateComponent;
    let fixture: ComponentFixture<RosterUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let rosterService: RosterService;
    let teamService: TeamService;
    let eventCategoryService: EventCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RosterUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RosterUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RosterUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      rosterService = TestBed.inject(RosterService);
      teamService = TestBed.inject(TeamService);
      eventCategoryService = TestBed.inject(EventCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Team query and add missing value', () => {
        const roster: IRoster = { id: 456 };
        const team: ITeam = { id: 77222 };
        roster.team = team;

        const teamCollection: ITeam[] = [{ id: 36446 }];
        spyOn(teamService, 'query').and.returnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [team];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        spyOn(teamService, 'addTeamToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ roster });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call EventCategory query and add missing value', () => {
        const roster: IRoster = { id: 456 };
        const eventCategory: IEventCategory = { id: 88562 };
        roster.eventCategory = eventCategory;

        const eventCategoryCollection: IEventCategory[] = [{ id: 19281 }];
        spyOn(eventCategoryService, 'query').and.returnValue(of(new HttpResponse({ body: eventCategoryCollection })));
        const additionalEventCategories = [eventCategory];
        const expectedCollection: IEventCategory[] = [...additionalEventCategories, ...eventCategoryCollection];
        spyOn(eventCategoryService, 'addEventCategoryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ roster });
        comp.ngOnInit();

        expect(eventCategoryService.query).toHaveBeenCalled();
        expect(eventCategoryService.addEventCategoryToCollectionIfMissing).toHaveBeenCalledWith(
          eventCategoryCollection,
          ...additionalEventCategories
        );
        expect(comp.eventCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const roster: IRoster = { id: 456 };
        const team: ITeam = { id: 40697 };
        roster.team = team;
        const eventCategory: IEventCategory = { id: 64142 };
        roster.eventCategory = eventCategory;

        activatedRoute.data = of({ roster });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(roster));
        expect(comp.teamsSharedCollection).toContain(team);
        expect(comp.eventCategoriesSharedCollection).toContain(eventCategory);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const roster = { id: 123 };
        spyOn(rosterService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ roster });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: roster }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(rosterService.update).toHaveBeenCalledWith(roster);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const roster = new Roster();
        spyOn(rosterService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ roster });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: roster }));
        saveSubject.complete();

        // THEN
        expect(rosterService.create).toHaveBeenCalledWith(roster);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const roster = { id: 123 };
        spyOn(rosterService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ roster });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(rosterService.update).toHaveBeenCalledWith(roster);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTeamById', () => {
        it('Should return tracked Team primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTeamById(0, entity);
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
