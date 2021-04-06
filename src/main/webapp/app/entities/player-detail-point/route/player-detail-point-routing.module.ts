import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlayerDetailPointComponent } from '../list/player-detail-point.component';
import { PlayerDetailPointDetailComponent } from '../detail/player-detail-point-detail.component';
import { PlayerDetailPointUpdateComponent } from '../update/player-detail-point-update.component';
import { PlayerDetailPointRoutingResolveService } from './player-detail-point-routing-resolve.service';

const playerDetailPointRoute: Routes = [
  {
    path: '',
    component: PlayerDetailPointComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlayerDetailPointDetailComponent,
    resolve: {
      playerDetailPoint: PlayerDetailPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlayerDetailPointUpdateComponent,
    resolve: {
      playerDetailPoint: PlayerDetailPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlayerDetailPointUpdateComponent,
    resolve: {
      playerDetailPoint: PlayerDetailPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(playerDetailPointRoute)],
  exports: [RouterModule],
})
export class PlayerDetailPointRoutingModule {}
