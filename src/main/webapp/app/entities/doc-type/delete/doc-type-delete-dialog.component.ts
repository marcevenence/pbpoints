import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocType } from '../doc-type.model';
import { DocTypeService } from '../service/doc-type.service';

@Component({
  templateUrl: './doc-type-delete-dialog.component.html',
})
export class DocTypeDeleteDialogComponent {
  docType?: IDocType;

  constructor(protected docTypeService: DocTypeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.docTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
