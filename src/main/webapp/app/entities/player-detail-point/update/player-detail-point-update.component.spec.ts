jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PlayerDetailPointService } from '../service/player-detail-point.service';
import { IPlayerDetailPoint, PlayerDetailPoint } from '../player-detail-point.model';
import { IPlayerPoint } from 'app/entities/player-point/player-point.model';
import { PlayerPointService } from 'app/entities/player-point/service/player-point.service';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/service/event-category.service';

import { PlayerDetailPointUpdateComponent } from './player-detail-point-update.component';

describe('Component Tests', () => {
  describe('PlayerDetailPoint Management Update Component', () => {
    let comp: PlayerDetailPointUpdateComponent;
    let fixture: ComponentFixture<PlayerDetailPointUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let playerDetailPointService: PlayerDetailPointService;
    let playerPointService: PlayerPointService;
    let eventCategoryService: EventCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlayerDetailPointUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PlayerDetailPointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlayerDetailPointUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      playerDetailPointService = TestBed.inject(PlayerDetailPointService);
      playerPointService = TestBed.inject(PlayerPointService);
      eventCategoryService = TestBed.inject(EventCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call PlayerPoint query and add missing value', () => {
        const playerDetailPoint: IPlayerDetailPoint = { id: 456 };
        const playerPoint: IPlayerPoint = { id: 32381 };
        playerDetailPoint.playerPoint = playerPoint;

        const playerPointCollection: IPlayerPoint[] = [{ id: 32310 }];
        spyOn(playerPointService, 'query').and.returnValue(of(new HttpResponse({ body: playerPointCollection })));
        const additionalPlayerPoints = [playerPoint];
        const expectedCollection: IPlayerPoint[] = [...additionalPlayerPoints, ...playerPointCollection];
        spyOn(playerPointService, 'addPlayerPointToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ playerDetailPoint });
        comp.ngOnInit();

        expect(playerPointService.query).toHaveBeenCalled();
        expect(playerPointService.addPlayerPointToCollectionIfMissing).toHaveBeenCalledWith(
          playerPointCollection,
          ...additionalPlayerPoints
        );
        expect(comp.playerPointsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call EventCategory query and add missing value', () => {
        const playerDetailPoint: IPlayerDetailPoint = { id: 456 };
        const eventCategory: IEventCategory = { id: 99043 };
        playerDetailPoint.eventCategory = eventCategory;

        const eventCategoryCollection: IEventCategory[] = [{ id: 71329 }];
        spyOn(eventCategoryService, 'query').and.returnValue(of(new HttpResponse({ body: eventCategoryCollection })));
        const additionalEventCategories = [eventCategory];
        const expectedCollection: IEventCategory[] = [...additionalEventCategories, ...eventCategoryCollection];
        spyOn(eventCategoryService, 'addEventCategoryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ playerDetailPoint });
        comp.ngOnInit();

        expect(eventCategoryService.query).toHaveBeenCalled();
        expect(eventCategoryService.addEventCategoryToCollectionIfMissing).toHaveBeenCalledWith(
          eventCategoryCollection,
          ...additionalEventCategories
        );
        expect(comp.eventCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const playerDetailPoint: IPlayerDetailPoint = { id: 456 };
        const playerPoint: IPlayerPoint = { id: 40648 };
        playerDetailPoint.playerPoint = playerPoint;
        const eventCategory: IEventCategory = { id: 95340 };
        playerDetailPoint.eventCategory = eventCategory;

        activatedRoute.data = of({ playerDetailPoint });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(playerDetailPoint));
        expect(comp.playerPointsSharedCollection).toContain(playerPoint);
        expect(comp.eventCategoriesSharedCollection).toContain(eventCategory);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const playerDetailPoint = { id: 123 };
        spyOn(playerDetailPointService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ playerDetailPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: playerDetailPoint }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(playerDetailPointService.update).toHaveBeenCalledWith(playerDetailPoint);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const playerDetailPoint = new PlayerDetailPoint();
        spyOn(playerDetailPointService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ playerDetailPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: playerDetailPoint }));
        saveSubject.complete();

        // THEN
        expect(playerDetailPointService.create).toHaveBeenCalledWith(playerDetailPoint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const playerDetailPoint = { id: 123 };
        spyOn(playerDetailPointService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ playerDetailPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(playerDetailPointService.update).toHaveBeenCalledWith(playerDetailPoint);
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
