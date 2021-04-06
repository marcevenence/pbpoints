import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITeamDetailPoint } from '../team-detail-point.model';
import { TeamDetailPointService } from '../service/team-detail-point.service';

@Component({
  templateUrl: './team-detail-point-delete-dialog.component.html',
})
export class TeamDetailPointDeleteDialogComponent {
  teamDetailPoint?: ITeamDetailPoint;

  constructor(protected teamDetailPointService: TeamDetailPointService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.teamDetailPointService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
