import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITeamPoint } from '../team-point.model';

@Component({
  selector: 'jhi-team-point-detail',
  templateUrl: './team-point-detail.component.html',
})
export class TeamPointDetailComponent implements OnInit {
  teamPoint: ITeamPoint | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teamPoint }) => {
      this.teamPoint = teamPoint;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
