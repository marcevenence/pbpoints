import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoster } from '../roster.model';

@Component({
  selector: 'jhi-roster-detail',
  templateUrl: './roster-detail.component.html',
})
export class RosterDetailComponent implements OnInit {
  roster: IRoster | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roster }) => {
      this.roster = roster;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
