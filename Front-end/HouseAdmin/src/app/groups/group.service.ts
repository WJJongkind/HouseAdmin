import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CFM } from '../common/common-functions.module';
import { BackendRequest, HttpMethod } from '../BackendCommunication/BackendRequest';
import { Group } from '../persistence/Group';
import { EmptyResponse } from '../persistence/EmptyResponse';
import { ResponseParser, ArrayResponseParser } from '../BackendCommunication/ResponseParser';
import { JSONWrapper } from '../BackendCommunication/JSONConvertable';

@Injectable({
  providedIn: 'root'
})
export class GroupService {
  private readonly targetAPI = "groups.json";
  private readonly managementAPI = "groupmanagement.json";

  constructor(private http: HttpClient, private funcs: CFM) { }

  createGroup(name: string, description: string, completion: (response: Group) => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.post, this.targetAPI, new ResponseParser(Group));
    request.body = new JSONWrapper({
      Name: name,
      Description: description,
    })

    request.performRequest(completion, errorHandler)
  }

  getAllUserGroups(completion: (response: Group[]) => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.get, this.targetAPI, new ArrayResponseParser(Group));
    request.setQueryParameter("options", "1");
    
    request.performRequest(completion, errorHandler)
  }

  deleteGroup(id: string, completion: () => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.delete, this.targetAPI, new ResponseParser(EmptyResponse));
    request.setQueryParameter("ID", id);

    request.performRequest(() => {
      completion()
    }, errorHandler)
  }

  getGroup(id: string, completion: (group: Group) => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.get, this.targetAPI, new ResponseParser(Group));
    request.setQueryParameter("ID", id);

    request.performRequest(completion, errorHandler);
  }

  updateGroup(group: Group, completion: (group: Group) => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.patch, this.targetAPI, new ResponseParser(Group));
    request.body = group

    request.performRequest(completion, errorHandler);
  }

  addGroupMember(email: string, groupId: string, completion: () => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.post, this.managementAPI, new ResponseParser(EmptyResponse));
    request.body = new JSONWrapper({
      Email: email,
      ID: groupId
    })

    request.performRequest((response: {}) => {
      completion()
    }, errorHandler);
  }

  deleteGroupMember(email: string, id: string, completion: () => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.delete, this.managementAPI, new ResponseParser(EmptyResponse));
    request.setQueryParameter("ID", id);
    request.setQueryParameter("Email", email);

    request.performRequest((response: {}) => {
      completion()
    }, errorHandler);
  }
}
