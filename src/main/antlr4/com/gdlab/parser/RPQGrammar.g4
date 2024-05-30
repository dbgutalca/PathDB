grammar RPQGrammar;

query: expression EOF;
expression: label # edge
        | '!' label # negatedEdge
        | label '^' # reverseEdge
        | '(' expression ')' # parenthesis
	| expression '?' # optional
	| expression '+' # plus
	| expression '*' # star
	| expression '.' expression # concatenation
	| expression '|' expression # alternative
    ;

label: LETTER ( LETTER | DIGIT )*;
LETTER: [a-zA-Z];
DIGIT: [0-9];
WS: [ \t\r\n]+ -> skip;
// a (b* c+) concat(concat(label(a), star(label(b))), label(c))
// a.b|c
//conoce.sigue*.algo+

