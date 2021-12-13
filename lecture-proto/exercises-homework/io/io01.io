"Hello world!" println

("Hello" .. " " .. "all!") println

helloMsg := "Hello universe!"

updateSlot("helloMsg", "Hello universe!")

helloMsg println

printHello := method(name,
	"Hello #{name}!" interpolate println
)

printHello("User")