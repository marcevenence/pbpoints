import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { RosterComponent } from './list/roster.component';
import { RosterDetailComponent } from './detail/roster-detail.component';
import { RosterSearchComponent } from './search/roster-search.component';
import { RosterUpdateComponent } from './update/roster-update.component';
import { RosterDeleteDialogComponent } from './delete/roster-delete-dialog.component';
import { RosterRoutingModule } from './route/roster-routing.module';

@NgModule({
  imports: [SharedModule, RosterRoutingModule],
  declarations: [RosterComponent, RosterDetailComponent, RosterSearchComponent, RosterUpdateComponent, RosterDeleteDialogComponent],
  entryComponents: [RosterDeleteDialogComponent],
})
export class RosterModule {}
