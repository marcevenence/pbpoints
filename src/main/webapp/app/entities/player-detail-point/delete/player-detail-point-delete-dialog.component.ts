import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayerDetailPoint } from '../player-detail-point.model';
import { PlayerDetailPointService } from '../service/player-detail-point.service';

@Component({
  templateUrl: './player-detail-point-delete-dialog.component.html',
})
export class PlayerDetailPointDeleteDialogComponent {
  playerDetailPoint?: IPlayerDetailPoint;

  constructor(protected playerDetailPointService: PlayerDetailPointService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.playerDetailPointService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
