/**
 * Interface for parsers that can parse a JavaScript object to a TypeScript object.
 */
export interface ObjectToTypescriptParser<T> {
    parse(input: T): T
}

/**
 * Class used to parse a non-array JavaScript object to a non-array TypeScript object.
 */
export class ResponseParser<T> implements ObjectToTypescriptParser<T> {

    /**
     * Constructs a ResponseParser.
     * @param constructor The constructor with which objects of type T can be instantiated.
     */
    constructor(private constructor: new () => T) { }

    parse(input: T): T {
        let output = new this.constructor()
        Object.assign(output, input)
        return output
    }
}

/**
 * Class used to parse an array of JavaScript object to an array of TypeScript object.
 */
export class ArrayResponseParser<T> implements ObjectToTypescriptParser<T[]> {

    /**
     * Constructs a ArrayResponseParser.
     * @param constructor The constructor with which objects of type T can be instantiated.
     */
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