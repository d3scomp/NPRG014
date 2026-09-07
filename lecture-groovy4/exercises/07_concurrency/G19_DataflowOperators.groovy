import groovyx.gpars.dataflow.DataflowQueue
import static groovyx.gpars.dataflow.Dataflow.operator
import static groovyx.gpars.dataflow.Dataflow.task

/**
 * Builds a network of dataflow operators, which will in turn complete the provided urls, download them, search for the words
 * 'java' and 'scala' in them and return reports telling, which site refers to which of the two languages.
 *
 */

//TASK Wire the provided operators with the provided queues, so that the network can process incoming urls.

final DataflowQueue urlsRequests = new DataflowQueue()
final DataflowQueue urls = new DataflowQueue()
final DataflowQueue pagesForJava = new DataflowQueue()
final DataflowQueue pagesForScala = new DataflowQueue()
final DataflowQueue resultsFromJava = new DataflowQueue()
final DataflowQueue resultsFromScala = new DataflowQueue()
final DataflowQueue reports = new DataflowQueue()

//Completes the url
def urlResolver = operator() {
    bindOutput([url: "http://www.${it}.com"])
}

//Downloads the contents of the urls
def downloader = operator(inputs: [], outputs: []) {
    def content = it.url.toURL().text
    it.content = content
    bindAllOutputsAtomically it
}

//Scans the web content for ocurrences of 'java', outputs a map with two entries - url, the found word
def javaScanner = operator(inputs: [], outputs: []) {
    def foundWord = it.content.toLowerCase().contains('java') ? 'Java' : ''
    bindOutput([url: it.url, foundWord: foundWord])
}
//Scans the web content for ocurrences of 'scala', outputs a map with two entries - url, the found word
def scalaScanner = operator(inputs: [], outputs: []) {
    def foundWord = it.content.toLowerCase().contains('scala') ? 'Scala' : ''
    bindOutput([url: it.url, foundWord: foundWord])
}

//Collects results for each url and outputs a brief report
def reporter = operator(inputs: [], outputs: []) {g, s ->
    assert g.url == s.url
    def words = [g.foundWord, s.foundWord].findAll {it}
    def result
    switch (words.size()) {
        case 2:
            result = "${g.foundWord} and ${s.foundWord}"
            break
        case 1:
            result = words[0]
            break
        default:
            result = 'No interesting words'
    }
    bindOutput "$result found at ${g.url}"
}

//Prints reports
task {
    for (;;) {
        println reports.val
    }
}

//Insert urls
['dzone', 'infoq', 'jetbrains', 'oracle', 'apache', 'scalalang', 'grails'].each {
    urlsRequests << it
}

sleep 10000