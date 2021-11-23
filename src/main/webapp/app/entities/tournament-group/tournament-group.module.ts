import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TournamentGroupComponent } from './list/tournament-group.component';
import { TournamentGroupDetailComponent } from './detail/tournament-group-detail.component';
import { TournamentGroupUpdateComponent } from './update/tournament-group-update.component';
import { TournamentGroupDeleteDialogComponent } from './delete/tournament-group-delete-dialog.component';
import { TournamentGroupRoutingModule } from './route/tournament-group-routing.module';

@NgModule({
  imports: [SharedModule, TournamentGroupRoutingModule],
  declarations: [
    TournamentGroupComponent,
    TournamentGroupDetailComponent,
    TournamentGroupUpdateComponent,
    TournamentGroupDeleteDialogComponent,
  ],
  entryComponents: [TournamentGroupDeleteDialogComponent],
})
export class TournamentGroupModule {}
