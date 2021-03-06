import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { RosterComponent } from './list/roster.component';
import { RosterDetailComponent } from './detail/roster-detail.component';
import { RosterSearchComponent } from './search/roster-search.component';
import { RosterSubsComponent } from './subs/roster-subs.component';
import { RosterUpdateComponent } from './update/roster-update.component';
import { RosterDeleteDialogComponent } from './delete/roster-delete-dialog.component';
import { RosterRoutingModule } from './route/roster-routing.module';
import { PlayerFilterPipe } from 'app/shared/player-filter.pipe';

@NgModule({
  imports: [SharedModule, RosterRoutingModule],
  declarations: [
    RosterComponent,
    RosterDetailComponent,
    RosterSearchComponent,
    RosterSubsComponent,
    RosterUpdateComponent,
    RosterDeleteDialogComponent,
    PlayerFilterPipe,
  ],
  entryComponents: [RosterDeleteDialogComponent],
})
export class RosterModule {}
