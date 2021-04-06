import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITeamDetailPoint } from '../team-detail-point.model';

@Component({
  selector: 'jhi-team-detail-point-detail',
  templateUrl: './team-detail-point-detail.component.html',
})
export class TeamDetailPointDetailComponent implements OnInit {
  teamDetailPoint: ITeamDetailPoint | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teamDetailPoint }) => {
      this.teamDetailPoint = teamDetailPoint;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
