jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PlayerDetailPointService } from '../service/player-detail-point.service';
import { IPlayerDetailPoint, PlayerDetailPoint } from '../player-detail-point.model';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { IPlayerPoint } from 'app/entities/player-point/player-point.model';
import { PlayerPointService } from 'app/entities/player-point/service/player-point.service';

import { PlayerDetailPointUpdateComponent } from './player-detail-point-update.component';

describe('Component Tests', () => {
  describe('PlayerDetailPoint Management Update Component', () => {
    let comp: PlayerDetailPointUpdateComponent;
    let fixture: ComponentFixture<PlayerDetailPointUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let playerDetailPointService: PlayerDetailPointService;
    let eventService: EventService;
    let playerPointService: PlayerPointService;

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
      eventService = TestBed.inject(EventService);
      playerPointService = TestBed.inject(PlayerPointService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Event query and add missing value', () => {
        const playerDetailPoint: IPlayerDetailPoint = { id: 456 };
        const event: IEvent = { id: 22148 };
        playerDetailPoint.event = event;

        const eventCollection: IEvent[] = [{ id: 44062 }];
        spyOn(eventService, 'query').and.returnValue(of(new HttpResponse({ body: eventCollection })));
        const additionalEvents = [event];
        const expectedCollection: IEvent[] = [...additionalEvents, ...eventCollection];
        spyOn(eventService, 'addEventToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ playerDetailPoint });
        comp.ngOnInit();

        expect(eventService.query).toHaveBeenCalled();
        expect(eventService.addEventToCollectionIfMissing).toHaveBeenCalledWith(eventCollection, ...additionalEvents);
        expect(comp.eventsSharedCollection).toEqual(expectedCollection);
      });

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

      it('Should update editForm', () => {
        const playerDetailPoint: IPlayerDetailPoint = { id: 456 };
        const event: IEvent = { id: 36811 };
        playerDetailPoint.event = event;
        const playerPoint: IPlayerPoint = { id: 40648 };
        playerDetailPoint.playerPoint = playerPoint;

        activatedRoute.data = of({ playerDetailPoint });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(playerDetailPoint));
        expect(comp.eventsSharedCollection).toContain(event);
        expect(comp.playerPointsSharedCollection).toContain(playerPoint);
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
      describe('trackEventById', () => {
        it('Should return tracked Event primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEventById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPlayerPointById', () => {
        it('Should return tracked PlayerPoint primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPlayerPointById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
