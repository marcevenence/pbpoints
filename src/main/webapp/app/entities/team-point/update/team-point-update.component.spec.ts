jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TeamPointService } from '../service/team-point.service';
import { ITeamPoint, TeamPoint } from '../team-point.model';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';

import { TeamPointUpdateComponent } from './team-point-update.component';

describe('Component Tests', () => {
  describe('TeamPoint Management Update Component', () => {
    let comp: TeamPointUpdateComponent;
    let fixture: ComponentFixture<TeamPointUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let teamPointService: TeamPointService;
    let teamService: TeamService;
    let tournamentService: TournamentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TeamPointUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TeamPointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TeamPointUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      teamPointService = TestBed.inject(TeamPointService);
      teamService = TestBed.inject(TeamService);
      tournamentService = TestBed.inject(TournamentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Team query and add missing value', () => {
        const teamPoint: ITeamPoint = { id: 456 };
        const team: ITeam = { id: 41436 };
        teamPoint.team = team;

        const teamCollection: ITeam[] = [{ id: 12627 }];
        spyOn(teamService, 'query').and.returnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [team];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        spyOn(teamService, 'addTeamToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ teamPoint });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Tournament query and add missing value', () => {
        const teamPoint: ITeamPoint = { id: 456 };
        const tournament: ITournament = { id: 47715 };
        teamPoint.tournament = tournament;

        const tournamentCollection: ITournament[] = [{ id: 79828 }];
        spyOn(tournamentService, 'query').and.returnValue(of(new HttpResponse({ body: tournamentCollection })));
        const additionalTournaments = [tournament];
        const expectedCollection: ITournament[] = [...additionalTournaments, ...tournamentCollection];
        spyOn(tournamentService, 'addTournamentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ teamPoint });
        comp.ngOnInit();

        expect(tournamentService.query).toHaveBeenCalled();
        expect(tournamentService.addTournamentToCollectionIfMissing).toHaveBeenCalledWith(tournamentCollection, ...additionalTournaments);
        expect(comp.tournamentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const teamPoint: ITeamPoint = { id: 456 };
        const team: ITeam = { id: 97157 };
        teamPoint.team = team;
        const tournament: ITournament = { id: 1313 };
        teamPoint.tournament = tournament;

        activatedRoute.data = of({ teamPoint });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(teamPoint));
        expect(comp.teamsSharedCollection).toContain(team);
        expect(comp.tournamentsSharedCollection).toContain(tournament);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const teamPoint = { id: 123 };
        spyOn(teamPointService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ teamPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: teamPoint }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(teamPointService.update).toHaveBeenCalledWith(teamPoint);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const teamPoint = new TeamPoint();
        spyOn(teamPointService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ teamPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: teamPoint }));
        saveSubject.complete();

        // THEN
        expect(teamPointService.create).toHaveBeenCalledWith(teamPoint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const teamPoint = { id: 123 };
        spyOn(teamPointService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ teamPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(teamPointService.update).toHaveBeenCalledWith(teamPoint);
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
