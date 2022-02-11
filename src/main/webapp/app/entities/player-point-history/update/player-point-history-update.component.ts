import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPlayerPointHistory, PlayerPointHistory } from '../player-point-history.model';
import { PlayerPointHistoryService } from '../service/player-point-history.service';
import { IPlayerPoint } from 'app/entities/player-point/player-point.model';
import { PlayerPointService } from 'app/entities/player-point/service/player-point.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ISeason } from 'app/entities/season/season.model';
import { SeasonService } from 'app/entities/season/service/season.service';

@Component({
  selector: 'jhi-player-point-history-update',
  templateUrl: './player-point-history-update.component.html',
})
export class PlayerPointHistoryUpdateComponent implements OnInit {
  isSaving = false;

  playerPointsSharedCollection: IPlayerPoint[] = [];
  categoriesSharedCollection: ICategory[] = [];
  seasonsSharedCollection: ISeason[] = [];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.required]],
    playerPoint: [],
    category: [null, Validators.required],
    season: [null, Validators.required],
  });

  constructor(
    protected playerPointHistoryService: PlayerPointHistoryService,
    protected playerPointService: PlayerPointService,
    protected categoryService: CategoryService,
    protected seasonService: SeasonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playerPointHistory }) => {
      this.updateForm(playerPointHistory);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playerPointHistory = this.createFromForm();
    if (playerPointHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.playerPointHistoryService.update(playerPointHistory));
    } else {
      this.subscribeToSaveResponse(this.playerPointHistoryService.create(playerPointHistory));
    }
  }

  trackPlayerPointById(index: number, item: IPlayerPoint): number {
    return item.id!;
  }

  trackCategoryById(index: number, item: ICategory): number {
    return item.id!;
  }

  trackSeasonById(index: number, item: ISeason): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayerPointHistory>>): void {
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

  protected updateForm(playerPointHistory: IPlayerPointHistory): void {
    this.editForm.patchValue({
      id: playerPointHistory.id,
      points: playerPointHistory.points,
      playerPoint: playerPointHistory.playerPoint,
      category: playerPointHistory.category,
      season: playerPointHistory.season,
    });

    this.playerPointsSharedCollection = this.playerPointService.addPlayerPointToCollectionIfMissing(
      this.playerPointsSharedCollection,
      playerPointHistory.playerPoint
    );
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(
      this.categoriesSharedCollection,
      playerPointHistory.category
    );
    this.seasonsSharedCollection = this.seasonService.addSeasonToCollectionIfMissing(
      this.seasonsSharedCollection,
      playerPointHistory.season
    );
  }

  protected loadRelationshipsOptions(): void {
    this.playerPointService
      .query()
      .pipe(map((res: HttpResponse<IPlayerPoint[]>) => res.body ?? []))
      .pipe(
        map((playerPoints: IPlayerPoint[]) =>
          this.playerPointService.addPlayerPointToCollectionIfMissing(playerPoints, this.editForm.get('playerPoint')!.value)
        )
      )
      .subscribe((playerPoints: IPlayerPoint[]) => (this.playerPointsSharedCollection = playerPoints));

    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));

    this.seasonService
      .query()
      .pipe(map((res: HttpResponse<ISeason[]>) => res.body ?? []))
      .pipe(map((seasons: ISeason[]) => this.seasonService.addSeasonToCollectionIfMissing(seasons, this.editForm.get('season')!.value)))
      .subscribe((seasons: ISeason[]) => (this.seasonsSharedCollection = seasons));
  }

  protected createFromForm(): IPlayerPointHistory {
    return {
      ...new PlayerPointHistory(),
      id: this.editForm.get(['id'])!.value,
      points: this.editForm.get(['points'])!.value,
      playerPoint: this.editForm.get(['playerPoint'])!.value,
      category: this.editForm.get(['category'])!.value,
      season: this.editForm.get(['season'])!.value,
    };
  }
}
