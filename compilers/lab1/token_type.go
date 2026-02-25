package main

type TokenType int

const (
	NUMBER TokenType = iota
	ID
	STRING
	VAR
	PRINT
	IF
	ELSE
	WHILE
	PLUS
	MINUS
	STAR
	SLASH
	EQ
	EQEQ
	EXCL
	NEQ
	LT
	GT
	LTEQ
	GTEQ
	AND
	OR
	LPAREN
	RPAREN
	LBRACE
	RBRACE
	SEMICOLON
	EOF
	UNKNOWN
)

func (t TokenType) String() string {
	switch t {
	case NUMBER:
		return "NUMBER"
	case ID:
		return "ID"
	case STRING:
		return "STRING"
	case VAR:
		return "VAR"
	case PRINT:
		return "PRINT"
	case IF:
		return "IF"
	case ELSE:
		return "ELSE"
	case WHILE:
		return "WHILE"
	case PLUS:
		return "PLUS"
	case MINUS:
		return "MINUS"
	case STAR:
		return "STAR"
	case SLASH:
		return "SLASH"
	case EQ:
		return "EQ"
	case EQEQ:
		return "EQEQ"
	case EXCL:
		return "EXCL"
	case NEQ:
		return "NEQ"
	case LT:
		return "LT"
	case GT:
		return "GT"
	case LTEQ:
		return "LTEQ"
	case GTEQ:
		return "GTEQ"
	case AND:
		return "AND"
	case OR:
		return "OR"
	case LPAREN:
		return "LPAREN"
	case RPAREN:
		return "RPAREN"
	case LBRACE:
		return "LBRACE"
	case RBRACE:
		return "RBRACE"
	case SEMICOLON:
		return "SEMICOLON"
	case EOF:
		return "EOF"
	case UNKNOWN:
		return "UNKNOWN"
	default:
		return "UNKNOWN"
	}
}
