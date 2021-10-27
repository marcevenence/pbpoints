import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGame } from '../game.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { GameService } from '../service/game.service';
import { GameDeleteDialogComponent } from '../delete/game-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-game',
  templateUrl: './game.component.html',
})
export class GameComponent implements OnInit {
  games: IGame[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  ecatId = 0;

  constructor(protected gameService: GameService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.games = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    if (this.ecatId !== 0) {
      this.gameService
        .query({
          'eventCategoryId.equals': this.ecatId,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IGame[]>) => {
            this.isLoading = false;
            this.paginateGames(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
    } else {
      this.gameService
        .query({
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IGame[]>) => {
            this.isLoading = false;
            this.paginateGames(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
    }
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
    this.ecatId = history.state.ecatId ?? 0;
    this.loadAll();
  }

  trackId(index: number, item: IGame): number {
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
