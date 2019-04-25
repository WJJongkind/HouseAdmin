import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { LogoutService } from './logout.service';
import { CookieService } from 'ngx-cookie-service';

/**
 * The CFM (CommonFunctionsModule) class contains a wide variety of commonly used functions throughout the application. This class it a placeholder and all functions will be segragated into appropriate
 * classes in the future.
 */
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

  /**
   * Obtain the date with the given offset. The offset is calculated from the current date.
   * @param daydiff The amount of days to offset the returned date from the current date.
   * @param monthdiff The amount of months to offset the returned date from the current date.
   * @param yeardiff The amount of years to offset the returned date from the current date.
   */
  public static getDate(daydiff: number=0, monthdiff: number=0, yeardiff: number=0): Date {
    var date = new Date();
    date.setDate(date.getDate() + daydiff);
    date.setFullYear(date.getFullYear() + yeardiff);
    date.setMonth(date.getMonth() + monthdiff);
  
    return date;
  }

  /**
   * Converts a string-representation of a date to a Date object.
   * @param date The string which has to be converted to a date.
   */
  public static makeDate(date: string): Date {
    var dateParts = date.split('-');
    var _date = new Date(+dateParts[0], +dateParts[1] - 1, +dateParts[2]); 
    return _date;
  }

  /**
   * Converts the given Date object to a string-representation formatted as yyyy-mm-dd.
   * @param date The Date object to be converted to a string.
   */
  public static makeDateString(date: Date): string {
    var month = (date.getMonth() + 1) + "";
    var day = date.getDate() + "";

    month = month.length == 1 ? "0" + month : month
    day = day.length == 1 ? "0" + day : day

    return date.getFullYear() + "-" + month + "-" + day;
  }

  /**
   * Returns the string-representation of the value of a HTML DOM element with the given ID.
   * @param id The HTML-id of the element of which the value has to be obtained.
   */
  static getElementByIDValue(id: string): string {
    return (<HTMLInputElement>document.getElementById(id)).value;
  }

  /**
   * Returns a typescript HTMLInputElement representation of a HTML DOM element with the given ID.
   * @param id The HTML-id of the element which has to be obtained.
   */
  static getElementByID(id: string): HTMLInputElement {
    return (<HTMLInputElement>document.getElementById(id));
  }

  /**
   * Performs a logout on the current session & redirects the user to the root-path of the given router.
   * @param router Router used to navigate to the root-path.
   */
  logout(router: Router) {
    this.logoutService.logout(this.getSessionID(), () => {
      // Do nothing
    }, (error: any) => void {
      // Yeah, well... If logging out doesn't go right then we cant really do much now can we?
    });
    this.deleteCookies()
    router.navigateByUrl('')
  }

  /**
   * Obtains the value of the cookie with the given name.
   * @param name Name of the cookie.
   */
  getCookie(name: string): string {
    return this.cookie.get(name);
  }

  /**
   * Deletes any cookies with the given name.
   * @param name The name of the cookie which has to be deleted.
   */
  deleteCookie(name: string) {
    this.cookie.delete(name);
    this.cookie.delete(name, "/");
  }

  /**
   * Sets a new cookie.
   * @param name The name of the new cookie.
   * @param value The value of the new cookie.
   */
  setCookie(name: string, value: string) {
    this.cookie.set(name, value,undefined,"/");
  }

  /**
   * Deletes all cookies.
   */
  deleteCookies() {
    this.cookie.deleteAll();
    this.cookie.deleteAll("/");
  }

  /**
   * Obtains the ID of the current session, used for back-end communication.
   */
  getSessionID(): string {
    return this.getCookie("sessionID");
  }

  /**
   * Sets the current group with which the user is interacting.
   * @param id The ID of the group with which the user is interacting.
   */
  setActivatedGroup(id: string) {
    this.setCookie("groupID", id);
  }

  /**
   * Obtains the ID of the group with which the user is currently interacting.
   */
  getActivatedGroup() {
    return this.getCookie("groupID");
  }

  /**
   * Removes the given object from the given array and returns the newly created array.
   * @param array The array from which the object has to be removed.
   * @param obj The object that needs to be removed.
   */
  public static deleteFromArray<T>(array: [T], obj: T): [T] {
    let index = array.indexOf(obj);

    if(index > -1) {
      array.splice(index, 1);
    }

    return array;
  }

  /**
   * Find & returns the first entry in an array of JSON objects that contains the given value for the given property.
   * @param array The array in which the entry has to be searched.
   * @param propertyName The name of the property.
   * @param value The value which the property has to have.
   */
  public static findKeyValueInArray(array: {}[], propertyName, value): {} {
    for(var i = 0; i < array.length; i++) {
      let entry = array[i];
      if(entry[propertyName] == value) {
        return entry;
      }
    }

    return null;
  }

  /**
   * Sorts a JSON array by the given property with the given method.
   * @param array The array which has to be sorted.
   * @param propertyName The name of the property by which the array has to be sorted. 
   * @param method The sorting-method.
   */
  public static sortJSONArrayByProperty(array: {}[], propertyName: string, method: SortMethod) {
    array.sort(function(ra, rb) {
      var a = ra[propertyName];
      var b = rb[propertyName];

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

  /**
   * Binds the given action to all HTML elements that have the given id. The action is triggered when the given key is pressed.
   * @param elementId The ID of the HTML element.
   * @param event The event for which the action has to be triggered (keyup/keydown).
   * @param key The key for which the action is triggered.
   * @param action The action that needs to be executed.
   */
  public static linkKeyEventOnInputToAction(elementId: string, event: KeyEvents, key: KeyEventKeys, action: () => void) {
    document.getElementById(elementId)
    .addEventListener(event, function(event) {
      event.preventDefault();
      if (event.keyCode == key) {
          action();
      }
    });
  }
}

/**
 * Enum containing the supported sorting methods.
 */
export enum SortMethod {
  ascending,
  descending
}

/**
 * Enum containing the supported KeyEvents for binding actions to key-press events.
 */
export enum KeyEvents {
  keyUp = "keyup",
  keyDown = "keydown"
}

/**
 * Enum containing the supported keys for binding actinos to key-press events.
 */
export enum KeyEventKeys {
  enter = 13
}