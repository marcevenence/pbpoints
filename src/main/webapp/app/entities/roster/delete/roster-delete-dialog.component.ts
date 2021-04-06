import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoster } from '../roster.model';
import { RosterService } from '../service/roster.service';

@Component({
  templateUrl: './roster-delete-dialog.component.html',
})
export class RosterDeleteDialogComponent {
  roster?: IRoster;

  constructor(protected rosterService: RosterService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rosterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
