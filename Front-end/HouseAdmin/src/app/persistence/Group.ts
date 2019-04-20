import { JSONConvertable } from "../BackendCommunication/JSONConvertable";

export class Group implements JSONConvertable {
    public id: String;
    public name: String;
    public description: String;
    public admin: String;
    public members: String[];

    public get json(): {} {
        return {
            ID: this.id,
            Name: this.name,
            Description: this.description,
            Admin: this.admin,
        }
    }
}