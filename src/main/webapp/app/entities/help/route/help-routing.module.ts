import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HelpComponent } from '../list/help.component';

const helpRoute: Routes = [
  {
    path: '',
    component: HelpComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(helpRoute)],
  exports: [RouterModule],
})
export class HelpRoutingModule {}
