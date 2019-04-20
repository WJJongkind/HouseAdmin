import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BasicComponentsModule } from '../../../basic-components/basic-components.module';
import { RegisterIncomeComponent } from './register.component';

@NgModule({
  imports: [
    CommonModule,
    BasicComponentsModule
  ],
  declarations: [RegisterIncomeComponent]
})
export class RegisterIncomeModule { }
