import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ObjectToTypescriptParser } from "./ResponseParser";
import { JSONConvertable } from "./JSONConvertable";

/**
 * Supported methods for performing HTTP requests.
 */
export enum HttpMethod {
  get,
  post,
  patch,
  delete
}

/**
 * Objects of this class represent backend requests that can be performed. It offers high-level functionality for communicating with the backend server. 
 */
export class BackendRequest<ResponseType> {

  // MARK: - Constants

  private readonly $headers = "headers";

  // MARK: - Private properties

  private _options: { withCredentials: boolean; headers: { [string: string]: string } };
  private _body: JSONConvertable = null;
  private _queryParameters: { [string: string]: string } = {};
  private static readonly BASE_URL = "http://192.168.178.69:6969/CowLiteCommunicationServer/HouseAdmin/"

  // MARK: - Object lifecycle

  constructor(private httpProvider: HttpClient, private _method: HttpMethod, public targetAPI: string, private responseParser: ObjectToTypescriptParser<ResponseType>) {
    this._options = {
      withCredentials: true,
      headers: {}
    };
  }

  // MARK: - Getters & Setters

  public get headers(): { [string: string]: string } {
    return this._options[this.$headers];
  }

  public set headers(value: { [string: string]: string }) {
    this._options[this.$headers] = value;
  }

  public setHeader(key: string, value: string) {
    this.headers[key] = value;
  }

  public getHeader(key: string): string {
    return this.headers[key];
  }

  public get body(): any {
    return this._body;
  }

  public set body(value: any) {
    if (this.httpMethod == HttpMethod.post || this.httpMethod == HttpMethod.patch) {
      this._body = value;
    } else {
      throw Error("Attempted to set the body for a request type that doesnt allow a body. Found HTTP method was: " + this._method);
    }
  }

  public setQueryParameter(key: string, value: string) {
    this._queryParameters[key] = value;
  }

  public getQueryParameter(key: string): string {
    return this._queryParameters[key];
  }

  public get httpMethod(): HttpMethod {
    return this._method;
  }

  public set httpMethod(value: HttpMethod) {
    this._method = value

    switch (value) {
      case HttpMethod.delete, HttpMethod.get: {
        this.body = null
      }
    }
  }

  // MARK: - Performing requests

  /**
   * Performs the request with the options that have been set before calling this method. 
   * 
   * @param completion The closure which needs to be executed once the request is successful. If a parsed response object came back, this will be passed on to the completion handler.
   * @param errorHandler The error handler for when an erroneous response code is returned by the server or when the request fails.
   */
  public performRequest(completion: (response: ResponseType) => void, errorHandler: (error: any) => void): void {
    if (this.targetAPI == null) {
      throw Error("API has not been set for backend request");
    }
    if (this._method == null) {
      throw Error("HTTP method has not been set for backend request");
    }
    if (this._options == null) {
      throw Error("HTTP options are null for backend request");
    }

    let observable = this.sendRequest();
    let subscription = observable.subscribe((response: ResponseType) => {
      completion(this.responseParser.parse(response));
      subscription.unsubscribe();
    }, (error: any) => {
      errorHandler(error);
      subscription.unsubscribe();
    });
  }

  private sendRequest(): Observable<any> {
    var apiURL = BackendRequest.BASE_URL + this.targetAPI

    var query = this.createQuery();
    if (query.length > 1) {
      apiURL += "?" + query;
    }

    switch (this.httpMethod) {
      case HttpMethod.patch: {
        return this.httpProvider.put(apiURL, this.body.json, this._options);
      }
      case HttpMethod.post: {
        return this.httpProvider.post(apiURL, this.body.json, this._options);
      }
      case HttpMethod.delete: {
        return this.httpProvider.delete(apiURL, this._options);
      }
      case HttpMethod.get: {
        return this.httpProvider.get(apiURL, this._options);
      }
    }
    throw Error("Unknown HTTP method was specified."); // Should theoretically never be possible?
  }

  private createQuery(): string {
    var query = "";
    for (let key of Object.keys(this._queryParameters)) {
      if (query.length > 0) {
        query += "&";
      }
      query += key + "=" + this._queryParameters[key];
    }

    return query;
  }
}