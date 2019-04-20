import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpMethod, BackendRequest } from '../BackendCommunication/BackendRequest';
import { CFM } from './common-functions.module';
import { Expense } from '../persistence/Expense';
import { EmptyResponse } from '../persistence/EmptyResponse';
import { ResponseParser } from '../BackendCommunication/ResponseParser';
import { JSONWrapper } from '../BackendCommunication/JSONConvertable';

@Injectable({
  providedIn: 'root'
})
export class LogoutService {

  private readonly targetAPI = "logout.json"

  constructor(private http: HttpClient) { }

  logout(sessionID: string, completion: () => void, errorHandler: (error: any) => void) {
    let request = new BackendRequest(this.http, HttpMethod.post, new ResponseParser(EmptyResponse));
    request.targetAPI = this.targetAPI;
    request.body = new JSONWrapper({sessionID: sessionID});
    
    request.performRequest((response: {}) => {
      completion()
    }, errorHandler);
  }
}
