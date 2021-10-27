jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GameService } from '../service/game.service';
import { IGame, Game } from '../game.model';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/service/event-category.service';

import { GameUpdateComponent } from './game-update.component';

describe('Component Tests', () => {
  describe('Game Management Update Component', () => {
    let comp: GameUpdateComponent;
    let fixture: ComponentFixture<GameUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let gameService: GameService;
    let teamService: TeamService;
    let eventCategoryService: EventCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GameUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GameUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GameUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      gameService = TestBed.inject(GameService);
      teamService = TestBed.inject(TeamService);
      eventCategoryService = TestBed.inject(EventCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Team query and add missing value', () => {
        const game: IGame = { id: 456 };
        const teamA: ITeam = { id: 6404 };
        game.teamA = teamA;
        const teamB: ITeam = { id: 3285 };
        game.teamB = teamB;

        const teamCollection: ITeam[] = [{ id: 13467 }];
        spyOn(teamService, 'query').and.returnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [teamA, teamB];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        spyOn(teamService, 'addTeamToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ game });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call EventCategory query and add missing value', () => {
        const game: IGame = { id: 456 };
        const eventCategory: IEventCategory = { id: 99043 };
        game.eventCategory = eventCategory;

        const eventCategoryCollection: IEventCategory[] = [{ id: 71329 }];
        spyOn(eventCategoryService, 'query').and.returnValue(of(new HttpResponse({ body: eventCategoryCollection })));
        const additionalEventCategories = [eventCategory];
        const expectedCollection: IEventCategory[] = [...additionalEventCategories, ...eventCategoryCollection];
        spyOn(eventCategoryService, 'addEventCategoryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ game });
        comp.ngOnInit();

        expect(eventCategoryService.query).toHaveBeenCalled();
        expect(eventCategoryService.addEventCategoryToCollectionIfMissing).toHaveBeenCalledWith(
          eventCategoryCollection,
          ...additionalEventCategories
        );
        expect(comp.eventCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const game: IGame = { id: 456 };
        const teamA: ITeam = { id: 21093 };
        game.teamA = teamA;
        const teamB: ITeam = { id: 32001 };
        game.teamB = teamB;
        const eventCategory: IEventCategory = { id: 95340 };
        game.eventCategory = eventCategory;

        activatedRoute.data = of({ game });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(game));
        expect(comp.teamsSharedCollection).toContain(teamA);
        expect(comp.teamsSharedCollection).toContain(teamB);
        expect(comp.eventCategoriesSharedCollection).toContain(eventCategory);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const game = { id: 123 };
        spyOn(gameService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ game });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: game }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(gameService.update).toHaveBeenCalledWith(game);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const game = new Game();
        spyOn(gameService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ game });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: game }));
        saveSubject.complete();

        // THEN
        expect(gameService.create).toHaveBeenCalledWith(game);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const game = { id: 123 };
        spyOn(gameService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ game });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(gameService.update).toHaveBeenCalledWith(game);
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
