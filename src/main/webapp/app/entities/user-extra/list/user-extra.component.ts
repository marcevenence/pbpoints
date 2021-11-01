import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserExtra } from '../user-extra.model';
import { UserExtraService } from '../service/user-extra.service';
import { UserExtraDeleteDialogComponent } from '../delete/user-extra-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-user-extra',
  templateUrl: './user-extra.component.html',
})
export class UserExtraComponent implements OnInit {
  userExtras?: IUserExtra[];
  isLoading = false;

  constructor(protected userExtraService: UserExtraService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.userExtraService.query().subscribe(
      (res: HttpResponse<IUserExtra[]>) => {
        this.isLoading = false;
        this.userExtras = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IUserExtra): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(userExtra: IUserExtra): void {
    const modalRef = this.modalService.open(UserExtraDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userExtra = userExtra;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
