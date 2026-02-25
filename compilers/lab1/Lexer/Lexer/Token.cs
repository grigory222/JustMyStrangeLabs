namespace Lexer
{
    public class Token
    {
        public TokenType Type { get; }
        public string Value { get; }
        public int Position { get; }
        public Token(TokenType type, string value, int position)
        {
            Type = type;
            Value = value;
            Position = position;
        }
        public override string ToString()
        {
            return $"Token(Type: {Type}, Value: '{Value}', Position: {Position})";
        }
    }
}