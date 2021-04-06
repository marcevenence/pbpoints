import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPlayerPoint, PlayerPoint } from '../player-point.model';
import { PlayerPointService } from '../service/player-point.service';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';

@Component({
  selector: 'jhi-player-point-update',
  templateUrl: './player-point-update.component.html',
})
export class PlayerPointUpdateComponent implements OnInit {
  isSaving = false;

  tournamentsSharedCollection: ITournament[] = [];
  usersSharedCollection: IUser[] = [];
  categoriesSharedCollection: ICategory[] = [];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.required]],
    tournament: [null, Validators.required],
    user: [null, Validators.required],
    category: [],
  });

  constructor(
    protected playerPointService: PlayerPointService,
    protected tournamentService: TournamentService,
    protected userService: UserService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playerPoint }) => {
      this.updateForm(playerPoint);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playerPoint = this.createFromForm();
    if (playerPoint.id !== undefined) {
      this.subscribeToSaveResponse(this.playerPointService.update(playerPoint));
    } else {
      this.subscribeToSaveResponse(this.playerPointService.create(playerPoint));
    }
  }

  trackTournamentById(index: number, item: ITournament): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackCategoryById(index: number, item: ICategory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayerPoint>>): void {
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

  protected updateForm(playerPoint: IPlayerPoint): void {
    this.editForm.patchValue({
      id: playerPoint.id,
      points: playerPoint.points,
      tournament: playerPoint.tournament,
      user: playerPoint.user,
      category: playerPoint.category,
    });

    this.tournamentsSharedCollection = this.tournamentService.addTournamentToCollectionIfMissing(
      this.tournamentsSharedCollection,
      playerPoint.tournament
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, playerPoint.user);
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(
      this.categoriesSharedCollection,
      playerPoint.category
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tournamentService
      .query()
      .pipe(map((res: HttpResponse<ITournament[]>) => res.body ?? []))
      .pipe(
        map((tournaments: ITournament[]) =>
          this.tournamentService.addTournamentToCollectionIfMissing(tournaments, this.editForm.get('tournament')!.value)
        )
      )
      .subscribe((tournaments: ITournament[]) => (this.tournamentsSharedCollection = tournaments));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));
  }

  protected createFromForm(): IPlayerPoint {
    return {
      ...new PlayerPoint(),
      id: this.editForm.get(['id'])!.value,
      points: this.editForm.get(['points'])!.value,
      tournament: this.editForm.get(['tournament'])!.value,
      user: this.editForm.get(['user'])!.value,
      category: this.editForm.get(['category'])!.value,
    };
  }
}
