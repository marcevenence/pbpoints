import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProvince } from '../province.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ProvinceService } from '../service/province.service';
import { ProvinceDeleteDialogComponent } from '../delete/province-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-province',
  templateUrl: './province.component.html',
})
export class ProvinceComponent implements OnInit {
  provinces: IProvince[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected provinceService: ProvinceService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.provinces = [];
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

    this.provinceService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IProvince[]>) => {
          this.isLoading = false;
          this.paginateProvinces(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.provinces = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IProvince): number {
    return item.id!;
  }

  delete(province: IProvince): void {
    const modalRef = this.modalService.open(ProvinceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.province = province;
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

  protected paginateProvinces(data: IProvince[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.provinces.push(d);
      }
    }
  }
}
