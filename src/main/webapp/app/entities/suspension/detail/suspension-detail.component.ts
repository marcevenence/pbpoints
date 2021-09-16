import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISuspension } from '../suspension.model';

@Component({
  selector: 'jhi-suspension-detail',
  templateUrl: './suspension-detail.component.html',
})
export class SuspensionDetailComponent implements OnInit {
  suspension: ISuspension | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ suspension }) => {
      this.suspension = suspension;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
