using Lexer;

Console.WriteLine("=== Lexer Test ===\n");

// Test code
string testCode = @"
var x = 10;
var name = ""Hello World"";
if (x > 5) {
    print(name);
}
while (x != 0) {
    x = x - 1;
}
";

Console.WriteLine("Input code:");
Console.WriteLine(testCode);
Console.WriteLine("\n=== Tokens ===\n");

var lexer = new Lexer.Lexer(testCode);
var tokens = lexer.Tokenize();

foreach (var token in tokens)
{
    Console.WriteLine(token);
}
