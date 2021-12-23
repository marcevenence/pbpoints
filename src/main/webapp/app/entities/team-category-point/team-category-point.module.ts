import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TeamCategoryPointComponent } from './list/team-category-point.component';
import { TeamCategoryPointDetailComponent } from './detail/team-category-point-detail.component';
import { TeamCategoryPointUpdateComponent } from './update/team-category-point-update.component';
import { TeamCategoryPointDeleteDialogComponent } from './delete/team-category-point-delete-dialog.component';
import { TeamCategoryPointRoutingModule } from './route/team-category-point-routing.module';

@NgModule({
  imports: [SharedModule, TeamCategoryPointRoutingModule],
  declarations: [
    TeamCategoryPointComponent,
    TeamCategoryPointDetailComponent,
    TeamCategoryPointUpdateComponent,
    TeamCategoryPointDeleteDialogComponent,
  ],
  entryComponents: [TeamCategoryPointDeleteDialogComponent],
})
export class TeamCategoryPointModule {}
