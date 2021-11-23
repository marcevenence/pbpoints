import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TournamentGroupComponent } from '../list/tournament-group.component';
import { TournamentGroupDetailComponent } from '../detail/tournament-group-detail.component';
import { TournamentGroupUpdateComponent } from '../update/tournament-group-update.component';
import { TournamentGroupRoutingResolveService } from './tournament-group-routing-resolve.service';

const tournamentGroupRoute: Routes = [
  {
    path: '',
    component: TournamentGroupComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TournamentGroupDetailComponent,
    resolve: {
      tournamentGroup: TournamentGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TournamentGroupUpdateComponent,
    resolve: {
      tournamentGroup: TournamentGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TournamentGroupUpdateComponent,
    resolve: {
      tournamentGroup: TournamentGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tournamentGroupRoute)],
  exports: [RouterModule],
})
export class TournamentGroupRoutingModule {}
