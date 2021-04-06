import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayerPoint } from '../player-point.model';
import { PlayerPointService } from '../service/player-point.service';

@Component({
  templateUrl: './player-point-delete-dialog.component.html',
})
export class PlayerPointDeleteDialogComponent {
  playerPoint?: IPlayerPoint;

  constructor(protected playerPointService: PlayerPointService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.playerPointService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
