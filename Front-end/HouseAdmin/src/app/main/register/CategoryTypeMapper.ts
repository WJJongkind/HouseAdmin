export class CategoryTypeMapper {
    private mapped: {[category:string]:string[]} = {};

    constructor(unmapped?: {category: string, type: string}[]) { 
        if(unmapped != undefined) {
            this.mapped = {};
    
            for(var i = 0; i < unmapped.length; i++) {
                let mapping = unmapped[i];
                this.addTypeToCategory(mapping.type, mapping.category);
            }
        }
    }

    public addTypeToCategory(type: string, category: string) {
        var categoryTypes = this.mapped[category];

        if(categoryTypes == undefined) {
            categoryTypes = [type];
            this.mapped[category] = categoryTypes
        } else if(categoryTypes.indexOf(type) == -1) {
            categoryTypes.push(type);
        }
    }

    public getCategories(): string[] {
        return Object.keys(this.mapped);
    }
    
    public getTypesForCategory(category: string): string[] {
        return this.mapped[category];
    }
}