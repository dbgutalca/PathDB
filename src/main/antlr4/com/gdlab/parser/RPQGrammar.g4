grammar RPQGrammar;

query: 'MATCH' restrictorsStatement? pathPattern returnStatement limitStatement? ';' EOF;

restrictorsStatement: 'WALK' | 'TRAIL' | 'ACYCLIC' | 'SIMPLE';
pathPattern: pathName '=' nodePatternLeft edgePattern nodePatternRight conditionalExpression?;
returnStatement: 'RETURN' returnOption (',' returnOption)*;
returnOption: '*' # returnAll
        | variable # returnVariable
        | variable '.' property # returnVariableWithProperty
        ;

limitStatement: 'LIMIT' unsignedInteger;
nodePatternLeft: nodePattern;
nodePatternRight: nodePattern;
nodePattern: '(' filterVar? ')';
edgePattern: '-['regularExpressionRule']' rangeRecursive? '->' ;
rangeRecursive: '{' rangeMinValue? '..' rangeMaxValue '}'
        |'{' rangeMinValue '..' rangeMaxValue? '}'
        ;

rangeMinValue: integer;
rangeMaxValue: integer;
pathName: id;
filterVar: id;

conditionalExpression: 'WHERE' conditionals;
conditionals: '(' conditionals ')' # parenthesisConditionals
        | conditionals 'AND' conditionals # andConditionals
        | conditionals 'OR' conditionals # orConditionals
        | conditionalsEvaluation # conditionalsEval
        ;

conditionalsEvaluation: variable '.' property ( comparisonString | comparisonNumber);
variable: id;
property: id;

comparisonString: '=' '"' string '"' # stringEquals
        | '!=' '"' string '"' # stringNotEquals
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
integer: '-'? DIGIT+;
decimal: '-'? DIGIT+ ('.' DIGIT+)?;
string: EXTENDEDLETTER*;

EXTENDEDLETTER: [0-9a-zA-Z\u00C0-\u00FF];
LETTER: [a-zA-Z];
DIGIT: [0-9];
WS: [ \t\r\n]+ -> skip;