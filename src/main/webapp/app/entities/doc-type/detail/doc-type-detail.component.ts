import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocType } from '../doc-type.model';

@Component({
  selector: 'jhi-doc-type-detail',
  templateUrl: './doc-type-detail.component.html',
})
export class DocTypeDetailComponent implements OnInit {
  docType: IDocType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ docType }) => {
      this.docType = docType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
