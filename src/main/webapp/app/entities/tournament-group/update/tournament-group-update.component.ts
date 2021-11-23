import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITournamentGroup, TournamentGroup } from '../tournament-group.model';
import { TournamentGroupService } from '../service/tournament-group.service';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';

@Component({
  selector: 'jhi-tournament-group-update',
  templateUrl: './tournament-group-update.component.html',
})
export class TournamentGroupUpdateComponent implements OnInit {
  isSaving = false;

  tournamentsSharedCollection: ITournament[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    tournamentA: [null, Validators.required],
    tournamentB: [null, Validators.required],
  });

  constructor(
    protected tournamentGroupService: TournamentGroupService,
    protected tournamentService: TournamentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tournamentGroup }) => {
      this.updateForm(tournamentGroup);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tournamentGroup = this.createFromForm();
    if (tournamentGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.tournamentGroupService.update(tournamentGroup));
    } else {
      this.subscribeToSaveResponse(this.tournamentGroupService.create(tournamentGroup));
    }
  }

  trackTournamentById(index: number, item: ITournament): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITournamentGroup>>): void {
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

  protected updateForm(tournamentGroup: ITournamentGroup): void {
    this.editForm.patchValue({
      id: tournamentGroup.id,
      name: tournamentGroup.name,
      tournamentA: tournamentGroup.tournamentA,
      tournamentB: tournamentGroup.tournamentB,
    });

    this.tournamentsSharedCollection = this.tournamentService.addTournamentToCollectionIfMissing(
      this.tournamentsSharedCollection,
      tournamentGroup.tournamentA,
      tournamentGroup.tournamentB
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tournamentService
      .query()
      .pipe(map((res: HttpResponse<ITournament[]>) => res.body ?? []))
      .pipe(
        map((tournaments: ITournament[]) =>
          this.tournamentService.addTournamentToCollectionIfMissing(
            tournaments,
            this.editForm.get('tournamentA')!.value,
            this.editForm.get('tournamentB')!.value
          )
        )
      )
      .subscribe((tournaments: ITournament[]) => (this.tournamentsSharedCollection = tournaments));
  }

  protected createFromForm(): ITournamentGroup {
    return {
      ...new TournamentGroup(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      tournamentA: this.editForm.get(['tournamentA'])!.value,
      tournamentB: this.editForm.get(['tournamentB'])!.value,
    };
  }
}
