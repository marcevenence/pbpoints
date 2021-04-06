import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TeamDetailPointComponent } from './list/team-detail-point.component';
import { TeamDetailPointDetailComponent } from './detail/team-detail-point-detail.component';
import { TeamDetailPointUpdateComponent } from './update/team-detail-point-update.component';
import { TeamDetailPointDeleteDialogComponent } from './delete/team-detail-point-delete-dialog.component';
import { TeamDetailPointRoutingModule } from './route/team-detail-point-routing.module';

@NgModule({
  imports: [SharedModule, TeamDetailPointRoutingModule],
  declarations: [
    TeamDetailPointComponent,
    TeamDetailPointDetailComponent,
    TeamDetailPointUpdateComponent,
    TeamDetailPointDeleteDialogComponent,
  ],
  entryComponents: [TeamDetailPointDeleteDialogComponent],
})
export class TeamDetailPointModule {}
