export class CategoryTypeMapper {
    private mapped: Map<string, Set<string>>

    constructor(unmapped?: {category: string, type: string}[]) { 
        if(unmapped != undefined) {
            this.mapped = new Map();
    
            for(var i = 0; i < unmapped.length; i++) {
                let mapping = unmapped[i];

                if(this.mapped.has(mapping.category)) {
                    this.mapped.get(mapping.category).add(mapping.type)
                } else {
                    let newMapping = new Set<string>()
                    newMapping.add(mapping.type)
                    this.mapped.set(mapping.category, newMapping)
                }
            }
        }
    }

    public get types(): Set<string> {
        var allTypes: string[] = []

        this.mapped.forEach((types: Set<string>) => {
            allTypes = allTypes.concat(Array.from(types))
        });

        return new Set(allTypes)
    }

    public get categories(): Set<string> {
        return this.mapped != undefined ? new Set(this.mapped.keys()) : new Set()
    }
    
    public getTypesForCategory(category: string): Set<string> {
        return this.mapped.get(category);
    }
}