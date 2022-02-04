import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISeason, Season } from '../season.model';
import { SeasonService } from '../service/season.service';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-season-update',
  templateUrl: './season-update.component.html',
})
export class SeasonUpdateComponent implements OnInit {
  isSaving = false;
  statusValues = Object.keys(Status);

  tournamentsSharedCollection: ITournament[] = [];

  editForm = this.fb.group({
    id: [],
    anio: [null, [Validators.required]],
    status: [null, [Validators.required]],
    tournament: [null, Validators.required],
  });

  constructor(
    protected seasonService: SeasonService,
    protected tournamentService: TournamentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ season }) => {
      this.updateForm(season);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const season = this.createFromForm();
    if (season.id !== undefined) {
      this.subscribeToSaveResponse(this.seasonService.update(season));
    } else {
      this.subscribeToSaveResponse(this.seasonService.create(season));
    }
  }

  trackTournamentById(index: number, item: ITournament): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISeason>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
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

  protected updateForm(season: ISeason): void {
    this.editForm.patchValue({
      id: season.id,
      anio: season.anio,
      status: season.status,
      tournament: season.tournament,
    });

    this.tournamentsSharedCollection = this.tournamentService.addTournamentToCollectionIfMissing(
      this.tournamentsSharedCollection,
      season.tournament
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

  protected createFromForm(): ISeason {
    return {
      ...new Season(),
      id: this.editForm.get(['id'])!.value,
      anio: this.editForm.get(['anio'])!.value,
      status: this.editForm.get(['status'])!.value,
      tournament: this.editForm.get(['tournament'])!.value,
    };
  }
}
