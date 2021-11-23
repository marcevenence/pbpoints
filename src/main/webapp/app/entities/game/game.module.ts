import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { GameComponent } from './list/game.component';
import { GameDetailComponent } from './detail/game-detail.component';
import { GameUpdateComponent } from './update/game-update.component';
import { GameResultComponent } from './result/game-result.component';
import { GameDeleteDialogComponent } from './delete/game-delete-dialog.component';
import { GameRoutingModule } from './route/game-routing.module';

@NgModule({
  imports: [SharedModule, GameRoutingModule],
  declarations: [GameComponent, GameDetailComponent, GameUpdateComponent, GameDeleteDialogComponent, GameResultComponent],
  entryComponents: [GameDeleteDialogComponent],
})
export class GameModule {}
