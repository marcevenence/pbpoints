import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MainRosterComponent } from '../list/main-roster.component';
import { MainRosterDetailComponent } from '../detail/main-roster-detail.component';
import { MainRosterUpdateComponent } from '../update/main-roster-update.component';
import { MainRosterRoutingResolveService } from './main-roster-routing-resolve.service';

const mainRosterRoute: Routes = [
  {
    path: '',
    component: MainRosterComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MainRosterDetailComponent,
    resolve: {
      mainRoster: MainRosterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MainRosterUpdateComponent,
    resolve: {
      mainRoster: MainRosterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MainRosterUpdateComponent,
    resolve: {
      mainRoster: MainRosterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mainRosterRoute)],
  exports: [RouterModule],
})
export class MainRosterRoutingModule {}
