import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SponsorComponent } from './list/sponsor.component';
import { SponsorDetailComponent } from './detail/sponsor-detail.component';
import { SponsorUpdateComponent } from './update/sponsor-update.component';
import { SponsorDeleteDialogComponent } from './delete/sponsor-delete-dialog.component';
import { SponsorRoutingModule } from './route/sponsor-routing.module';

@NgModule({
  imports: [SharedModule, SponsorRoutingModule],
  declarations: [SponsorComponent, SponsorDetailComponent, SponsorUpdateComponent, SponsorDeleteDialogComponent],
  entryComponents: [SponsorDeleteDialogComponent],
})
export class SponsorModule {}
