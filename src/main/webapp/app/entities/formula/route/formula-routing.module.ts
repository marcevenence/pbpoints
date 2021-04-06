import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormulaComponent } from '../list/formula.component';
import { FormulaDetailComponent } from '../detail/formula-detail.component';
import { FormulaUpdateComponent } from '../update/formula-update.component';
import { FormulaRoutingResolveService } from './formula-routing-resolve.service';

const formulaRoute: Routes = [
  {
    path: '',
    component: FormulaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormulaDetailComponent,
    resolve: {
      formula: FormulaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormulaUpdateComponent,
    resolve: {
      formula: FormulaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormulaUpdateComponent,
    resolve: {
      formula: FormulaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formulaRoute)],
  exports: [RouterModule],
})
export class FormulaRoutingModule {}
