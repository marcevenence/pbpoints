import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { EventCategoryComponent } from './list/event-category.component';
import { EventCategoryDetailComponent } from './detail/event-category-detail.component';
import { EventCategoryUpdateComponent } from './update/event-category-update.component';
import { EventCategoryDeleteDialogComponent } from './delete/event-category-delete-dialog.component';
import { EventCategoryRoutingModule } from './route/event-category-routing.module';

@NgModule({
  imports: [SharedModule, EventCategoryRoutingModule],
  declarations: [EventCategoryComponent, EventCategoryDetailComponent, EventCategoryUpdateComponent, EventCategoryDeleteDialogComponent],
  entryComponents: [EventCategoryDeleteDialogComponent],
})
export class EventCategoryModule {}
