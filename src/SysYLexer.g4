lexer grammar SysYLexer;

// 识别关键字
CONST : 'const';
IF: 'if';
ELSE: 'else';
WHILE: 'while';
RETURN: 'return';
INT: 'int';
VOID: 'void';
BREAK : 'break';
CONTINUE : 'continue';

// 识别运算符
PLUS : '+';
MINUS : '-';
MUL : '*';
DIV : '/';
MOD : '%';
ASSIGN : '=';
EQ : '==';
NEQ : '!=';
LT : '<';
GT : '>';
LE : '<=';
GE : '>=';
NOT : '!';
AND : '&&';
OR : '||';
L_PAREN : '(';
R_PAREN : ')';
L_BRACE : '{';
R_BRACE : '}';
L_BRACKT : '[';
R_BRACKT : ']';
COMMA : ',';
SEMICOLON : ';';

// 识别标识符
IDENT : ([_a-zA-Z])([_a-zA-Z0-9])*;

// 识别整数字面量
INTEGR_CONST : [1-9][0-9]*  // 十进制
               | '0'
               | '0'([0-7])+ // 八进制
               | '0'('x'|'X')[0-9a-fA-F]+ // 十六进制
               ;

// 识别注释
LINE_COMMENT : '//' .*? '\n' -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;

// 忽略空白字符
WS : [ \r\n\t]+ -> skip;