import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMainRoster } from '../main-roster.model';

@Component({
  selector: 'jhi-main-roster-detail',
  templateUrl: './main-roster-detail.component.html',
})
export class MainRosterDetailComponent implements OnInit {
  mainRoster: IMainRoster | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mainRoster }) => {
      this.mainRoster = mainRoster;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
