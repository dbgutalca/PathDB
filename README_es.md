# PathDB

PathDB es una aplicación escrita en Java que permite evaluar Regular Path Queries (RPQs) sobre un grafo dirigido con etiquetas cargado en memoria (RAM). La característica principal de PathDB es el uso de una algebra de caminos para la evaluación de las consultas en vez de usar algoritmos comunes (como lo hacen la mayoría de los sistemas de bases de datos de grafos). Por lo tanto, PathDB genera arboles de evaluación que pueden ser fácilmente manipulados para realizar optimizaciones.
<!-- Por ahora esta cargado en memoria RAM pero se esta desarrollando en una version en disco -->
## Requisitos de PathDB

Antes de correr PathDB, necesitas tener las siguientes herramientas instaladas:
* [Java 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html) (or above).

<!-- Para correr pathdb solo se necesita tener instalado java 18 o mas -->

Una vez que tenga instalado lo necesario, puede descargar PathDB desde su ultima versión haciendo [clic aquí](https://github.com/dbgutalca/PathDB/releases/tag/v0.2.1) y descargando el archivo PathDB.jar

Otro requisito de PathDB es el formato en que se carga un grafo. PathDB trabaja con archivos PGDF, por lo que si desea cargar usted un grafo debe tener dos archivos, uno de nodos y otro de aristas, con extensión y formato PGDF.

Un ejemplo de un archivo de nodos y aristas en formato PGDF es el siguiente:

##### Nodos
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

##### Aristas
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

## Cargando un Grafo

Por defecto, PathDB posee un grafo de prueba, por lo que, si quiere usar dicho grafo, simplemente corra PathDB de la siguiente manera:
```bash
$ java -jar PathDB.jar
```

Si usted tiene un grafo que sigue la estructura mencionada en los requisitos anteriormente mencionados. puede cargar el grafo usando el siguiente comando:
siguiente manera:
```bash
$ java -jar PathDB.jar -n nodes_file.pgdf -e edges_file.pgdf
```

Esto cargará su grafo en PathDB. Vera un mensaje satisfactorio si el grafo fue cargado correctamente, en caso contrario, PathDB usara el grafo por defecto si alguno de los dos archivos contiene algún error.


## Grafo de Prueba

El grafo de prueba que posee PathDB es una representación muy pequeña de una red social. Teniendo el siguiente esquema base:
- Tipos de nodos: Person(name), Message(text).
- Tipos de aristas: Knows(Person, Person), Likes(Person, Message) y Has_Creator(Message, Person).

El grafo contiene un total de **7 nodos** y **11 aristas** y se ve de la siguiente manera:
<div align="center">
  <img src="readmeAssets/image-3.png" alt="Social network simulating property graph">
</div>

## Realizando Consultas

PathDB posee un lenguaje bastante completo y similar al lenguaje de consultas GQL. Esta estructura permite buscar patrones de nodos y aristas, aplicando restricciones, devolver resultados específicos y limitar la cantidad de información obtenida.

La forma general de una consulta es:

```
MATCH [Restricción] <Patrón de camino> WHERE [Conditions] RETURN <Opciones de retorno> [LIMIT <número>];
```

Todo lo que esta encerrado entre `[]` es **opcional**.

## Consulta de Prueba
Usando la base de datos por defecto de PathDB y considerando la siguiente consulta:

```
MATCH TRAIL p = (x)-[Knows*]->(y) RETURN LABEL(FIRST()), LABEL(LAST()), x.name, y.name LIMIT 2;
```

PathDB retorna los siguientes resultados en formato de tabla:

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

## Componentes de una consulta

#### 1. `MATCH` (obligatorio)
Toda consulta realizada en PathDB debe comenzar con la palabra reservada `MATCH`. Esto indica que quiere realizar una búsqueda de un patrón en el grafo.

#### 2. Restricciones del Camino (opcional)
Después del `MATCH` puede indicar que restricción debe cumplir los caminos obtenidos. Actualmente, PathDB soporta las siguientes semánticas:

- **WALK** → Permite repetir nodos y aristas.  
- **TRAIL** → Permite repetir nodos, pero **no aristas**.  
- **ACYCLIC** → El camino no puede tener ciclos (no se repite ningún nodo).  
- **SIMPLE** → No se repiten **ni nodos ni aristas**.  

#### 3. Patrón de Camino (obligatorio)
Se define de la forma:

```
nombreCamino = (nodoInicial)-[etiquetaDeArista]{..n}->(nodoFinal)
```

- `(nodo)` → Un nodo identificado por una variable.  
- `-[etiqueta]->` → Una arista, que puede tener un **nombre** o una **expresión regular** para combinar varios tipos de aristas.  
- `{..n}` → Cantidad de repeticiones de los operadores recursivos (opcional y por defecto 4).

#### 4. Condiciones del Camino (opcional)
PathDB permite definir condiciones que deben cumplir los componentes de un camino para que los resultados sean validos. Todas las condiciones tienen la siguiente forma:

```
Funcion <operador> Valor
```

Todas las funciones que permite PathDB apuntan a algún componente de un camino. Y son las siguientes:
- `FIRST().property` → Propiedad del primer nodo.
- `LAST().property` → Propiedad del ultimo nodo.
- `NODE(#).property` → Propiedad del # nodo.
- `EDGE(#).property` → Propiedad de la # arista.
- `LABEL(FIRST())` → Etiqueta del primer nodo.
- `LABEL(LAST())` → Etiqueta del ultimo nodo.

Los operadores de comparación de valores pueden ser: 
- `=` → Igual.
- `!=` → Distinto.
- `>` → Mayor que.
- `>=` → Mayor o igual que.
- `<` → Menor que.
- `<=` → Menor o igual que.

*Nota: PathDB es capaz de "saber" los tipos de datos que son comparados, por lo que, para los operadores de comparación que sean del tipo numérico solamente aceptarán números sino, arrojará un error. De la misma manera, para que PathDB reconozca una cadena de texto como valor a compara debe estar entre comillas dobles `"`*

Estas pueden ser agrupadas por paréntesis y operadores booleanos como `AND` y `OR`. Un ejemplo completo de esta sección podría ser:

```
... WHERE (FIRST().property = "algo" AND LAST().property >= 100) OR LABEL(NODE(2)) = "Persona" ...
```

#### 5. Retorno de Información (obligatorio)
La sección de `RETURN` entrega total libertad de retornar variables o valores, pudiendo retornar el camino completo, nodos inicial y/o final o valores de algún componente del camino. En caso de retornar el camino completo, PathDB retorna todas las propiedades de todos los componentes del camino de manera ordenada. Si se retorna el nombre de la variable del nodo inicial y/o final, retorna todos los valores de las propiedades de dicho nodo. En otro caso se pueden retornar valores específicos a través de los nombres de las variables o utilizando funciones. Siendo todas las opciones posibles los elementos de la siguiente lista:

- `Variable` → Retorna toda la información de una variable.
- `Variable.propiedad` → Retorna el valor de la propiedad de la variable.
- `FIRST()` → Retorna toda la información del primer nodo del camino.
- `FIRST().property` → Retorna el valor de la propiedad del primer nodo del camino.
- `LAST()` → Retorna toda la información del último nodo del camino.
- `LAST().property` → Retorna el valor de la propiedad del último nodo del camino.
- `NODE(#)` → Retorna toda la información del nodo en la posición # del camino.
- `NODE(#).property` → Retorna el valor de la propiedad del nodo en la posición # del camino.
- `EDGE(#)` → Retorna toda la información de la arista en la posición # del camino.
- `EDGE(#).property` → Retorna el valor de la propiedad de la arista en la posición # del camino.
- `LABEL(FIRST())` → Retorna la etiqueta del primer nodo del camino.
- `LABEL(LAST())` → Retorna la etiqueta del último nodo del camino.
- `LABEL(NODE(#))` → Retorna la etiqueta del nodo en la posición # del camino.
- `LABEL(EDGE(#))` → Retorna la etiqueta de la arista en la posición # del camino.

*Nota #1: En caso que no exista una variable, PathDB va a lanzar un error de variable no encontrada en la consulta.*
*Nota #2: Si no se encuentra la propiedad al momento de llamar alguna función, esta devolverá nulo.*

#### 6. Limite de resultados (opcional)
La última sección de la consulta corresponde al límite de resultados. Si está familiarizado con bases de datos relacionales la sintaxis es idéntica:

```
LIMIT #
```

Donde `#` es la cantidad de resultados que se desean obtener. Si no se desea limitar la cantidad de resultados basta con no agregar esta sección a la consulta.

## Contribuidores
* Renzo Angles.
* Roberto García.
* Sebastián Ferrada.
* Vicente Rojas.

## Licencia

Este software se distribuye como código abierto bajo la Licencia Apache, 
Versión 2.0 (la "Licencia"); no puede utilizar este archivo excepto en 
cumplimiento con la Licencia. Puede obtener una copia de la Licencia en:

http://www.apache.org/licenses/LICENSE-2.0

A menos que la ley aplicable lo requiera o se acuerde por escrito, el software
se distribuye "TAL CUAL", SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ya sean expresas o implícitas.
Consulte la Licencia para conocer el lenguaje específico que rige los permisos y
limitaciones bajo la Licencia.
