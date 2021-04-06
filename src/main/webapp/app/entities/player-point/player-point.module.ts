import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PlayerPointComponent } from './list/player-point.component';
import { PlayerPointDetailComponent } from './detail/player-point-detail.component';
import { PlayerPointUpdateComponent } from './update/player-point-update.component';
import { PlayerPointDeleteDialogComponent } from './delete/player-point-delete-dialog.component';
import { PlayerPointRoutingModule } from './route/player-point-routing.module';

@NgModule({
  imports: [SharedModule, PlayerPointRoutingModule],
  declarations: [PlayerPointComponent, PlayerPointDetailComponent, PlayerPointUpdateComponent, PlayerPointDeleteDialogComponent],
  entryComponents: [PlayerPointDeleteDialogComponent],
})
export class PlayerPointModule {}
