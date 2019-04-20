import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainComponent } from './main.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { MainRoutingModule } from './main-routing.module';
import { BasicComponentsModule } from '../basic-components/basic-components.module';
import { OverviewModule } from './overview/overview.module';
import { CookieService } from 'ngx-cookie-service';
import { RegisterExpensesModule } from './register/register-expenses/register-expenses.module';
import { RegisterIncomeModule } from './register/register-income/register-income.module';

@NgModule({
  imports: [
    CommonModule,
    BasicComponentsModule,
    MainRoutingModule,
    OverviewModule,
    RegisterExpensesModule,
    RegisterIncomeModule
  ],
  declarations: [
    MainComponent, 
    SidebarComponent
  ],
  exports: [
    MainComponent,
    SidebarComponent
  ],
  providers: [
    CookieService
  ]
})
export class MainModule { }
