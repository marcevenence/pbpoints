jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FormulaService } from '../service/formula.service';
import { IFormula, Formula } from '../formula.model';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';

import { FormulaUpdateComponent } from './formula-update.component';

describe('Component Tests', () => {
  describe('Formula Management Update Component', () => {
    let comp: FormulaUpdateComponent;
    let fixture: ComponentFixture<FormulaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let formulaService: FormulaService;
    let tournamentService: TournamentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FormulaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FormulaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FormulaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      formulaService = TestBed.inject(FormulaService);
      tournamentService = TestBed.inject(TournamentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Tournament query and add missing value', () => {
        const formula: IFormula = { id: 456 };
        const tournament: ITournament = { id: 24581 };
        formula.tournament = tournament;

        const tournamentCollection: ITournament[] = [{ id: 23326 }];
        spyOn(tournamentService, 'query').and.returnValue(of(new HttpResponse({ body: tournamentCollection })));
        const additionalTournaments = [tournament];
        const expectedCollection: ITournament[] = [...additionalTournaments, ...tournamentCollection];
        spyOn(tournamentService, 'addTournamentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ formula });
        comp.ngOnInit();

        expect(tournamentService.query).toHaveBeenCalled();
        expect(tournamentService.addTournamentToCollectionIfMissing).toHaveBeenCalledWith(tournamentCollection, ...additionalTournaments);
        expect(comp.tournamentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const formula: IFormula = { id: 456 };
        const tournament: ITournament = { id: 41598 };
        formula.tournament = tournament;

        activatedRoute.data = of({ formula });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(formula));
        expect(comp.tournamentsSharedCollection).toContain(tournament);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const formula = { id: 123 };
        spyOn(formulaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ formula });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: formula }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(formulaService.update).toHaveBeenCalledWith(formula);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const formula = new Formula();
        spyOn(formulaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ formula });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: formula }));
        saveSubject.complete();

        // THEN
        expect(formulaService.create).toHaveBeenCalledWith(formula);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const formula = { id: 123 };
        spyOn(formulaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ formula });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(formulaService.update).toHaveBeenCalledWith(formula);
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
