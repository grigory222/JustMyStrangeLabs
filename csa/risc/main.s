    .data
input_addr:      .word  0x80
output_addr:     .word  0x84
sum_hi:          .word  0x0
sum_lo:          .word  0x0

    .text

    .org 0x200

_start:

    lui      t0, %hi(input_addr)
    addi     t0, t0, %lo(input_addr)
    lw       t0, 0(t0)

    lui      t1, %hi(output_addr)
    addi     t1, t1, %lo(output_addr)
    lw       t1, 0(t1)

    lui      t2, %hi(sum_hi)
    addi     t2, t2, %lo(sum_hi)

    lui      t3, %hi(sum_lo)
    addi     t3, t3, %lo(sum_lo)

    jal      ra, solution

    jal      ra, print_result

    halt

solution:
    ; положить на стек адрес возврата
    addi     sp, sp, -4
    sw       ra, 0(sp)

loop:
    ; считать слово
    jal      ra, read_word

    ; если = 0, то break
    beqz     a0, end_loop

    ; сумировать с имеющимся результатом
    jal      ra, sum_word
    j        loop

end_loop:

    ; взять со стека адрес возврата и вернуться
    lw       ra, 0(sp)
    addi     sp, sp, 4
    jr        ra

read_word:
    ; нет вложенных вызовов,
    ; поэтому можно не класть адрес возврата на стек
    lw       a0, 0(t0)
    jr       ra

sum_word:
    lw       a1, 0(t3)
    lw       a2, 0(t2)
    add      a3, a1, a0
    ble      a0, zero, no_carry                
    bgtu     a3, a1, no_carry
    addi     a2, a2, 1                       ; если переполнение, то в старшую часть +1
    sw       a2, 0(t2)
no_carry:
    sw       a3, 0(t3)
    jr       ra

print_result:
    lw       a1, 0(t2)
    lw       a2, 0(t3)

    sw       a1, 0(t1)
    sw       a2, 0(t1)
    jr       ra
