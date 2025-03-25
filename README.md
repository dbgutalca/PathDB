# PathDB

PathDB is a Java-based graph database designed for loading and querying data in memory. It uses Regular Path Queries (RPQ) and a closed path algebra to process path queries.

#### Requirements

Before running PathDB, you must make sure you have the following requirements installed:
* [Java 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html) (or above).
* [Maven](https://maven.apache.org/download.cgi).

#### Preliminaries 

The current structure of PathDB for handling nodes and edges is based on a property graph. Nodes must have an ID and a label, and optionally, properties can be added. For edges, they must currently have 4 attributes: label, direction, source node, and target node, but they do not support additional properties.

The language used by PathDB is based on GQL and has the following structure: `(x{prop:value})-[RE]->(y{prop:value});`. For now, PathDB only allows filtering one property per node, for example: `(x{name:Juan})-[knows*]->(y{name:Pedro});`.

Additionally, a regular expression can be formed using the following operations supported by PathDB:

```ANTLR4
expression: label                   // Single Label
    | '!' label                     // Negation
    | '(' expression ')'            // Parenthesis
    | expression '?'                // Optional
    | expression '+'                // Plus
    | expression '*'                // Kleene
    | expression '.' expression     // Concatenation
    | expression '|' expression     // Union
    ;   
```

With these operations available, an example of a regular expression accepted by PathDB would be `knows+|(likes.hasCreator)+`.

#### Running PathDB

Once the requirements are installed, you can download our latest release by [clicking here](). You will get a file with a `.jar` extension. To run it, open a terminal and navigate to where `PathDB.jar` is located.

PathDB has two execution arguments that are optional, but if provided, both must be included.

If you do not have a test database and want to use the default one, simply run PathDB as follows:

```bash
$ java -jar PathDB.jar
```

If you have a database that follows the structure mentioned in the [preliminaries](#preliminaries), you can load it into PathDB using the following command:

```bash
$ java -jar PathDB.jar -n NodesFile -e EdgesFile
```

If you have already loaded a database or are using one of the default ones, we recommend using the `/help` command to see all the configuration options available in PathDB.

#### Sample Dataset

If you want to test PathDB with the same datasets used during testing, you can download the nodes and edges file by [clicking here](https://drive.google.com/file/d/1IR1kSo4gCvRAoaywpWMPweDd4AiUjObx/view?usp=sharing).

##### Sample Data

| Type  | Attributes                | Example                                  |
|-------|---------------------------|------------------------------------------|
| Node  | ID, Label, Name           | 1970866537 \\| Author \\| Renzo Angles         |
| Node  | ID, Label, Name           | 2106576185 \\| Author \\| Claudio Gutierrez   |
| Node  | ID, Label, Name           | 1969282344 \\| Author \\| Alberto O. Mendelzon   |
| Node  | ID, Label, Name           | 2289364316 \\| Author \\| Jeffrey D. Ullman   |
| Node  | ID, Label, Name           | 2156312790 \\| Author \\| Ron Graham   |
| Node  | ID, Label, Name           | 1337865506 \\| Author \\| Paul Erdös          |

##### Sample Query

`PathDB> (x{name:Renzo Angles})-[COAUTHOR+]->(y{name:Paul Erdös});`

##### Sample Output

```java
Path #1 - 1970866537 E486243(COAUTHOR)
          2106576185 E14464(COAUTHOR)
          1969282344 E7069515(COAUTHOR)
          2289364316 E2339853(COAUTHOR)
          2156312790 E1662154(COAUTHOR)
          1337865506
```

#### Contributors
* Renzo Angles.
* Roberto García.
* Sebastian Ferrada.
* Vicente Rojas.

#### License

This project is licensed under [License Name] - see the LICENSE file for more details.