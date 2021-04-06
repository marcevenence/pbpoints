import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TeamPointComponent } from './list/team-point.component';
import { TeamPointDetailComponent } from './detail/team-point-detail.component';
import { TeamPointUpdateComponent } from './update/team-point-update.component';
import { TeamPointDeleteDialogComponent } from './delete/team-point-delete-dialog.component';
import { TeamPointRoutingModule } from './route/team-point-routing.module';

@NgModule({
  imports: [SharedModule, TeamPointRoutingModule],
  declarations: [TeamPointComponent, TeamPointDetailComponent, TeamPointUpdateComponent, TeamPointDeleteDialogComponent],
  entryComponents: [TeamPointDeleteDialogComponent],
})
export class TeamPointModule {}
