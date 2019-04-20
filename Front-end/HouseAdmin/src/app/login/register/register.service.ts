import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BackendRequest, HttpMethod } from '../../BackendCommunication/BackendRequest';
import { EmptyResponse } from '../../persistence/EmptyResponse';
import { ResponseParser } from '../../BackendCommunication/ResponseParser';
import { JSONWrapper } from '../../BackendCommunication/JSONConvertable';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  private readonly targetAPI = "Register.json";

  constructor(private http: HttpClient) { }

  register(name: string, pw: string, email: string,completion: () => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.post, new ResponseParser(EmptyResponse));
    request.targetAPI = this.targetAPI;
    request.body = new JSONWrapper({
      username: name,
      password: pw,
      email: email
    })
    request.performRequest(() => {
      completion()
    }, errorHandler);
  }
}
