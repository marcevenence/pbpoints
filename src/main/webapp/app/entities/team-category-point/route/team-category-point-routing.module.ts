import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TeamCategoryPointComponent } from '../list/team-category-point.component';
import { TeamCategoryPointDetailComponent } from '../detail/team-category-point-detail.component';
import { TeamCategoryPointUpdateComponent } from '../update/team-category-point-update.component';
import { TeamCategoryPointRoutingResolveService } from './team-category-point-routing-resolve.service';

const teamCategoryPointRoute: Routes = [
  {
    path: '',
    component: TeamCategoryPointComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TeamCategoryPointDetailComponent,
    resolve: {
      teamCategoryPoint: TeamCategoryPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TeamCategoryPointUpdateComponent,
    resolve: {
      teamCategoryPoint: TeamCategoryPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TeamCategoryPointUpdateComponent,
    resolve: {
      teamCategoryPoint: TeamCategoryPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(teamCategoryPointRoute)],
  exports: [RouterModule],
})
export class TeamCategoryPointRoutingModule {}
