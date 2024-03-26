grammar RPQGrammar;

query: expression EOF;
expression: label
	| expression '?'
	| expression '+'
	| expression '*'
	| expression '|' expression
	| expression '.' expression
    | '(' expression ')'
    ;

label: '!'? LETTER ( LETTER | DIGIT)*;
LETTER: [a-zA-Z];
DIGIT: [0-9];
WS: [ \t\r\n]+ -> skip;
// a b* c+

//conoce.sigue*.algo+

