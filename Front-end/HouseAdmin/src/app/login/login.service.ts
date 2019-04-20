import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BackendRequest, HttpMethod } from '../BackendCommunication/BackendRequest';
import { LoginResponse } from '../persistence/LoginResponse';
import { ResponseParser } from '../BackendCommunication/ResponseParser';
import { JSONWrapper } from '../BackendCommunication/JSONConvertable';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private readonly targetAPI = "Login.json"

  constructor(private http: HttpClient) { }

  login(name: string, pw: string, completion: (response: LoginResponse) => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.post, this.targetAPI, new ResponseParser(LoginResponse));
    request.body = new JSONWrapper({
      username: name,
      password: pw
    })
    
    request.performRequest(completion, errorHandler)
  }
}