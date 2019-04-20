import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './login/register/register.component';
import { GroupsComponent } from './groups/groups.component';
import { CreateComponent } from './groups/create/create.component';
import { EditComponent } from './groups/edit/edit.component';
import { ManagementComponent } from './groups/management/management.component';

const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'register', component: RegisterComponent},
  { path: 'groups', component: GroupsComponent},
  { path: 'creategroup', component: CreateComponent},
  { path: 'editgroup/:id', component: EditComponent},
  { path: 'managegroup/:id', component: ManagementComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }