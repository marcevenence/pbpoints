import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITeamCategoryPoint } from '../team-category-point.model';
import { TeamCategoryPointService } from '../service/team-category-point.service';

@Component({
  templateUrl: './team-category-point-delete-dialog.component.html',
})
export class TeamCategoryPointDeleteDialogComponent {
  teamCategoryPoint?: ITeamCategoryPoint;

  constructor(protected teamCategoryPointService: TeamCategoryPointService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.teamCategoryPointService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
