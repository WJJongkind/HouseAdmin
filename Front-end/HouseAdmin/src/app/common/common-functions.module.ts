import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { LogoutService } from './logout.service';
import { CookieService } from 'ngx-cookie-service';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: []
})
@Injectable({
  providedIn: 'root'
})
export class CFM {
  constructor(private logoutService: LogoutService, private cookie: CookieService){}

  public static  getDate = function(daydiff=0, monthdiff=0, yeardiff=0): Date {
    var date = new Date();
    date.setDate(date.getDate() + daydiff);
    date.setFullYear(date.getFullYear() + yeardiff);
    date.setMonth(date.getMonth() + monthdiff);

    var year = date.getFullYear();
    var day = date.getDate();
    var month = date.getMonth() + 1;
  
    return new Date(year, month - 1, day);
  }

  public static makeDate(date: string): Date {
    var dateParts = date.split('-');
    var _date = new Date(+dateParts[0], +dateParts[1] - 1, +dateParts[2]); 
    return _date;
  }

  public static makeDateString(date: Date): string {
    var month = (date.getMonth() + 1) + "";
    var day = date.getDate() + "";

    month.length == 1 ? month = "0" + month : month = month
    day.length == 1 ? day = "0" + day : day = day

    return date.getFullYear() + "-" + month + "-" + day;
  }

  static getElementByIDValue(id) {
    return (<HTMLInputElement>document.getElementById(id)).value;
  }

  static getElementByID(id) {
    return (<HTMLInputElement>document.getElementById(id));
  }

  logout(router: Router) {
    this.logoutService.logout(this.getSessionID(), () => {
      // Do nothing
    }, (error: any) => void {
      // Yeah, well... If logging out doesn't go right then we cant really do much now can we?
    });
    this.deleteCookies()
    router.navigateByUrl('')
  }

  getCookie(name: string) {
    return this.cookie.get(name);
  }

  deleteCookie(name: string) {
    this.cookie.delete(name);
    this.cookie.delete(name, "/");
  }

  setCookie(name: string, value: string) {
    this.cookie.set(name, value,undefined,"/");
  }

  deleteCookies() {
    this.cookie.deleteAll();
    this.cookie.deleteAll("/");
  }

  getSessionID() {
    return this.getCookie("sessionID");
  }

  setActivatedGroup(id: string) {
    this.setCookie("groupID", id);
  }

  getActivatedGroup() {
    return this.getCookie("groupID");
  }

  public static deleteFromArray(array, obj) {
    let index = array.indexOf(obj);

    if(index > -1) {
      array.splice(index, 1);
    }

    return array;
  }

  public static findKeyValueInArray(array: {}[], key, value) {
    for(var i = 0; i < array.length; i++) {
      let entry = array[i];
      if(entry[key] == value) {
        return entry;
      }
    }

    return null;
  }

  public static sortJSONArrayByProperty(array: {}[], key: string, method: SortMethod) {
    array.sort(function(ra, rb) {
      var a = ra[key];
      var b = rb[key];

      switch(method) {
        case SortMethod.ascending: {
          return a > b ? 1 : -1;
        }
        case SortMethod.descending: {
          return b > a ? 1 : -1;
        }
      }
    })
  }

  public static linkKeyEventOnInputToAction(inputID: string, event: KeyEvents, key: KeyEventKeys, action: () => void) {
    document.getElementById(inputID)
    .addEventListener(event, function(event) {
      event.preventDefault();
      if (event.keyCode == key) {
          action();
      }
    });
  }
}

export enum SortMethod {
  ascending,
  descending
}

export enum KeyEvents {
  keyUp = "keyup",
  keyDown = "keydown"
}

export enum KeyEventKeys {
  enter = 13
}