import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPlayerDetailPoint, PlayerDetailPoint } from '../player-detail-point.model';
import { PlayerDetailPointService } from '../service/player-detail-point.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { IPlayerPoint } from 'app/entities/player-point/player-point.model';
import { PlayerPointService } from 'app/entities/player-point/service/player-point.service';

@Component({
  selector: 'jhi-player-detail-point-update',
  templateUrl: './player-detail-point-update.component.html',
})
export class PlayerDetailPointUpdateComponent implements OnInit {
  isSaving = false;

  eventsSharedCollection: IEvent[] = [];
  playerPointsSharedCollection: IPlayerPoint[] = [];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.required]],
    event: [null, Validators.required],
    playerPoint: [null, Validators.required],
  });

  constructor(
    protected playerDetailPointService: PlayerDetailPointService,
    protected eventService: EventService,
    protected playerPointService: PlayerPointService,
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

  trackEventById(index: number, item: IEvent): number {
    return item.id!;
  }

  trackPlayerPointById(index: number, item: IPlayerPoint): number {
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
      event: playerDetailPoint.event,
      playerPoint: playerDetailPoint.playerPoint,
    });

    this.eventsSharedCollection = this.eventService.addEventToCollectionIfMissing(this.eventsSharedCollection, playerDetailPoint.event);
    this.playerPointsSharedCollection = this.playerPointService.addPlayerPointToCollectionIfMissing(
      this.playerPointsSharedCollection,
      playerDetailPoint.playerPoint
    );
  }

  protected loadRelationshipsOptions(): void {
    this.eventService
      .query()
      .pipe(map((res: HttpResponse<IEvent[]>) => res.body ?? []))
      .pipe(map((events: IEvent[]) => this.eventService.addEventToCollectionIfMissing(events, this.editForm.get('event')!.value)))
      .subscribe((events: IEvent[]) => (this.eventsSharedCollection = events));

    this.playerPointService
      .query()
      .pipe(map((res: HttpResponse<IPlayerPoint[]>) => res.body ?? []))
      .pipe(
        map((playerPoints: IPlayerPoint[]) =>
          this.playerPointService.addPlayerPointToCollectionIfMissing(playerPoints, this.editForm.get('playerPoint')!.value)
        )
      )
      .subscribe((playerPoints: IPlayerPoint[]) => (this.playerPointsSharedCollection = playerPoints));
  }

  protected createFromForm(): IPlayerDetailPoint {
    return {
      ...new PlayerDetailPoint(),
      id: this.editForm.get(['id'])!.value,
      points: this.editForm.get(['points'])!.value,
      event: this.editForm.get(['event'])!.value,
      playerPoint: this.editForm.get(['playerPoint'])!.value,
    };
  }
}
