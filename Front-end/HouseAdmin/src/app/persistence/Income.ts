import { CFM } from "../common/common-functions.module";
import { JSONConvertable } from "../BackendCommunication/JSONConvertable";

export class IncomeEntry implements JSONConvertable {

    // MARK: - Public properties

    public category: string;
    public comments: string;
    public person: string;
    public id: string;
    public group: string;

    // MARK: Private properties

    private _date: Date;
    private _amount: number;

    // MARK: - Getters & Setters
    
	public get date(): Date {
		return this._date;
    }
    
	public set date(value: Date) {
		this._date = value instanceof Date ? value : new Date(value);
    }

	public get amount(): number {
		return this._amount;
    }
    
	public set amount(value: number) {
        if(value >= 0) {
            this._amount = value;
        } else {
            throw Error("Income can't be negative. It should be an expense then.");
        }
    }

    // MARK: - JSONConvertable

    public get json(): {} {
        return {
            id: this.id,
            category: this.category,
            amount: this._amount,
            comments: this.comments,
            date: CFM.makeDateString(this._date),
            person: this.person,
            groupID: this.group
        }
    }

}