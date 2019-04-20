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
    var request = new BackendRequest(this.http, HttpMethod.post, new ResponseParser(Group));
    request.targetAPI = this.targetAPI;
    request.body = new JSONWrapper({
      Name: name,
      Description: description,
    })

    request.performRequest(completion, errorHandler)
  }

  getAllUserGroups(completion: (response: Group[]) => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.get, new ArrayResponseParser(Group));
    request.targetAPI = this.targetAPI;
    request.setQueryParameter("options", "1");
    
    request.performRequest(completion, errorHandler)
  }

  deleteGroup(id: string, completion: () => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.delete, new ResponseParser(EmptyResponse));
    request.targetAPI = this.targetAPI;
    request.setQueryParameter("ID", id);

    request.performRequest(() => {
      completion()
    }, errorHandler)
  }

  getGroup(id: string, completion: (group: Group) => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.get, new ResponseParser(Group));
    request.targetAPI = this.targetAPI;
    request.setQueryParameter("ID", id);

    request.performRequest(completion, errorHandler);
  }

  updateGroup(group: Group, completion: (group: Group) => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.patch, new ResponseParser(Group));
    request.targetAPI = this.targetAPI;
    request.body = group

    request.performRequest(completion, errorHandler);
  }

  addGroupMember(email: string, groupId: string, completion: () => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.post, new ResponseParser(EmptyResponse));
    request.targetAPI = this.managementAPI;
    request.body = new JSONWrapper({
      Email: email,
      ID: groupId
    })

    request.performRequest((response: {}) => {
      completion()
    }, errorHandler);
  }

  deleteGroupMember(email: string, id: string, completion: () => void, errorHandler: (error: any) => void) {
    var request = new BackendRequest(this.http, HttpMethod.delete, new ResponseParser(EmptyResponse));
    request.setQueryParameter("ID", id);
    request.setQueryParameter("Email", email);
    request.targetAPI = this.managementAPI

    request.performRequest((response: {}) => {
      completion()
    }, errorHandler);
  }
}
