import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ParseLinks } from 'app/core/util/parse-links.service';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

import { HttpHeaders, HttpResponse } from '@angular/common/http';
import * as dayjs from 'dayjs';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { ITournament } from 'app/entities/tournament/tournament.model';
import { TournamentService } from 'app/entities/tournament/service/tournament.service';
import { ICity } from 'app/entities/city/city.model';
import { CityService } from 'app/entities/city/service/city.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  events: IEvent[];
  eventsOld: IEvent[];
  tournaments: ITournament[];
  cities: ICity[];
  links: { [key: string]: number };

  constructor(
    private accountService: AccountService,
    private router: Router,
    protected parseLinks: ParseLinks,
    private tournamentService: TournamentService,
    private cityService: CityService,
    private eventService: EventService
  ) {
    this.events = [];
    this.eventsOld = [];
    this.tournaments = [];
    this.cities = [];
    this.links = {
      last: 0,
    };
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.loadEvents();
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  live(event: IEvent): boolean {
    if (event.fromDate! <= dayjs().startOf('day') && event.endDate! >= dayjs().startOf('day')) {
      return true;
    } else {
      return false;
    }
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  trackId(index: number, item: IEvent): number {
    return item.id!;
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  loadEvents(): void {
    this.eventService
      .query({
        'status.in': ['CREATED', 'IN_PROGRESS'],
        page: 1 - 1,
        size: 200,
        sort: ['fromDate,asc', 'endDate,asc', 'name,asc'],
      })
      .subscribe((res: HttpResponse<IEvent[]>) => this.paginateEvents(res.body, res.headers));

    this.eventService
      .query({
        'status.in': ['DONE', 'CANCEL'],
        page: 1 - 1,
        size: 200,
        sort: ['fromDate,desc', 'endDate,desc', 'name,asc'],
      })
      .subscribe((res: HttpResponse<IEvent[]>) => this.paginateEventsOld(res.body, res.headers));

    this.tournamentService
      .query({
        size: 2000,
      })
      .subscribe((res: HttpResponse<ITournament[]>) => this.paginateTournament(res.body, res.headers));

    this.cityService
      .query({
        size: 2000,
      })
      .subscribe((res: HttpResponse<ICity[]>) => this.paginateCity(res.body, res.headers));
  }

  protected paginateEvents(data: IEvent[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.events.push(d);
      }
    }
  }

  protected paginateEventsOld(data: IEvent[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.eventsOld.push(d);
      }
    }
  }

  protected paginateTournament(data: IEvent[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.tournaments.push(d);
      }
    }
  }

  protected paginateCity(data: IEvent[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.cities.push(d);
      }
    }
  }
}
