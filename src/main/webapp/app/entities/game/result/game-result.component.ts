import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGame } from '../game.model';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/service/event-category.service';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { GameService } from '../service/game.service';
import { GameDeleteDialogComponent } from '../delete/game-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-game-result',
  templateUrl: './game-result.component.html',
})
export class GameResultComponent implements OnInit {
  games: IGame[];
  eventCategories: IEventCategory[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  evId = 0;

  constructor(
    protected eventCategoryService: EventCategoryService,
    protected gameService: GameService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.games = [];
    this.eventCategories = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.eventCategoryService
      .query({
        'eventId.equals': this.evId,
        page: 0,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IEventCategory[]>) => {
        this.eventCategories = res.body!;
      });
    this.gameService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IGame[]>) => {
        this.games = res.body!;
      });
  }

  loadGames(evCatId: number): IGame[] {
    this.gameService
      .query({
        'eventCategoryId.equals': evCatId,
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IGame[]>) => {
        this.games = res.body!;
      });
    return this.games;
  }

  reset(): void {
    this.page = 0;
    this.games = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.evId = history.state.evId ?? 0;
    this.loadAll();
  }

  trackId(index: number, item: IGame): number {
    return item.id!;
  }

  trackEventCategoryId(index: number, item: IEventCategory): number {
    return item.id!;
  }

  delete(game: IGame): void {
    const modalRef = this.modalService.open(GameDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.game = game;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  convertToTimeFormat(time: any): string {
    const val = String((time - (time % 60)) / 60).padStart(2, '0') + ':' + String(time % 60).padStart(2, '0');
    return val;
  }

  Cancel(): void {
    window.history.back();
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateGames(data: IGame[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.games.push(d);
      }
    }
  }
}
