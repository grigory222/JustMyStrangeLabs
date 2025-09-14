    .data

buf:             .byte  'Hello, _________________________'
protection:      .byte  0,0
name_size:       .word  23
input_addr:      .word  0x80
output_addr:     .word  0x84
count_hello:     .word  7
exclamation:     .byte  '!', 0, '__'
question:        .byte  'What is your name?\n', 0

    .text
    .org 0x1000
_start:
    lit question a!
    print_cstr

    prepare_string

    lit buf a!
    print_cstr

    halt

prepare_string:
    @p count_hello           \ a = buf + 7
    lit buf +
    a!

    @p input_addr b!         \ b for input

    @p name_size             \ counter

while:
    dup                      \ check if counter == 0
    if bad

    @b lit 255 and           \ read from input

    dup if zero              \ if char == 0 then input finished
    skip ;
zero:                        
    @p exclamation !+        \ if 0 -> place !,0 and continue reading
skip:
    dup                      \ if char == 10 then input finished
    lit 10 inv               \
    lit 1 +                  \ ~10 + 1
    +                        \ char - 10
    if done

    !+                       \ save symbol

    lit -1 +
    while ;

done:
    @p exclamation !         \ save "!"
    ;
bad:
    @p output_addr b!
    lit 0xCCCCCCCC !b         
    halt


    \ a points to c-string
print_cstr:
    @p output_addr b!
loop:
    @+ lit 255 and           \ read c-string
    dup
    if ret

    !b

    loop ;

ret:
    ;

