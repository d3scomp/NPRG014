#!/usr/bin/env groovy
import helpers.*

/**
 * ============================================================================
 *  Conversation History — how this script handles context across multiple turns
 * ============================================================================
 *
 * Large Language Models used in a "completion" style are fundamentally stateless:
 * each request is independent and has no memory of previous interactions.
 * To simulate a multi-turn conversation, the client must manually maintain and
 * send back the entire history of exchanged messages on every turn.
 *
 * In this script the history is managed by the `LLMChatConnector` helper
 * (see `helpers/LLMChatConnector.groovy`).  It holds an internal list
 * (`messages`) of role-labelled entries — "user" and "assistant".
 *
 * Each call to `connector.chat(...)` does three things in sequence:
 *
 *   1. Appends the new user message to the list.
 *   2. Sends the COMPLETE list (all previous messages + the new one)
 *      inside a JSON payload to the LLM's /api/chat endpoint.
 *   3. Receives the assistant's reply, appends it to the list, and
 *      returns the text.
 *
 * Because the list is never cleared between turns, every request carries
 * the full conversation so far.  This gives the LLM the context it needs
 * to resolve pronouns, references, and implicit follow-ups like
 * "And Germany?" or "Which of the two is bigger?".
 *
 * The trade-off is that the payload grows with each turn, which increases
 * latency and token cost.  In production systems you would typically
 * introduce a maximum window (e.g., keep only the last N messages) or
 * rely on a server-side session store.  Here we simply let the list
 * grow unbounded for clarity.
 * ============================================================================
 */

// Use the LLMChatConnector helper for a conversation
//def connector = new LLMGenerateConnector(debug: false)
def connector = new LLMChatConnector(debug: false)

// First question
def answer1 = connector.ask("What is the capital of France?")
println answer1
println '--------------------------------------------------'
// Follow-up question
def answer2 = connector.ask("And Germany?")
println answer2
println '--------------------------------------------------'
// Final question referencing both answers
def answer3 = connector.ask("Which of the two is bigger?")
println answer3
println '--------------------------------------------------'