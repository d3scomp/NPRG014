#!/usr/bin/env groovy
import helpers.*

enum TravelClass {
    economy, business, firstClass
}

@groovy.transform.ToString
class FlightRequest {
    String dateRangeStart
    String dateRangeEnd
    int durationInDays
    String startAirport
    String destination
    boolean returnTicket
    String travelerFirstName    
    String lastFirstName        
    TravelClass travelClass
}

// Use the LLMChatConnector helper for a conversation
def connector = new LLMChatConnector(debug: false, model: 'qwen3.6', systemPrompt: '''
You are a coding assistant and a precise travel agent.
All your responses must be valid idiomatic Groovy code. More specifically, you assume an object `flightRequest` is available already as a local variable in the context (you must not create any local variables)
and you write code that initializes the object by setting some or all of its properties. E.g. `flightRequest.travelerFirstName = 'Joe'`.

You analyze the context provided by the user and extract information that is deducible from it to build the code.
IMPORTANT: You must not invent and assign values that cannot be derived from the provided context! Leave those properties unassigned.

As a final statement in the Groovy script code that you create always include one of:
- `return 'success'` if all properties of flightRequest could be determined and set
- `return 'incomplete'` if values for some properties could not be determined; list the missing properties
- `return 'invalid'` if none properties could be determined

The definitions of classes used:

enum TravelClass {
    economy, business, firstClass
}

class FlightRequest {
    String dateRangeStart
    String dateRangeEnd
    int durationInDays
    String startAirport
    String destination
    boolean returnTicket
    String travelerFirstName    
    String lastFirstName        
    TravelClass travelClass
}

''')

def request = '''
 I'd like to travel to London this November or better in the second half of November or the first three days of December for 3 days.
 I live in Prague, prefer first class.
 Regards,
 Dave Smith
'''

//request = '''
// I'd like to travel to London this November or better in the second half of November or the first three days of December for 3 days.
// Regards,
// Dave Smith
//'''

//request = '''
// I'd like to travel.
//'''

def answer = connector.ask(request)

// EXTRACT CODE FROM MARKDOWN
String codeToRun = answer
def matcher = answer =~ /(?s)```(?:groovy)?\s*(.*?)```/
if (matcher.find()) {
    codeToRun = matcher.group(1).trim()
}

println "$request : $codeToRun"

if (!isValid(codeToRun)) {
    throw new IllegalArgumentException("The supplied code is not valid: $codeToRun")
}


def binding = new Binding()
binding.flightRequest = new FlightRequest()
def shell = new GroovyShell(this.class.getClassLoader(), binding)
String result = shell.evaluate(codeToRun)
println result
println binding.flightRequest


boolean isValid(String code) {
    def validator = new LLMChatConnector(debug: false, model: 'qwen3.6', systemPrompt: '''
You validate whether the provided code is valid Groovy script code.
The code sets properties on a flightRequest object:

enum TravelClass {
    economy, business, firstClass
}

class FlightRequest {
    String dateRangeStart
    String dateRangeEnd
    int durationInDays
    String startAirport
    String destination
    boolean returnTicket
    String travelerFirstName    
    String lastFirstName        
    TravelClass travelClass
}

Several, one or even none of the properties on flightRequest may be set in the script.
The script code ends with a `return 'some text'` statement, where "some text" can be an arbitrary string value.
You must respond with text starting with either 'CORRECT' or 'INVALID'. An detailed explanation of your decision should follow only if the code is INVALID.
    ''')
    String answer = validator.ask(code)
    println "Validation: $answer"
    return answer.toUpperCase().startsWith('CORRECT')
}