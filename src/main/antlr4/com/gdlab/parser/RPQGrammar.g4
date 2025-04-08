grammar RPQGrammar;

query: 'MATCH' restrictors spath '=' '(' sfiltervar ')-[' regularexpression ']->(' tFilterVar ')' conditionalexpression EOF;
restrictors: 'TRAIL' | 'SIMPLE' | 'ACYCLIC' | 'WALKS' ;
spath: LETTER ( LETTER | DIGIT )*;
sfiltervar: LETTER ( LETTER | DIGIT )*;
tFilterVar: LETTER ( LETTER | DIGIT )*;
conditionalexpression: 'WHERE' conditionals;

conditionals: '(' conditionals ')' # parenthesisConditionals
        | conditionals 'AND' conditionals # andConditionals
        | conditionals 'OR' conditionals # orConditionals
        | conditionalsevaluation # conditionalsEval
        ;

conditionalsevaluation: filterevaluationvar '.' sproperty comparisonconditional comparisonvalue;
filterevaluationvar: LETTER ( LETTER | DIGIT )*;
sproperty: LETTER ( LETTER | DIGIT )*;
comparisonconditional: '=' | '>' | '<' | '>=' | '<=' | '!=';
comparisonvalue: ( LETTER | DIGIT )+;

regularexpression: label # edge
        | '!' label # negatedEdge
        | label '^' # reverseEdge
        | '(' regularexpression ')' # parenthesis
	| regularexpression '?' # optional
	| regularexpression '+' # plus
	| regularexpression '*' # star
	| regularexpression '.' regularexpression # concatenation
        | regularexpression '|' regularexpression # alternative
        ;

label: LETTER ( LETTER | DIGIT )*;
LETTER: [a-zA-Z];
DIGIT: [0-9];
WS: [ \t\r\n]+ -> skip;
