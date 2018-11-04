/*
Features:
- Javascript mixin pattern
- class implements another class

https://www.typescriptlang.org/docs/handbook/mixins.html
*/

namespace e08 {

    // Disposable Mixin
    class Disposable {
        isDisposed: boolean
        dispose() {
            this.isDisposed = true
        }

    }

    // Activatable Mixin
    class Activatable {
        isActive: boolean;
        activate() {
            this.isActive = true
        }
        deactivate() {
            this.isActive = false
        }
    }

    class SmartObject implements Disposable, Activatable {
        constructor() {
            setInterval(() => console.log(this.isActive + " : " + this.isDisposed), 500)
        }

        interact() {
            this.activate()
        }

        // Disposable
        isDisposed: boolean = false
        dispose: () => void

        // Activatable
        isActive: boolean = false
        activate: () => void
        deactivate: () => void
    }


    function applyMixins(derivedCtor: any, baseCtors: any[]) {
        baseCtors.forEach(baseCtor => {
            Object.getOwnPropertyNames(baseCtor.prototype).forEach(name => {
                derivedCtor.prototype[name] = baseCtor.prototype[name]
            });
        });
    }


    applyMixins(SmartObject, [Disposable, Activatable])


    let smartObj = new SmartObject()
    setTimeout(() => smartObj.interact(), 1000)
}



