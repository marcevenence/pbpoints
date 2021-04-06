import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBracket } from '../bracket.model';
import { BracketService } from '../service/bracket.service';

@Component({
  templateUrl: './bracket-delete-dialog.component.html',
})
export class BracketDeleteDialogComponent {
  bracket?: IBracket;

  constructor(protected bracketService: BracketService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bracketService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
