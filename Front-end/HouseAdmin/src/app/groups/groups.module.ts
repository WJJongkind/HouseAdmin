import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GroupsComponent } from './groups.component';
import { GrouptableComponent } from './grouptable/grouptable.component';
import { CreateComponent } from './create/create.component';
import { RouterModule } from '@angular/router';
import { EditComponent } from './edit/edit.component';
import { ManagementComponent } from './management/management.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule
  ],
  exports: [
    GroupsComponent,
    GrouptableComponent
  ],
  declarations: [
    GroupsComponent, 
    GrouptableComponent, 
    CreateComponent, 
    EditComponent, ManagementComponent
  ]
})
export class GroupsModule { }
