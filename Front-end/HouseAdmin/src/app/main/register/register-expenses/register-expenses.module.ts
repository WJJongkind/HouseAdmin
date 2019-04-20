import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BasicComponentsModule } from '../../../basic-components/basic-components.module';
import { RegisterExpensesComponent } from './register.component';

@NgModule({
  imports: [
    CommonModule,
    BasicComponentsModule
  ],
  declarations: [RegisterExpensesComponent]
})
export class RegisterExpensesModule { }
