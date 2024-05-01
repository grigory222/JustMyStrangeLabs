ORG 0x0
V0: WORD $default, 0x180
V1: WORD $default, 0x180
V2: WORD $int2,      0x180
V3: WORD $int3,      0x180
V4: WORD $default, 0x180
V5: WORD $default, 0x180
V6: WORD $default, 0x180
V7: WORD $default, 0x180

ORG 0x01A
X: WORD 0

max: WORD 0x002D    ;  45 максимальное значение Х
min: WORD 0xFFD9     ; -39 минимальное значение Х
default:    IRET          ; Обработка прерывания по умолчанию


START:    DI
      CLA
      OUT 0x1   ; Запрет прерываний для неиспользуемых ВУ
     OUT 0x3
      OUT 0xB 
      OUT 0xE
      OUT 0x12
      OUT 0x16
      OUT 0x1A
      OUT 0x1E
  LD #0xA    ; Загрузка в аккумулятор MR (1|010=1010)
      OUT 5     ; Разрешение прерываний для 2 ВУ
      LD #0xB    ; Загрузка в аккумулятор MR (1|011=1011)
      OUT 7     ; Разрешение прерываний для 3 ВУ
      EI
;2d
main:   DI     ; Запрет прерываний чтобы обеспечить атомарность операции
     LD X
  INC
     CALL check
      ST X
      EI
      JUMP main

int2:  DI    ; Обработка прерывания на ВУ-2
     LD X ; DEBUG
     HLT
     IN 4
  	NOP
  	SUB X
     ST X
     HLT ; NOP
     EI
     IRET

int3: DI
      PUSH  
      LD X
      NOP ;HLT
      ADD X
      ADD X
      SUB #10
      OUT 6
      HLT ;NOP
      POP
      EI
      IRET
      
check:              ; Проверка принадлежности X к ОДЗ
check_min:  
	CMP min     ; Если x > min переход на проверку верхней границы
     BGE check_max
     JUMP ld_min    ; Иначе загрузка min в аккумулятор
check_max:   CMP max     ; Проверка пересечения верхней границы X
      BLT return    ; Если x < max переход
ld_min:  LD min      ; Загрузка минимального значения в X
return:  RET        ; Метка возврата из проверки на ОДЗ
