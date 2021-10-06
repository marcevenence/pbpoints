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
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-main-roster-update',
  templateUrl: './main-roster-update.component.html',
})
export class MainRosterUpdateComponent implements OnInit {
  isSaving = false;

  teamsSharedCollection: ITeam[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    team: [],
    user: [],
  });

  constructor(
    protected mainRosterService: MainRosterService,
    protected teamService: TeamService,
    protected userService: UserService,
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

  trackUserById(index: number, item: IUser): number {
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
      user: mainRoster.user,
    });

    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, mainRoster.team);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, mainRoster.user);
  }

  protected loadRelationshipsOptions(): void {
    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('team')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IMainRoster {
    return {
      ...new MainRoster(),
      id: this.editForm.get(['id'])!.value,
      team: this.editForm.get(['team'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
