jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EventService } from '../service/event.service';
import { IEvent, Event } from '../event.model';
import { ICity } from 'app/entities/city/city.model';
import { CityService } from 'app/entities/city/service/city.service';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';

import { EventUpdateComponent } from './event-update.component';

describe('Component Tests', () => {
  describe('Event Management Update Component', () => {
    let comp: EventUpdateComponent;
    let fixture: ComponentFixture<EventUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let eventService: EventService;
    let cityService: CityService;
    let tournamentService: TournamentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EventUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EventUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EventUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      eventService = TestBed.inject(EventService);
      cityService = TestBed.inject(CityService);
      tournamentService = TestBed.inject(TournamentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call City query and add missing value', () => {
        const event: IEvent = { id: 456 };
        const city: ICity = { id: 71215 };
        event.city = city;

        const cityCollection: ICity[] = [{ id: 92821 }];
        spyOn(cityService, 'query').and.returnValue(of(new HttpResponse({ body: cityCollection })));
        const additionalCities = [city];
        const expectedCollection: ICity[] = [...additionalCities, ...cityCollection];
        spyOn(cityService, 'addCityToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ event });
        comp.ngOnInit();

        expect(cityService.query).toHaveBeenCalled();
        expect(cityService.addCityToCollectionIfMissing).toHaveBeenCalledWith(cityCollection, ...additionalCities);
        expect(comp.citiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Tournament query and add missing value', () => {
        const event: IEvent = { id: 456 };
        const tournament: ITournament = { id: 546 };
        event.tournament = tournament;

        const tournamentCollection: ITournament[] = [{ id: 83403 }];
        spyOn(tournamentService, 'query').and.returnValue(of(new HttpResponse({ body: tournamentCollection })));
        const additionalTournaments = [tournament];
        const expectedCollection: ITournament[] = [...additionalTournaments, ...tournamentCollection];
        spyOn(tournamentService, 'addTournamentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ event });
        comp.ngOnInit();

        expect(tournamentService.query).toHaveBeenCalled();
        expect(tournamentService.addTournamentToCollectionIfMissing).toHaveBeenCalledWith(tournamentCollection, ...additionalTournaments);
        expect(comp.tournamentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const event: IEvent = { id: 456 };
        const city: ICity = { id: 24430 };
        event.city = city;
        const tournament: ITournament = { id: 51078 };
        event.tournament = tournament;

        activatedRoute.data = of({ event });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(event));
        expect(comp.citiesSharedCollection).toContain(city);
        expect(comp.tournamentsSharedCollection).toContain(tournament);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const event = { id: 123 };
        spyOn(eventService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ event });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: event }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(eventService.update).toHaveBeenCalledWith(event);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const event = new Event();
        spyOn(eventService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ event });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: event }));
        saveSubject.complete();

        // THEN
        expect(eventService.create).toHaveBeenCalledWith(event);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const event = { id: 123 };
        spyOn(eventService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ event });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(eventService.update).toHaveBeenCalledWith(event);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCityById', () => {
        it('Should return tracked City primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTournamentById', () => {
        it('Should return tracked Tournament primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTournamentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
