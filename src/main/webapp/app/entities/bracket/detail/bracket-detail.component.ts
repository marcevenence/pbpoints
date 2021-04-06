import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBracket } from '../bracket.model';

@Component({
  selector: 'jhi-bracket-detail',
  templateUrl: './bracket-detail.component.html',
})
export class BracketDetailComponent implements OnInit {
  bracket: IBracket | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bracket }) => {
      this.bracket = bracket;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
