import { JSONConvertable } from "../BackendCommunication/JSONConvertable";
import { CFM } from "../common/common-functions.module";

export class Expense implements JSONConvertable {
  
    // MARK: - Public properties

    public id: string;
    public category: string;
    public person: string;
    public group: string;
    public type: string;
    public comments: string;

    // MARK: - Private properties

    private _amount: number;
    private _date: Date;

    // MARK: - Getters & Setters

	public get amount(): number {
		return this._amount;
	}

	public set amount(value: number) {
        if(value >= 0) {
            this._amount = value;
        } else {
            throw new Error("Expenses should not be negative. If they are, they should be considered income.");
        }
    }
    
	public get date(): Date {
		return this._date;
	}

	public set date(value: Date) {
		this._date = value instanceof Date ? value : new Date(value);
    }
    
    // MARK: - JSONConvertable

    public get json(): {} {
        return {
            id: this.id,
            category: this.category,
            amount: this._amount,
            type: this.type,
            comments: this.comments,
            date: CFM.makeDateString(this._date),
            person: this.person,
            groupID: this.group
        }
    }

  }
  