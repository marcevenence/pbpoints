import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EventCategoryComponent } from '../list/event-category.component';
import { EventCategoryDetailComponent } from '../detail/event-category-detail.component';
import { EventCategoryUpdateComponent } from '../update/event-category-update.component';
import { EventCategoryRoutingResolveService } from './event-category-routing-resolve.service';

const eventCategoryRoute: Routes = [
  {
    path: '',
    component: EventCategoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EventCategoryDetailComponent,
    resolve: {
      eventCategory: EventCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EventCategoryUpdateComponent,
    resolve: {
      eventCategory: EventCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EventCategoryUpdateComponent,
    resolve: {
      eventCategory: EventCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eventCategoryRoute)],
  exports: [RouterModule],
})
export class EventCategoryRoutingModule {}
