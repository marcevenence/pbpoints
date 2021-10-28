import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SponsorComponent } from '../list/sponsor.component';
import { SponsorDetailComponent } from '../detail/sponsor-detail.component';
import { SponsorUpdateComponent } from '../update/sponsor-update.component';
import { SponsorRoutingResolveService } from './sponsor-routing-resolve.service';

const sponsorRoute: Routes = [
  {
    path: '',
    component: SponsorComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SponsorDetailComponent,
    resolve: {
      sponsor: SponsorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SponsorUpdateComponent,
    resolve: {
      sponsor: SponsorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SponsorUpdateComponent,
    resolve: {
      sponsor: SponsorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sponsorRoute)],
  exports: [RouterModule],
})
export class SponsorRoutingModule {}
