import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlayerPointComponent } from '../list/player-point.component';
import { PlayerPointDetailComponent } from '../detail/player-point-detail.component';
import { PlayerPointUpdateComponent } from '../update/player-point-update.component';
import { PlayerPointRoutingResolveService } from './player-point-routing-resolve.service';

const playerPointRoute: Routes = [
  {
    path: '',
    component: PlayerPointComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlayerPointDetailComponent,
    resolve: {
      playerPoint: PlayerPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlayerPointUpdateComponent,
    resolve: {
      playerPoint: PlayerPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlayerPointUpdateComponent,
    resolve: {
      playerPoint: PlayerPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(playerPointRoute)],
  exports: [RouterModule],
})
export class PlayerPointRoutingModule {}
