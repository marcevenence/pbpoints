import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AccountService } from 'app/core/auth/account.service';
import { IEventCategory } from '../event-category.model';

import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { EventCategoryService } from '../service/event-category.service';
import { EventCategoryDeleteDialogComponent } from '../delete/event-category-delete-dialog.component';

@Component({
  selector: 'jhi-event-category',
  templateUrl: './event-category.component.html',
})
export class EventCategoryComponent implements OnInit {
  eventCategories?: IEventCategory[];
  event?: IEvent;
  currentAccount: any;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  evId = 0;
  updateAllow = true;
  enableNew = false;

  constructor(
    protected eventCategoryService: EventCategoryService,
    protected eventService: EventService,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;
    if (this.evId) {
      this.eventCategoryService
        .query({
          'eventId.equals': this.evId,
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IEventCategory[]>) => {
            this.isLoading = false;
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          () => {
            this.isLoading = false;
            this.onError();
          }
        );
    } else {
      this.eventCategoryService
        .query({
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IEventCategory[]>) => {
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

  ngOnInit(): void {
    this.evId = history.state.evId ?? 0;
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.currentAccount = account;
      }
    });
    this.handleNavigation();
  }

  trackId(index: number, item: IEventCategory): number {
    return item.id!;
  }

  delete(eventCategory: IEventCategory): void {
    const modalRef = this.modalService.open(EventCategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.eventCategory = eventCategory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  enableUpdate(): boolean {
    return this.updateAllow;
  }

  getEnableNew(): boolean {
    return this.enableNew;
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
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
      if (this.evId !== 0) {
        this.eventService.queryOne(this.evId).subscribe((res: HttpResponse<IEvent>) => this.paginateEvent(res.body));
      }
    });
  }

  protected onSuccess(data: IEventCategory[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/event-category'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.eventCategories = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  protected paginateEvent(data: any): void {
    this.event = data;
    const today = new Date();
    if (this.currentAccount.authorities.includes('ROLE_ADMIN')) {
      this.updateAllow = true;
      this.enableNew = true;
    } else {
      if (
        this.currentAccount.authorities.includes('ROLE_OWNER_TOURNAMENT') &&
        this.event!.tournament!.owner!.id!.toString() === this.currentAccount?.id!.toString()
      ) {
        this.updateAllow = true;
        this.enableNew = true;
      } else {
        this.enableNew = false;
        if (this.event!.endInscriptionDate!.toDate() < today) {
          this.updateAllow = false;
        } else {
          this.updateAllow = true;
        }
      }
    }
  }
}
