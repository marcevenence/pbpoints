import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IGame, Game } from '../game.model';
import { GameService } from '../service/game.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/service/event-category.service';

@Component({
  selector: 'jhi-game-update',
  templateUrl: './game-update.component.html',
})
export class GameUpdateComponent implements OnInit {
  isSaving = false;
  eCatId = 0;
  teamsSharedCollection: ITeam[] = [];
  eventCategoriesSharedCollection: IEventCategory[] = [];

  editForm = this.fb.group({
    id: [],
    pointsA: [],
    pointsB: [],
    splitDeckNum: [],
    timeLeft: [],
    status: [null, [Validators.required]],
    overtimeA: [],
    overtimeB: [],
    uvuA: [],
    uvuB: [],
    group: [],
    clasif: [],
    teamA: [null, Validators.required],
    teamB: [null, Validators.required],
    eventCategory: [null, Validators.required],
  });

  constructor(
    protected gameService: GameService,
    protected teamService: TeamService,
    protected eventCategoryService: EventCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ game }) => {
      this.updateForm(game);
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const game = this.createFromForm();
    if (game.id !== undefined) {
      this.subscribeToSaveResponse(this.gameService.update(game));
    } else {
      this.subscribeToSaveResponse(this.gameService.create(game));
    }
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  trackEventCategoryById(index: number, item: IEventCategory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGame>>): void {
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

  protected updateForm(game: IGame): void {
    this.editForm.patchValue({
      id: game.id,
      pointsA: game.pointsA,
      pointsB: game.pointsB,
      splitDeckNum: game.splitDeckNum,
      timeLeft: game.timeLeft,
      status: game.status,
      overtimeA: game.overtimeA,
      overtimeB: game.overtimeB,
      uvuA: game.uvuA,
      uvuB: game.uvuB,
      group: game.group,
      clasif: game.clasif,
      teamA: game.teamA,
      teamB: game.teamB,
      eventCategory: game.eventCategory,
    });

    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, game.teamA, game.teamB);
    this.eventCategoriesSharedCollection = this.eventCategoryService.addEventCategoryToCollectionIfMissing(
      this.eventCategoriesSharedCollection,
      game.eventCategory
    );
  }

  protected loadRelationshipsOptions(): void {
    this.eCatId = history.state.eCatId ?? 0;
    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(
        map((teams: ITeam[]) =>
          this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('teamA')!.value, this.editForm.get('teamB')!.value)
        )
      )
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));

    if (this.eCatId !== 0) {
      this.eventCategoryService
        .query({ 'id.equals': this.eCatId })
        .pipe(map((res: HttpResponse<IEventCategory[]>) => res.body ?? []))
        .pipe(
          map((eventCategories: IEventCategory[]) =>
            this.eventCategoryService.addEventCategoryToCollectionIfMissing(eventCategories, this.editForm.get('eventCategory')!.value)
          )
        )
        .subscribe((eventCategories: IEventCategory[]) => (this.eventCategoriesSharedCollection = eventCategories));
    } else {
      this.eventCategoryService
        .query()
        .pipe(map((res: HttpResponse<IEventCategory[]>) => res.body ?? []))
        .pipe(
          map((eventCategories: IEventCategory[]) =>
            this.eventCategoryService.addEventCategoryToCollectionIfMissing(eventCategories, this.editForm.get('eventCategory')!.value)
          )
        )
        .subscribe((eventCategories: IEventCategory[]) => (this.eventCategoriesSharedCollection = eventCategories));
    }
  }

  protected createFromForm(): IGame {
    return {
      ...new Game(),
      id: this.editForm.get(['id'])!.value,
      pointsA: this.editForm.get(['pointsA'])!.value,
      pointsB: this.editForm.get(['pointsB'])!.value,
      splitDeckNum: this.editForm.get(['splitDeckNum'])!.value,
      timeLeft: this.editForm.get(['timeLeft'])!.value,
      status: this.editForm.get(['status'])!.value,
      overtimeA: this.editForm.get(['overtimeA'])!.value,
      overtimeB: this.editForm.get(['overtimeB'])!.value,
      uvuA: this.editForm.get(['uvuA'])!.value,
      uvuB: this.editForm.get(['uvuB'])!.value,
      group: this.editForm.get(['group'])!.value,
      clasif: this.editForm.get(['clasif'])!.value,
      teamA: this.editForm.get(['teamA'])!.value,
      teamB: this.editForm.get(['teamB'])!.value,
      eventCategory: this.editForm.get(['eventCategory'])!.value,
    };
  }
}
