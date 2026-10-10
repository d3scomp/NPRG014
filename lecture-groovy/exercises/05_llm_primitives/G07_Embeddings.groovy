//def embeddings = new helpers.LLMEmbeddingConnector(model: 'all-minilm')
def e = new helpers.LLMEmbeddingConnector(model: 'nomic-embed-text')

// The `embedding` operation is expensive, unlike the `cosineSimilarity` operation, so cache the vectors

def a = e.embedding('I like programming languages.')
def b = e.embedding('I enjoy writing software.')
def c = e.embedding('The weather is rainy today.')

println e.cosineSimilarity(a, b)
println e.cosineSimilarity(a, c)


def words = ['Me', 'My', 'Water']
def v = e.embeddings(words)
def word = e.embedding('Mine')

println 'Compare \'Mine\' with ' + words
println v.collect {e.cosineSimilarity(it, word)}