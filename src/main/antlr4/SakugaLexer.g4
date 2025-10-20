lexer grammar SakugaLexer;

@header {
package com.github.eutkin.sakuga;
}

ATTRIBUTE
    : ':'.+?':'
    ;

BOOLEAN_ATTRIBUTE
    : ':'.+?':' -> skip
    ;

NUMBER_ATTRIBUTE
    : ':'.+?':' -> skip
    ;

STRING_ATTRIBUTE
    : ':'.+?':' -> skip
    ;

NOT
    : '!'
    ;

EQUALS
    : '='
    ;

NOT_EQUALS
    : '!='
    ;

GREATER_OR_EQUALS
    : '>='
    ;

LESS_OR_EQUALS
    : '<='
    ;

GREATER
    : '>'
    ;

LESS
    : '<'
    ;

BOOLEAN_LITERAL
    : 'true' | 'false'
    ;

NUMBER_LITERAL
    : [0-9][0-9]*
    | [0-9]+'.'[0-9]+
    ;


STRING_LITERAL
    : '\''.+?'\''
    ;

 WS
    : [ \t]+? -> skip
    ; // toss out whitespace