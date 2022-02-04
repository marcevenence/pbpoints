import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EventService } from '../service/event.service';
import { IEvent, Event } from '../event.model';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';
import { IField } from 'app/entities/field/field.model';
import { FieldService } from 'app/entities/field/service/field.service';
import { ISeason } from 'app/entities/season/season.model';
import { SeasonService } from 'app/entities/season/service/season.service';

import { EventUpdateComponent } from './event-update.component';

describe('Event Management Update Component', () => {
  let comp: EventUpdateComponent;
  let fixture: ComponentFixture<EventUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eventService: EventService;
  let tournamentService: TournamentService;
  let fieldService: FieldService;
  let seasonService: SeasonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EventUpdateComponent],
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
      .overrideTemplate(EventUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eventService = TestBed.inject(EventService);
    tournamentService = TestBed.inject(TournamentService);
    fieldService = TestBed.inject(FieldService);
    seasonService = TestBed.inject(SeasonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tournament query and add missing value', () => {
      const event: IEvent = { id: 456 };
      const tournament: ITournament = { id: 1311 };
      event.tournament = tournament;

      const tournamentCollection: ITournament[] = [{ id: 63988 }];
      jest.spyOn(tournamentService, 'query').mockReturnValue(of(new HttpResponse({ body: tournamentCollection })));
      const additionalTournaments = [tournament];
      const expectedCollection: ITournament[] = [...additionalTournaments, ...tournamentCollection];
      jest.spyOn(tournamentService, 'addTournamentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ event });
      comp.ngOnInit();

      expect(tournamentService.query).toHaveBeenCalled();
      expect(tournamentService.addTournamentToCollectionIfMissing).toHaveBeenCalledWith(tournamentCollection, ...additionalTournaments);
      expect(comp.tournamentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Field query and add missing value', () => {
      const event: IEvent = { id: 456 };
      const field: IField = { id: 32981 };
      event.field = field;

      const fieldCollection: IField[] = [{ id: 10965 }];
      jest.spyOn(fieldService, 'query').mockReturnValue(of(new HttpResponse({ body: fieldCollection })));
      const additionalFields = [field];
      const expectedCollection: IField[] = [...additionalFields, ...fieldCollection];
      jest.spyOn(fieldService, 'addFieldToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ event });
      comp.ngOnInit();

      expect(fieldService.query).toHaveBeenCalled();
      expect(fieldService.addFieldToCollectionIfMissing).toHaveBeenCalledWith(fieldCollection, ...additionalFields);
      expect(comp.fieldsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Season query and add missing value', () => {
      const event: IEvent = { id: 456 };
      const season: ISeason = { id: 67928 };
      event.season = season;

      const seasonCollection: ISeason[] = [{ id: 11064 }];
      jest.spyOn(seasonService, 'query').mockReturnValue(of(new HttpResponse({ body: seasonCollection })));
      const additionalSeasons = [season];
      const expectedCollection: ISeason[] = [...additionalSeasons, ...seasonCollection];
      jest.spyOn(seasonService, 'addSeasonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ event });
      comp.ngOnInit();

      expect(seasonService.query).toHaveBeenCalled();
      expect(seasonService.addSeasonToCollectionIfMissing).toHaveBeenCalledWith(seasonCollection, ...additionalSeasons);
      expect(comp.seasonsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const event: IEvent = { id: 456 };
      const tournament: ITournament = { id: 48109 };
      event.tournament = tournament;
      const field: IField = { id: 94807 };
      event.field = field;
      const season: ISeason = { id: 38420 };
      event.season = season;

      activatedRoute.data = of({ event });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(event));
      expect(comp.tournamentsSharedCollection).toContain(tournament);
      expect(comp.fieldsSharedCollection).toContain(field);
      expect(comp.seasonsSharedCollection).toContain(season);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Event>>();
      const event = { id: 123 };
      jest.spyOn(eventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
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
      const saveSubject = new Subject<HttpResponse<Event>>();
      const event = new Event();
      jest.spyOn(eventService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
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
      const saveSubject = new Subject<HttpResponse<Event>>();
      const event = { id: 123 };
      jest.spyOn(eventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
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
    describe('trackTournamentById', () => {
      it('Should return tracked Tournament primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTournamentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFieldById', () => {
      it('Should return tracked Field primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFieldById(0, entity);
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
