import helpers.*


/**
 * Returns true if text1 supports text2.
 */
String summarize(String text) {

    def llm = new LLMChatConnector()
    llm.systemPrompt = '''
        When given text, return a short summary of it. Nothing else, just a summary reduced down to the bone.
'''
    def summary = llm.chat(text)
    summary
}

String spellCorrect(String text) {
    def llm = new LLMChatConnector()
    llm.systemPrompt = '''
        When given text, return a version with all spelling errors corrected.
        You must not alter or reduce the meaning of the text.
'''
    def shortText = llm.chat(text)
    shortText
}

String rephrase(String text) {
    def llm = new LLMChatConnector(model: 'qwen3.6')
    llm.systemPrompt = '''
        When given text, return a rephrased variant of the text.
        You must not alter or reduce the meaning of the text. Change the words and phrases used.
        Use a tone of an old grumpy professor.
'''
    def shortText = llm.chat(text)
    shortText
}

String.metaClass.summarize {->
    summarize(delegate)
}

String.metaClass.spellCorrect {->
    spellCorrect(delegate)
}

String.metaClass.rephrase {->
    rephrase(delegate)
}

println ('Long stori shot, I came laet to the porty..'.spellCorrect())

println ''

println '''
In the contemporary landscape of advanced computational paradigms, Large Language Models, which are frequently and colloquially abbreviated as LLMs
 within both academic and industry-oriented spheres of discourse, unequivocally represent a monumentally significant paradigm shift in the overarching
  field of artificial intelligence, primarily due to their unprecedented and highly sophisticated capacity to not only ingest and process,
   but also to synthesize and generate textual outputs that bear a striking, almost indistinguishable resemblance to natural human linguistic expression.
    Through the exhaustive and computationally intensive process of being subjected to rigorous training regimens that utilize unimaginably vast,
     multi-terabyte corpora of diverse textual data aggregated from across the global internet, these deeply layered neural network architectures 
     methodically assimilate an intricately nuanced understanding of grammatical structures, semantic relationships, and syntactical idiosyncrasies. 
     Consequently, this allows them to execute a remarkably multifaceted spectrum of operational tasks, ranging from the highly nuanced translation 
     of disparate languages and the distillation of voluminous documents into succinct summaries, all the way to the generation of functional, syntactically correct 
     source code for various software engineering applications. Fundamentally eschewing the historically prevalent reliance on rigid, deterministic, and manually 
     hard-coded heuristic rule sets, the foundational operational mechanism of a given LLM relies almost exclusively on the probabilistic calculation and subsequent 
     prediction of the most statistically viable subsequent token or word within any given sequence, contingent entirely upon the contextual parameters established 
     by the preceding input prompt. As these monumental models are iteratively refined and exponentially scaled up in terms of both their total parameter counts 
     and their underlying algorithmic sophistication, they are precipitating a profound and irreversible transformation in the fundamental modalities through 
     which human beings interface with digital technology, effectively serving as highly dynamic, remarkably versatile conversational agents that are 
     increasingly well-equipped to autonomously navigate and resolve an ever-expanding array of exceptionally complex analytical, 
     logical, and creative computational challenges.
'''.summarize()

println ''

// TASK Implement rephrase in the style of an old grumpy professor.
println '''
 Large Language Models (LLMs) represent a major breakthrough in artificial intelligence by enabling machines to understand and generate human-like text.
 Trained on vast datasets of written content, these neural networks learn complex patterns in language, allowing them to perform a wide variety of tasks such as translation,
 summarization, and writing code. Instead of relying on rigid, pre-programmed rules, an LLM fundamentally works by predicting the most logical next word
 in a sequence based on the context it has been given. As these models continue to scale in size and sophistication, they are rapidly transforming
 how we interact with technology, acting as dynamic conversational agents capable of tackling increasingly complex analytical and creative challenges.
'''.rephrase()

println 'ok'