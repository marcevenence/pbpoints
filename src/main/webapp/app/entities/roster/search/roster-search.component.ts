import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { map } from 'rxjs/operators';
import { DataUtils } from 'app/core/util/data-util.service';
import { AccountService } from 'app/core/auth/account.service';
import { IRosterEvent } from '../roster-event.model';
import { RosterEventService } from '../service/roster-event.service';
import { IEventCategory } from 'app/entities/event-category/event-category.model';
import { EventCategoryService } from 'app/entities/event-category/service/event-category.service';

@Component({
  selector: 'jhi-roster-search',
  templateUrl: './roster-search.component.html',
})
export class RosterSearchComponent implements OnInit {
  rosters?: IRosterEvent[];
  currentAccount: any;
  eventCategoriesSharedCollection: IEventCategory[] = [];
  predicate!: string;
  ascending!: boolean;

  findForm = this.fb.group({
    eventCategory: [null, Validators.required],
  });

  constructor(
    protected rosterEventService: RosterEventService,
    protected eventCategoryService: EventCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected accountService: AccountService,
    protected router: Router,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.rosters = [];
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.activatedRoute.data.subscribe(() => {
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  onChange(): void {
    this.rosterEventService.query(this.findForm.get(['eventCategory'])!.value?.id).subscribe(
      (res: HttpResponse<IRosterEvent[]>) => {
        this.rosters = res.body ?? [];
      },
      () => {
        this.onError();
      }
    );
  }

  loadPage(): void {
    // no hace nada pero necesario para mostrar datos
  }
  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  trackRosterEventId(index: number, item: IRosterEvent): string {
    return item.id!;
  }
  trackEventCategoryById(index: number, item: IEventCategory): number {
    return item.id!;
  }

  filterEventCategoryFunction(): IEventCategory[] {
    if (!this.currentAccount.authorities.includes('ROLE_ADMIN')) {
      return this.eventCategoriesSharedCollection.filter(evCat => evCat.event!.tournament!.owner!.id === this.currentAccount.id);
    } else {
      return this.eventCategoriesSharedCollection;
    }
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onError(): void {
    // Api for inheritance.
    alert('error');
  }

  protected loadRelationshipsOptions(): void {
    this.eventCategoryService
      .query()
      .pipe(map((res: HttpResponse<IEventCategory[]>) => res.body ?? []))
      .pipe(
        map((eventCategories: IEventCategory[]) =>
          this.eventCategoryService.addEventCategoryToCollectionIfMissing(eventCategories, this.findForm.get('eventCategory')!.value)
        )
      )
      .subscribe((eventCategories: IEventCategory[]) => (this.eventCategoriesSharedCollection = eventCategories));
  }
}
