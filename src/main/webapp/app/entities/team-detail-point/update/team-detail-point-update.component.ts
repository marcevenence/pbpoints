import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITeamDetailPoint, TeamDetailPoint } from '../team-detail-point.model';
import { TeamDetailPointService } from '../service/team-detail-point.service';
import { ITeamPoint } from 'app/entities/team-point/team-point.model';
import { TeamPointService } from 'app/entities/team-point/service/team-point.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';

@Component({
  selector: 'jhi-team-detail-point-update',
  templateUrl: './team-detail-point-update.component.html',
})
export class TeamDetailPointUpdateComponent implements OnInit {
  isSaving = false;

  teamPointsSharedCollection: ITeamPoint[] = [];
  eventsSharedCollection: IEvent[] = [];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.required]],
    position: [],
    teamPoint: [null, Validators.required],
    event: [null, Validators.required],
  });

  constructor(
    protected teamDetailPointService: TeamDetailPointService,
    protected teamPointService: TeamPointService,
    protected eventService: EventService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teamDetailPoint }) => {
      this.updateForm(teamDetailPoint);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const teamDetailPoint = this.createFromForm();
    if (teamDetailPoint.id !== undefined) {
      this.subscribeToSaveResponse(this.teamDetailPointService.update(teamDetailPoint));
    } else {
      this.subscribeToSaveResponse(this.teamDetailPointService.create(teamDetailPoint));
    }
  }

  trackTeamPointById(index: number, item: ITeamPoint): number {
    return item.id!;
  }

  trackEventById(index: number, item: IEvent): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeamDetailPoint>>): void {
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

  protected updateForm(teamDetailPoint: ITeamDetailPoint): void {
    this.editForm.patchValue({
      id: teamDetailPoint.id,
      points: teamDetailPoint.points,
      position: teamDetailPoint.position,
      teamPoint: teamDetailPoint.teamPoint,
      event: teamDetailPoint.event,
    });

    this.teamPointsSharedCollection = this.teamPointService.addTeamPointToCollectionIfMissing(
      this.teamPointsSharedCollection,
      teamDetailPoint.teamPoint
    );
    this.eventsSharedCollection = this.eventService.addEventToCollectionIfMissing(this.eventsSharedCollection, teamDetailPoint.event);
  }

  protected loadRelationshipsOptions(): void {
    this.teamPointService
      .query()
      .pipe(map((res: HttpResponse<ITeamPoint[]>) => res.body ?? []))
      .pipe(
        map((teamPoints: ITeamPoint[]) =>
          this.teamPointService.addTeamPointToCollectionIfMissing(teamPoints, this.editForm.get('teamPoint')!.value)
        )
      )
      .subscribe((teamPoints: ITeamPoint[]) => (this.teamPointsSharedCollection = teamPoints));

    this.eventService
      .query()
      .pipe(map((res: HttpResponse<IEvent[]>) => res.body ?? []))
      .pipe(map((events: IEvent[]) => this.eventService.addEventToCollectionIfMissing(events, this.editForm.get('event')!.value)))
      .subscribe((events: IEvent[]) => (this.eventsSharedCollection = events));
  }

  protected createFromForm(): ITeamDetailPoint {
    return {
      ...new TeamDetailPoint(),
      id: this.editForm.get(['id'])!.value,
      points: this.editForm.get(['points'])!.value,
      position: this.editForm.get(['position'])!.value,
      teamPoint: this.editForm.get(['teamPoint'])!.value,
      event: this.editForm.get(['event'])!.value,
    };
  }
}
