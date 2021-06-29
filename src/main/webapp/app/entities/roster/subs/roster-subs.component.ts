import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { map } from 'rxjs/operators';
import { DataUtils } from 'app/core/util/data-util.service';
import { AccountService } from 'app/core/auth/account.service';
import { IRoster } from 'app/entities/roster/roster.model';
import { RosterService } from 'app/entities/roster/service/roster.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { IUserExtra } from 'app/entities/user-extra/user-extra.model';
import { UserExtraService } from 'app/entities/user-extra/service/user-extra.service';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/service/event-category.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

@Component({
  selector: 'jhi-roster-subs',
  templateUrl: './roster-subs.component.html',
})
export class RosterSubsComponent implements OnInit {
  players?: IPlayer[];
  playerNews?: IPlayer[];
  rosters?: IRoster[];
  currentAccount: any;
  userExtras?: IUserExtra[];
  eventCategoriesSharedCollection: IEventCategory[] = [];
  teamsSharedCollection: ITeam[] = [];
  predicate!: string;
  ascending!: boolean;
  evCatId?: number;

  findForm = this.fb.group({
    eventCategory: [null, Validators.required],
    team: [null, Validators.required],
  });

  constructor(
    protected playerService: PlayerService,
    protected rosterService: RosterService,
    protected userExtraService: UserExtraService,
    protected eventCategoryService: EventCategoryService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected accountService: AccountService,
    protected router: Router,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.players = [];
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.activatedRoute.data.subscribe(() => {
      this.loadRelationshipsOptions();
    });
    this.activatedRoute.queryParams.subscribe(params => {
      this.evCatId = +params.get('evCatId')!;
    });
  }

  previousState(): void {
    window.history.back();
  }

  onChange(): void {
    this.rosterService.query({ 'teamId.equals': this.findForm.get('team')!.value?.id }).subscribe(
      (res: HttpResponse<IRoster[]>) => {
        this.onRosterSuccess(res.body);
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

  addPlayer(player: IPlayer): void {
    alert('Agregado: ' + player.user!.lastName! + ' como jugador');
  }

  addStaff(player: IPlayer): void {
    alert('Agregado: ' + player.user!.lastName! + ' como staff');
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onRosterSuccess(data: IRoster[] | null): void {
    this.rosters = data ?? [];
  }

  protected onPlayerSuccess(data: IPlayer[] | null): void {
    this.players = data ?? [];
  }

  protected onUserExtraSuccess(data: IUserExtra[] | null): void {
    this.userExtras = data ?? [];
  }

  protected onError(): void {
    // Api for inheritance.
  }

  protected loadRelationshipsOptions(): void {
    this.eventCategoryService
      .query({ 'id.equals': 11 })
      .pipe(map((res: HttpResponse<IEventCategory[]>) => res.body ?? []))
      .pipe(
        map((eventCategories: IEventCategory[]) =>
          this.eventCategoryService.addEventCategoryToCollectionIfMissing(eventCategories, this.findForm.get('eventCategory')!.value)
        )
      )
      .subscribe((eventCategories: IEventCategory[]) => (this.eventCategoriesSharedCollection = eventCategories));

    this.teamService
      .query({ 'ownerId.equals': this.currentAccount.id })
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.findForm.get('team')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
  }
}
