    .data

.org             0x200
input_addr:      .word  0x80
output_addr:     .word  0x84
count:           .word  0x00
n:               .word  0x00
one:             .word  0x01
mask:            .word  0x7fffffff

    .text

_start:
    load_ind     input_addr
    store        n

    ble          negative
    jmp          while

negative:
    and          mask
    store        n
    load_imm     1
    store        count
    load         n
    ;    while n > 0:
    ;        count += n & 1
    ;        n >>= 1

while:
    and          one                         ; n & 1
    add          count
    bvs          overflow
    store        count                       ; count += n & 1

    load         n
    beqz         end_while
    shiftr       one                         ; n >>= 1
    store        n
    jmp          while

end_while:
    load         count
    jmp          exit

overflow:
    load_imm     0xCCCCCCCC
    jmp          exit

exit:
    store_ind    output_addr
    halt
