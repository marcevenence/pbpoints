jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PlayerPointService } from '../service/player-point.service';
import { IPlayerPoint, PlayerPoint } from '../player-point.model';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';

import { PlayerPointUpdateComponent } from './player-point-update.component';

describe('Component Tests', () => {
  describe('PlayerPoint Management Update Component', () => {
    let comp: PlayerPointUpdateComponent;
    let fixture: ComponentFixture<PlayerPointUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let playerPointService: PlayerPointService;
    let tournamentService: TournamentService;
    let userService: UserService;
    let categoryService: CategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlayerPointUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PlayerPointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlayerPointUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      playerPointService = TestBed.inject(PlayerPointService);
      tournamentService = TestBed.inject(TournamentService);
      userService = TestBed.inject(UserService);
      categoryService = TestBed.inject(CategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Tournament query and add missing value', () => {
        const playerPoint: IPlayerPoint = { id: 456 };
        const tournament: ITournament = { id: 92431 };
        playerPoint.tournament = tournament;

        const tournamentCollection: ITournament[] = [{ id: 13950 }];
        spyOn(tournamentService, 'query').and.returnValue(of(new HttpResponse({ body: tournamentCollection })));
        const additionalTournaments = [tournament];
        const expectedCollection: ITournament[] = [...additionalTournaments, ...tournamentCollection];
        spyOn(tournamentService, 'addTournamentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ playerPoint });
        comp.ngOnInit();

        expect(tournamentService.query).toHaveBeenCalled();
        expect(tournamentService.addTournamentToCollectionIfMissing).toHaveBeenCalledWith(tournamentCollection, ...additionalTournaments);
        expect(comp.tournamentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const playerPoint: IPlayerPoint = { id: 456 };
        const user: IUser = { id: 13820 };
        playerPoint.user = user;

        const userCollection: IUser[] = [{ id: 8136 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ playerPoint });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Category query and add missing value', () => {
        const playerPoint: IPlayerPoint = { id: 456 };
        const category: ICategory = { id: 8745 };
        playerPoint.category = category;

        const categoryCollection: ICategory[] = [{ id: 85439 }];
        spyOn(categoryService, 'query').and.returnValue(of(new HttpResponse({ body: categoryCollection })));
        const additionalCategories = [category];
        const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
        spyOn(categoryService, 'addCategoryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ playerPoint });
        comp.ngOnInit();

        expect(categoryService.query).toHaveBeenCalled();
        expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(categoryCollection, ...additionalCategories);
        expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const playerPoint: IPlayerPoint = { id: 456 };
        const tournament: ITournament = { id: 81274 };
        playerPoint.tournament = tournament;
        const user: IUser = { id: 62330 };
        playerPoint.user = user;
        const category: ICategory = { id: 89081 };
        playerPoint.category = category;

        activatedRoute.data = of({ playerPoint });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(playerPoint));
        expect(comp.tournamentsSharedCollection).toContain(tournament);
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.categoriesSharedCollection).toContain(category);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const playerPoint = { id: 123 };
        spyOn(playerPointService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ playerPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: playerPoint }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(playerPointService.update).toHaveBeenCalledWith(playerPoint);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const playerPoint = new PlayerPoint();
        spyOn(playerPointService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ playerPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: playerPoint }));
        saveSubject.complete();

        // THEN
        expect(playerPointService.create).toHaveBeenCalledWith(playerPoint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const playerPoint = { id: 123 };
        spyOn(playerPointService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ playerPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(playerPointService.update).toHaveBeenCalledWith(playerPoint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTournamentById', () => {
        it('Should return tracked Tournament primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTournamentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCategoryById', () => {
        it('Should return tracked Category primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCategoryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
