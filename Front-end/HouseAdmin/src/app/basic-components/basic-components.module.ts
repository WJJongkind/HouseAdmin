import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FinanceTableComponent } from './finance-table/finance-table.component';
import { RouterModule } from "@angular/router";
import { FormsModule } from '@angular/forms';

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
