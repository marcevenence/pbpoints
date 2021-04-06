import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPlayer, Player } from '../player.model';
import { PlayerService } from '../service/player.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IRoster } from 'app/entities/roster/roster.model';
import { RosterService } from 'app/entities/roster/service/roster.service';

@Component({
  selector: 'jhi-player-update',
  templateUrl: './player-update.component.html',
})
export class PlayerUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  rostersSharedCollection: IRoster[] = [];

  editForm = this.fb.group({
    id: [],
    profile: [],
    user: [],
    roster: [null, Validators.required],
  });

  constructor(
    protected playerService: PlayerService,
    protected userService: UserService,
    protected rosterService: RosterService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ player }) => {
      this.updateForm(player);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const player = this.createFromForm();
    if (player.id !== undefined) {
      this.subscribeToSaveResponse(this.playerService.update(player));
    } else {
      this.subscribeToSaveResponse(this.playerService.create(player));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackRosterById(index: number, item: IRoster): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayer>>): void {
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

  protected updateForm(player: IPlayer): void {
    this.editForm.patchValue({
      id: player.id,
      profile: player.profile,
      user: player.user,
      roster: player.roster,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, player.user);
    this.rostersSharedCollection = this.rosterService.addRosterToCollectionIfMissing(this.rostersSharedCollection, player.roster);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.rosterService
      .query()
      .pipe(map((res: HttpResponse<IRoster[]>) => res.body ?? []))
      .pipe(map((rosters: IRoster[]) => this.rosterService.addRosterToCollectionIfMissing(rosters, this.editForm.get('roster')!.value)))
      .subscribe((rosters: IRoster[]) => (this.rostersSharedCollection = rosters));
  }

  protected createFromForm(): IPlayer {
    return {
      ...new Player(),
      id: this.editForm.get(['id'])!.value,
      profile: this.editForm.get(['profile'])!.value,
      user: this.editForm.get(['user'])!.value,
      roster: this.editForm.get(['roster'])!.value,
    };
  }
}
