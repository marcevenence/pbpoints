import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SuspensionComponent } from '../list/suspension.component';
import { SuspensionDetailComponent } from '../detail/suspension-detail.component';
import { SuspensionUpdateComponent } from '../update/suspension-update.component';
import { SuspensionRoutingResolveService } from './suspension-routing-resolve.service';

const suspensionRoute: Routes = [
  {
    path: '',
    component: SuspensionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SuspensionDetailComponent,
    resolve: {
      suspension: SuspensionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SuspensionUpdateComponent,
    resolve: {
      suspension: SuspensionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SuspensionUpdateComponent,
    resolve: {
      suspension: SuspensionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(suspensionRoute)],
  exports: [RouterModule],
})
export class SuspensionRoutingModule {}
