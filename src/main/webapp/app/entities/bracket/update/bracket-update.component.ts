import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBracket, Bracket } from '../bracket.model';
import { BracketService } from '../service/bracket.service';

@Component({
  selector: 'jhi-bracket-update',
  templateUrl: './bracket-update.component.html',
})
export class BracketUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    teams: [null, [Validators.required]],
    teams5A: [null, [Validators.required]],
    teams5B: [null, [Validators.required]],
    teams6A: [null, [Validators.required]],
    teams6B: [null, [Validators.required]],
  });

  constructor(protected bracketService: BracketService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bracket }) => {
      this.updateForm(bracket);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bracket = this.createFromForm();
    if (bracket.id !== undefined) {
      this.subscribeToSaveResponse(this.bracketService.update(bracket));
    } else {
      this.subscribeToSaveResponse(this.bracketService.create(bracket));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBracket>>): void {
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

  protected updateForm(bracket: IBracket): void {
    this.editForm.patchValue({
      id: bracket.id,
      teams: bracket.teams,
      teams5A: bracket.teams5A,
      teams5B: bracket.teams5B,
      teams6A: bracket.teams6A,
      teams6B: bracket.teams6B,
    });
  }

  protected createFromForm(): IBracket {
    return {
      ...new Bracket(),
      id: this.editForm.get(['id'])!.value,
      teams: this.editForm.get(['teams'])!.value,
      teams5A: this.editForm.get(['teams5A'])!.value,
      teams5B: this.editForm.get(['teams5B'])!.value,
      teams6A: this.editForm.get(['teams6A'])!.value,
      teams6B: this.editForm.get(['teams6B'])!.value,
    };
  }
}
