import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayerPointHistory } from '../player-point-history.model';
import { PlayerPointHistoryService } from '../service/player-point-history.service';
import { PlayerPointHistoryDeleteDialogComponent } from '../delete/player-point-history-delete-dialog.component';

@Component({
  selector: 'jhi-player-point-history',
  templateUrl: './player-point-history.component.html',
})
export class PlayerPointHistoryComponent implements OnInit {
  playerPointHistories?: IPlayerPointHistory[];
  isLoading = false;

  constructor(protected playerPointHistoryService: PlayerPointHistoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.playerPointHistoryService.query().subscribe({
      next: (res: HttpResponse<IPlayerPointHistory[]>) => {
        this.isLoading = false;
        this.playerPointHistories = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPlayerPointHistory): number {
    return item.id!;
  }

  delete(playerPointHistory: IPlayerPointHistory): void {
    const modalRef = this.modalService.open(PlayerPointHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.playerPointHistory = playerPointHistory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
