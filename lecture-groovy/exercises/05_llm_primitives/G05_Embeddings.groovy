// TASK - switch the embedding model and see the change in the size of the vector space

def embeddings = new helpers.LLMEmbeddingConnector(model: 'all-minilm')
//def embeddings = new helpers.LLMEmbeddingConnector(model: 'nomic-embed-text')

def vector = embeddings.embedding(
    'The cat is sitting on the mat.'
)

println "Dimensions: ${vector.size()}"
println vector

