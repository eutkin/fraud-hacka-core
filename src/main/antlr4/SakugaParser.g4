parser grammar SakugaParser;

@header {
package com.github.eutkin.sakuga;
}

options { tokenVocab = SakugaLexer; }

line
    : expression EOF
    ;

expression
    : boolean_expression
    ;

boolean_expression
    : boolean_expression op=(EQUALS | NOT_EQUALS) boolean_expression #BooleanExpression
    | string_expression op=(EQUALS | NOT_EQUALS | GREATER | LESS | GREATER_OR_EQUALS | LESS_OR_EQUALS) string_expression #StringExpression
    | number_expression op=(EQUALS | NOT_EQUALS | GREATER | LESS | GREATER_OR_EQUALS | LESS_OR_EQUALS) number_expression #NumberExpression
    ;

number_expression
    : NUMBER_LITERAL #NumberLiteral
    | NUMBER_ATTRIBUTE #NumberAttribute
    ;

string_expression
    : STRING_LITERAL   #StringLiteral
    | STRING_ATTRIBUTE #StringAttribute
    ;