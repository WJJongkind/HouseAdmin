import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavpointComponent } from './navpoint/navpoint.component';
import { FinanceTableComponent } from './finance-table/finance-table.component';
import { RouterModule, Routes } from "@angular/router";
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    FormsModule
  ],
  declarations: [FinanceTableComponent, NavpointComponent],
  exports: [
    NavpointComponent,
    FinanceTableComponent
  ]
})
export class BasicComponentsModule { }
