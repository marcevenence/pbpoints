import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITournamentGroup } from '../tournament-group.model';
import { TournamentGroupService } from '../service/tournament-group.service';
import { TournamentGroupDeleteDialogComponent } from '../delete/tournament-group-delete-dialog.component';

@Component({
  selector: 'jhi-tournament-group',
  templateUrl: './tournament-group.component.html',
})
export class TournamentGroupComponent implements OnInit {
  tournamentGroups?: ITournamentGroup[];
  isLoading = false;

  constructor(protected tournamentGroupService: TournamentGroupService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tournamentGroupService.query().subscribe(
      (res: HttpResponse<ITournamentGroup[]>) => {
        this.isLoading = false;
        this.tournamentGroups = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITournamentGroup): number {
    return item.id!;
  }

  delete(tournamentGroup: ITournamentGroup): void {
    const modalRef = this.modalService.open(TournamentGroupDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tournamentGroup = tournamentGroup;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
