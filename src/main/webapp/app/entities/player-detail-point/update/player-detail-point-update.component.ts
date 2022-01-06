import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPlayerDetailPoint, PlayerDetailPoint } from '../player-detail-point.model';
import { PlayerDetailPointService } from '../service/player-detail-point.service';
import { IPlayerPoint } from 'app/entities/player-point/player-point.model';
import { PlayerPointService } from 'app/entities/player-point/service/player-point.service';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/service/event-category.service';

@Component({
  selector: 'jhi-player-detail-point-update',
  templateUrl: './player-detail-point-update.component.html',
})
export class PlayerDetailPointUpdateComponent implements OnInit {
  isSaving = false;

  playerPointsSharedCollection: IPlayerPoint[] = [];
  eventCategoriesSharedCollection: IEventCategory[] = [];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.required]],
    playerPoint: [null, Validators.required],
    eventCategory: [],
  });

  constructor(
    protected playerDetailPointService: PlayerDetailPointService,
    protected playerPointService: PlayerPointService,
    protected eventCategoryService: EventCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playerDetailPoint }) => {
      this.updateForm(playerDetailPoint);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playerDetailPoint = this.createFromForm();
    if (playerDetailPoint.id !== undefined) {
      this.subscribeToSaveResponse(this.playerDetailPointService.update(playerDetailPoint));
    } else {
      this.subscribeToSaveResponse(this.playerDetailPointService.create(playerDetailPoint));
    }
  }

  trackPlayerPointById(index: number, item: IPlayerPoint): number {
    return item.id!;
  }

  trackEventCategoryById(index: number, item: IEventCategory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayerDetailPoint>>): void {
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

  protected updateForm(playerDetailPoint: IPlayerDetailPoint): void {
    this.editForm.patchValue({
      id: playerDetailPoint.id,
      points: playerDetailPoint.points,
      playerPoint: playerDetailPoint.playerPoint,
      eventCategory: playerDetailPoint.eventCategory,
    });

    this.playerPointsSharedCollection = this.playerPointService.addPlayerPointToCollectionIfMissing(
      this.playerPointsSharedCollection,
      playerDetailPoint.playerPoint
    );
    this.eventCategoriesSharedCollection = this.eventCategoryService.addEventCategoryToCollectionIfMissing(
      this.eventCategoriesSharedCollection,
      playerDetailPoint.eventCategory
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

  protected createFromForm(): IPlayerDetailPoint {
    return {
      ...new PlayerDetailPoint(),
      id: this.editForm.get(['id'])!.value,
      points: this.editForm.get(['points'])!.value,
      playerPoint: this.editForm.get(['playerPoint'])!.value,
      eventCategory: this.editForm.get(['eventCategory'])!.value,
    };
  }
}
