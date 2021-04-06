import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormat } from '../format.model';
import { FormatService } from '../service/format.service';

@Component({
  templateUrl: './format-delete-dialog.component.html',
})
export class FormatDeleteDialogComponent {
  format?: IFormat;

  constructor(protected formatService: FormatService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formatService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
