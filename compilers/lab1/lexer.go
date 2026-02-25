package main

import "unicode"

type Lexer struct {
	input    string
	position int
	length   int
}

func NewLexer(input string) *Lexer {
	return &Lexer{
		input:    input,
		length:   len(input),
		position: 0,
	}
}

// Peek - смотрит на следующий символ без изменения позиции
func (l *Lexer) peek(offset int) rune {
	peekPos := l.position + offset
	if peekPos < l.length {
		return rune(l.input[peekPos])
	}
	return '\000'
}

// Next - возвращает текущий символ и продвигает позицию
func (l *Lexer) next() rune {
	if l.position >= l.length {
		return '\000'
	}
	current := rune(l.input[l.position])
	l.position++
	return current
}

func (l *Lexer) Tokenize() []Token {
	var tokens []Token
	for l.position < l.length {
		current := rune(l.input[l.position])
		if unicode.IsSpace(current) {
			l.position++
		} else if unicode.IsDigit(current) {
			tokens = append(tokens, l.readNumber())
		} else if unicode.IsLetter(current) || current == '_' {
			tokens = append(tokens, l.readIdentifierOrKeyword())
		} else if current == '"' {
			tokens = append(tokens, l.readString())
		} else {
			tokens = append(tokens, l.readSymbol())
		}
	}
	tokens = append(tokens, NewToken(EOF, "", l.position))
	return tokens
}

func (l *Lexer) readNumber() Token {
	start := l.position
	for l.position < l.length && unicode.IsDigit(rune(l.input[l.position])) {
		l.position++
	}
	value := l.input[start:l.position]
	return NewToken(NUMBER, value, start)
}

func (l *Lexer) readIdentifierOrKeyword() Token {
	start := l.position
	for l.position < l.length && (unicode.IsLetter(rune(l.input[l.position])) || unicode.IsDigit(rune(l.input[l.position])) || l.input[l.position] == '_') {
		l.position++
	}
	value := l.input[start:l.position]
	switch value {
	case "var":
		return NewToken(VAR, value, start)
	case "print":
		return NewToken(PRINT, value, start)
	case "if":
		return NewToken(IF, value, start)
	case "else":
		return NewToken(ELSE, value, start)
	case "while":
		return NewToken(WHILE, value, start)
	default:
		return NewToken(ID, value, start)
	}
}

func (l *Lexer) readString() Token {
	start := l.position
	l.position++ // Skip opening quote
	for l.position < l.length && l.input[l.position] != '"' {
		if l.input[l.position] == '\\' && l.position+1 < l.length {
			l.position++ // Skip escape character
		}
		l.position++
	}
	if l.position < l.length {
		l.position++ // Skip closing quote
	}
	value := l.input[start:l.position]
	return NewToken(STRING, value, start)
}

func (l *Lexer) readSymbol() Token {
	start := l.position
	current := rune(l.input[l.position])
	l.position++

	// Проверка двухсимвольных операторов
	switch current {
	case '=':
		if l.peek(0) == '=' {
			l.position++
			return NewToken(EQEQ, "==", start)
		}
		return NewToken(EQ, "=", start)

	case '!':
		if l.peek(0) == '=' {
			l.position++
			return NewToken(NEQ, "!=", start)
		}
		return NewToken(EXCL, "!", start)

	case '<':
		if l.peek(0) == '=' {
			l.position++
			return NewToken(LTEQ, "<=", start)
		}
		return NewToken(LT, "<", start)

	case '>':
		if l.peek(0) == '=' {
			l.position++
			return NewToken(GTEQ, ">=", start)
		}
		return NewToken(GT, ">", start)

	case '&':
		if l.peek(0) == '&' {
			l.position++
			return NewToken(AND, "&&", start)
		}
		return NewToken(UNKNOWN, "&", start)

	case '|':
		if l.peek(0) == '|' {
			l.position++
			return NewToken(OR, "||", start)
		}
		return NewToken(UNKNOWN, "|", start)

	case '+':
		return NewToken(PLUS, "+", start)
	case '-':
		return NewToken(MINUS, "-", start)
	case '*':
		return NewToken(STAR, "*", start)
	case '/':
		return NewToken(SLASH, "/", start)
	case '(':
		return NewToken(LPAREN, "(", start)
	case ')':
		return NewToken(RPAREN, ")", start)
	case '{':
		return NewToken(LBRACE, "{", start)
	case '}':
		return NewToken(RBRACE, "}", start)
	case ';':
		return NewToken(SEMICOLON, ";", start)
	default:
		return NewToken(UNKNOWN, string(current), start)
	}
}
