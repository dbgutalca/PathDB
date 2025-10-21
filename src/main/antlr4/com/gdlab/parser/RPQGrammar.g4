grammar RPQGrammar;

query: MATCH restrictorsStatement? pathPattern returnStatement limitStatement? ';' EOF;

restrictorsStatement: 'WALK' | 'TRAIL' | 'ACYCLIC' | 'SIMPLE';
pathPattern: pathName '=' nodePatternLeft edgePattern nodePatternRight conditionalExpression?;
returnStatement: 'RETURN' returnOption (',' returnOption)*;
returnOption: variable # returnVariable
        | variable '.' property # returnVariableWithProperty
        | 'FIRST()' # returnFirst
        | 'FIRST().' property # returnFirstWithProperty
        | 'LAST()' # returnLast
        | 'LAST().' property # returnLastWithProperty
        | 'NODE(' unsignedInteger ')' # returnNode
        | 'NODE(' unsignedInteger ').' property # returnNodeWithProperty
        | 'EDGE(' unsignedInteger ')' # returnEdge
        | 'EDGE(' unsignedInteger ').' property # returnEdgeWithProperty
        | 'LABEL(NODE(' unsignedInteger '))' # returnLabelNode
        | 'LABEL(EDGE(' unsignedInteger '))' # returnLabelEdge
        | 'LABEL(FIRST())' # returnLabelFirst
        | 'LABEL(LAST())' # returnLabelLast
        ;

limitStatement: 'LIMIT' unsignedInteger;
nodePatternLeft: nodePattern;
nodePatternRight: nodePattern;
nodePattern: '(' filterVar? ')';
edgePattern: '-['regularExpressionRule']' rangeRecursive? '->' ;
rangeRecursive: '{..' rangeMaxValue '}';

// rangeMinValue: integer;
rangeMaxValue: unsignedInteger;
pathName: id;
filterVar: id;

conditionalExpression: 'WHERE' conditionals;
conditionals: '(' conditionals ')' # parenthesisConditionals
        | conditionals 'AND' conditionals # andConditionals
        | conditionals 'OR' conditionals # orConditionals
        | conditionalsEvaluation # conditionalsEval
        | conditionalsFunction # conditionalsEvalFunction
        ;

conditionalsEvaluation: variable '.' property ( comparisonString | comparisonNumber);
conditionalsFunction: 'FIRST().' property ( comparisonString | comparisonNumber)
        | 'LAST().' property ( comparisonString | comparisonNumber)
        | 'NODE(' unsignedInteger ').' property ( comparisonString | comparisonNumber)
        | 'EDGE(' unsignedInteger ').' property ( comparisonString | comparisonNumber)
        | 'LABEL(NODE(' unsignedInteger '))' comparisonString
        | 'LABEL(EDGE(' unsignedInteger '))' comparisonString
        | 'LABEL(FIRST())' comparisonString
        | 'LABEL(LAST())' comparisonString
        ;

variable: id;
property: id;

comparisonString: '=' STRING # stringEquals
        | '!=' STRING # stringNotEquals
        ;

comparisonNumber: '=' decimal # numberEquals
        | '!=' decimal # numberNotEquals
        | '>' decimal # numberGreaterThan
        | '<' decimal # numberLessThan
        | '>=' decimal # numberGreaterThanOrEquals
        | '<=' decimal # numberLessThanOrEquals
        ;

regularExpressionRule: regularExpression;
regularExpression: id # edge
        | '!' id # negatedEdge
        | id '^' # reverseEdge
        | '(' regularExpression ')' # parenthesis
	| regularExpression '?' # optional
	| regularExpression '+' # plus
	| regularExpression '*' # star
	| regularExpression '.' regularExpression # concatenation
        | regularExpression '|' regularExpression # alternative
        ;

id: LETTER (LETTER | DIGIT)*;
unsignedInteger: DIGIT+;
integer: '-'? DIGIT+ ;
decimal: '-'? DIGIT+ ('.' DIGIT+)?;

MATCH: [Mm][Aa][Tt][Cc][Hh];
STRING: '"' (~["\\] | '\\' .)* '"' ;
LETTER: [a-zA-Z];
DIGIT: [0-9];
WS: [ \t\r\n]+ -> skip;