import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PlayerPointHistoryComponent } from './list/player-point-history.component';
import { PlayerPointHistoryDetailComponent } from './detail/player-point-history-detail.component';
import { PlayerPointHistoryUpdateComponent } from './update/player-point-history-update.component';
import { PlayerPointHistoryDeleteDialogComponent } from './delete/player-point-history-delete-dialog.component';
import { PlayerPointHistoryRoutingModule } from './route/player-point-history-routing.module';

@NgModule({
  imports: [SharedModule, PlayerPointHistoryRoutingModule],
  declarations: [
    PlayerPointHistoryComponent,
    PlayerPointHistoryDetailComponent,
    PlayerPointHistoryUpdateComponent,
    PlayerPointHistoryDeleteDialogComponent,
  ],
  entryComponents: [PlayerPointHistoryDeleteDialogComponent],
})
export class PlayerPointHistoryModule {}
