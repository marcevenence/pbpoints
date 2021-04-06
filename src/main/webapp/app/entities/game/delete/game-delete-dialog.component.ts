import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGame } from '../game.model';
import { GameService } from '../service/game.service';

@Component({
  templateUrl: './game-delete-dialog.component.html',
})
export class GameDeleteDialogComponent {
  game?: IGame;

  constructor(protected gameService: GameService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gameService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
