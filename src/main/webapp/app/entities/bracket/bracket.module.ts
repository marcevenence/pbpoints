import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BracketComponent } from './list/bracket.component';
import { BracketDetailComponent } from './detail/bracket-detail.component';
import { BracketUpdateComponent } from './update/bracket-update.component';
import { BracketDeleteDialogComponent } from './delete/bracket-delete-dialog.component';
import { BracketRoutingModule } from './route/bracket-routing.module';

@NgModule({
  imports: [SharedModule, BracketRoutingModule],
  declarations: [BracketComponent, BracketDetailComponent, BracketUpdateComponent, BracketDeleteDialogComponent],
  entryComponents: [BracketDeleteDialogComponent],
})
export class BracketModule {}
