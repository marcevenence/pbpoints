import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFormat, Format } from '../format.model';
import { FormatService } from '../service/format.service';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';

@Component({
  selector: 'jhi-format-update',
  templateUrl: './format-update.component.html',
})
export class FormatUpdateComponent implements OnInit {
  isSaving = false;
  tourId: any;
  tournamentsSharedCollection: ITournament[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    coeficient: [null, [Validators.required]],
    playersQty: [],
    tournament: [null, Validators.required],
  });

  constructor(
    protected formatService: FormatService,
    protected tournamentService: TournamentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.tourId = history.state.tourId ?? 0;
    this.activatedRoute.data.subscribe(({ format }) => {
      this.updateForm(format);
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const format = this.createFromForm();
    if (format.id !== undefined) {
      this.subscribeToSaveResponse(this.formatService.update(format));
    } else {
      this.subscribeToSaveResponse(this.formatService.create(format));
    }
  }

  trackTournamentById(index: number, item: ITournament): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormat>>): void {
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

  protected updateForm(format: IFormat): void {
    this.editForm.patchValue({
      id: format.id,
      name: format.name,
      description: format.description,
      coeficient: format.coeficient,
      playersQty: format.playersQty,
      tournament: format.tournament,
    });

    this.tournamentsSharedCollection = this.tournamentService.addTournamentToCollectionIfMissing(
      this.tournamentsSharedCollection,
      format.tournament
    );
  }

  protected loadRelationshipsOptions(): void {
    if (this.tourId === 0) {
      this.tournamentService
        .query()
        .pipe(map((res: HttpResponse<ITournament[]>) => res.body ?? []))
        .pipe(
          map((tournaments: ITournament[]) =>
            this.tournamentService.addTournamentToCollectionIfMissing(tournaments, this.editForm.get('tournament')!.value)
          )
        )
        .subscribe((tournaments: ITournament[]) => (this.tournamentsSharedCollection = tournaments));
    } else {
      this.tournamentService
        .query({ 'id.equals': this.tourId })
        .pipe(map((res: HttpResponse<ITournament[]>) => res.body ?? []))
        .pipe(
          map((tournaments: ITournament[]) =>
            this.tournamentService.addTournamentToCollectionIfMissing(tournaments, this.editForm.get('tournament')!.value)
          )
        )
        .subscribe((tournaments: ITournament[]) => (this.tournamentsSharedCollection = tournaments));
    }
  }

  protected createFromForm(): IFormat {
    return {
      ...new Format(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      coeficient: this.editForm.get(['coeficient'])!.value,
      playersQty: this.editForm.get(['playersQty'])!.value,
      tournament: this.editForm.get(['tournament'])!.value,
    };
  }
}
