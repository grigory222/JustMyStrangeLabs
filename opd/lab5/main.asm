ORG 0x140
STR_ADDR:    WORD 0x5F9   ; адрес строки
CURRENT:     WORD ?       ; адрес текущих 2 символов
FINISH_SYM:  WORD 0x0D    ; символ конца строки
TMP:         WORD ?       ; временное хранилище старшего байта
START:
  CLA
  LD STR_ADDR
  ST CURRENT
SYM1:
  CLA              ; очистить AC от старых символов
  IN 5             ; получить признак готовности
  AND #0x40        ; проверить готовность
  BZS SYM1         ; если не готовы, повторить
  IN 4             ; записать DR#4 в AL
  CMP FINISH_SYM   ; если символ конца строки
  BEQ SAVE_AND_HLT ; сохранить и завершить программу
  SWAB             ; записать символ в AH
  ST TMP           ; сохранить первый символ
SYM2:
  IN 5             ; получить признак готовности
  AND #0x40        ; проверить готовность
  BZS SYM2         ; если не готовы, повторить
  IN 4             ; записать DR#4 в AL
  ADD TMP          ; записать первый символ в AH
  ST (CURRENT)+    ; сохранить очередные 2 символа
  SXTB             ; проверяем только второй символ
  CMP FINISH_SYM   ; равно 0x0D (Carriage Return) ?
  BEQ _HLT         ; завершаем программу, без сохранения (т.к. уже сохранили)

  BR SYM1          ; повторить чтение
SAVE_AND_HLT:
  SWAB             ; 0x0D лежит в младшем байте AC
  ST (CURRENT)     ; сохранить последний символ
_HLT:
  HLT
ORG 0x5F9
STR: WORD ?

