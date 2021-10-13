jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MainRosterService } from '../service/main-roster.service';
import { IMainRoster, MainRoster } from '../main-roster.model';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IUserExtra } from 'app/entities/user-extra/user-extra.model';
import { UserExtraService } from 'app/entities/user-extra/service/user-extra.service';

import { MainRosterUpdateComponent } from './main-roster-update.component';

describe('Component Tests', () => {
  describe('MainRoster Management Update Component', () => {
    let comp: MainRosterUpdateComponent;
    let fixture: ComponentFixture<MainRosterUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let mainRosterService: MainRosterService;
    let teamService: TeamService;
    let userExtraService: UserExtraService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MainRosterUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MainRosterUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MainRosterUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      mainRosterService = TestBed.inject(MainRosterService);
      teamService = TestBed.inject(TeamService);
      userExtraService = TestBed.inject(UserExtraService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Team query and add missing value', () => {
        const mainRoster: IMainRoster = { id: 456 };
        const team: ITeam = { id: 6404 };
        mainRoster.team = team;

        const teamCollection: ITeam[] = [{ id: 3285 }];
        spyOn(teamService, 'query').and.returnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [team];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        spyOn(teamService, 'addTeamToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ mainRoster });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call UserExtra query and add missing value', () => {
        const mainRoster: IMainRoster = { id: 456 };
        const userExtra: IUserExtra = { id: 69019 };
        mainRoster.userExtra = userExtra;

        const userExtraCollection: IUserExtra[] = [{ id: 59131 }];
        spyOn(userExtraService, 'query').and.returnValue(of(new HttpResponse({ body: userExtraCollection })));
        const additionalUserExtras = [userExtra];
        const expectedCollection: IUserExtra[] = [...additionalUserExtras, ...userExtraCollection];
        spyOn(userExtraService, 'addUserExtraToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ mainRoster });
        comp.ngOnInit();

        expect(userExtraService.query).toHaveBeenCalled();
        expect(userExtraService.addUserExtraToCollectionIfMissing).toHaveBeenCalledWith(userExtraCollection, ...additionalUserExtras);
        expect(comp.userExtrasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const mainRoster: IMainRoster = { id: 456 };
        const team: ITeam = { id: 13467 };
        mainRoster.team = team;
        const userExtra: IUserExtra = { id: 45246 };
        mainRoster.userExtra = userExtra;

        activatedRoute.data = of({ mainRoster });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(mainRoster));
        expect(comp.teamsSharedCollection).toContain(team);
        expect(comp.userExtrasSharedCollection).toContain(userExtra);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mainRoster = { id: 123 };
        spyOn(mainRosterService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mainRoster });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mainRoster }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(mainRosterService.update).toHaveBeenCalledWith(mainRoster);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mainRoster = new MainRoster();
        spyOn(mainRosterService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mainRoster });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mainRoster }));
        saveSubject.complete();

        // THEN
        expect(mainRosterService.create).toHaveBeenCalledWith(mainRoster);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mainRoster = { id: 123 };
        spyOn(mainRosterService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mainRoster });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(mainRosterService.update).toHaveBeenCalledWith(mainRoster);
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

      describe('trackUserExtraById', () => {
        it('Should return tracked UserExtra primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserExtraById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
