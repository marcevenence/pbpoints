import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITeamDetailPoint } from '../team-detail-point.model';
import { TeamDetailPointService } from '../service/team-detail-point.service';
import { TeamDetailPointDeleteDialogComponent } from '../delete/team-detail-point-delete-dialog.component';

@Component({
  selector: 'jhi-team-detail-point',
  templateUrl: './team-detail-point.component.html',
})
export class TeamDetailPointComponent implements OnInit {
  teamDetailPoints?: ITeamDetailPoint[];
  isLoading = false;

  constructor(protected teamDetailPointService: TeamDetailPointService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.teamDetailPointService.query().subscribe(
      (res: HttpResponse<ITeamDetailPoint[]>) => {
        this.isLoading = false;
        this.teamDetailPoints = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITeamDetailPoint): number {
    return item.id!;
  }

  delete(teamDetailPoint: ITeamDetailPoint): void {
    const modalRef = this.modalService.open(TeamDetailPointDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.teamDetailPoint = teamDetailPoint;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
