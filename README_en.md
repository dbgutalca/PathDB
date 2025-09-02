# PathDB

PathDB is a Java application that allows evaluating Regular Path Queries (RPQs) over a labeled directed graph loaded in memory (RAM). The main feature of PathDB is the use of a path algebra for query evaluation instead of common algorithms (as most graph database systems do). Therefore, PathDB generates evaluation trees that can be easily manipulated for optimizations.

## PathDB Requirements

Before running PathDB, you need to have the following tools installed:
* [Java 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html) (or above).

Once you have installed the requirements, you can download PathDB from its latest release by [clicking here](https://github.com/dbgutalca/PathDB/releases/tag/v0.2.1) and downloading the PathDB.jar file.

Another requirement of PathDB is the graph format. PathDB works with PGDF files, so if you want to load a graph you must provide two files, one for nodes and another for edges, both in PGDF format.

Un ejemplo de un archivo de nodos y aristas en formato PGDF es el siguiente:

##### Nodes
```
@id|@label|name
n1|Person|Moe
n2|Person|Bart
n3|Person|Lisa
n4|Person|Apu
@id|@label|txt
n5|Message|Msg1
n6|Message|Msg2
n7|Message|Msg3
```

##### Edges
```
@id|@label|@dir|@out|@in
e1|Knows|T|p1|p2
e2|Knows|T|p2|p3
e3|Knows|T|p3|p2
e4|Knows|T|p2|p4
e5|Likes|T|p2|m3
e6|Likes|T|p4|m3
e7|Likes|T|p3|m2
e8|Likes|T|p1|m1
e9|HasCreator|T|m3|p1
e10|HasCreator|T|m2|p4
e11|HasCreator|T|m1|p3
```

## Loading a Graph

By default, PathDB includes a test graph, so if you want to use it, simply run PathDB as follows:
```bash
$ java -jar PathDB.jar
```

If you have a graph that follows the structure mentioned in the requirements, you can load it using the following command:
```bash
$ java -jar PathDB.jar -n nodes_file.pgdf -e edges_file.pgdf
```

This will load your graph into PathDB. You will see a success message if the graph was loaded correctly; otherwise, PathDB will use the default graph if either of the two files contains an error.


## Test Graph

The test graph provided by PathDB is a very small representation of a social network, with the following basic schema:
- Tipos de nodos: Person(name), Message(text).
- Tipos de aristas: Knows(Person, Person), Likes(Person, Message) y Has_Creator(Message, Person).

El grafo contiene un total de **7 nodos** y **11 aristas** y se ve de la siguiente manera:
<div align="center">
  <img src="readmeAssets/image-3.png" alt="Social network simulating property graph">
</div>

## Running Queries

PathDB includes a fairly complete query language, similar to GQL. This structure allows searching for node and edge patterns, applying restrictions, returning specific results, and limiting the amount of information retrieved.

The general form of a query is:

```
MATCH [Restricción] <Patrón de camino> WHERE [Conditions] RETURN <Opciones de retorno> [LIMIT <número>];
```

Everything enclosed in `[]` is **optional**.

## Example Query
Using the default PathDB database and considering the following query:

```
MATCH TRAIL p = (x)-[Knows*]->(y) RETURN LABEL(FIRST()), LABEL(LAST()), x.name, y.name LIMIT 2;
```

PathDB returns the following results in table format:

```
┌──────────┬──────────────────────────────┬──────────────────────────────┬──────────────────────────────┬──────────────────────────────┐
│    #     │        LABEL(FIRST())        │        LABEL(LAST())         │            x.name            │            y.name            │
├──────────┼──────────────────────────────┼──────────────────────────────┼──────────────────────────────┼──────────────────────────────┤
│    1     │            Person            │            Person            │             Moe              │             Moe              │
├──────────┼──────────────────────────────┼──────────────────────────────┼──────────────────────────────┼──────────────────────────────┤
│    2     │            Person            │            Person            │             Bart             │             Bart             │
└──────────┴──────────────────────────────┴──────────────────────────────┴──────────────────────────────┴──────────────────────────────┘

Total paths: 2 paths
Execution time: 0.005 seconds
```

## Query Components

#### 1. `MATCH` (mandatory)
Every query in PathDB must start with the reserved keyword `MATCH`. This indicates that you want to search for a pattern in the graph.

#### 2. Path Restrictions (optional)
After `MATCH` you can specify the restriction that paths must satisfy. Currently, PathDB supports the following semantics:

- **WALK** → Allows repeating nodes and edges.  
- **TRAIL** → Allows repeating nodes but **not edges**.  
- **ACYCLIC** → The path cannot have cycles (no node is repeated).  
- **SIMPLE** → Neither nodes nor edges are repeated.  

#### 3. Path Pattern (mandatory)
It is defined as:

```
nombreCamino = (nodoInicial)-[etiquetaDeArista]{..n}->(nodoFinal)
```

- `(nodo)` → A node identified by a variable.  
- `-[etiqueta]->` → An edge, which can have a **name** or a **regular expression** to match multiple types of edges.  
- `{..n}` → Number of repetitions of recursive operators (optional, default 4).

#### 4. Path Conditions (optional)
PathDB allows defining conditions that the path components must satisfy for results to be valid. All conditions have the following form:

```
Funcion <operador> Valor
```

Todas las funciones que permite PathDB apuntan a algún componente de un camino. Y son las siguientes:
- `FIRST().property` → Property of the first node.
- `LAST().property` → Property of the last node.
- `NODE(#).property` → Property of node #.
- `EDGE(#).property` → Property of edge #.
- `LABEL(FIRST())` → Label of the first node.
- `LABEL(LAST())` → Label of the last node.

Los operadores de comparación de valores pueden ser: 
- `=` → Equal.
- `!=` → Not equal.
- `>` → Greater than.
- `>=` → Greater or equal.
- `<` → Less than.
- `<=` → Less or equal.

*Note: PathDB is able to “know” the types of data being compared, so comparison operators that are numeric will only accept numbers; otherwise, they will return an error. Similarly, for PathDB to recognize a text string as a value to be compared, it must be enclosed in double quotation marks `"`*

These can be grouped with parentheses and boolean operators such as `AND` and `OR`. A complete example of this section could be:

```
... WHERE (FIRST().property = "algo" AND LAST().property >= 100) OR LABEL(NODE(2)) = "Persona" ...
```

#### 5. Return Information (mandatory)
The `RETURN` section gives full freedom to return variables or values, being able to return the entire path, start and/or end nodes, or values of any component of the path. If the full path is returned, PathDB returns all properties of all path components in an ordered way. If the variable name of the start and/or end node is returned, it returns all property values of that node. In other cases, specific values can be returned using variable names or functions. All possible options are listed below:

- `Variable` → Returns all information of a variable.
- `Variable.propiedad` → Returns the value of the variable property.
- `FIRST()` → Returns all information of the first node of the path.
- `FIRST().property` → Returns the value of the first node property in the path.
- `LAST()` → Returns all information of the last node of the path.
- `LAST().property` → Returns the value of the last node property in the path.
- `NODE(#)` → Returns all information of the node in position # of the path.
- `NODE(#).property` → Returns the value of the node property in position # of the path.
- `EDGE(#)` → Returns all information of the edge in position # of the path.
- `EDGE(#).property` → Returns the value of the edge property in position # of the path.
- `LABEL(FIRST())` → Returns the label of the first node of the path.
- `LABEL(LAST())` → Returns the label of the last node of the path.
- `LABEL(NODE(#))` → Returns the label of the node in position # of the path.
- `LABEL(EDGE(#))` → Returns the label of the edge in position # of the path.

*Note #1: If a variable does not exist, PathDB will throw a variable not found error in the query.*
*Note #2: If the property is not found when calling a function, it will return null.*

#### 6. Result Limit (optional)
The last section of the query corresponds to the result limit. If you are familiar with relational databases, the syntax is identical:

```
LIMIT #
```

Where `#` is the number of results you want to obtain. If you do not want to limit the number of results, simply do not add this section to the query.

## Contributors
* Renzo Angles.
* Roberto García.
* Sebastián Ferrada.
* Vicente Rojas.

## License

This software is distributed as open source under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, the software is distributed "AS IS", WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
