import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import { IPlayer } from '../player.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PlayerService } from '../service/player.service';
import { PlayerDeleteDialogComponent } from '../delete/player-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-player',
  templateUrl: './player.component.html',
})
export class PlayerComponent implements OnInit {
  currentAccount: any;
  players: IPlayer[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  rId?: number;
  ascending: boolean;

  constructor(
    protected playerService: PlayerService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService
  ) {
    this.players = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  Cancel(): void {
    window.history.back();
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.rId) {
      this.playerService
        .query({
          'rosterId.equals': this.rId,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IPlayer[]>) => {
            this.isLoading = false;
            this.paginatePlayers(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
    } else {
      this.playerService
        .query({
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IPlayer[]>) => {
            this.isLoading = false;
            this.paginatePlayers(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
    }
  }

  reset(): void {
    this.page = 0;
    this.players = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.rId = +params['rId'] || 0;
    });
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.loadAll();
  }

  isTheOwner(ownerId: number): boolean {
    if (ownerId.toString() === this.currentAccount.id.toString()) {
      return true;
    } else {
      return false;
    }
  }

  trackId(index: number, item: IPlayer): number {
    return item.id!;
  }

  delete(player: IPlayer): void {
    const modalRef = this.modalService.open(PlayerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.player = player;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePlayers(data: IPlayer[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.players.push(d);
      }
    }
  }
}
