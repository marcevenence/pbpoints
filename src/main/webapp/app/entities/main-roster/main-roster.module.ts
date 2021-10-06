import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MainRosterComponent } from './list/main-roster.component';
import { MainRosterDetailComponent } from './detail/main-roster-detail.component';
import { MainRosterUpdateComponent } from './update/main-roster-update.component';
import { MainRosterDeleteDialogComponent } from './delete/main-roster-delete-dialog.component';
import { MainRosterRoutingModule } from './route/main-roster-routing.module';

@NgModule({
  imports: [SharedModule, MainRosterRoutingModule],
  declarations: [MainRosterComponent, MainRosterDetailComponent, MainRosterUpdateComponent, MainRosterDeleteDialogComponent],
  entryComponents: [MainRosterDeleteDialogComponent],
})
export class MainRosterModule {}
