import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITeamCategoryPoint } from '../team-category-point.model';

@Component({
  selector: 'jhi-team-category-point-detail',
  templateUrl: './team-category-point-detail.component.html',
})
export class TeamCategoryPointDetailComponent implements OnInit {
  teamCategoryPoint: ITeamCategoryPoint | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teamCategoryPoint }) => {
      this.teamCategoryPoint = teamCategoryPoint;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
