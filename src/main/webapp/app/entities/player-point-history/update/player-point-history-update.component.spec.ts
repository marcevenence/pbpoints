import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PlayerPointHistoryService } from '../service/player-point-history.service';
import { IPlayerPointHistory, PlayerPointHistory } from '../player-point-history.model';
import { IPlayerPoint } from 'app/entities/player-point/player-point.model';
import { PlayerPointService } from 'app/entities/player-point/service/player-point.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ISeason } from 'app/entities/season/season.model';
import { SeasonService } from 'app/entities/season/service/season.service';

import { PlayerPointHistoryUpdateComponent } from './player-point-history-update.component';

describe('PlayerPointHistory Management Update Component', () => {
  let comp: PlayerPointHistoryUpdateComponent;
  let fixture: ComponentFixture<PlayerPointHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let playerPointHistoryService: PlayerPointHistoryService;
  let playerPointService: PlayerPointService;
  let categoryService: CategoryService;
  let seasonService: SeasonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PlayerPointHistoryUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PlayerPointHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlayerPointHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    playerPointHistoryService = TestBed.inject(PlayerPointHistoryService);
    playerPointService = TestBed.inject(PlayerPointService);
    categoryService = TestBed.inject(CategoryService);
    seasonService = TestBed.inject(SeasonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PlayerPoint query and add missing value', () => {
      const playerPointHistory: IPlayerPointHistory = { id: 456 };
      const playerPoint: IPlayerPoint = { id: 93835 };
      playerPointHistory.playerPoint = playerPoint;

      const playerPointCollection: IPlayerPoint[] = [{ id: 51861 }];
      jest.spyOn(playerPointService, 'query').mockReturnValue(of(new HttpResponse({ body: playerPointCollection })));
      const additionalPlayerPoints = [playerPoint];
      const expectedCollection: IPlayerPoint[] = [...additionalPlayerPoints, ...playerPointCollection];
      jest.spyOn(playerPointService, 'addPlayerPointToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ playerPointHistory });
      comp.ngOnInit();

      expect(playerPointService.query).toHaveBeenCalled();
      expect(playerPointService.addPlayerPointToCollectionIfMissing).toHaveBeenCalledWith(playerPointCollection, ...additionalPlayerPoints);
      expect(comp.playerPointsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Category query and add missing value', () => {
      const playerPointHistory: IPlayerPointHistory = { id: 456 };
      const category: ICategory = { id: 48149 };
      playerPointHistory.category = category;

      const categoryCollection: ICategory[] = [{ id: 29548 }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [category];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ playerPointHistory });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(categoryCollection, ...additionalCategories);
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Season query and add missing value', () => {
      const playerPointHistory: IPlayerPointHistory = { id: 456 };
      const season: ISeason = { id: 39768 };
      playerPointHistory.season = season;

      const seasonCollection: ISeason[] = [{ id: 5745 }];
      jest.spyOn(seasonService, 'query').mockReturnValue(of(new HttpResponse({ body: seasonCollection })));
      const additionalSeasons = [season];
      const expectedCollection: ISeason[] = [...additionalSeasons, ...seasonCollection];
      jest.spyOn(seasonService, 'addSeasonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ playerPointHistory });
      comp.ngOnInit();

      expect(seasonService.query).toHaveBeenCalled();
      expect(seasonService.addSeasonToCollectionIfMissing).toHaveBeenCalledWith(seasonCollection, ...additionalSeasons);
      expect(comp.seasonsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const playerPointHistory: IPlayerPointHistory = { id: 456 };
      const playerPoint: IPlayerPoint = { id: 68688 };
      playerPointHistory.playerPoint = playerPoint;
      const category: ICategory = { id: 75581 };
      playerPointHistory.category = category;
      const season: ISeason = { id: 35648 };
      playerPointHistory.season = season;

      activatedRoute.data = of({ playerPointHistory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(playerPointHistory));
      expect(comp.playerPointsSharedCollection).toContain(playerPoint);
      expect(comp.categoriesSharedCollection).toContain(category);
      expect(comp.seasonsSharedCollection).toContain(season);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PlayerPointHistory>>();
      const playerPointHistory = { id: 123 };
      jest.spyOn(playerPointHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playerPointHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playerPointHistory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(playerPointHistoryService.update).toHaveBeenCalledWith(playerPointHistory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PlayerPointHistory>>();
      const playerPointHistory = new PlayerPointHistory();
      jest.spyOn(playerPointHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playerPointHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playerPointHistory }));
      saveSubject.complete();

      // THEN
      expect(playerPointHistoryService.create).toHaveBeenCalledWith(playerPointHistory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PlayerPointHistory>>();
      const playerPointHistory = { id: 123 };
      jest.spyOn(playerPointHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playerPointHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(playerPointHistoryService.update).toHaveBeenCalledWith(playerPointHistory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPlayerPointById', () => {
      it('Should return tracked PlayerPoint primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlayerPointById(0, entity);
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

    describe('trackSeasonById', () => {
      it('Should return tracked Season primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSeasonById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
