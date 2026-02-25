package main

import "fmt"

type Token struct {
	Type     TokenType
	Value    string
	Position int
}

func NewToken(tokenType TokenType, value string, position int) Token {
	return Token{
		Type:     tokenType,
		Value:    value,
		Position: position,
	}
}

func (t Token) String() string {
	return fmt.Sprintf("Token(Type: %s, Value: '%s', Position: %d)", t.Type, t.Value, t.Position)
}
