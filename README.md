	# NPRG014
Supplementary materials for the course of "Concepts of Modern Programming Languages" (NPRG014).

# Lessons

## Lesson 1 - 5th October 2026
### Agenda
* Language dynamism - typing, casting, object construction, method invocation
* Scripting
* LLM generation and chat API use in Groovy

### Pre-Lecture Setup Instructions

Please bring your laptop to the lecture with the following software pre-installed. Doing this ahead of time ensures we can jump straight into the exercises!

#### 1. Install Java JDK (17 or higher)
*   **Download:** Get the latest JDK 17+ from the [Java download site](https://www.oracle.com/java/technologies/downloads/) (or use an open-source distribution like [Adoptium](https://adoptium.net/)).
*   **Verify:** Open your terminal or command prompt and run `java -version` to ensure it is installed correctly.

#### 2. Install Groovy (5.0.x or later)
*   **Download:** Grab Groovy 5.0.x or later from the [Groovy download page](http://groovy-lang.org/download.html).
*   **Verify:** Make sure the `groovyConsole` editor tool can be started from your command line. *(Note: Mind the capitalization of the command!)*

#### 3. Install Git & Clone the Repository
*   **Install Git:** If you don't have it installed, download it from [git-scm.com](https://git-scm.com/).
*   **Checkout Code:** Clone this repository to get the source code of the examples and homework assignments that will be used during the lecture:
    ```bash
    git clone <INSERT_YOUR_REPO_URL_HERE>
    ```
#### 4. Install & Configure Ollama
We will use Ollama to run Large Language Models (LLMs) locally. Alternatively, you may use a remote server-based LLM, provided it exposes a standard /v1/chat/completions API endpoint and is accessible from the classroom network.

*   **macOS:** Download from [ollama.com/download](https://ollama.com/download), unzip it, and drag the app to your Applications folder.
*   **Windows:** Download the `.exe` installer from [ollama.com/download](https://ollama.com/download) and run it.
*   **Linux:** Open your terminal and run the official install script:
    ```bash
    curl -fsSL [https://ollama.com/install.sh](https://ollama.com/install.sh) | sh
    ```
**Network Configuration:** 
Once Ollama is installed and running, open the application **Settings** (usually located in your system tray or menu bar) and enable **"Expose Ollama on the network"**. 

*   **Troubleshooting:** If your operating system UI doesn't show this setting or it fails to apply, you can manually expose the network by setting the environment variable `OLLAMA_HOST=0.0.0.0` before starting the Ollama application or service.

#### 5. Download the Required LLMs
**Hardware Note:** Running the 12-billion parameter model (`gemma4:12b`) locally requires significant memory (roughly 8–12GB of RAM just to load the model). **A laptop with at least 16GB of total system RAM is highly recommended.** If your machine has 8GB or less, please stick to the `4b` models during the exercises to avoid crashing your system.

With Ollama running in the background, open your terminal or command prompt and run the following commands to download the necessary models. 

*(Please do this before coming to class, as these files are several gigabytes in size and require a stable internet connection!)*

```bash
ollama pull gemma3:4b
ollama pull gemma4
ollama pull gemma4:12b
ollama pull qwen3.6
```
### Resources
* Exercises to work with during the lesson are located in the “lecture-groovy/exercises” folder
* The homework is to be found at “lecture-groovy/homework/homework_1005”
* The “lecture-groovy/slides” holds the slides for the lecture

## Lesson 2 - 12th October 2026
### Agenda

* Dynamic meta-programming
* Intro into Domain Specific Languages
* Domain specific languages
* Builders

### Preparation
* Same as for Lesson 1
* Do a fresh checkout of this repository to get updated source code for examples and homework assignments

### Resources
* Exercises to work with during the lesson are located in the “lecture-groovy/exercises” folder
* The homework is to be found at “lecture-groovy/homework/homework_1012”
* The “lecture-groovy/slides” holds the slides for the lecture

## Lesson 3 - 19th October 2026
### Agenda

* Static meta-programming
* AST transformations

### Preparation
* Same as for Lesson 1
* Do a fresh checkout of this repository to get updated source code for examples and homework assignments

### Resources
* Exercises to work with during the lesson are located in the “lecture-groovy/exercises” folder
* The homework is to be found at “lecture-groovy/homework/homework_1019”
* The “lecture-groovy/slides” holds the slides for the lecture

## Lesson 4 - 2nd November 2026
### Agenda
* Bytecode
* Statically-typed languages (Scala) - Part I

### Preparation
* Grab and install SBT (http://www.scala-sbt.org/)
* Install Java JDK (if you don't have it installed) from the Java download site
* Checkout this repository to get all examples
* Go to lecture-scala/exercises-homework and run "sbt compile" to download all necessary packages (Scala and related libraries)

### Resources
* Exercises to work with during the lesson and the homework are located in the lecture-scala/exercises-homework” folder
* The “lecture-scala/slides” holds the slides for the lecture


## Lesson 5 - 9th November 2026
### Agenda
* Statically-typed languages (Scala) - Part II

### Preparation
* Same as for Lesson 4


## Lesson 6 - 16th November 2026
### Agenda
* Statically-typed languages (Scala) - Part III

### Preparation
* Same as for Lesson 4

## Lesson 7 - 23rd November 2026
### Agenda
* Statically-typed languages (Scala) - Part IV

### Preparation
* Same as for Lesson 4

## Lesson 8 - 30th November 2026
### Agenda

* Concurrency abstractions in modern languages
  ** Dataflow
  ** Fork-join
  ** Actors
  ** Parallel collections
  ** Agents

### Preparation
* Same as for Lesson 1 (Groovy, JDK, Ollama)
* Do a fresh checkout of this repository to get updated source code for examples and homework assignments

### Resources
* Exercises to work with during the lesson are located in the “lecture-groovy/exercises” folder
* The homework is to be found at “lecture-groovy/homework/homework_1130”
* The “lecture-groovy/slides” holds the slides for the lecture

## Lesson 9 - 7th December 2026
### Agenda
* Introduction to prototype-based languages (IO)

### Preparation
* Download IO interpreter from http://iolanguage.org/binaries.html
* Checkout this repository to get all examples


## Lesson 10 - 14th December 2026
### Agenda
* Advanced types in TypeScript

### Preparation
* If you do not have Node.js installed, get the LTS version from https://nodejs.org (but any reasonably recent version should work) and 
  check the README.md in the lecture materials for more details.
* You will need an editor that does type checking in background. WebStorm (from JetBrains) or VSCode are good choices, but anything will do.
  

### Resources
