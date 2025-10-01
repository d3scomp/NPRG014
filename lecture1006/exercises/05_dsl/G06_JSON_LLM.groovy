import groovy.json.JsonBuilder

def builder = new JsonBuilder()
builder {
    model "gpt-5"
    messages([
        [role: "system", content: "You are a helpful assistant."],
        [role: "user",   content: "Explain JsonBuilder in Groovy."]
    ])
    max_tokens 200
    temperature 0.7
}

println builder.toPrettyString()
