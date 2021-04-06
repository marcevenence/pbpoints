import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormatComponent } from '../list/format.component';
import { FormatDetailComponent } from '../detail/format-detail.component';
import { FormatUpdateComponent } from '../update/format-update.component';
import { FormatRoutingResolveService } from './format-routing-resolve.service';

const formatRoute: Routes = [
  {
    path: '',
    component: FormatComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormatDetailComponent,
    resolve: {
      format: FormatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormatUpdateComponent,
    resolve: {
      format: FormatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormatUpdateComponent,
    resolve: {
      format: FormatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formatRoute)],
  exports: [RouterModule],
})
export class FormatRoutingModule {}
