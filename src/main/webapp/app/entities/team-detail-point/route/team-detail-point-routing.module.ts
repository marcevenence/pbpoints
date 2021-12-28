import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TeamDetailPointComponent } from '../list/team-detail-point.component';
import { TeamDetailPointDetailComponent } from '../detail/team-detail-point-detail.component';
import { TeamDetailPointUpdateComponent } from '../update/team-detail-point-update.component';
import { TeamDetailPointRoutingResolveService } from './team-detail-point-routing-resolve.service';

const teamDetailPointRoute: Routes = [
  {
    path: '',
    component: TeamDetailPointComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TeamDetailPointDetailComponent,
    resolve: {
      teamDetailPoint: TeamDetailPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TeamDetailPointUpdateComponent,
    resolve: {
      teamDetailPoint: TeamDetailPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TeamDetailPointUpdateComponent,
    resolve: {
      teamDetailPoint: TeamDetailPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(teamDetailPointRoute)],
  exports: [RouterModule],
})
export class TeamDetailPointRoutingModule {}
