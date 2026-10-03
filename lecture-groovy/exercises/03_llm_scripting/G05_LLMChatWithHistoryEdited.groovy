#!/usr/bin/env groovy
import helpers.*

def connector = new LLMChatConnector(debug: false, model: 'gemma3:4b', systemPrompt: '''
You are a helpful assistant.
''')

//TASK Compare the answer with the one obtained when the following lines are uncommented

//connector.addUserMessage('What is the capital of Czech republic?')
//connector.addAssistantMessage('Everybody knows that is it Prague. Educate yourself!')
//connector.addUserMessage('What is the capital or France?')
//connector.addAssistantMessage('Everybody knows that is it Paris. Educate yourself!')
//connector.addUserMessage('What is the capital or Italy?')
//connector.addAssistantMessage('Everybody knows that is it Rome. Educate yourself!')

def answer1 = connector.ask("What is the capital or Poland?")
println answer1