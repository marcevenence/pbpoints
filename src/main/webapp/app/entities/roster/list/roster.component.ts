import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoster } from '../roster.model';
import { AccountService } from 'app/core/auth/account.service';
import * as dayjs from 'dayjs';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { RosterService } from '../service/roster.service';
import { RosterDeleteDialogComponent } from '../delete/roster-delete-dialog.component';

@Component({
  selector: 'jhi-roster',
  templateUrl: './roster.component.html',
})
export class RosterComponent implements OnInit {
  currentAccount: any;
  rosters?: IRoster[];
  roster?: IRoster[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  teId?: number;
  evCatId?: number;

  constructor(
    protected rosterService: RosterService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    if (this.evCatId !== 0) {
      this.rosterService
        .query({
          'eventCategoryId.equals': this.evCatId,
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IRoster[]>) => {
            this.isLoading = false;
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          () => {
            this.isLoading = false;
            this.onError();
          }
        );
    } else {
      if (this.teId !== 0) {
        this.rosterService
          .query({
            'teamId.equals': this.teId,
            page: pageToLoad - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
          })
          .subscribe(
            (res: HttpResponse<IRoster[]>) => {
              this.isLoading = false;
              this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
            },
            () => {
              this.isLoading = false;
              this.onError();
            }
          );
      } else {
        this.rosterService
          .query({
            page: pageToLoad - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
          })
          .subscribe(
            (res: HttpResponse<IRoster[]>) => {
              this.isLoading = false;
              this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
            },
            () => {
              this.isLoading = false;
              this.onError();
            }
          );
      }
    }
  }

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.handleNavigation();
  }

  trackId(index: number, item: IRoster): number {
    return item.id!;
  }

  delete(roster: IRoster): void {
    const modalRef = this.modalService.open(RosterDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.roster = roster;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  isAllowed(ownerId: number, status: string, endInscriptionDate: any): boolean {
    if (ownerId.toString() === this.currentAccount.id.toString()) {
      if (status === 'DONE' || status === 'CANCEL') {
        return false;
      } else {
        if (dayjs(endInscriptionDate, DATE_FORMAT) < dayjs().startOf('day')) {
          return false;
        } else {
          return true;
        }
      }
    } else {
      return false;
    }
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
    this.teId = history.state.teId ?? 0;
    this.evCatId = history.state.evCatId ?? 0;

    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';

      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IRoster[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/roster'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.rosters = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onOneSuccess(data: IRoster[] | null): void {
    this.roster = data ?? [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
