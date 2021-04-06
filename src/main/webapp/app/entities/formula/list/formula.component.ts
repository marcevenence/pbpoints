import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormula } from '../formula.model';
import { FormulaService } from '../service/formula.service';
import { FormulaDeleteDialogComponent } from '../delete/formula-delete-dialog.component';

@Component({
  selector: 'jhi-formula',
  templateUrl: './formula.component.html',
})
export class FormulaComponent implements OnInit {
  formulas?: IFormula[];
  isLoading = false;

  constructor(protected formulaService: FormulaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.formulaService.query().subscribe(
      (res: HttpResponse<IFormula[]>) => {
        this.isLoading = false;
        this.formulas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFormula): number {
    return item.id!;
  }

  delete(formula: IFormula): void {
    const modalRef = this.modalService.open(FormulaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.formula = formula;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
