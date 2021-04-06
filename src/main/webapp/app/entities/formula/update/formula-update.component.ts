import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFormula, Formula } from '../formula.model';
import { FormulaService } from '../service/formula.service';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';

@Component({
  selector: 'jhi-formula-update',
  templateUrl: './formula-update.component.html',
})
export class FormulaUpdateComponent implements OnInit {
  isSaving = false;

  tournamentsSharedCollection: ITournament[] = [];

  editForm = this.fb.group({
    id: [],
    formula: [null, [Validators.required]],
    var1: [null, [Validators.required]],
    var2: [],
    var3: [],
    description: [null, [Validators.required]],
    example: [],
    tournament: [],
  });

  constructor(
    protected formulaService: FormulaService,
    protected tournamentService: TournamentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formula }) => {
      this.updateForm(formula);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formula = this.createFromForm();
    if (formula.id !== undefined) {
      this.subscribeToSaveResponse(this.formulaService.update(formula));
    } else {
      this.subscribeToSaveResponse(this.formulaService.create(formula));
    }
  }

  trackTournamentById(index: number, item: ITournament): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormula>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(formula: IFormula): void {
    this.editForm.patchValue({
      id: formula.id,
      formula: formula.formula,
      var1: formula.var1,
      var2: formula.var2,
      var3: formula.var3,
      description: formula.description,
      example: formula.example,
      tournament: formula.tournament,
    });

    this.tournamentsSharedCollection = this.tournamentService.addTournamentToCollectionIfMissing(
      this.tournamentsSharedCollection,
      formula.tournament
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tournamentService
      .query()
      .pipe(map((res: HttpResponse<ITournament[]>) => res.body ?? []))
      .pipe(
        map((tournaments: ITournament[]) =>
          this.tournamentService.addTournamentToCollectionIfMissing(tournaments, this.editForm.get('tournament')!.value)
        )
      )
      .subscribe((tournaments: ITournament[]) => (this.tournamentsSharedCollection = tournaments));
  }

  protected createFromForm(): IFormula {
    return {
      ...new Formula(),
      id: this.editForm.get(['id'])!.value,
      formula: this.editForm.get(['formula'])!.value,
      var1: this.editForm.get(['var1'])!.value,
      var2: this.editForm.get(['var2'])!.value,
      var3: this.editForm.get(['var3'])!.value,
      description: this.editForm.get(['description'])!.value,
      example: this.editForm.get(['example'])!.value,
      tournament: this.editForm.get(['tournament'])!.value,
    };
  }
}
