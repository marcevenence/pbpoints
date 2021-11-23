jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TournamentGroupService } from '../service/tournament-group.service';
import { ITournamentGroup, TournamentGroup } from '../tournament-group.model';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';

import { TournamentGroupUpdateComponent } from './tournament-group-update.component';

describe('Component Tests', () => {
  describe('TournamentGroup Management Update Component', () => {
    let comp: TournamentGroupUpdateComponent;
    let fixture: ComponentFixture<TournamentGroupUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tournamentGroupService: TournamentGroupService;
    let tournamentService: TournamentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TournamentGroupUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TournamentGroupUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TournamentGroupUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tournamentGroupService = TestBed.inject(TournamentGroupService);
      tournamentService = TestBed.inject(TournamentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Tournament query and add missing value', () => {
        const tournamentGroup: ITournamentGroup = { id: 456 };
        const tournamentA: ITournament = { id: 54173 };
        tournamentGroup.tournamentA = tournamentA;
        const tournamentB: ITournament = { id: 18619 };
        tournamentGroup.tournamentB = tournamentB;

        const tournamentCollection: ITournament[] = [{ id: 80570 }];
        spyOn(tournamentService, 'query').and.returnValue(of(new HttpResponse({ body: tournamentCollection })));
        const additionalTournaments = [tournamentA, tournamentB];
        const expectedCollection: ITournament[] = [...additionalTournaments, ...tournamentCollection];
        spyOn(tournamentService, 'addTournamentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ tournamentGroup });
        comp.ngOnInit();

        expect(tournamentService.query).toHaveBeenCalled();
        expect(tournamentService.addTournamentToCollectionIfMissing).toHaveBeenCalledWith(tournamentCollection, ...additionalTournaments);
        expect(comp.tournamentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tournamentGroup: ITournamentGroup = { id: 456 };
        const tournamentA: ITournament = { id: 546 };
        tournamentGroup.tournamentA = tournamentA;
        const tournamentB: ITournament = { id: 83403 };
        tournamentGroup.tournamentB = tournamentB;

        activatedRoute.data = of({ tournamentGroup });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tournamentGroup));
        expect(comp.tournamentsSharedCollection).toContain(tournamentA);
        expect(comp.tournamentsSharedCollection).toContain(tournamentB);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tournamentGroup = { id: 123 };
        spyOn(tournamentGroupService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tournamentGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tournamentGroup }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tournamentGroupService.update).toHaveBeenCalledWith(tournamentGroup);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tournamentGroup = new TournamentGroup();
        spyOn(tournamentGroupService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tournamentGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tournamentGroup }));
        saveSubject.complete();

        // THEN
        expect(tournamentGroupService.create).toHaveBeenCalledWith(tournamentGroup);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tournamentGroup = { id: 123 };
        spyOn(tournamentGroupService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tournamentGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tournamentGroupService.update).toHaveBeenCalledWith(tournamentGroup);
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
    });
  });
});
