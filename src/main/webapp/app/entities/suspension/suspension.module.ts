import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SuspensionComponent } from './list/suspension.component';
import { SuspensionDetailComponent } from './detail/suspension-detail.component';
import { SuspensionUpdateComponent } from './update/suspension-update.component';
import { SuspensionDeleteDialogComponent } from './delete/suspension-delete-dialog.component';
import { SuspensionRoutingModule } from './route/suspension-routing.module';

@NgModule({
  imports: [SharedModule, SuspensionRoutingModule],
  declarations: [SuspensionComponent, SuspensionDetailComponent, SuspensionUpdateComponent, SuspensionDeleteDialogComponent],
  entryComponents: [SuspensionDeleteDialogComponent],
})
export class SuspensionModule {}
