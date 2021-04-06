import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITournament } from '../tournament.model';
import { TournamentService } from '../service/tournament.service';

@Component({
  templateUrl: './tournament-delete-dialog.component.html',
})
export class TournamentDeleteDialogComponent {
  tournament?: ITournament;

  constructor(protected tournamentService: TournamentService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tournamentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
