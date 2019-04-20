import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ResponseParser, Parser } from "./ResponseParser";
import { JSONConvertable } from "./JSONConvertable";

export enum HttpMethod {
  get,
  post,
  patch,
  delete
}

export class BackendRequest<ResponseType> {
  private readonly $headers = "headers";
  private _options: { withCredentials: boolean; headers: { [string: string]: string } };
  private _body: JSONConvertable;
  private _targetAPI: string;
  private _queryParameters: { [string: string]: string };
  private static readonly BASE_URL = "http://localhost:8084/CowLiteCommunicationServer/HouseAdmin/"

  constructor(private httpProvider: HttpClient, private _method: HttpMethod, private responseParser: Parser<ResponseType>) {
    this._options = {
      withCredentials: true,
      headers: {}
    };
    this._queryParameters = {};
    this._body = null;
    this._targetAPI = null;
  }

  /**
   * Getter headers
   * @return {{[string:string]: string}}
   */
  public get headers(): { [string: string]: string } {
    return this._options[this.$headers];
  }

  /**
   * Setter headers
   * @param {{[string:string]: string} value
   */
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

  /**
   * Getter targetAPI
   * @return {string}
   */
  public get targetAPI(): string {
    return this._targetAPI;
  }

  /**
   * Setter targetAPI
   * @param {string} value
   */
  public set targetAPI(value: string) {
    this._targetAPI = value;
  }

  /**
  * Getter queryParameters
  * @return {{[string:string]: string}}
  */
  public get queryParameters(): { [string: string]: string } {
    return this._queryParameters;
  }

  /**
   * Setter queryParameters
   * @param {{[string:string]: string}} value
   */
  public set queryParameters(value: { [key: string]: string }) {
    for (let key in value) {
      value[key] = this.filter(value[key]);
    }

    this._queryParameters = value;
  }

  public setQueryParameter(key: string, value: string) {
    this.queryParameters[key] = this.filter(value);
  }

  public getQueryParameter(key: string): string {
    return this.queryParameters[key];
  }

  /**
  * Getter httpMethod
  * @return {HttpMethod}
  */
  public get httpMethod(): HttpMethod {
    return this._method;
  }

  /**
   * Setter httpMethod
   * @param {HttpMethod} value
   */
  public set httpMethod(value: HttpMethod) {
    this._method = value

    switch (value) {
      case HttpMethod.delete, HttpMethod.get: {
        this.body = null
      }
      case HttpMethod.get, HttpMethod.patch: {
        if (this.body == null) {
          this.body = null
        }
      }
    }
  }

  private filter(value: string | String): string {
    return value.replace(/\\/g, "\\\\");
  }

  public performRequest(responseCallback: (response: ResponseType) => void, errorCallback: (error: any) => void): void {
    if (this.targetAPI == null) {
      throw Error("API has not been set for backend request");
    }
    if (this._method == null) {
      throw Error("HTTP method has not been set for backend request");
    }
    if (this._options == null) {
      throw Error("HTTP options are null for backend request");
    }

    let observable = this.talkToBackend();
      let subscription = observable.subscribe((response: ResponseType) => {
        responseCallback(this.responseParser.parse(response));
        subscription.unsubscribe();
      }, (error: any) => {
        errorCallback(error);
        subscription.unsubscribe();
      });
  }

  private talkToBackend(): Observable<any> {
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
    for (let key of Object.keys(this.queryParameters)) {
      if (query.length > 0) {
        query += "&";
      }
      query += key + "=" + this.queryParameters[key];
    }

    return query;
  }
}