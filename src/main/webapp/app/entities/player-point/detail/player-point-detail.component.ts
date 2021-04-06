import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlayerPoint } from '../player-point.model';

@Component({
  selector: 'jhi-player-point-detail',
  templateUrl: './player-point-detail.component.html',
})
export class PlayerPointDetailComponent implements OnInit {
  playerPoint: IPlayerPoint | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playerPoint }) => {
      this.playerPoint = playerPoint;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
