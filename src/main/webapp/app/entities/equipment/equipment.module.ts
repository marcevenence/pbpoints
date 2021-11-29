import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { EquipmentComponent } from './list/equipment.component';
import { EquipmentDetailComponent } from './detail/equipment-detail.component';
import { EquipmentUpdateComponent } from './update/equipment-update.component';
import { EquipmentDeleteDialogComponent } from './delete/equipment-delete-dialog.component';
import { EquipmentRoutingModule } from './route/equipment-routing.module';

@NgModule({
  imports: [SharedModule, EquipmentRoutingModule],
  declarations: [EquipmentComponent, EquipmentDetailComponent, EquipmentUpdateComponent, EquipmentDeleteDialogComponent],
  entryComponents: [EquipmentDeleteDialogComponent],
})
export class EquipmentModule {}
