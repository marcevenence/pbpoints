import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICity } from '../city.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { CityService } from '../service/city.service';
import { CityDeleteDialogComponent } from '../delete/city-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-city',
  templateUrl: './city.component.html',
})
export class CityComponent implements OnInit {
  cities: ICity[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;
  prId = 0;

  constructor(protected cityService: CityService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.cities = [];
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

    if (this.prId !== 0) {
      this.cityService
        .query({
          'provinceId.equals': this.prId,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<ICity[]>) => {
            this.isLoading = false;
            this.paginateCities(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
    } else {
      this.cityService
        .query({
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<ICity[]>) => {
            this.isLoading = false;
            this.paginateCities(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
    }
  }

  reset(): void {
    this.page = 0;
    this.cities = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.prId = history.state.prId ?? 0;
    this.loadAll();
  }

  trackId(index: number, item: ICity): number {
    return item.id!;
  }

  delete(city: ICity): void {
    const modalRef = this.modalService.open(CityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.city = city;
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

  protected paginateCities(data: ICity[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.cities.push(d);
      }
    }
  }
}
