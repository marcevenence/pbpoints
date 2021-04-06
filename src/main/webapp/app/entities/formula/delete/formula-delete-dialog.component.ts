import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormula } from '../formula.model';
import { FormulaService } from '../service/formula.service';

@Component({
  templateUrl: './formula-delete-dialog.component.html',
})
export class FormulaDeleteDialogComponent {
  formula?: IFormula;

  constructor(protected formulaService: FormulaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formulaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
