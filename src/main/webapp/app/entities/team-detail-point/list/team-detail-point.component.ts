import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventCategory } from '../../event-category/event-category.model';
import { EventCategoryService } from '../../event-category/service/event-category.service';

import { ITeamDetailPoint } from '../team-detail-point.model';
import { TeamDetailPointService } from '../service/team-detail-point.service';
import { TeamDetailPointDeleteDialogComponent } from '../delete/team-detail-point-delete-dialog.component';

@Component({
  selector: 'jhi-team-detail-point',
  templateUrl: './team-detail-point.component.html',
})
export class TeamDetailPointComponent implements OnInit {
  teamDetailPoints?: ITeamDetailPoint[];
  eventCategories?: IEventCategory[];
  isLoading = false;
  evId: any;
  tpId: any;

  constructor(
    protected teamDetailPointService: TeamDetailPointService,
    protected eventCategoryService: EventCategoryService,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    if (this.evId !== 0) {
      this.eventCategoryService
        .query({
          'eventId.equals': this.evId,
        })
        .subscribe(
          (res: HttpResponse<IEventCategory[]>) => {
            this.isLoading = false;
            this.eventCategories = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      this.teamDetailPointService.query().subscribe(
        (res: HttpResponse<ITeamDetailPoint[]>) => {
          this.isLoading = false;
          this.teamDetailPoints = res.body ?? [];
        },
        () => {
          this.isLoading = false;
        }
      );
    } else {
      if (this.tpId !== 0) {
        this.teamDetailPointService
          .query({
            'teamPointId.equals': this.tpId,
          })
          .subscribe(
            (res: HttpResponse<ITeamDetailPoint[]>) => {
              this.isLoading = false;
              this.teamDetailPoints = res.body ?? [];
            },
            () => {
              this.isLoading = false;
            }
          );
      } else {
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
    }
  }

  ngOnInit(): void {
    this.evId = history.state.evId ?? 0;
    this.tpId = history.state.tpId ?? 0;
    this.loadAll();
  }

  trackId(index: number, item: ITeamDetailPoint): number {
    return item.id!;
  }
  trackIdEc(index: number, item: IEventCategory): number {
    return item.id!;
  }

  Cancel(): void {
    window.history.back();
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
