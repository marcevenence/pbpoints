import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventCategory } from '../event-category.model';
import { EventCategoryService } from '../service/event-category.service';

@Component({
  templateUrl: './event-category-delete-dialog.component.html',
})
export class EventCategoryDeleteDialogComponent {
  eventCategory?: IEventCategory;

  constructor(protected eventCategoryService: EventCategoryService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eventCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
