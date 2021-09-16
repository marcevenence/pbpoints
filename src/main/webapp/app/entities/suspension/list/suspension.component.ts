import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISuspension } from '../suspension.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { SuspensionService } from '../service/suspension.service';
import { SuspensionDeleteDialogComponent } from '../delete/suspension-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-suspension',
  templateUrl: './suspension.component.html',
})
export class SuspensionComponent implements OnInit {
  suspensions: ISuspension[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected suspensionService: SuspensionService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.suspensions = [];
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

    this.suspensionService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ISuspension[]>) => {
          this.isLoading = false;
          this.paginateSuspensions(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.suspensions = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISuspension): number {
    return item.id!;
  }

  delete(suspension: ISuspension): void {
    const modalRef = this.modalService.open(SuspensionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.suspension = suspension;
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

  protected paginateSuspensions(data: ISuspension[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.suspensions.push(d);
      }
    }
  }
}
