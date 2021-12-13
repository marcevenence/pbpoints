import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { EventService } from '../service/event.service';

@Component({
  templateUrl: './event-submit-dialog.component.html',
})
export class EventSubmitDialogComponent {
  selectedFiles?: FileList;

  constructor(public activeModal: NgbActiveModal, protected eventService: EventService) {}

  selectFile(event: any): void {
    this.selectedFiles = event.target.files;
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  upload(): void {
    if (this.selectedFiles) {
      const file: File | null = this.selectedFiles.item(0);
      if (file) {
        this.eventService.submit(file).subscribe(() => {
          this.activeModal.close('yes');
        });
      }
    }
  }
}
