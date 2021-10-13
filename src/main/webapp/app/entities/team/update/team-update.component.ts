import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITeam, Team } from '../team.model';
import { TeamService } from '../service/team.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { IUserExtra } from 'app/entities/user-extra/user-extra.model';
import { UserExtraService } from 'app/entities/user-extra/service/user-extra.service';
import { AccountService } from 'app/core/auth/account.service';
import { IMainRoster, MainRoster } from 'app/entities/main-roster/main-roster.model';
import { MainRosterService } from 'app/entities/main-roster/service/main-roster.service';

@Component({
  selector: 'jhi-team-update',
  templateUrl: './team-update.component.html',
})
export class TeamUpdateComponent implements OnInit {
  isSaving = false;
  currentAccount: any;
  currentOwner: IUser = {};
  userExtrasSharedCollection: IUserExtra[] = [];
  mainRosters: IMainRoster[] = [];
  userExtra: any;
  team: ITeam = {};

  editForm = this.fb.group({
    id: [],
    name: [],
    active: [],
    logo: [],
    logoContentType: [],
    owner: [],
    playerId: [],
    playerCode: [],
    playerProfile: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected teamService: TeamService,
    protected userExtraService: UserExtraService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService,
    protected fb: FormBuilder,
    protected mainRosterService: MainRosterService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ team }) => {
      this.updateForm(team);
      this.uploadPlayers(team);
      this.loadRelationshipsOptions();
    });
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
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

  addNewPlayer(): void {
    if (this.editForm.get('playerId')!.value === null || this.editForm.get('playerCode')!.value === '') {
      alert('Debe ingresar id y codigo');
    } else {
      this.userExtraService
        .queryOneByIdAndCode(+this.editForm.get('playerId')!.value, this.editForm.get('playerCode')!.value)
        .subscribe((res: HttpResponse<IUserExtra>) => {
          this.userExtra = res.body;
          const mainRoster = this.createNewMainRoster();
          mainRoster.userExtra = this.userExtra;
          const targetIdx = this.mainRosters.map(item => item.userExtra?.user?.login).indexOf(mainRoster.userExtra?.user?.login);
          if (targetIdx === -1) {
            this.mainRosters.push(mainRoster);
          } else {
            alert('No es posible agregar');
          }
        });
    }
  }

  save(): void {
    this.isSaving = true;
    const team = this.createFromForm();
    if (team.id !== undefined) {
      if (this.mainRosters.length > 0) {
        this.subscribeToSaveResponse(this.teamService.updateWithRoster(team, this.mainRosters));
      } else {
        this.subscribeToSaveResponse(this.teamService.update(team));
      }
    } else {
      if (this.mainRosters.length > 0) {
        this.subscribeToSaveResponse(this.teamService.createWithRoster(team, this.mainRosters));
      } else {
        this.subscribeToSaveResponse(this.teamService.create(team));
      }
    }
  }

  deletePlayer(mainRoster: IMainRoster): void {
    alert('Eliminado');
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackUserExtraById(index: number, item: IUserExtra): number {
    return item.id!;
  }

  trackMainRosterId(index: number, item: IMainRoster): number {
    return item.id!;
  }

  protected onError(): void {
    // Api for inheritance.
  }

  protected createNewMainRoster(): IMainRoster {
    return {
      ...new MainRoster(),
      userExtra: this.userExtra,
      team: this.team,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeam>>): void {
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

  protected updateForm(team: ITeam): void {
    this.editForm.patchValue({
      id: team.id,
      name: team.name,
      active: team.active,
      logo: team.logo,
      logoContentType: team.logoContentType,
      owner: team.owner,
    });

    this.userExtrasSharedCollection = this.userExtraService.addUserExtraToCollectionIfMissing(this.userExtrasSharedCollection, team.owner);
  }

  protected uploadPlayers(team: ITeam): void {
    if (team.id !== undefined) {
      this.mainRosterService
        .query({ 'teamId.equals': team.id })
        .pipe(map((res: HttpResponse<IMainRoster[]>) => res.body ?? []))
        .subscribe((mainRosters: IMainRoster[]) => (this.mainRosters = mainRosters));
    }
  }

  protected loadRelationshipsOptions(): void {
    this.userExtraService
      .query()
      .pipe(map((res: HttpResponse<IUserExtra[]>) => res.body ?? []))
      .pipe(
        map((userExtras: IUserExtra[]) =>
          this.userExtraService.addUserExtraToCollectionIfMissing(userExtras, this.editForm.get('owner')!.value)
        )
      )
      .subscribe((userExtras: IUserExtra[]) => (this.userExtrasSharedCollection = userExtras));
  }

  protected createFromForm(): ITeam {
    return {
      ...new Team(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      active: this.editForm.get(['active'])!.value,
      logoContentType: this.editForm.get(['logoContentType'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      owner: this.editForm.get(['owner'])!.value || this.currentAccount,
    };
  }
}
