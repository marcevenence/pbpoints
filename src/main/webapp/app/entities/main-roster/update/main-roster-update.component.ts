import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMainRoster, MainRoster } from '../main-roster.model';
import { MainRosterService } from '../service/main-roster.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IUserExtra } from 'app/entities/user-extra/user-extra.model';
import { UserExtraService } from 'app/entities/user-extra/service/user-extra.service';

@Component({
  selector: 'jhi-main-roster-update',
  templateUrl: './main-roster-update.component.html',
})
export class MainRosterUpdateComponent implements OnInit {
  isSaving = false;

  teamsSharedCollection: ITeam[] = [];
  userExtrasSharedCollection: IUserExtra[] = [];

  editForm = this.fb.group({
    id: [],
    team: [],
    userExtra: [],
  });

  constructor(
    protected mainRosterService: MainRosterService,
    protected teamService: TeamService,
    protected userExtraService: UserExtraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mainRoster }) => {
      this.updateForm(mainRoster);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mainRoster = this.createFromForm();
    if (mainRoster.id !== undefined) {
      this.subscribeToSaveResponse(this.mainRosterService.update(mainRoster));
    } else {
      this.subscribeToSaveResponse(this.mainRosterService.create(mainRoster));
    }
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  trackUserExtraById(index: number, item: IUserExtra): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMainRoster>>): void {
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

  protected updateForm(mainRoster: IMainRoster): void {
    this.editForm.patchValue({
      id: mainRoster.id,
      team: mainRoster.team,
      userExtra: mainRoster.userExtra,
    });

    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, mainRoster.team);
    this.userExtrasSharedCollection = this.userExtraService.addUserExtraToCollectionIfMissing(
      this.userExtrasSharedCollection,
      mainRoster.userExtra
    );
  }

  protected loadRelationshipsOptions(): void {
    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('team')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));

    this.userExtraService
      .query()
      .pipe(map((res: HttpResponse<IUserExtra[]>) => res.body ?? []))
      .pipe(
        map((userExtras: IUserExtra[]) =>
          this.userExtraService.addUserExtraToCollectionIfMissing(userExtras, this.editForm.get('userExtra')!.value)
        )
      )
      .subscribe((userExtras: IUserExtra[]) => (this.userExtrasSharedCollection = userExtras));
  }

  protected createFromForm(): IMainRoster {
    return {
      ...new MainRoster(),
      id: this.editForm.get(['id'])!.value,
      team: this.editForm.get(['team'])!.value,
      userExtra: this.editForm.get(['userExtra'])!.value,
    };
  }
}
