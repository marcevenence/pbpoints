import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayerPointHistory } from '../player-point-history.model';
import { PlayerPointHistoryService } from '../service/player-point-history.service';

@Component({
  templateUrl: './player-point-history-delete-dialog.component.html',
})
export class PlayerPointHistoryDeleteDialogComponent {
  playerPointHistory?: IPlayerPointHistory;

  constructor(protected playerPointHistoryService: PlayerPointHistoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.playerPointHistoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
