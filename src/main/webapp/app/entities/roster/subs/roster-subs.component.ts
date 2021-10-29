import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { DataUtils } from 'app/core/util/data-util.service';
import { AccountService } from 'app/core/auth/account.service';
import { IRoster, Roster } from 'app/entities/roster/roster.model';
import { RosterService } from 'app/entities/roster/service/roster.service';
import { IPlayer, Player } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { IUserExtra } from 'app/entities/user-extra/user-extra.model';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { UserExtraService } from 'app/entities/user-extra/service/user-extra.service';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/service/event-category.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IMainRoster } from 'app/entities/main-roster/main-roster.model';
import { MainRosterService } from 'app/entities/main-roster/service/main-roster.service';
import { ProfileUser } from 'app/entities/enumerations/profile-user.model';

@Component({
  selector: 'jhi-roster-subs',
  templateUrl: './roster-subs.component.html',
})
export class RosterSubsComponent implements OnInit {
  players?: IPlayer[];
  playerNews?: IPlayer[];
  mainRosters?: IMainRoster[];
  currentAccount: any;
  userExtras?: IUserExtra[];
  eventCategory?: IEventCategory;
  teamsSharedCollection: ITeam[] = [];
  predicate!: string;
  ascending!: boolean;
  evCatId!: number;
  newPlayer?: any;
  roster?: IRoster;
  Nplayer?: IPlayer;
  user?: IUser;

  findForm = this.fb.group({
    eventCategory: [null, Validators.required],
    id: [],
    code: [],
    profile: [],
    team: [null, Validators.required],
  });

  constructor(
    protected playerService: PlayerService,
    protected mainRosterService: MainRosterService,
    protected rosterService: RosterService,
    protected userService: UserService,
    protected userExtraService: UserExtraService,
    protected eventCategoryService: EventCategoryService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected accountService: AccountService,
    protected router: Router,
    protected fb: FormBuilder
  ) {
    this.evCatId = 0;
    this.roster = {};
    this.newPlayer = {};
    this.Nplayer = {};
  }

  ngOnInit(): void {
    this.players = [];
    this.playerNews = [];
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.handleNavigation();
    this.activatedRoute.data.subscribe(() => {
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  onChange(): void {
    this.mainRosterService.query({ 'teamId.equals': this.findForm.get('team')!.value?.id }).subscribe(
      (res: HttpResponse<IMainRoster[]>) => {
        this.onMainRosterSuccess(res.body);
      },
      () => {
        this.onError();
      }
    );

    this.playerService.query({ 'teamId.equals': this.findForm.get('team')!.value?.id }).subscribe(
      (res: HttpResponse<IPlayer[]>) => {
        this.onPlayerSuccess(res.body);
      },
      () => {
        this.onError();
      }
    );

    this.userExtraService.query().subscribe(
      (res: HttpResponse<IUserExtra[]>) => {
        this.onUserExtraSuccess(res.body);
      },
      () => {
        this.onError();
      }
    );
  }

  loadPage(): void {
    // no hace nada pero necesario para mostrar datos
  }
  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  trackMainRosterId(index: number, item: IMainRoster): number {
    return item.id!;
  }

  trackRosterId(index: number, item: IRoster): number {
    return item.id!;
  }

  trackPlayerId(index: number, item: IPlayer): number {
    return item.id!;
  }

  trackUserExtraId(index: number, item: IUserExtra): number {
    return item.id!;
  }

  trackEventCategoryById(index: number, item: IEventCategory): number {
    return item.id!;
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  saveAll(): void {
    this.subscribeToSaveResponseRoster(
      this.rosterService.createWithPlayers(this.playerNews!, this.findForm.get('team')!.value, this.eventCategory!)
    );
    window.history.back();
  }

  addNewPlayer(): void {
    if (this.findForm.get('id')!.value === null || this.findForm.get('code')!.value === '') {
      alert('Debe ingresar id y codigo');
    } else {
      this.userExtraService
        .queryOneByIdAndCode(this.findForm.get('id')!.value, this.findForm.get('code')!.value)
        .subscribe((res: HttpResponse<IUserExtra>) => {
          this.newPlayer = res.body;
          this.checkInRoster();
          if (
            this.findForm.get('profile')!.value.toString() !== '' &&
            this.newPlayer?.code !== undefined &&
            this.evCatId.toString() !== ''
          ) {
            if (this.roster?.id === undefined) {
              this.roster = this.createNewRoster();
            }
            const nPlayer = this.createNewPlayer();
            const targetIdx = this.playerNews!.map(item => item.user?.login).indexOf(nPlayer.user?.login);
            if (targetIdx === -1) {
              this.playerNews!.push(nPlayer);
            } else {
              alert('No es posible agregar');
            }
          }
        });
    }
  }

  addPlayer(player1: IPlayer): void {
    let targetIdx = this.playerNews!.map(item => item.user?.login).indexOf(player1.user?.login);
    this.playerNews!.splice(targetIdx, 1);
    if (player1.roster?.id === 0) {
      targetIdx = this.playerNews!.map(item => item.user?.login).indexOf(player1.user?.login);
      if (targetIdx === -1) {
        player1.profile = ProfileUser.PLAYER;
        this.playerNews!.push(player1);
        const targetIdx2 = this.players!.map(item => item.user?.login).indexOf(player1.user?.login);
        if (targetIdx2 !== -1) {
          this.players!.splice(targetIdx2, 1);
        }
      } else {
        const pl = player1.user;
        const cat = player1.category;
        player1 = {};
        player1.user = pl;
        player1.category = cat;
        player1.profile = ProfileUser.PLAYER;
        this.playerNews![targetIdx] = player1;
      }
    } else {
      const pl = player1.user;
      const cat = player1.category;
      player1 = {};
      player1.user = pl;
      player1.category = cat;
      player1.profile = ProfileUser.PLAYER;
      player1.roster = {};
      this.playerNews!.push(player1);
    }
  }

  addUserPlayer(user1: IUser): void {
    this.userExtraService.find(user1.id!).subscribe((res: HttpResponse<IUserExtra>) => {
      this.newPlayer = res.body;
      if (!this.validateCategory(user1.id!, 1)) {
        alert('El jugador pertenece a una categoria mas alta');
      } else {
        if (this.evCatId.toString() !== '') {
          if (this.roster?.id === undefined) {
            this.roster = this.createNewRoster();
          }
          const nPlayer = this.createNewPlayer();
          nPlayer.profile = ProfileUser.PLAYER;
          const targetIdx = this.playerNews!.map(item => item.user?.login).indexOf(nPlayer.user?.login);
          if (targetIdx === -1) {
            this.playerNews!.push(nPlayer);
          } else {
            alert('No es posible agregar');
          }
        }
      }
    });
  }

  addUserStaff(user1: IUser): void {
    this.userExtraService.find(user1.id!).subscribe((res: HttpResponse<IUserExtra>) => {
      this.newPlayer = res.body;
      if (this.evCatId.toString() !== '') {
        if (this.roster?.id === undefined) {
          this.roster = this.createNewRoster();
        }
        const nPlayer = this.createNewPlayer();
        nPlayer.profile = ProfileUser.STAFF;
        const targetIdx = this.playerNews!.map(item => item.user?.login).indexOf(nPlayer.user?.login);
        if (targetIdx === -1) {
          this.playerNews!.push(nPlayer);
        } else {
          alert('No es posible agregar');
        }
      }
    });
  }

  addStaff(player1: IPlayer): void {
    let targetIdx = this.playerNews!.map(item => item.user?.login).indexOf(player1.user?.login);
    this.playerNews!.splice(targetIdx, 1);
    if (player1.roster?.id === 0) {
      targetIdx = this.playerNews!.map(item => item.user?.login).indexOf(player1.user?.login);
      if (targetIdx === -1) {
        player1.profile = ProfileUser.STAFF;
        this.playerNews!.push(player1);
      } else {
        const pl = player1.user;
        const cat = player1.category;
        player1 = {};
        player1.user = pl;
        player1.category = cat;
        player1.profile = ProfileUser.STAFF;
        this.playerNews![targetIdx] = player1;
      }
    } else {
      const pl = player1.user;
      const cat = player1.category;
      player1 = {};
      player1.user = pl;
      player1.category = cat;
      player1.profile = ProfileUser.STAFF;
      player1.roster = {};
      this.playerNews!.push(player1);
    }
  }

  delPlayer(player: IPlayer): void {
    this.players!.push(player);
    const targetIdx = this.playerNews!.map(item => item.user?.id).indexOf(player.user?.id);
    this.playerNews!.splice(targetIdx, 1);
  }

  enableSaveAll(): boolean {
    if (this.playerNews!.length !== 0) {
      return true;
    } else {
      return false;
    }
  }

  protected validateCategory(id: number, cat: number): boolean {
    return false;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onMainRosterSuccess(data: IMainRoster[] | null): void {
    this.mainRosters = data ?? [];
  }

  protected onPlayerSuccess(data: IPlayer[] | null): void {
    this.players = data ?? [];
  }

  protected onUserExtraSuccess(data: IUserExtra[] | null): void {
    this.userExtras = data ?? [];
  }

  protected onNewPlayerSuccess(data: any): void {
    this.newPlayer = data ?? [];
  }

  protected onError(): void {
    // Api for inheritance.
  }

  protected checkInRoster(): void {
    alert('Pendiente Chequeando en Rosters RosterResource.CheckInRoster');
  }

  protected loadRelationshipsOptions(): void {
    if (this.currentAccount.authorities.includes('ROLE_ADMIN')) {
      this.teamService
        .query({ size: 1000 })
        .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
        .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.findForm.get('team')!.value)))
        .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
    } else {
      if (history.state.evCatId !== 0) {
        this.teamService
          .findNotAll(+this.currentAccount.id, history.state.evCatId ?? 0)
          .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
          .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.findForm.get('team')!.value)))
          .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
      } else {
        this.teamService
          .query({ size: 1000 })
          .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
          .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.findForm.get('team')!.value)))
          .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
      }
    }
  }

  protected handleNavigation(): void {
    this.evCatId = history.state.evCatId ?? 0;
    if (this.evCatId !== 0) {
      this.eventCategoryService.find(this.evCatId).subscribe((res: HttpResponse<IEventCategory>) => this.paginateEventCategory(res.body));
    }
  }

  protected paginateEventCategory(data: any): void {
    this.eventCategory = data;
  }

  protected createNewRoster(): IRoster {
    return {
      ...new Roster(),
      active: true,
      team: this.findForm.get(['team'])!.value,
      eventCategory: this.eventCategory,
    };
  }

  protected createNewPlayer(): IPlayer {
    return {
      ...new Player(),
      profile: this.findForm.get(['profile'])!.value,
      user: this.newPlayer?.user,
      roster: this.roster,
      category: this.eventCategory?.category,
    };
  }

  protected subscribeToSaveResponseRoster(result: Observable<HttpResponse<IRoster>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected subscribeToSaveResponsePlayer(result: Observable<HttpResponse<IPlayer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    // Api for inheritance.
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    // Api for inheritance.
  }

  protected paginateUser(data: any): void {
    this.user = data;
  }
}
