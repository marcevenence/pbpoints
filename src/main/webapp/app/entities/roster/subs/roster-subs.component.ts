import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { DataUtils } from 'app/core/util/data-util.service';
import { AccountService } from 'app/core/auth/account.service';
import { IRoster, Roster } from 'app/entities/roster/roster.model';
import { IRosterSubs } from 'app/entities/roster/roster-subs.model';
import { IRosterSubsPl } from 'app/entities/roster/roster-subs-pl.model';
import { RosterService } from 'app/entities/roster/service/roster.service';
import { IPlayer, Player } from 'app/entities/player/player.model';
import { IPlayerPoint } from 'app/entities/player-point/player-point.model';
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
  validPlayer?: IPlayerPoint;
  checked?: IPlayer;
  rosterSubs?: IRosterSubs;
  rosterSubsPl?: IRosterSubsPl;
  returnPlayer?: IPlayer;
  rosterParam?: any;

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
    this.rosterSubs = {};
    this.rosterSubsPl = {};
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
    if (this.rosterParam.rId !== 0 && this.rosterParam.rId !== undefined) {
      this.subscribeToSaveResponseRoster(this.rosterService.updateWithPlayers(this.playerNews!, this.rosterParam.rId));
    } else {
      this.subscribeToSaveResponseRoster(
        this.rosterService.createWithPlayers(this.playerNews!, this.findForm.get('team')!.value.id, this.eventCategory!.id!)
      );
    }
    window.history.back();
  }

  addNewPlayer(): void {
    if (this.findForm.get('id')!.value === null || this.findForm.get('code')!.value === '') {
      alert('Debe ingresar id y codigo');
    } else {
      if (
        this.findForm.get('id')!.value !== null &&
        this.findForm.get('id')!.value !== undefined &&
        this.eventCategory?.event?.tournament?.id !== undefined &&
        this.eventCategory.category?.id !== undefined
      ) {
        if (this.roster?.id === undefined) {
          this.roster = this.createNewRoster();
        }
        this.rosterSubs!.id = this.findForm.get('id')!.value;
        this.rosterSubs!.code = this.findForm.get('code')!.value;
        this.rosterSubs!.profile = this.findForm.get('profile')!.value;
        this.rosterSubs!.eventCategoryId = this.eventCategory.id;
        this.rosterSubs!.roster = this.roster;
        this.rosterSubs!.players = this.playerNews;

        this.rosterService.validatePlayer(this.rosterSubs!).subscribe((res: HttpResponse<IPlayer>) => {
          this.Nplayer = res.body!;
          const targetIdx = this.playerNews!.map(item => item.user?.login).indexOf(this.Nplayer.user?.login);
          if (targetIdx === -1) {
            this.playerNews!.push(this.Nplayer);
          } else {
            alert('No es posible agregar');
          }
        });
      }
    }
  }

  addPlayer(player: IPlayer): void {
    let player1;
    this.rosterSubsPl!.players = this.playerNews;
    if (
      (player.roster?.id === undefined || player.roster.id === 0) &&
      this.rosterParam.rId !== undefined &&
      this.rosterParam.rId !== null
    ) {
      player.roster = this.rosterParam;
    }
    this.rosterSubsPl!.player = player;
    this.rosterService.validatePlayer2(this.rosterSubsPl!).subscribe((res: HttpResponse<IPlayer>) => {
      player1 = res.body!;
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
          player1.profile = ProfileUser.PLAYER;
          this.playerNews![targetIdx] = player1;
        }
      } else {
        player1.profile = ProfileUser.PLAYER;
        this.playerNews!.push(player1);
      }
    });
  }

  addUserPlayer(user1: IUser): void {
    this.userExtraService.find(user1.id!).subscribe((res: HttpResponse<IUserExtra>) => {
      this.newPlayer = res.body;
      if (this.roster?.id === undefined) {
        this.roster = this.createNewRoster();
      }
      this.rosterSubs!.id = this.newPlayer.id;
      this.rosterSubs!.code = this.newPlayer.code;
      this.rosterSubs!.profile = ProfileUser.PLAYER.toString();
      this.rosterSubs!.eventCategoryId = this.eventCategory?.id;
      this.rosterSubs!.roster = this.roster;
      this.rosterSubs!.players = this.playerNews;

      this.rosterService.validatePlayer(this.rosterSubs!).subscribe((res2: HttpResponse<IPlayer>) => {
        this.Nplayer = res2.body!;
        const targetIdx = this.playerNews!.map(item => item.user?.login).indexOf(this.Nplayer.user?.login);
        if (targetIdx === -1) {
          this.playerNews!.push(this.Nplayer);
        } else {
          alert('No es posible agregar');
        }
      });
    });
  }

  addUserStaff(user1: IUser): void {
    this.userExtraService.find(user1.id!).subscribe((res: HttpResponse<IUserExtra>) => {
      this.newPlayer = res.body;
      if (this.roster?.id === undefined) {
        this.roster = this.createNewRoster();
      }
      this.rosterSubs!.id = this.newPlayer.id;
      this.rosterSubs!.code = this.newPlayer.code;
      this.rosterSubs!.profile = ProfileUser.STAFF.toString();
      this.rosterSubs!.eventCategoryId = this.eventCategory?.id;
      this.rosterSubs!.roster = this.roster;
      this.rosterSubs!.players = this.playerNews;

      this.rosterService.validatePlayer(this.rosterSubs!).subscribe((res2: HttpResponse<IPlayer>) => {
        this.Nplayer = res2.body!;
        const targetIdx = this.playerNews!.map(item => item.user?.login).indexOf(this.Nplayer.user?.login);
        if (targetIdx === -1) {
          this.playerNews!.push(this.Nplayer);
        } else {
          alert('No es posible agregar');
        }
      });
    });
  }

  addStaff(player1: IPlayer): void {
    if (
      (player1.roster?.id === undefined || player1.roster.id === 0) &&
      this.rosterParam.rId !== undefined &&
      this.rosterParam.rId !== null
    ) {
      player1.roster = this.rosterParam;
      this.roster = this.rosterParam;
    }
    let targetIdx = this.playerNews!.map(item => item.user?.login).indexOf(player1.user?.login);
    this.playerNews!.splice(targetIdx, 1);
    if (player1.roster?.id === 0) {
      targetIdx = this.playerNews!.map(item => item.user?.login).indexOf(player1.user?.login);
      if (targetIdx === -1) {
        player1.profile = ProfileUser.STAFF;
        this.playerNews!.push(player1);
      } else {
        player1.roster = this.roster;
        player1.profile = ProfileUser.STAFF;
        this.playerNews![targetIdx] = player1;
      }
    } else {
      player1.profile = ProfileUser.STAFF;
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

  protected validateCategory(id: number, tId: number, cat: number, profile: string): boolean {
    if (profile !== 'STAFF') {
      this.playerService.validateCategory(id, tId, cat).subscribe((res: HttpResponse<IPlayerPoint>) => {
        this.validPlayer = res.body ?? {};
        return this.validPlayer.id !== undefined;
      });
    }
    return true;
  }

  protected checkInRosters(id: number, profile: string): boolean {
    if (profile !== 'STAFF') {
      this.rosterService.checkinRosters(id, profile).subscribe((res: HttpResponse<IPlayer>) => {
        this.checked = res.body ?? {};
        if (this.checked.id !== undefined) {
          return true;
        } else {
          return false;
        }
      });
    }
    return true;
  }

  protected loadRelationshipsOptions(): void {
    if (this.currentAccount.authorities.includes('ROLE_ADMIN')) {
      this.teamService
        .query()
        .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
        .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.findForm.get('team')!.value)))
        .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
    } else {
      if (history.state.evCatId !== 0 || history.state.roster.evCatId !== 0) {
        this.teamService
          .findNotAll(+this.currentAccount.id, history.state.evCatId ?? 0)
          .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
          .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.findForm.get('team')!.value)))
          .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
      } else {
        this.teamService
          .query()
          .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
          .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.findForm.get('team')!.value)))
          .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
      }
    }
  }

  protected handleNavigation(): void {
    this.evCatId = history.state.evCatId ?? 0;
    this.rosterParam = history.state.roster;
    if (this.evCatId === 0 && this.rosterParam.evCatId !== 0) {
      this.evCatId = this.rosterParam.evCatId ?? 0;
      if (this.rosterParam.te !== undefined) {
        this.findForm.patchValue({ team: this.rosterParam.te });
        this.onChange();
        this.playerService.query({ 'rosterId.equals': this.rosterParam.rId }).subscribe((res: HttpResponse<IPlayer[]>) => {
          this.playerNews = res.body ?? [];
        });
      }
    }
    if (this.evCatId !== 0) {
      this.eventCategoryService.find(this.evCatId).subscribe((res: HttpResponse<IEventCategory>) => (this.eventCategory = res.body ?? {}));
    }
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
