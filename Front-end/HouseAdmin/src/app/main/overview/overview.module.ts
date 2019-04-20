import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OverviewComponent } from './overview.component';
import { BasicComponentsModule } from '../../basic-components/basic-components.module';
import { OverviewTableComponent } from './overview-table/overview-table.component';

@NgModule({
  imports: [
    CommonModule,
    BasicComponentsModule
  ],
  exports: [
    OverviewComponent,
    OverviewTableComponent
  ],
  declarations: [
    OverviewComponent,
    OverviewTableComponent
  ]
})
export class OverviewModule { }
