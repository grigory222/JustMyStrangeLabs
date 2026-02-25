package main

import "fmt"

func main() {
	fmt.Println("=== Lexer Test ===\n")

	// Test code
	testCode := `
var x = 10;
var name = "Hello World";
if (x > 5) {
    print(name);
}
while (x != 0) {
    x = x - 1;
}
`

	fmt.Println("Input code:")
	fmt.Println(testCode)
	fmt.Println("\n=== Tokens ===\n")

	lexer := NewLexer(testCode)
	tokens := lexer.Tokenize()

	for _, token := range tokens {
		fmt.Println(token)
	}
}
