# PathDB

PathDB is a Java-based application which allows to evaluate regular path queries over a property graph loaded in main memory (RAM). The novel characteristic of PathDB is the use of a path algebra for query evaluation instead of ad-hoc algorithms (like most graph database systems do). Hence, PathDB generates evaluation trees that can be manipulated for query optimization.  

## Running PathDB

Before running PathDB, you must make sure you have the following requirements installed:
* [Java 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html) (or above).
* [Maven](https://maven.apache.org/download.cgi).

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

PathDB posee un lenguaje de consultas propio. para poder ver todas las opciones disponibles se recomienda correr el comando `/help`.

```plaintext
PathDB> /help
```

PathDB contains a default property graph representing a small social network. The schema of the graph is the following:
- Types of Nodes: Person(name), Message(txt).
- Types of Edges: Knows (Person, Person), Likes (Person, Message), Has_Creator (Message, Person).

The property graph contains **7 nodes** and **11 edges** and looks as follows:
<div align="center">
  <img src="readmeAssets/image-3.png" alt="Social network simulating property graph">
</div>

A simple example of recursive property graph query is the following:

***Query:***
```plaintext
PathDB> MATCH TRAIL p = (x)-[knows*]{..4}->(y) WHERE x.id = "p1" RETURN p LIMIT 3;
```

***Results:***
- Path #1: p1 E1(knows) p2  
- Path #2: p1 E1(knows) p2 E2(knows) p3  
- Path #3: p1 E1(knows) p2 E4(knows) p4  
- Path #4: p1 E1(knows) p2 E2(knows) p3 E3(knows) p2  
- Path #5: p1 E1(knows) p2 E2(knows) p3 E3(knows) p2 E4(knows) p4  

A more complex example is the following:

***Query:***
```plaintext
PathDB> MATCH WALK path = (source)-[(likes.hasCreator)*]{..3}->(target) WHERE source.id="p1" RETURN p LIMIT 2;
```

***Results:***
- Path #1: p1 E5(likes) m1 E9(hasCreator) p3  
- Path #2: p1 E5(likes) m1 E9(hasCreator) p3 E6(likes) m2 E10(hasCreator) p4  
- Path #3: p1 E5(likes) m1 E9(hasCreator) p3 E6(likes) m2 E10(hasCreator) p4 E7(likes) m3 E11(hasCreator) p1  


<!-- ## Demo 2: Co-author network (DBLP)

The graph represents a co-authorship network created with data obtained from [DBLP](https://dblp.org). Specifically, the nodes are **authors** and the edges represent the **co-authorship** relation. Two authors are connected by an edge if they co-authored the same article. The schema of the graph is shown in the following figure:

<div align="center">
  <img src="readmeAssets/image-2.png" alt="DBLPGraph">
</div>

The data was extracted from the dataset **"DBLP-Citation-network V12"**, this set contains articles up to the year 2020, accessible through DBLP, and is available at  [AMiner](https://www.aminer.cn/citation). A **subgraph** was processed, containing only co-authorship relationships. You can download it clicking [here](https://drive.google.com/file/d/1e4vtARAzhwEuTehOSE3-YFmecyx65wwS/view?usp=sharing). This graph contains **2,155,848 nodes** and **14,531,802 edges**.


An interesting query for this dataset involves calculating the **Erdős distance** or **Erdős number**, which describes the collaborative distance between two authors. PathDB allows retrieving the **shortest path** and its length to determine the Erdős distance.

For example, to calculate the Erdős distance for the author **Renzo Angles**:
1. Configure PathDB to limit results to 1:  
   ```plaintext
   PathDB> /lim 1
   ```
2. Set the maximum recursion depth to 5:  
   ```plaintext
   PathDB> /mr 5
   ```
3. Execute the query:  
   ```plaintext
   PathDB> (x{name:Renzo Angles})-[COAUTHOR+]->(y{name:Paul Erdős});
   ```

***Result:***  
```plaintext
Path #1 - 1970866537 E486243(COAUTHOR) 2106576185 E14464(COAUTHOR)  
          1969282344 E7069515(COAUTHOR) 2289364316 E2339853(COAUTHOR)  
          2156312790 E1662154(COAUTHOR) 1337865506
```
The result is a path between "Renzo Angles" and "Paul Erdős" with a length of 5. -->

## Contributors
* Renzo Angles.
* Roberto García.
* Sebastian Ferrada.
* Vicente Rojas.

## License

 This software is released in open source under the Apache License, 
 Version 2.0 (the "License"); you may not use this file except in 
 compliance with the License. You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
