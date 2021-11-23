import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITournamentGroup } from '../tournament-group.model';
import { TournamentGroupService } from '../service/tournament-group.service';

@Component({
  templateUrl: './tournament-group-delete-dialog.component.html',
})
export class TournamentGroupDeleteDialogComponent {
  tournamentGroup?: ITournamentGroup;

  constructor(protected tournamentGroupService: TournamentGroupService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tournamentGroupService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
