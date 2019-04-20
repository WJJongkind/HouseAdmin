export interface Parser<T> {
    parse(input: T): T
}

export class ResponseParser<T> implements Parser<T> {
    constructor(private constructor: new () => T) { }
    parse(input: T): T {
        let output = new this.constructor()
        Object.assign(output, input)
        return output
    }
}

export class ArrayResponseParser<T> implements Parser<T[]> {

    constructor(private typeConstructor: new () => T) { }

    parse(input: T[]): T[] {
        let output: T[] = []

        input.forEach((value: T) => {
            let parsedValue = new this.typeConstructor()
            Object.assign(parsedValue, value)
            output.push(parsedValue)
        })

        return output
    }
}