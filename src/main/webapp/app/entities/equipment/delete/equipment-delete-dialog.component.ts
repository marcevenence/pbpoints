import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEquipment } from '../equipment.model';
import { EquipmentService } from '../service/equipment.service';

@Component({
  templateUrl: './equipment-delete-dialog.component.html',
})
export class EquipmentDeleteDialogComponent {
  equipment?: IEquipment;

  constructor(protected equipmentService: EquipmentService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.equipmentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
