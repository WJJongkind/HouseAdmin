export interface JSONConvertable {
    json: {}
}

export class JSONWrapper implements JSONConvertable {

    constructor(public json: {}) { }

}