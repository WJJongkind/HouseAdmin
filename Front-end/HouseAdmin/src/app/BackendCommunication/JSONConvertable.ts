/**
 * Interface to which objects have to adhere that are send with the "BackendRequest" class.
 */
export interface JSONConvertable {

    /**
     * The JSON representation of the object.
     */
    json: {}

}

/**
 * Convenience class with which objects of type "{}" can be wrapped and send with "BackendRequest".
 */
export class JSONWrapper implements JSONConvertable {

    /**
     * Wraps JSON in a JSONWrapper.
     * @param json The JSON that has to be wrapped so that it can be send with "BackendRequest".
     */
    constructor(public json: {}) { }

}