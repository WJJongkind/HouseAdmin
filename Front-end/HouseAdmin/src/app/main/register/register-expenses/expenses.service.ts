import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { BackendRequest, HttpMethod } from '../../../BackendCommunication/BackendRequest';
import { Expense } from '../../../persistence/Expense';
import { EmptyResponse } from '../../../persistence/EmptyResponse';
import { ResponseParser, ArrayResponseParser } from '../../../BackendCommunication/ResponseParser';

@Injectable({
  providedIn: 'root'
})
export class ExpensesService {

  private readonly targetAPI = "Expenses.json";

  constructor(private http: HttpClient) { }

  obtainTransactions(groupID: string, completion: (expenses: Expense[]) => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.get, new ArrayResponseParser(Expense));
    request.targetAPI = this.targetAPI;
    request.setQueryParameter("ID", groupID);
    request.setQueryParameter("options", "1");

    request.performRequest(completion, errorHandler);
  }

  postExpense(expense: Expense, completion: (createdExpense: Expense) => void, errorHandler: (error: any) => void) {
    let method: HttpMethod
    if(expense.id == undefined) {
      method = HttpMethod.post
    } else {
      method = HttpMethod.patch
    }

    let request = new BackendRequest(this.http, method, new ResponseParser(Expense));
    request.targetAPI = this.targetAPI;
    request.body = expense;
    
    request.performRequest(completion, errorHandler);
  }

  deleteExpense(expense: Expense, completion: () => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.delete, new ResponseParser(EmptyResponse));
    request.targetAPI = this.targetAPI;
    request.setQueryParameter("ID", expense.id);
    
    request.performRequest((response: {}) => {
      completion()
    }, errorHandler);
  }
}
