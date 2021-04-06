import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TeamPointComponent } from '../list/team-point.component';
import { TeamPointDetailComponent } from '../detail/team-point-detail.component';
import { TeamPointUpdateComponent } from '../update/team-point-update.component';
import { TeamPointRoutingResolveService } from './team-point-routing-resolve.service';

const teamPointRoute: Routes = [
  {
    path: '',
    component: TeamPointComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TeamPointDetailComponent,
    resolve: {
      teamPoint: TeamPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TeamPointUpdateComponent,
    resolve: {
      teamPoint: TeamPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TeamPointUpdateComponent,
    resolve: {
      teamPoint: TeamPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(teamPointRoute)],
  exports: [RouterModule],
})
export class TeamPointRoutingModule {}
