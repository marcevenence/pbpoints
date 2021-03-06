import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AccountService } from 'app/core/auth/account.service';
import { IEquipment } from '../equipment.model';
import { EquipmentService } from '../service/equipment.service';
import { EquipmentDeleteDialogComponent } from '../delete/equipment-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-equipment',
  templateUrl: './equipment.component.html',
})
export class EquipmentComponent implements OnInit {
  currentAccount: any;
  authSubscription?: Subscription;
  equipment?: IEquipment[];
  isLoading = false;

  constructor(
    protected equipmentService: EquipmentService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    protected accountService: AccountService
  ) {}

  loadAll(): void {
    this.isLoading = true;
    if (this.currentAccount.authorities.includes('ROLE_ADMIN')) {
      this.equipmentService.query().subscribe(
        (res: HttpResponse<IEquipment[]>) => {
          this.isLoading = false;
          this.equipment = res.body ?? [];
        },
        () => {
          this.isLoading = false;
        }
      );
    } else {
      this.equipmentService.query({ 'userId.equals': this.currentAccount.id }).subscribe(
        (res: HttpResponse<IEquipment[]>) => {
          this.isLoading = false;
          this.equipment = res.body ?? [];
        },
        () => {
          this.isLoading = false;
        }
      );
    }
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.currentAccount = account));
    this.loadAll();
  }

  trackId(index: number, item: IEquipment): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(equipment: IEquipment): void {
    const modalRef = this.modalService.open(EquipmentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.equipment = equipment;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
