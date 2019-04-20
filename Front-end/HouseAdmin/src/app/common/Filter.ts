import { CFM } from "./common-functions.module";

export enum FilterType {
    equals,
    moreThan,
    lessThan,
    equalOrMoreThan,
    equalOrLessThan
}

export class Filter<T> {
    private filters: SubFilter<T>[] = []

    constructor() { }

    addCondition(key: string, value: any, type: FilterType) {
        this.filters.push(new SubFilter<T>(key, value, type))
    }

    apply(sourceArray: T[]): T[] {
        return this.applySubfilters(Object.assign([], sourceArray), Object.assign([], this.filters))
    }

    private applySubfilters(sourceArray: T[], filters: SubFilter<T>[]): T[] {
        if(filters.length > 0) {
            var filtered: T[] = filters[0].apply(sourceArray)

            return this.applySubfilters(filtered, CFM.deleteFromArray(filters, filters[0]))
        } else {
            return sourceArray
        }
    }
}

class SubFilter<T> {
    constructor(private key: string, private value: any, private type: FilterType) { }

    apply(sourceArray: T[]): T[] {
        var filtered: T[] = []

        for(var i = 0; i < sourceArray.length; i++) {
            let value = sourceArray[i]

            switch(this.type) {
                case FilterType.equals: {
                    value[this.key] == this.value ? filtered.push(value) : null
                    break;
                }
                case FilterType.lessThan: {
                    value[this.key] < this.value ? filtered.push(value) : null
                    break;
                }
                case FilterType.moreThan: {
                    value[this.key] > this.value ? filtered.push(value) : null
                    break;
                }
                case FilterType.equalOrLessThan: {
                    value[this.key] <= this.value ? filtered.push(value) : null
                    break;
                }
                case FilterType.equalOrMoreThan: {
                    value[this.key] >= this.value ? filtered.push(value) : null
                    break;
                }
            }
        }

        return filtered
    }
}