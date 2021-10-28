import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISponsor } from '../sponsor.model';
import { SponsorService } from '../service/sponsor.service';

@Component({
  templateUrl: './sponsor-delete-dialog.component.html',
})
export class SponsorDeleteDialogComponent {
  sponsor?: ISponsor;

  constructor(protected sponsorService: SponsorService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sponsorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
