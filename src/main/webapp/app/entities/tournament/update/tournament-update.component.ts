import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITournament, Tournament } from '../tournament.model';
import { TournamentService } from '../service/tournament.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-tournament-update',
  templateUrl: './tournament-update.component.html',
})
export class TournamentUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    closeInscrDays: [],
    status: [],
    categorize: [],
    logo: [],
    logoContentType: [],
    cantPlayersNextCategory: [],
    qtyTeamGroups: [],
    startSeason: [null, [Validators.required]],
    endSeason: [null, [Validators.required]],
    owner: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected tournamentService: TournamentService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tournament }) => {
      this.updateForm(tournament);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('pbpointsApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tournament = this.createFromForm();
    if (tournament.id !== undefined) {
      this.subscribeToSaveResponse(this.tournamentService.update(tournament));
    } else {
      this.subscribeToSaveResponse(this.tournamentService.create(tournament));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITournament>>): void {
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

  protected updateForm(tournament: ITournament): void {
    this.editForm.patchValue({
      id: tournament.id,
      name: tournament.name,
      closeInscrDays: tournament.closeInscrDays,
      status: tournament.status,
      categorize: tournament.categorize,
      logo: tournament.logo,
      logoContentType: tournament.logoContentType,
      cantPlayersNextCategory: tournament.cantPlayersNextCategory,
      qtyTeamGroups: tournament.qtyTeamGroups,
      startSeason: tournament.startSeason,
      endSeason: tournament.endSeason,
      owner: tournament.owner,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, tournament.owner);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('owner')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ITournament {
    return {
      ...new Tournament(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      closeInscrDays: this.editForm.get(['closeInscrDays'])!.value,
      status: this.editForm.get(['status'])!.value,
      categorize: this.editForm.get(['categorize'])!.value,
      logoContentType: this.editForm.get(['logoContentType'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      cantPlayersNextCategory: this.editForm.get(['cantPlayersNextCategory'])!.value,
      qtyTeamGroups: this.editForm.get(['qtyTeamGroups'])!.value,
      startSeason: this.editForm.get(['startSeason'])!.value,
      endSeason: this.editForm.get(['endSeason'])!.value,
      owner: this.editForm.get(['owner'])!.value,
    };
  }
}
