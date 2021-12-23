import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITeamCategoryPoint } from '../team-category-point.model';
import { TeamCategoryPointService } from '../service/team-category-point.service';
import { TeamCategoryPointDeleteDialogComponent } from '../delete/team-category-point-delete-dialog.component';

@Component({
  selector: 'jhi-team-category-point',
  templateUrl: './team-category-point.component.html',
})
export class TeamCategoryPointComponent implements OnInit {
  teamCategoryPoints?: ITeamCategoryPoint[];
  isLoading = false;

  constructor(protected teamCategoryPointService: TeamCategoryPointService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.teamCategoryPointService.query().subscribe(
      (res: HttpResponse<ITeamCategoryPoint[]>) => {
        this.isLoading = false;
        this.teamCategoryPoints = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITeamCategoryPoint): number {
    return item.id!;
  }

  delete(teamCategoryPoint: ITeamCategoryPoint): void {
    const modalRef = this.modalService.open(TeamCategoryPointDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.teamCategoryPoint = teamCategoryPoint;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
