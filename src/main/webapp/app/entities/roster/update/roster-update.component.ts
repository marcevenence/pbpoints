import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRoster, Roster } from '../roster.model';
import { RosterService } from '../service/roster.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/service/event-category.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-roster-update',
  templateUrl: './roster-update.component.html',
})
export class RosterUpdateComponent implements OnInit {
  isSaving = false;
  currentAccount: any;

  edit?: boolean;
  teId?: number;
  evCatId?: number;
  teamsSharedCollection: ITeam[] = [];
  eventCategoriesSharedCollection: IEventCategory[] = [];

  editForm = this.fb.group({
    id: [],
    active: [],
    team: [null, Validators.required],
    eventCategory: [null, Validators.required],
  });

  constructor(
    protected rosterService: RosterService,
    protected teamService: TeamService,
    protected eventCategoryService: EventCategoryService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.teId = history.state.roster.teId ?? 0;
    this.evCatId = history.state.roster.evCatId ?? 0;
    if (history.state.roster.teId !== 0) {
      this.edit = false;
    } else {
      this.edit = true;
    }

    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.activatedRoute.data.subscribe(({ roster }) => {
      this.updateForm(roster);
      this.loadRelationshipsOptions();
    });
  }

  getEdit(): boolean {
    return this.edit ?? false;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const roster = this.createFromForm();
    if (roster.id !== undefined) {
      this.subscribeToSaveResponse(this.rosterService.update(roster));
    } else {
      this.subscribeToSaveResponse(this.rosterService.create(roster));
    }
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  trackEventCategoryById(index: number, item: IEventCategory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoster>>): void {
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

  protected updateForm(roster: IRoster): void {
    this.editForm.patchValue({
      id: roster.id,
      active: roster.active,
      team: roster.team,
      eventCategory: roster.eventCategory,
    });

    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, roster.team);
    this.eventCategoriesSharedCollection = this.eventCategoryService.addEventCategoryToCollectionIfMissing(
      this.eventCategoriesSharedCollection,
      roster.eventCategory
    );
  }

  protected loadRelationshipsOptions(): void {
    if (this.currentAccount.authorities.includes('ROLE_ADMIN')) {
      this.teamService
        .query({ 'teamId.equals': this.teId })
        .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
        .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('team')!.value)))
        .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
    } else {
      this.teamService
        .query({ 'teamId.equals': this.teId, 'ownerId.equals': this.currentAccount.id })
        .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
        .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('team')!.value)))
        .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
    }
    if (this.evCatId !== 0) {
      this.eventCategoryService
        .query({ 'id.equals': this.evCatId })
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

  protected createFromForm(): IRoster {
    return {
      ...new Roster(),
      id: this.editForm.get(['id'])!.value,
      active: this.editForm.get(['active'])!.value,
      team: this.editForm.get(['team'])!.value,
      eventCategory: this.editForm.get(['eventCategory'])!.value,
    };
  }
}
