import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMainRoster } from '../main-roster.model';
import { MainRosterService } from '../service/main-roster.service';

@Component({
  templateUrl: './main-roster-delete-dialog.component.html',
})
export class MainRosterDeleteDialogComponent {
  mainRoster?: IMainRoster;

  constructor(protected mainRosterService: MainRosterService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mainRosterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
