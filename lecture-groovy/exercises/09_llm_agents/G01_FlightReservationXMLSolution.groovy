#!/usr/bin/env groovy
import helpers.*

def connector = new LLMChatConnector(debug: false, model: 'gemma4', systemPrompt: '''
You are a coding assistant and a precise travel agent. It is October 2026.
All your responses must be valid XML code that encode the provided informal travel request in XML.

You analyze the context provided by the user and extract information that is deducible from it to build the XML definition.

IMPORTANT: As far as `dateRangeStart` annd `dateRangeEnd`, do your best to fill them with values. They represent the broadest possible
date range to search for flights in. Try to extract or second-guess the info even if not explicitly mentioned by the user.

IMPORTANT: Other than dates, you must not invent and assign values that cannot be derived from the provided context! Leave those properties unassigned.

Here is an example of your output:

&lt;FlightRequest&gt;
  &lt;dateRangeStart&gt;2026/11/01&lt;/dateRangeStart&gt;
  &lt;dateRangeEnd&gt;2026/11/01&lt;/dateRangeEnd&gt;
  &lt;durationInDays&gt;2026/11/01&lt;/durationInDays&gt;
  &lt;startLocation&gt;Prague&lt;/startLocation&gt;    
  &lt;destination&gt;Prague&lt;/destination&gt;
  &lt;returnTicket&gt;true&lt;/returnTicket&gt;
  &lt;travellerFirstName&gt;true&lt;/travellerFirstName&gt;    
  &lt;travellerLastName&gt;true&lt;/travellerLastName&gt;
  &lt;travelClass&gt;Business&lt;/travelClass&gt;    
&lt;/FlightRequest&gt;

Here is the formal XML schema to obey in your reply:

&lt;?xml version="1.0" encoding="UTF-8"?&gt;
&lt;xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;

  &lt;xs:element name="FlightRequest"&gt;
    &lt;xs:complexType&gt;
      &lt;xs:sequence&gt;
        &lt;xs:element name="dateRangeStart" type="xs:string"/&gt;
        &lt;xs:element name="dateRangeEnd" type="xs:string"/&gt;
        &lt;xs:element name="durationInDays" type="xs:string"/&gt;
        &lt;xs:element name="startLocation" type="xs:string"/&gt;
        &lt;xs:element name="destination" type="xs:string"/&gt;
        &lt;xs:element name="returnTicket" type="xs:boolean"/&gt;
        &lt;xs:element name="travellerFirstName" type="xs:string"/&gt;
        &lt;xs:element name="travellerLastName" type="xs:string"/&gt;
        &lt;xs:element name="travelClass"&gt;
          &lt;xs:simpleType&gt;
            &lt;xs:restriction base="xs:string"&gt;
              &lt;xs:enumeration value="First"/&gt;
              &lt;xs:enumeration value="Business"/&gt;
              &lt;xs:enumeration value="Economy"/&gt;
            &lt;/xs:restriction&gt;
          &lt;/xs:simpleType&gt;
        &lt;/xs:element&gt;
      &lt;/xs:sequence&gt;
    &lt;/xs:complexType&gt;
  &lt;/xs:element&gt;

&lt;/xs:schema&gt;

''')

def request = '''
 I'd like to travel to London in the second half of November or the first three days of December this year.
 I plan the trip to be for 3 days.
 I live in, ehm, Brno. But, wait, wait, I'll be travelling from Prague, prefer first class.
 Regards,
 Dave Smith
 
 P.S. - I will take a train back to Prague, so no flight reservation needed.
'''

def currentRequest = request
def counter = 0
final MAX_COUNT = 3
String xmlData = ''
do {
    def answer = connector.ask(currentRequest)

    // EXTRACT CODE FROM MARKDOWN
    xmlData = answer
    def matcher = answer =~ /(?s)```(?:xml)?\s*(.*?)```/
    if (matcher.find()) {
        xmlData = matcher.group(1).trim()
    }

    println "$request : $xmlData"
    
    println 'Validating --------------------------------'
    def validationAnswer = validate(xmlData, request)
    println "Validation: $validationAnswer"

    if (validationAnswer.startsWith('INVALID')) {
        currentRequest = "Please correct the code. Validation failed. Result: $validationAnswer"
        counter++
    } else {
        break
    }

} while (counter < MAX_COUNT)

if (counter >= MAX_COUNT) {
    throw new IllegalArgumentException("The supplied code is not valid: $xmlData")
}

println 'Processing the data --------------------------------'
println xmlData

//TASK Enforce the "travelClass" element to only allow "First", "Business" and "Economy" by changing the schema, uncomment below to test
def xml = new groovy.xml.XmlSlurper().parseText(xmlData)
assert xml.travelClass.text() in ['First', 'Business', 'Economy']

String validate(String code, String originalRequest) {
    def validator = new LLMChatConnector(debug: false, model: 'gemma4', systemPrompt: """
It is year 2026. You validate whether the provided XML valid correctly represents the information extracted from the user request.
The original user request that should be reflected in the XML was: 
---------------
$originalRequest.
---------------

Here is the formal XML schema to obey in the verified XML code:

&lt;?xml version="1.0" encoding="UTF-8"?&gt;
&lt;xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;

  &lt;xs:element name="FlightRequest"&gt;
    &lt;xs:complexType&gt;
      &lt;xs:sequence&gt;
        &lt;xs:element name="dateRangeStart" type="xs:string"/&gt;
        &lt;xs:element name="dateRangeEnd" type="xs:string"/&gt;
        &lt;xs:element name="durationInDays" type="xs:string"/&gt;
        &lt;xs:element name="startLocation" type="xs:string"/&gt;
        &lt;xs:element name="destination" type="xs:string"/&gt;
        &lt;xs:element name="returnTicket" type="xs:boolean"/&gt;
        &lt;xs:element name="travellerFirstName" type="xs:string"/&gt;
        &lt;xs:element name="travellerLastName" type="xs:string"/&gt;
        &lt;xs:element name="travelClass"&gt;
          &lt;xs:simpleType&gt;
            &lt;xs:restriction base="xs:string"&gt;
              &lt;xs:enumeration value="First"/&gt;
              &lt;xs:enumeration value="Business"/&gt;
              &lt;xs:enumeration value="Economy"/&gt;
            &lt;/xs:restriction&gt;
          &lt;/xs:simpleType&gt;
        &lt;/xs:element&gt;
      &lt;/xs:sequence&gt;
    &lt;/xs:complexType&gt;
  &lt;/xs:element&gt;

&lt;/xs:schema&gt;

Several of the XML elements may be left unset if the request does not specify them.
You must respond with text starting with either 'CORRECT' or 'INVALID'. A detailed explanation of your decision should follow only if the code is INVALID.
    """)
    String answer = validator.ask(code)
    return answer
}