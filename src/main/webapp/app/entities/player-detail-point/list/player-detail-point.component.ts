import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayerDetailPoint } from '../player-detail-point.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PlayerDetailPointService } from '../service/player-detail-point.service';
import { PlayerDetailPointDeleteDialogComponent } from '../delete/player-detail-point-delete-dialog.component';

@Component({
  selector: 'jhi-player-detail-point',
  templateUrl: './player-detail-point.component.html',
})
export class PlayerDetailPointComponent implements OnInit {
  currentAccount: any;
  playerDetailPoints?: IPlayerDetailPoint[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  ppId?: number;

  constructor(
    protected playerDetailPointService: PlayerDetailPointService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.playerDetailPointService
      .query({
        'playerPointId.equals': this.ppId,
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IPlayerDetailPoint[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.handleNavigation();
  }

  trackId(index: number, item: IPlayerDetailPoint): number {
    return item.id!;
  }

  delete(playerDetailPoint: IPlayerDetailPoint): void {
    const modalRef = this.modalService.open(PlayerDetailPointDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.playerDetailPoint = playerDetailPoint;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
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

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      this.ppId = +params.get('ppId')! || 0;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IPlayerDetailPoint[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/player-detail-point'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.playerDetailPoints = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
