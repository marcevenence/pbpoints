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
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/service/event-category.service';

@Component({
  selector: 'jhi-team-detail-point-update',
  templateUrl: './team-detail-point-update.component.html',
})
export class TeamDetailPointUpdateComponent implements OnInit {
  isSaving = false;

  teamPointsSharedCollection: ITeamPoint[] = [];
  eventCategoriesSharedCollection: IEventCategory[] = [];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.required]],
    teamPoint: [null, Validators.required],
    eventCategory: [null, Validators.required],
  });

  constructor(
    protected teamDetailPointService: TeamDetailPointService,
    protected teamPointService: TeamPointService,
    protected eventCategoryService: EventCategoryService,
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

  trackEventCategoryById(index: number, item: IEventCategory): number {
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
      teamPoint: teamDetailPoint.teamPoint,
      eventCategory: teamDetailPoint.eventCategory,
    });

    this.teamPointsSharedCollection = this.teamPointService.addTeamPointToCollectionIfMissing(
      this.teamPointsSharedCollection,
      teamDetailPoint.teamPoint
    );
    this.eventCategoriesSharedCollection = this.eventCategoryService.addEventCategoryToCollectionIfMissing(
      this.eventCategoriesSharedCollection,
      teamDetailPoint.eventCategory
    );
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

  protected createFromForm(): ITeamDetailPoint {
    return {
      ...new TeamDetailPoint(),
      id: this.editForm.get(['id'])!.value,
      points: this.editForm.get(['points'])!.value,
      teamPoint: this.editForm.get(['teamPoint'])!.value,
      eventCategory: this.editForm.get(['eventCategory'])!.value,
    };
  }
}
