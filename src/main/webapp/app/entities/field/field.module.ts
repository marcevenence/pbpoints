import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FieldComponent } from './list/field.component';
import { FieldDetailComponent } from './detail/field-detail.component';
import { FieldUpdateComponent } from './update/field-update.component';
import { FieldDeleteDialogComponent } from './delete/field-delete-dialog.component';
import { FieldRoutingModule } from './route/field-routing.module';

@NgModule({
  imports: [SharedModule, FieldRoutingModule],
  declarations: [FieldComponent, FieldDetailComponent, FieldUpdateComponent, FieldDeleteDialogComponent],
  entryComponents: [FieldDeleteDialogComponent],
})
export class FieldModule {}
