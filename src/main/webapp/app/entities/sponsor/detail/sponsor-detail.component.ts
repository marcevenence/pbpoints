import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISponsor } from '../sponsor.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-sponsor-detail',
  templateUrl: './sponsor-detail.component.html',
})
export class SponsorDetailComponent implements OnInit {
  sponsor: ISponsor | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sponsor }) => {
      this.sponsor = sponsor;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
