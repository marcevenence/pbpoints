import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEventCategory, EventCategory } from '../event-category.model';
import { EventCategoryService } from '../service/event-category.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IFormat } from 'app/entities/format/format.model';
import { FormatService } from 'app/entities/format/service/format.service';

@Component({
  selector: 'jhi-event-category-update',
  templateUrl: './event-category-update.component.html',
})
export class EventCategoryUpdateComponent implements OnInit {
  isSaving = false;

  eventsSharedCollection: IEvent[] = [];
  categoriesSharedCollection: ICategory[] = [];
  formatsSharedCollection: IFormat[] = [];

  editForm = this.fb.group({
    id: [],
    splitDeck: [],
    event: [null, Validators.required],
    category: [null, Validators.required],
    format: [null, Validators.required],
  });

  constructor(
    protected eventCategoryService: EventCategoryService,
    protected eventService: EventService,
    protected categoryService: CategoryService,
    protected formatService: FormatService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventCategory }) => {
      this.updateForm(eventCategory);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eventCategory = this.createFromForm();
    if (eventCategory.id !== undefined) {
      if (eventCategory.id) {
        this.subscribeToSaveResponse(this.eventCategoryService.update(eventCategory));
      } else {
        this.subscribeToSaveResponse(this.eventCategoryService.create(eventCategory));
      }
    } else {
      this.subscribeToSaveResponse(this.eventCategoryService.create(eventCategory));
    }
  }

  trackEventById(index: number, item: IEvent): number {
    return item.id!;
  }

  trackCategoryById(index: number, item: ICategory): number {
    return item.id!;
  }

  trackFormatById(index: number, item: IFormat): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventCategory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(eventCategory: IEventCategory): void {
    this.editForm.patchValue({
      id: eventCategory.id,
      splitDeck: eventCategory.splitDeck,
      event: eventCategory.event,
      category: eventCategory.category,
      format: eventCategory.format,
    });

    this.eventsSharedCollection = this.eventService.addEventToCollectionIfMissing(this.eventsSharedCollection, eventCategory.event);
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(
      this.categoriesSharedCollection,
      eventCategory.category
    );
    this.formatsSharedCollection = this.formatService.addFormatToCollectionIfMissing(this.formatsSharedCollection, eventCategory.format);
  }

  protected loadRelationshipsOptions(): void {
    this.eventService
      .query({
        'tournamentId.equals': +localStorage.getItem('TOURNAMENTID')!,
      })
      .pipe(map((res: HttpResponse<IEvent[]>) => res.body ?? []))
      .pipe(map((events: IEvent[]) => this.eventService.addEventToCollectionIfMissing(events, this.editForm.get('event')!.value)))
      .subscribe((events: IEvent[]) => (this.eventsSharedCollection = events));

    this.categoryService
      .query({
        'tournamentId.equals': +localStorage.getItem('TOURNAMENTID')!,
      })
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));

    this.formatService
      .query({
        'tournamentId.equals': +localStorage.getItem('TOURNAMENTID')!,
      })
      .pipe(map((res: HttpResponse<IFormat[]>) => res.body ?? []))
      .pipe(map((formats: IFormat[]) => this.formatService.addFormatToCollectionIfMissing(formats, this.editForm.get('format')!.value)))
      .subscribe((formats: IFormat[]) => (this.formatsSharedCollection = formats));
  }

  protected createFromForm(): IEventCategory {
    return {
      ...new EventCategory(),
      id: this.editForm.get(['id'])!.value,
      splitDeck: this.editForm.get(['splitDeck'])!.value,
      event: this.editForm.get(['event'])!.value,
      category: this.editForm.get(['category'])!.value,
      format: this.editForm.get(['format'])!.value,
    };
  }
}
