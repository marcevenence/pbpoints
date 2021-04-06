import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BracketComponent } from '../list/bracket.component';
import { BracketDetailComponent } from '../detail/bracket-detail.component';
import { BracketUpdateComponent } from '../update/bracket-update.component';
import { BracketRoutingResolveService } from './bracket-routing-resolve.service';

const bracketRoute: Routes = [
  {
    path: '',
    component: BracketComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BracketDetailComponent,
    resolve: {
      bracket: BracketRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BracketUpdateComponent,
    resolve: {
      bracket: BracketRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BracketUpdateComponent,
    resolve: {
      bracket: BracketRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bracketRoute)],
  exports: [RouterModule],
})
export class BracketRoutingModule {}
