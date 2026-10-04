//TASK Play with texts and observe the changes in similarity

//def embeddings = new helpers.LLMEmbeddingConnector(model: 'all-minilm')
def embeddings = new helpers.LLMEmbeddingConnector(model: 'nomic-embed-text')

def similarity = embeddings.similarity(
    'The cat is sitting on the mat.',
    'A cat is resting on a rug.'
)

println "Similarity: ${similarity}"

similarity = embeddings.similarity(
    'The cat is sitting on the mat.',
    'A car run round a house.'
)

println "Similarity: ${similarity}"

