import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBracket } from '../bracket.model';
import { BracketService } from '../service/bracket.service';
import { BracketDeleteDialogComponent } from '../delete/bracket-delete-dialog.component';

@Component({
  selector: 'jhi-bracket',
  templateUrl: './bracket.component.html',
})
export class BracketComponent implements OnInit {
  brackets?: IBracket[];
  isLoading = false;

  constructor(protected bracketService: BracketService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.bracketService.query().subscribe(
      (res: HttpResponse<IBracket[]>) => {
        this.isLoading = false;
        this.brackets = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBracket): number {
    return item.id!;
  }

  delete(bracket: IBracket): void {
    const modalRef = this.modalService.open(BracketDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bracket = bracket;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
