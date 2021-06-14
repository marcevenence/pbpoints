import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import { ICategory } from '../category.model';
import { combineLatest } from 'rxjs';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { CategoryService } from '../service/category.service';
import { CategoryDeleteDialogComponent } from '../delete/category-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-category',
  templateUrl: './category.component.html',
})
export class CategoryComponent implements OnInit {
  categories: ICategory[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  tourId = 0;
  ascending: boolean;

  constructor(
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.categories = [];
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
    if (this.tourId) {
      this.categoryService
        .query({
          'tournamentId.equals': this.tourId,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<ICategory[]>) => {
            this.isLoading = false;
            this.paginateCategories(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
    } else {
      this.categoryService
        .query({
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<ICategory[]>) => {
            this.isLoading = false;
            this.paginateCategories(res.body, res.headers);
          },
          () => {
            this.isLoading = false;
          }
        );
    }
  }

  reset(): void {
    this.page = 0;
    this.categories = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      this.tourId = +params.get('tourId')!;
    });
    this.loadAll();
  }

  trackId(index: number, item: ICategory): number {
    return item.id!;
  }

  delete(category: ICategory): void {
    const modalRef = this.modalService.open(CategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.category = category;
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

  protected paginateCategories(data: ICategory[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.categories.push(d);
      }
    }
  }
}
