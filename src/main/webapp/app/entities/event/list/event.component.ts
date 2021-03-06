import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import { IEvent } from '../event.model';
import { AccountService } from 'app/core/auth/account.service';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { EventService } from '../service/event.service';
import { EventDeleteDialogComponent } from '../delete/event-delete-dialog.component';
import { EventSubmitDialogComponent } from '../submit/event-submit-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { saveAs } from 'file-saver';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-event',
  templateUrl: './event.component.html',
})
export class EventComponent implements OnInit {
  currentAccount: any;
  events: IEvent[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  tourId = 0;
  ascending: boolean;

  constructor(
    protected eventService: EventService,
    protected activatedRoute: ActivatedRoute,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected accountService: AccountService
  ) {
    this.events = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'fromDate';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.tourId !== 0) {
      if (this.currentAccount.authorities.includes('ROLE_ADMIN') || this.currentAccount.authorities.includes('ROLE_OWNER_TOURNAMENT')) {
        this.eventService
          .query({
            'tournamentId.equals': this.tourId,
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort(),
          })
          .subscribe(
            (res: HttpResponse<IEvent[]>) => {
              this.isLoading = false;
              this.paginateEvents(res.body, res.headers);
            },
            () => {
              this.isLoading = false;
            }
          );
      } else {
        this.eventService
          .query({
            'tournamentId.equals': this.tourId,
            'status.in': ['CREATED', 'IN_PROGRESS', 'DONE'],
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort(),
          })
          .subscribe(
            (res: HttpResponse<IEvent[]>) => {
              this.isLoading = false;
              this.paginateEvents(res.body, res.headers);
            },
            () => {
              this.isLoading = false;
            }
          );
      }
    } else {
      if (this.currentAccount.authorities.includes('ROLE_ADMIN') || this.currentAccount.authorities.includes('ROLE_OWNER_TOURNAMENT')) {
        this.eventService
          .query({
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort(),
          })
          .subscribe(
            (res: HttpResponse<IEvent[]>) => {
              this.isLoading = false;
              this.paginateEvents(res.body, res.headers);
            },
            () => {
              this.isLoading = false;
            }
          );
      } else {
        this.eventService
          .query({
            'status.in': ['CREATED', 'IN_PROGRESS'],
            page: this.page,
            size: this.itemsPerPage,
            sort: this.sort(),
          })
          .subscribe(
            (res: HttpResponse<IEvent[]>) => {
              this.isLoading = false;
              this.paginateEvents(res.body, res.headers);
            },
            () => {
              this.isLoading = false;
            }
          );
      }
    }
  }

  canGenerate(status: string, endInscriptionDate: any): boolean {
    if (status === 'PENDING' || status === 'CANCEL') {
      return false;
    } else {
      if (endInscriptionDate <= dayjs().startOf('day') || this.currentAccount.authorities.includes('ROLE_OWNER_TOURNAMENT')) {
        return true;
      } else {
        return false;
      }
    }
  }

  reset(): void {
    this.page = 0;
    this.events = [];
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
    if (history.state.tourId === undefined) {
      if (this.currentAccount.authorities.includes('ROLE_ADMIN')) {
        this.tourId = 0;
      } else {
        history.go(-1);
      }
    } else {
      this.tourId = history.state.tourId;
    }
    localStorage.setItem('TOURNAMENTID', this.tourId.toString());
    this.loadAll();
  }

  trackId(index: number, item: IEvent): number {
    return item.id!;
  }

  delete(event: IEvent): void {
    const modalRef = this.modalService.open(EventDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.event = event;
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

  generatePDF(id: number, name: string): void {
    this.eventService.generatePDF(id).subscribe((data: any) => {
      saveAs(data, 'PBPoint_' + name + '.pdf');
    }),
      () => {
        alert('Error al generar el archivo');
      };
  }

  generateXML(id: number, name: string): void {
    this.eventService.generateXML(id).subscribe((data: any) => {
      saveAs(data, 'PBPoint_' + name + '.pbp');
    }),
      () => {
        alert('Error al generar el archivo');
      };
  }

  submitFile(): void {
    const modalRef = this.modalService.open(EventSubmitDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.event = event;
    modalRef.closed.subscribe();
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateEvents(data: IEvent[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.events.push(d);
      }
    }
  }
}
