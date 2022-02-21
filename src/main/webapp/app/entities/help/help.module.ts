import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { HelpComponent } from './list/help.component';
import { HelpRoutingModule } from './route/help-routing.module';

@NgModule({
  imports: [SharedModule, HelpRoutingModule],
  declarations: [HelpComponent],
})
export class HelpModule {}
