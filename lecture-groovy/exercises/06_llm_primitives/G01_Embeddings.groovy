//def embeddings = new helpers.LLMEmbeddingConnector(model: 'all-minilm')
def embeddings = new helpers.LLMEmbeddingConnector(model: 'nomic-embed-text')

def vector = embeddings.embedding(
    'The cat is sitting on the mat.'
)

println "Dimensions: ${vector.size()}"
println vector

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

def e = new helpers.LLMEmbeddingConnector()

def a = e.embedding('I like programming languages.')
def b = e.embedding('I enjoy writing software.')
def c = e.embedding('The weather is rainy today.')

println e.cosineSimilarity(a, b)
println e.cosineSimilarity(a, c)

def v = e.embeddings(['Me', 'My', 'Ball'])
def word = e.embedding('Mine')
println v.collect {e.cosineSimilarity(it, word)}