import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITeamPoint } from '../team-point.model';
import { TeamPointService } from '../service/team-point.service';

@Component({
  templateUrl: './team-point-delete-dialog.component.html',
})
export class TeamPointDeleteDialogComponent {
  teamPoint?: ITeamPoint;

  constructor(protected teamPointService: TeamPointService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.teamPointService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
