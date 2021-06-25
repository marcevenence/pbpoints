jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PlayerService } from '../service/player.service';
import { IPlayer, Player } from '../player.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IRoster } from 'app/entities/roster/roster.model';
import { RosterService } from 'app/entities/roster/service/roster.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';

import { PlayerSearchComponent } from './player-search.component';

describe('Component Tests', () => {
  describe('Player Management Update Component', () => {
    let comp: PlayerSearchComponent;
    let fixture: ComponentFixture<PlayerSearchComponent>;
    let activatedRoute: ActivatedRoute;
    let playerService: PlayerService;
    let userService: UserService;
    let rosterService: RosterService;
    let categoryService: CategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlayerSearchComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PlayerSearchComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlayerSearchComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      playerService = TestBed.inject(PlayerService);
      userService = TestBed.inject(UserService);
      rosterService = TestBed.inject(RosterService);
      categoryService = TestBed.inject(CategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const player: IPlayer = { id: 456 };
        const user: IUser = { id: 27699 };
        player.user = user;

        const userCollection: IUser[] = [{ id: 87926 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ player });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Roster query and add missing value', () => {
        const player: IPlayer = { id: 456 };
        const roster: IRoster = { id: 60520 };
        player.roster = roster;

        const rosterCollection: IRoster[] = [{ id: 1769 }];
        spyOn(rosterService, 'query').and.returnValue(of(new HttpResponse({ body: rosterCollection })));
        const additionalRosters = [roster];
        const expectedCollection: IRoster[] = [...additionalRosters, ...rosterCollection];
        spyOn(rosterService, 'addRosterToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ player });
        comp.ngOnInit();

        expect(rosterService.query).toHaveBeenCalled();
        expect(rosterService.addRosterToCollectionIfMissing).toHaveBeenCalledWith(rosterCollection, ...additionalRosters);
        expect(comp.rostersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Category query and add missing value', () => {
        const player: IPlayer = { id: 456 };
        const category: ICategory = { id: 63062 };
        player.category = category;

        const categoryCollection: ICategory[] = [{ id: 32414 }];
        spyOn(categoryService, 'query').and.returnValue(of(new HttpResponse({ body: categoryCollection })));
        const additionalCategories = [category];
        const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
        spyOn(categoryService, 'addCategoryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ player });
        comp.ngOnInit();

        expect(categoryService.query).toHaveBeenCalled();
        expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(categoryCollection, ...additionalCategories);
        expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const player: IPlayer = { id: 456 };
        const user: IUser = { id: 47918 };
        player.user = user;
        const roster: IRoster = { id: 80108 };
        player.roster = roster;
        const category: ICategory = { id: 36230 };
        player.category = category;

        activatedRoute.data = of({ player });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(player));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.rostersSharedCollection).toContain(roster);
        expect(comp.categoriesSharedCollection).toContain(category);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const player = { id: 123 };
        spyOn(playerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ player });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: player }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(playerService.update).toHaveBeenCalledWith(player);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const player = new Player();
        spyOn(playerService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ player });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: player }));
        saveSubject.complete();

        // THEN
        expect(playerService.create).toHaveBeenCalledWith(player);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const player = { id: 123 };
        spyOn(playerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ player });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(playerService.update).toHaveBeenCalledWith(player);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackRosterById', () => {
        it('Should return tracked Roster primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRosterById(0, entity);
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
