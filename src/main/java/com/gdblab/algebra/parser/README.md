
# ANTLR4 / Parser

Las clases se generan automaticamente por lo que no hay que programar nada en si del parser.
la clase RPQGrammarBaseListener es la que se encarga de verificar las entradas y salidas de las reglas de la gramatica.
Existen dos funciones por cada regla: Enter y Exit. Cada una se ejecuta cuando una cadena de texto es reconocido por una regla. Enter es para "hacer algo" antes de visitar la regla y Exit es para "hacer algo" cuando sale de la regla.

El proceso es el siguiente:
* El arbol generado por ANTLR posee hijos que son exactamente igual que los padres (a nivel de contenido), por lo que, primero, se realiza una "limpieza" del arbol de parseo y se deja de una manera mas facil de ejecutar.
* Luego se evalua cada nodo del arbol. partiendo por la raiz se pregunta por la cantidad de hijos, si tiene 2 hijos es porque es una operacion binaria como la union o la concatenacion. Si es solo un hijo, significa que es una operacion unaria y si no tiene hijos es porque llego a la hoja y debe retornar los paths seleccionados de dicho nodo.
* Una vez qaue termina la evaluacion de cada nodo del arbol, calcula el tiempo de ejecucion.