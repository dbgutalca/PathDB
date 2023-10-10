grammar RPQGrammar;

query: expression EOF;
expression: term ( separator term)*;
term: base operator?;
base: '!'? ID | '(' expression ')';
operator: '*' | '+' | '?';
separator: '.';

ID: LETTER ( LETTER | DIGIT)*;
LETTER: [a-zA-Z];
DIGIT: [0-9];
WS: [ \t\r\n]+ -> skip;
// a b* c+

//conoce.sigue*.algo+

