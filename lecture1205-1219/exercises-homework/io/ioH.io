DescriptionUtils decorateObject

Class := Object clone

Class new := method(name,                 // Meta-class constructor
	cls := Class clone
	cls __name := name                    // Name of the class
	cls __instanceProto := Object clone   // Prototype of instances - hold instance field along with their initial values
	cls __instanceProto class := cls
	
	call argAt(1) doInContext(cls)
	cls
) 




/* HOMEWORK
 * Create a methods of object "Class" such that it allows for creating new classes 
 * that have one constructor, attributes (instance and static) and methods.
 * The code below illustrates the use.
 */

		
Dog := Class new("Dog",
	withConstructor(nameParm,
		name = nameParm
	)
	withInstanceAttribute("name", "")
	withInstanceAttribute("foodItemsEaten", 0)
	withStaticAttribute("allFoodItemsEaten", 0)
	withMethod("feed", howMuchFood, 
		foodItemsEaten = foodItemsEaten + howMuchFood
		class allFoodItemsEaten = allFoodItemsEaten + howMuchFood
		"#{name} has just eaten #{howMuchFood} food items." interpolate println
	)
	withMethod("feedSummary", 
		"So far #{foodItemsEaten} food items eaten by #{name} and #{class allFoodItemsEaten} in total by all dogs." interpolate println
	)
)

/* Prints out: (note that the "type" field is added automaticaly by cloning an object into something that starts with uppercase character)
Description of 'Dog'
  id: 0x02151030 "Dog" [Dog]
  protos: 
    0x02150E80 "Class" [Class]
      0x007A3860 "Object" [Object]
        0x00630420 "Lobby" [Object]
          0x006306C0 "Protos" [Object]
  content:
    new: 0x02151510 [Block]
    allFoodItemsEaten: 0x0063BD58 [Number]
    type: 0x0229E8E0 [Sequence]
    feedSummary: 0x02152E60 [Block]
    __name: 0x0229E8E0 [Sequence]
    __instanceProto: 0x02150C40 [Object]
    feed: 0x02151F00 [Block]
*/
Dog pd

barry := Dog new("Barry")

/* Prints out: 
Description of 'barry'
  id: 0x02211D60 "barry" [Dog]
  protos: 
    0x02196710 "Dog" [Dog]
      0x02195F90 "Class" [Class]
        0x00733860 "Object" [Object]
          0x0062FEE0 "Lobby" [Object]
  content:
    foodItemsEaten: 0x0063AD38 [Number]
    class: 0x02196710 "Dog" [Dog]
    name: 0x022E8D10 [Sequence]
*/
barry pd

barry name println                // Prints out: "Barry"
barry allFoodItemsEaten println   // Prints out: "0"
Dog allFoodItemsEaten println     // Prints out: "0"

lisa := Dog new("Lisa")

barry feed(12)                    // Prints out: "Barry has just eaten 12 food items."
lisa feed(5)                      // Prints out: "Lisa has just eaten 5 food items."

barry feedSummary                 // Prints out: "So far 12 food items eaten by Barry and 17 in total by all dogs."
lisa feedSummary                  // Prints out: "So far 5 food items eaten by Lisa and 17 in total by all dogs."

Dog allFoodItemsEaten println     // Prints out: "17"
