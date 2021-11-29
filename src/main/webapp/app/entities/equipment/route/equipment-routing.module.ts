import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EquipmentComponent } from '../list/equipment.component';
import { EquipmentDetailComponent } from '../detail/equipment-detail.component';
import { EquipmentUpdateComponent } from '../update/equipment-update.component';
import { EquipmentRoutingResolveService } from './equipment-routing-resolve.service';

const equipmentRoute: Routes = [
  {
    path: '',
    component: EquipmentComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EquipmentDetailComponent,
    resolve: {
      equipment: EquipmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EquipmentUpdateComponent,
    resolve: {
      equipment: EquipmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EquipmentUpdateComponent,
    resolve: {
      equipment: EquipmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(equipmentRoute)],
  exports: [RouterModule],
})
export class EquipmentRoutingModule {}
