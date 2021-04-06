import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormat } from '../format.model';

@Component({
  selector: 'jhi-format-detail',
  templateUrl: './format-detail.component.html',
})
export class FormatDetailComponent implements OnInit {
  format: IFormat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ format }) => {
      this.format = format;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
