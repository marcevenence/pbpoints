import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FormatComponent } from './list/format.component';
import { FormatDetailComponent } from './detail/format-detail.component';
import { FormatUpdateComponent } from './update/format-update.component';
import { FormatDeleteDialogComponent } from './delete/format-delete-dialog.component';
import { FormatRoutingModule } from './route/format-routing.module';

@NgModule({
  imports: [SharedModule, FormatRoutingModule],
  declarations: [FormatComponent, FormatDetailComponent, FormatUpdateComponent, FormatDeleteDialogComponent],
  entryComponents: [FormatDeleteDialogComponent],
})
export class FormatModule {}
