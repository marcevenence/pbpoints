import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayerPoint } from '../player-point.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PlayerPointService } from '../service/player-point.service';
import { PlayerPointDeleteDialogComponent } from '../delete/player-point-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'jhi-player-point',
  templateUrl: './player-point.component.html',
})
export class PlayerPointComponent implements OnInit {
  currentAccount: any;
  playerPoints: IPlayerPoint[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  currentImage: any;
  currentImageURL: any;

  constructor(
    protected playerPointService: PlayerPointService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected accountService: AccountService,
    private sanitizer: DomSanitizer
  ) {
    this.playerPoints = [];
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
    if (this.currentAccount.authorities.includes('ROLE_ADMIN')) {
      this.playerPointService
        .query({
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IPlayerPoint[]>) => {
            this.isLoading = false;
            this.paginatePlayerPoints(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
    } else {
      this.playerPointService
        .query({
          'userId.equals': this.currentAccount.id,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IPlayerPoint[]>) => {
            this.isLoading = false;
            this.paginatePlayerPoints(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
    }
  }

  reset(): void {
    this.page = 0;
    this.playerPoints = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.currentImageURL = 'data:' + String(this.currentAccount.pictureContentType) + ';base64,' + String(this.currentAccount.picture);
    this.currentImage = this.sanitizer.bypassSecurityTrustUrl(this.currentImageURL);
  }

  trackId(index: number, item: IPlayerPoint): number {
    return item.id!;
  }

  delete(playerPoint: IPlayerPoint): void {
    const modalRef = this.modalService.open(PlayerPointDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.playerPoint = playerPoint;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
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

  protected paginatePlayerPoints(data: IPlayerPoint[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.playerPoints.push(d);
      }
    }
  }
}
