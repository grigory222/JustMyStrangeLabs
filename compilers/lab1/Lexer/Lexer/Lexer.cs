using System.Collections.Generic;

namespace Lexer
{
    public class Lexer
    {
        private readonly string input;
        private int position;
        private readonly int length;

        public Lexer(string input)
        {
            this.input = input;
            this.length = input.Length;
            this.position = 0;
        }

        // Peek - смотрит на следующий символ без изменения позиции
        private char Peek(int offset = 1)
        {
            int peekPos = position + offset;
            return peekPos < length ? input[peekPos] : '\0';
        }

        // Next - возвращает текущий символ и продвигает позицию
        private char Next()
        {
            if (position >= length)
                return '\0';
            return input[position++];
        }

        // AddToken - добавляет токен в список
        private void AddToken(List<Token> tokens, TokenType type, string value, int startPos)
        {
            tokens.Add(new Token(type, value, startPos));
        }
        public List<Token> Tokenize()
        {
            var tokens = new List<Token>();
            while (position < length)
            {
                char current = input[position];
                if (char.IsWhiteSpace(current))
                {
                    position++;
                }
                else if (char.IsDigit(current))
                {
                    tokens.Add(ReadNumber());
                }
                else if (char.IsLetter(current) || current == '_')
                {
                    tokens.Add(ReadIdentifierOrKeyword());
                }
                else if (current == '"')
                {
                    tokens.Add(ReadString());
                }
                else
                {
                    tokens.Add(ReadSymbol());
                }
            }
            tokens.Add(new Token(TokenType.EOF, "", position));
            return tokens;
        }
        private Token ReadNumber()
        {
            int start = position;
            while (position < length && char.IsDigit(input[position]))
            {
                position++;
            }
            string value = input.Substring(start, position - start);
            return new Token(TokenType.NUMBER, value, start);
        }
        private Token ReadIdentifierOrKeyword()
        {
            int start = position;
            while (position < length && (char.IsLetterOrDigit(input[position]) || input[position] == '_'))
            {
                position++;
            }
            string value = input.Substring(start, position - start);
            return value switch
            {
                "var" => new Token(TokenType.VAR, value, start),
                "print" => new Token(TokenType.PRINT, value, start),
                "if" => new Token(TokenType.IF, value, start),
                "else" => new Token(TokenType.ELSE, value, start),
                "while" => new Token(TokenType.WHILE, value, start),
                _ => new Token(TokenType.ID, value, start)
            };
        }
        private Token ReadString()
        {
            int start = position;
            position++; // Skip opening quote
            while (position < length && input[position] != '"')
            {
                if (input[position] == '\\' && position + 1 < length)
                {
                    position++; // Skip escape character
                }
                position++;
            }
            if (position < length)
            {
                position++; // Skip closing quote
            }
            string value = input.Substring(start, position - start);
            return new Token(TokenType.STRING, value, start);
        }

        private Token ReadSymbol()
        {
            int start = position;
            char current = input[position];
            position++;

            // Проверка двухсимвольных операторов
            switch (current)
            {
                case '=':
                    if (Peek(0) == '=')
                    {
                        position++;
                        return new Token(TokenType.EQEQ, "==", start);
                    }
                    return new Token(TokenType.EQ, "=", start);

                case '!':
                    if (Peek(0) == '=')
                    {
                        position++;
                        return new Token(TokenType.NEQ, "!=", start);
                    }
                    return new Token(TokenType.EXCL, "!", start);

                case '<':
                    if (Peek(0) == '=')
                    {
                        position++;
                        return new Token(TokenType.LTEQ, "<=", start);
                    }
                    return new Token(TokenType.LT, "<", start);

                case '>':
                    if (Peek(0) == '=')
                    {
                        position++;
                        return new Token(TokenType.GTEQ, ">=", start);
                    }
                    return new Token(TokenType.GT, ">", start);

                case '&':
                    if (Peek(0) == '&')
                    {
                        position++;
                        return new Token(TokenType.AND, "&&", start);
                    }
                    return new Token(TokenType.UNKNOWN, "&", start);

                case '|':
                    if (Peek(0) == '|')
                    {
                        position++;
                        return new Token(TokenType.OR, "||", start);
                    }
                    return new Token(TokenType.UNKNOWN, "|", start);

                case '+':
                    return new Token(TokenType.PLUS, "+", start);
                case '-':
                    return new Token(TokenType.MINUS, "-", start);
                case '*':
                    return new Token(TokenType.STAR, "*", start);
                case '/':
                    return new Token(TokenType.SLASH, "/", start);
                case '(':
                    return new Token(TokenType.LPAREN, "(", start);
                case ')':
                    return new Token(TokenType.RPAREN, ")", start);
                case '{':
                    return new Token(TokenType.LBRACE, "{", start);
                case '}':
                    return new Token(TokenType.RBRACE, "}", start);
                case ';':
                    return new Token(TokenType.SEMICOLON, ";", start);
                default:
                    return new Token(TokenType.UNKNOWN, current.ToString(), start);
            }
        }
    }
}