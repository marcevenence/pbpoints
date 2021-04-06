import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PlayerDetailPointComponent } from './list/player-detail-point.component';
import { PlayerDetailPointDetailComponent } from './detail/player-detail-point-detail.component';
import { PlayerDetailPointUpdateComponent } from './update/player-detail-point-update.component';
import { PlayerDetailPointDeleteDialogComponent } from './delete/player-detail-point-delete-dialog.component';
import { PlayerDetailPointRoutingModule } from './route/player-detail-point-routing.module';

@NgModule({
  imports: [SharedModule, PlayerDetailPointRoutingModule],
  declarations: [
    PlayerDetailPointComponent,
    PlayerDetailPointDetailComponent,
    PlayerDetailPointUpdateComponent,
    PlayerDetailPointDeleteDialogComponent,
  ],
  entryComponents: [PlayerDetailPointDeleteDialogComponent],
})
export class PlayerDetailPointModule {}
