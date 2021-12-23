import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITeamCategoryPoint, TeamCategoryPoint } from '../team-category-point.model';
import { TeamCategoryPointService } from '../service/team-category-point.service';
import { ITeamDetailPoint } from 'app/entities/team-detail-point/team-detail-point.model';
import { TeamDetailPointService } from 'app/entities/team-detail-point/service/team-detail-point.service';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/service/event-category.service';

@Component({
  selector: 'jhi-team-category-point-update',
  templateUrl: './team-category-point-update.component.html',
})
export class TeamCategoryPointUpdateComponent implements OnInit {
  isSaving = false;

  teamDetailPointsSharedCollection: ITeamDetailPoint[] = [];
  eventCategoriesSharedCollection: IEventCategory[] = [];

  editForm = this.fb.group({
    id: [],
    points: [],
    position: [],
    teamDetailPoint: [],
    eventCategory: [],
  });

  constructor(
    protected teamCategoryPointService: TeamCategoryPointService,
    protected teamDetailPointService: TeamDetailPointService,
    protected eventCategoryService: EventCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teamCategoryPoint }) => {
      this.updateForm(teamCategoryPoint);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const teamCategoryPoint = this.createFromForm();
    if (teamCategoryPoint.id !== undefined) {
      this.subscribeToSaveResponse(this.teamCategoryPointService.update(teamCategoryPoint));
    } else {
      this.subscribeToSaveResponse(this.teamCategoryPointService.create(teamCategoryPoint));
    }
  }

  trackTeamDetailPointById(index: number, item: ITeamDetailPoint): number {
    return item.id!;
  }

  trackEventCategoryById(index: number, item: IEventCategory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeamCategoryPoint>>): void {
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

  protected updateForm(teamCategoryPoint: ITeamCategoryPoint): void {
    this.editForm.patchValue({
      id: teamCategoryPoint.id,
      points: teamCategoryPoint.points,
      position: teamCategoryPoint.position,
      teamDetailPoint: teamCategoryPoint.teamDetailPoint,
      eventCategory: teamCategoryPoint.eventCategory,
    });

    this.teamDetailPointsSharedCollection = this.teamDetailPointService.addTeamDetailPointToCollectionIfMissing(
      this.teamDetailPointsSharedCollection,
      teamCategoryPoint.teamDetailPoint
    );
    this.eventCategoriesSharedCollection = this.eventCategoryService.addEventCategoryToCollectionIfMissing(
      this.eventCategoriesSharedCollection,
      teamCategoryPoint.eventCategory
    );
  }

  protected loadRelationshipsOptions(): void {
    this.teamDetailPointService
      .query()
      .pipe(map((res: HttpResponse<ITeamDetailPoint[]>) => res.body ?? []))
      .pipe(
        map((teamDetailPoints: ITeamDetailPoint[]) =>
          this.teamDetailPointService.addTeamDetailPointToCollectionIfMissing(teamDetailPoints, this.editForm.get('teamDetailPoint')!.value)
        )
      )
      .subscribe((teamDetailPoints: ITeamDetailPoint[]) => (this.teamDetailPointsSharedCollection = teamDetailPoints));

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

  protected createFromForm(): ITeamCategoryPoint {
    return {
      ...new TeamCategoryPoint(),
      id: this.editForm.get(['id'])!.value,
      points: this.editForm.get(['points'])!.value,
      position: this.editForm.get(['position'])!.value,
      teamDetailPoint: this.editForm.get(['teamDetailPoint'])!.value,
      eventCategory: this.editForm.get(['eventCategory'])!.value,
    };
  }
}
