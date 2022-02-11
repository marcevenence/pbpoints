import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlayerPointHistoryComponent } from '../list/player-point-history.component';
import { PlayerPointHistoryDetailComponent } from '../detail/player-point-history-detail.component';
import { PlayerPointHistoryUpdateComponent } from '../update/player-point-history-update.component';
import { PlayerPointHistoryRoutingResolveService } from './player-point-history-routing-resolve.service';

const playerPointHistoryRoute: Routes = [
  {
    path: '',
    component: PlayerPointHistoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlayerPointHistoryDetailComponent,
    resolve: {
      playerPointHistory: PlayerPointHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlayerPointHistoryUpdateComponent,
    resolve: {
      playerPointHistory: PlayerPointHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlayerPointHistoryUpdateComponent,
    resolve: {
      playerPointHistory: PlayerPointHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(playerPointHistoryRoute)],
  exports: [RouterModule],
})
export class PlayerPointHistoryRoutingModule {}
