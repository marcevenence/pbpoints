import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DocTypeComponent } from './list/doc-type.component';
import { DocTypeDetailComponent } from './detail/doc-type-detail.component';
import { DocTypeUpdateComponent } from './update/doc-type-update.component';
import { DocTypeDeleteDialogComponent } from './delete/doc-type-delete-dialog.component';
import { DocTypeRoutingModule } from './route/doc-type-routing.module';

@NgModule({
  imports: [SharedModule, DocTypeRoutingModule],
  declarations: [DocTypeComponent, DocTypeDetailComponent, DocTypeUpdateComponent, DocTypeDeleteDialogComponent],
  entryComponents: [DocTypeDeleteDialogComponent],
})
export class DocTypeModule {}
