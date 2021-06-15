import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { map } from 'rxjs/operators';
import { DataUtils } from 'app/core/util/data-util.service';
import { AccountService } from 'app/core/auth/account.service';
import { IRoster } from '../roster.model';
import { RosterService } from '../service/roster.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { IUserExtra } from 'app/entities/user-extra/user-extra.model';
import { UserExtraService } from 'app/entities/user-extra/service/user-extra.service';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/service/event-category.service';

@Component({
  selector: 'jhi-roster-search',
  templateUrl: './roster-search.component.html',
})
export class RosterSearchComponent implements OnInit {
  rosters?: IRoster[];
  players?: IPlayer[];
  currentAccount: any;
  userExtras?: IUserExtra[];
  eventCategoriesSharedCollection: IEventCategory[] = [];
  predicate!: string;
  ascending!: boolean;

  findForm = this.fb.group({
    eventCategory: [null, Validators.required],
  });

  constructor(
    protected rosterService: RosterService,
    protected playerService: PlayerService,
    protected userExtraService: UserExtraService,
    protected eventCategoryService: EventCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected accountService: AccountService,
    protected router: Router,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.rosters = [];
    this.players = [];
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.activatedRoute.data.subscribe(() => {
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  onChange(): void {
    this.rosterService
      .query({
        'eventCategoryId.equals': this.findForm.get(['eventCategory'])!.value?.id,
      })
      .subscribe(
        (res: HttpResponse<IRoster[]>) => {
          this.onSuccess(res.body);
        },
        () => {
          this.onError();
        }
      );

    this.playerService.query({}).subscribe(
      (res: HttpResponse<IPlayer[]>) => {
        this.onPlayerSuccess(res.body);
      },
      () => {
        this.onError();
      }
    );

    this.userExtraService.query({}).subscribe(
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

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IRoster[] | null): void {
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
      .query()
      .pipe(map((res: HttpResponse<IEventCategory[]>) => res.body ?? []))
      .pipe(
        map((eventCategories: IEventCategory[]) =>
          this.eventCategoryService.addEventCategoryToCollectionIfMissing(eventCategories, this.findForm.get('eventCategory')!.value)
        )
      )
      .subscribe((eventCategories: IEventCategory[]) => (this.eventCategoriesSharedCollection = eventCategories));
  }
}
