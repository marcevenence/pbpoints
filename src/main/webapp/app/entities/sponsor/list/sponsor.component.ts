import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISponsor } from '../sponsor.model';
import { SponsorService } from '../service/sponsor.service';
import { SponsorDeleteDialogComponent } from '../delete/sponsor-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-sponsor',
  templateUrl: './sponsor.component.html',
})
export class SponsorComponent implements OnInit {
  sponsors?: ISponsor[];
  isLoading = false;

  constructor(protected sponsorService: SponsorService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.sponsorService.query().subscribe(
      (res: HttpResponse<ISponsor[]>) => {
        this.isLoading = false;
        this.sponsors = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISponsor): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(sponsor: ISponsor): void {
    const modalRef = this.modalService.open(SponsorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sponsor = sponsor;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
