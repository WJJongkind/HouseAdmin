import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { IncomeEntry } from '../../../persistence/Income';
import { BackendRequest, HttpMethod } from '../../../BackendCommunication/BackendRequest';
import { EmptyResponse } from '../../../persistence/EmptyResponse';
import { ResponseParser, ArrayResponseParser } from '../../../BackendCommunication/ResponseParser';

@Injectable({
  providedIn: 'root'
})
export class IncomeService {

  private readonly targetAPI = "Income.json";

  constructor(private http: HttpClient) { }

  obtainTransactions(groupID: string, completion: (response: IncomeEntry[]) => void, errorHandler: (error: any) => void) {    
    let request = new BackendRequest(this.http, HttpMethod.get, new ArrayResponseParser(IncomeEntry));
    request.targetAPI = this.targetAPI;
    request.setQueryParameter("ID", groupID);
    request.setQueryParameter("options", "1");

    request.performRequest(completion, errorHandler)
  }

  postIncome(income: IncomeEntry, completion: (response: IncomeEntry) => void, errorHandler: (error: any) => void) {
    let method: HttpMethod
    if(income.id == undefined) {
      method = HttpMethod.post
    } else {
      method = HttpMethod.patch
    }

    let request = new BackendRequest(this.http, method, new ResponseParser(IncomeEntry));
    request.targetAPI = this.targetAPI;
    request.body = income;
    
    request.performRequest(completion, errorHandler)
  }

  deleteIncome(income: IncomeEntry, completion: () => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.delete, new ResponseParser(EmptyResponse));
    request.targetAPI = this.targetAPI;
    request.setQueryParameter("ID", income.id);
    
    request.performRequest((response: {}) => {
      completion()
    }, errorHandler);
  }
}
