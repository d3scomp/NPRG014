/*
Features:
- interfaces
- type-checking based on structural subtyping

 https://www.typescriptlang.org/docs/handbook/interfaces.html
*/

namespace e02 {

    function printLabel(labelledObj: { label: string }) {
        console.log(labelledObj.label)
    }

    function createObj(label: string, size: number): { label: string, size: number } {
        return {label: label, size: size}
    }


    interface LabelledItem {
        label: string
    }

    function printLabel2(labelledObj: LabelledItem) {
        console.log(labelledObj.label)
    }


    interface SaleItem {
        label: string
    }

    interface Shoe extends SaleItem {
        label: string
        size: number
        readonly brand?: string // Optional property
    }

    function printLabel3(labelledObj: Shoe) {
        console.log(labelledObj.label)
    }


    let myObj = createObj("Size 10 Object", 10)

    printLabel(myObj)
    printLabel2(myObj)
    printLabel3(myObj)

    let shoe: Shoe = myObj
    printLabel2(shoe)

    let shoe2: Shoe = {label: "Size 11 shoe", size: 11, brand: "Adidas"}
    // shoe2.brand = "Puma"  - Compile error

}