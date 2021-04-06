import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEventCategory } from '../event-category.model';

@Component({
  selector: 'jhi-event-category-detail',
  templateUrl: './event-category-detail.component.html',
})
export class EventCategoryDetailComponent implements OnInit {
  eventCategory: IEventCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventCategory }) => {
      this.eventCategory = eventCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
