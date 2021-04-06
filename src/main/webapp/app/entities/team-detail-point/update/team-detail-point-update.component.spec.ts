jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TeamDetailPointService } from '../service/team-detail-point.service';
import { ITeamDetailPoint, TeamDetailPoint } from '../team-detail-point.model';
import { ITeamPoint } from 'app/entities/team-point/team-point.model';
import { TeamPointService } from 'app/entities/team-point/service/team-point.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';

import { TeamDetailPointUpdateComponent } from './team-detail-point-update.component';

describe('Component Tests', () => {
  describe('TeamDetailPoint Management Update Component', () => {
    let comp: TeamDetailPointUpdateComponent;
    let fixture: ComponentFixture<TeamDetailPointUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let teamDetailPointService: TeamDetailPointService;
    let teamPointService: TeamPointService;
    let eventService: EventService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TeamDetailPointUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TeamDetailPointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TeamDetailPointUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      teamDetailPointService = TestBed.inject(TeamDetailPointService);
      teamPointService = TestBed.inject(TeamPointService);
      eventService = TestBed.inject(EventService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call TeamPoint query and add missing value', () => {
        const teamDetailPoint: ITeamDetailPoint = { id: 456 };
        const teamPoint: ITeamPoint = { id: 33104 };
        teamDetailPoint.teamPoint = teamPoint;

        const teamPointCollection: ITeamPoint[] = [{ id: 19095 }];
        spyOn(teamPointService, 'query').and.returnValue(of(new HttpResponse({ body: teamPointCollection })));
        const additionalTeamPoints = [teamPoint];
        const expectedCollection: ITeamPoint[] = [...additionalTeamPoints, ...teamPointCollection];
        spyOn(teamPointService, 'addTeamPointToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ teamDetailPoint });
        comp.ngOnInit();

        expect(teamPointService.query).toHaveBeenCalled();
        expect(teamPointService.addTeamPointToCollectionIfMissing).toHaveBeenCalledWith(teamPointCollection, ...additionalTeamPoints);
        expect(comp.teamPointsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Event query and add missing value', () => {
        const teamDetailPoint: ITeamDetailPoint = { id: 456 };
        const event: IEvent = { id: 97725 };
        teamDetailPoint.event = event;

        const eventCollection: IEvent[] = [{ id: 67475 }];
        spyOn(eventService, 'query').and.returnValue(of(new HttpResponse({ body: eventCollection })));
        const additionalEvents = [event];
        const expectedCollection: IEvent[] = [...additionalEvents, ...eventCollection];
        spyOn(eventService, 'addEventToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ teamDetailPoint });
        comp.ngOnInit();

        expect(eventService.query).toHaveBeenCalled();
        expect(eventService.addEventToCollectionIfMissing).toHaveBeenCalledWith(eventCollection, ...additionalEvents);
        expect(comp.eventsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const teamDetailPoint: ITeamDetailPoint = { id: 456 };
        const teamPoint: ITeamPoint = { id: 76798 };
        teamDetailPoint.teamPoint = teamPoint;
        const event: IEvent = { id: 88157 };
        teamDetailPoint.event = event;

        activatedRoute.data = of({ teamDetailPoint });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(teamDetailPoint));
        expect(comp.teamPointsSharedCollection).toContain(teamPoint);
        expect(comp.eventsSharedCollection).toContain(event);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const teamDetailPoint = { id: 123 };
        spyOn(teamDetailPointService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ teamDetailPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: teamDetailPoint }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(teamDetailPointService.update).toHaveBeenCalledWith(teamDetailPoint);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const teamDetailPoint = new TeamDetailPoint();
        spyOn(teamDetailPointService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ teamDetailPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: teamDetailPoint }));
        saveSubject.complete();

        // THEN
        expect(teamDetailPointService.create).toHaveBeenCalledWith(teamDetailPoint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const teamDetailPoint = { id: 123 };
        spyOn(teamDetailPointService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ teamDetailPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(teamDetailPointService.update).toHaveBeenCalledWith(teamDetailPoint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTeamPointById', () => {
        it('Should return tracked TeamPoint primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTeamPointById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackEventById', () => {
        it('Should return tracked Event primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEventById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
