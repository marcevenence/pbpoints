import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RosterComponent } from '../list/roster.component';
import { RosterDetailComponent } from '../detail/roster-detail.component';
import { RosterUpdateComponent } from '../update/roster-update.component';
import { RosterRoutingResolveService } from './roster-routing-resolve.service';
import { RosterSearchComponent } from '../search/roster-search.component';

const rosterRoute: Routes = [
  {
    path: '',
    component: RosterComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RosterDetailComponent,
    resolve: {
      roster: RosterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RosterUpdateComponent,
    resolve: {
      roster: RosterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RosterUpdateComponent,
    resolve: {
      roster: RosterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'search',
    component: RosterSearchComponent,
    resolve: {
      roster: RosterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rosterRoute)],
  exports: [RouterModule],
})
export class RosterRoutingModule {}
