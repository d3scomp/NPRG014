/* 
 * Methods pd, psd for printing object descriptions.
 */
DescriptionUtils := Object clone do (
	getNameOfKnown := method(refId,
		preDef := Map clone do (
			atPut("Core", Core uniqueId)
			atPut("Lobby", Lobby uniqueId) 
		)
		
		refName := list(
			preDef keys select(name, preDef at(name) == refId),
			Core slotNames select(name, Core getSlot(name) uniqueId == refId),
			Lobby slotNames select(name, Lobby getSlot(name) uniqueId == refId)
		) flatten first		
	)
	
	getId := method(ref,
		uId := getSlot("ref") uniqueId // getSlot is used here to not activate activatables (i.e. methods) by accessing them.
		name := getNameOfKnown(uId)
		uId .. if(name != nil, " \"" .. name .. "\"", "") .. " [" .. getSlot("ref") type .. "]"  
	)
	
	getProtosRecursive := method(ref, levels, indent,
		if (levels > 1,
			(ref protos map(x, list(indent .. getId(x), getProtosRecursive(x, levels - 1, indent .. "  ")))),
			(ref protos map(x, indent .. getId(x)))
		)
	)
	
	getTarget := method(cl,
		if (cl message previous != nil,
			name := cl message previous name
			if (name != "", name, cl message previous arguments join(", ")),
			
			""
		)	
	)
	
	decorateObject := method(
		Object psd := method(
			(
				"'" .. DescriptionUtils getTarget(call) .. "' is " .. DescriptionUtils getId(self)
			) println
			nil
		)
		
		Object pd := method(
			setIsActivatable(false)	
			thisSelf := self
			list(
				"Description of '" .. DescriptionUtils getTarget(call) .. "'",
				"  id: " .. DescriptionUtils getId(self),
				"  protos: ",
				DescriptionUtils getProtosRecursive(self, 4, "    "),
				"  content:", 
				self slotNames map(x, "    " .. x .. ": " .. DescriptionUtils getId(thisSelf getSlot(x))),
				""
			) flatten map (println)
			nil
		)
	) 
)
