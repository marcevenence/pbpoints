import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICategory, Category } from '../category.model';
import { CategoryService } from '../service/category.service';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';

@Component({
  selector: 'jhi-category-update',
  templateUrl: './category-update.component.html',
})
export class CategoryUpdateComponent implements OnInit {
  isSaving = false;
  tourId: any;
  tournamentsSharedCollection: ITournament[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    gameTimeType: [null, [Validators.required]],
    gameTime: [null, [Validators.required]],
    stopTimeType: [null, [Validators.required]],
    stopTime: [null, [Validators.required]],
    totalPoints: [null, [Validators.required]],
    difPoints: [null, [Validators.required]],
    order: [null, [Validators.required]],
    tournament: [null, Validators.required],
  });

  constructor(
    protected categoryService: CategoryService,
    protected tournamentService: TournamentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.tourId = history.state.tourId ?? 0;
    this.activatedRoute.data.subscribe(({ category }) => {
      this.updateForm(category);
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const category = this.createFromForm();
    if (category.id !== undefined) {
      this.subscribeToSaveResponse(this.categoryService.update(category));
    } else {
      this.subscribeToSaveResponse(this.categoryService.create(category));
    }
  }

  trackTournamentById(index: number, item: ITournament): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategory>>): void {
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

  protected updateForm(category: ICategory): void {
    this.editForm.patchValue({
      id: category.id,
      name: category.name,
      description: category.description,
      gameTimeType: category.gameTimeType,
      gameTime: category.gameTime,
      stopTimeType: category.stopTimeType,
      stopTime: category.stopTime,
      totalPoints: category.totalPoints,
      difPoints: category.difPoints,
      order: category.order,
      tournament: category.tournament,
    });

    this.tournamentsSharedCollection = this.tournamentService.addTournamentToCollectionIfMissing(
      this.tournamentsSharedCollection,
      category.tournament
    );
  }

  protected loadRelationshipsOptions(): void {
    if (this.tourId === 0) {
      this.tournamentService
        .query()
        .pipe(map((res: HttpResponse<ITournament[]>) => res.body ?? []))
        .pipe(
          map((tournaments: ITournament[]) =>
            this.tournamentService.addTournamentToCollectionIfMissing(tournaments, this.editForm.get('tournament')!.value)
          )
        )
        .subscribe((tournaments: ITournament[]) => (this.tournamentsSharedCollection = tournaments));
    } else {
      this.tournamentService
        .query({ 'id.equals': this.tourId })
        .pipe(map((res: HttpResponse<ITournament[]>) => res.body ?? []))
        .pipe(
          map((tournaments: ITournament[]) =>
            this.tournamentService.addTournamentToCollectionIfMissing(tournaments, this.editForm.get('tournament')!.value)
          )
        )
        .subscribe((tournaments: ITournament[]) => (this.tournamentsSharedCollection = tournaments));
    }
  }

  protected createFromForm(): ICategory {
    return {
      ...new Category(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      gameTimeType: this.editForm.get(['gameTimeType'])!.value,
      gameTime: this.editForm.get(['gameTime'])!.value,
      stopTimeType: this.editForm.get(['stopTimeType'])!.value,
      stopTime: this.editForm.get(['stopTime'])!.value,
      totalPoints: this.editForm.get(['totalPoints'])!.value,
      difPoints: this.editForm.get(['difPoints'])!.value,
      order: this.editForm.get(['order'])!.value,
      tournament: this.editForm.get(['tournament'])!.value,
    };
  }
}
