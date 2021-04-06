import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlayerDetailPoint } from '../player-detail-point.model';

@Component({
  selector: 'jhi-player-detail-point-detail',
  templateUrl: './player-detail-point-detail.component.html',
})
export class PlayerDetailPointDetailComponent implements OnInit {
  playerDetailPoint: IPlayerDetailPoint | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playerDetailPoint }) => {
      this.playerDetailPoint = playerDetailPoint;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
