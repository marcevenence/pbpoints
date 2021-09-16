import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISuspension } from '../suspension.model';
import { SuspensionService } from '../service/suspension.service';

@Component({
  templateUrl: './suspension-delete-dialog.component.html',
})
export class SuspensionDeleteDialogComponent {
  suspension?: ISuspension;

  constructor(protected suspensionService: SuspensionService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.suspensionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
