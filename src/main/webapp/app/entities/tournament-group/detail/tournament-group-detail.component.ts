import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITournamentGroup } from '../tournament-group.model';

@Component({
  selector: 'jhi-tournament-group-detail',
  templateUrl: './tournament-group-detail.component.html',
})
export class TournamentGroupDetailComponent implements OnInit {
  tournamentGroup: ITournamentGroup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tournamentGroup }) => {
      this.tournamentGroup = tournamentGroup;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
