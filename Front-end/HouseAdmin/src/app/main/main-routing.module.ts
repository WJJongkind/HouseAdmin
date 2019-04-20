import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { MainComponent } from "./main.component";
import { OverviewComponent } from "./overview/overview.component";
import { RegisterExpensesComponent } from "./register/register-expenses/register.component";
import { RegisterIncomeComponent } from "./register/register-income/register.component";
import { GroupsComponent } from "../groups/groups.component";


const routes: Routes = [
    { path: "main", component: MainComponent },
    { path: "groups", component: GroupsComponent },
    { path: "registerexpenses/:id", component: RegisterExpensesComponent, outlet:  "mainrouter"},
    { path: "registerexpenses", component: RegisterExpensesComponent, outlet:  "mainrouter"},
    { path: "registerincome/:id", component: RegisterIncomeComponent, outlet:  "mainrouter"},
    { path: "registerincome", component: RegisterIncomeComponent, outlet:  "mainrouter"},
    { path: "overview", component: OverviewComponent, outlet: "mainrouter"}
];

@NgModule({
    exports: [RouterModule],
    imports: [RouterModule.forChild(routes)]
})
export class MainRoutingModule { }
export const routingComponents = [
    MainComponent
]