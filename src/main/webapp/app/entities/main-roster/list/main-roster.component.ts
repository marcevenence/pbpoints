import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMainRoster } from '../main-roster.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { MainRosterService } from '../service/main-roster.service';
import { MainRosterDeleteDialogComponent } from '../delete/main-roster-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-main-roster',
  templateUrl: './main-roster.component.html',
})
export class MainRosterComponent implements OnInit {
  mainRosters: IMainRoster[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected mainRosterService: MainRosterService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.mainRosters = [];
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

    this.mainRosterService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IMainRoster[]>) => {
          this.isLoading = false;
          this.paginateMainRosters(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.mainRosters = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMainRoster): number {
    return item.id!;
  }

  Cancel(): void {
    window.history.back();
  }

  delete(mainRoster: IMainRoster): void {
    const modalRef = this.modalService.open(MainRosterDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mainRoster = mainRoster;
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

  protected paginateMainRosters(data: IMainRoster[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.mainRosters.push(d);
      }
    }
  }
}
