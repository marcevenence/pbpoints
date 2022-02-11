import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlayerPointHistory } from '../player-point-history.model';

@Component({
  selector: 'jhi-player-point-history-detail',
  templateUrl: './player-point-history-detail.component.html',
})
export class PlayerPointHistoryDetailComponent implements OnInit {
  playerPointHistory: IPlayerPointHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playerPointHistory }) => {
      this.playerPointHistory = playerPointHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
