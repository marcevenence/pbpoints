import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocTypeComponent } from '../list/doc-type.component';
import { DocTypeDetailComponent } from '../detail/doc-type-detail.component';
import { DocTypeUpdateComponent } from '../update/doc-type-update.component';
import { DocTypeRoutingResolveService } from './doc-type-routing-resolve.service';

const docTypeRoute: Routes = [
  {
    path: '',
    component: DocTypeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocTypeDetailComponent,
    resolve: {
      docType: DocTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocTypeUpdateComponent,
    resolve: {
      docType: DocTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocTypeUpdateComponent,
    resolve: {
      docType: DocTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(docTypeRoute)],
  exports: [RouterModule],
})
export class DocTypeRoutingModule {}
