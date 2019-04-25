import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FinanceTableComponent } from './finance-table/finance-table.component';
import { RouterModule } from "@angular/router";
import { FormsModule } from '@angular/forms';

/**
 * BasicComponentsModule contains all components that are reused in multiple places throughout the application. Components for various purposes are available here.
 */
@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    FormsModule
  ],
  declarations: [FinanceTableComponent],
  exports: [
    FinanceTableComponent
  ]
})
export class BasicComponentsModule { }
