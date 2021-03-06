import { Component, OnInit } from '@angular/core';
import * as dayjs from 'dayjs';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { DataUtils } from 'app/core/util/data-util.service';
import { IUserExtra } from 'app/entities/user-extra/user-extra.model';
import { UserExtraService } from 'app/entities/user-extra/service/user-extra.service';
import { UserService } from 'app/entities/user/user.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { IPlayerPoint } from 'app/entities/player-point/player-point.model';
import { PlayerPointService } from 'app/entities/player-point/service/player-point.service';
import { ISuspension } from 'app/entities/suspension/suspension.model';
import { SuspensionService } from 'app/entities/suspension/service/suspension.service';
import { IMainRoster } from 'app/entities/main-roster/main-roster.model';
import { MainRosterService } from 'app/entities/main-roster/service/main-roster.service';

@Component({
  selector: 'jhi-player-search',
  templateUrl: './player-search.component.html',
})
export class PlayerSearchComponent implements OnInit {
  userExtras?: IUserExtra[];
  mainRosters: IMainRoster[];
  teams?: ITeam[];
  players?: IPlayer[];
  playerPoints?: IPlayerPoint[];
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  playerSusp: ISuspension;

  searchForm = this.fb.group({
    id: [],
  });

  constructor(
    protected userExtraService: UserExtraService,
    protected userService: UserService,
    protected teamService: TeamService,
    protected playerService: PlayerService,
    protected playerPointService: PlayerPointService,
    protected suspensionService: SuspensionService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected fb: FormBuilder,
    protected parseLinks: ParseLinks,
    protected mainRosterService: MainRosterService
  ) {
    this.userExtras = [];
    this.teams = [];
    this.players = [];
    this.playerPoints = [];
    this.playerSusp = {};
    this.mainRosters = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  ngOnInit(): void {
    this.userExtras = [];
    this.teams = [];
    this.players = [];
    this.playerPoints = [];
  }

  find(): void {
    this.userExtras = [];
    this.teams = [];
    this.players = [];
    this.playerPoints = [];
    if (this.searchForm.get(['id'])!.value) {
      this.userExtraService
        .query({
          'userId.equals': this.searchForm.get(['id'])!.value,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe((res: HttpResponse<IUserExtra[]>) => {
          this.userExtras = res.body ?? [];
        });

      this.teamService
        .query({
          'ownerId.equals': this.searchForm.get(['id'])!.value,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe((res: HttpResponse<ITeam[]>) => {
          this.teams = res.body ?? [];
        });

      this.mainRosterService
        .query({ 'userExtraId.equals': this.searchForm.get(['id'])!.value })
        .subscribe((res: HttpResponse<IMainRoster[]>) => {
          this.mainRosters = res.body ?? [];
        });

      this.playerService
        .query({
          'userId.equals': this.searchForm.get(['id'])!.value,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe((res: HttpResponse<IPlayer[]>) => {
          this.players = res.body ?? [];
        });

      this.playerPointService
        .query({
          'userId.equals': this.searchForm.get(['id'])!.value,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe((res: HttpResponse<IPlayerPoint[]>) => {
          this.playerPoints = res.body ?? [];
        });
    }
  }

  Suspend(user: any): void {
    this.playerSusp = {};
    this.playerSusp.user = user;
    this.playerSusp.startDate = dayjs().startOf('day');
    this.subscribeToSaveResponse(this.suspensionService.create(this.playerSusp));
  }

  suspendido(): boolean {
    return true;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  trackUserExtraId(index: number, item: IUserExtra): number {
    return item.id!;
  }

  trackTeamId(index: number, item: ITeam): number {
    return item.id!;
  }

  trackMrId(index: number, item: IMainRoster): number {
    return item.id!;
  }

  trackPlayerId(index: number, item: IPlayer): number {
    return item.id!;
  }

  trackPlayerPointId(index: number, item: IPlayerPoint): number {
    return item.id!;
  }

  reset(): void {
    this.page = 0;
    this.find();
  }

  loadPage(page: number): void {
    this.page = page;
    this.find();
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISuspension>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }
}
