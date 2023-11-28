
def error(s : str):
    print(f"=========== ОШИБКА ===========\n{s}")
    exit(0)

def parse_int(s: str):
    return int(s)

def parse_string(s: str):
    return s[1:-1]

def parse_bool(s: str):
    return True if s == 'true' else False

def isInt(s: str):
    return s.isdigit()

def isString(s: str):
    return len(s) > 1 and s.strip()[0] == s.strip()[-1] == '"'

def isBool(s: str):
    return s in ['true', 'false']

# парсит примитивные типы
def parse_smth(s: str):
    d = {
        isInt : parse_int,
        isString : parse_string,
        isBool : parse_bool
    }
    
    for foo1, foo2 in d.items():
        if foo1(s):
            return foo2(s)
    error(f'Неизвестный примитивный тип! {s}')


def findEndOfBrackets(s : str, i : int, brackets : str):
    # проверяет строку s на корректность скобок brackets начиная с символа i и возвращает индекс закрывающей скобки + 1
    # в случае некорректнности кидает ошибку

    num_brackets = 0 # кол-во открытых скобок
    while True:
        num_brackets += (s[i] == brackets[0]) - ((s[i] == brackets[1]))
        i += 1
        if i >= len(s) or num_brackets == 0:
            break

    if num_brackets != 0:
        error('Некорректная скобочная структура')
    else:
        return i

def findEndQoute(s: str, i: int):
    # находит закрывающую кавычку и возвращает индекс + 1
    # при отсутствии кидает error
    while True:                    
        i += 1
        if i >= len(s):
            error('Нет закрывающей кавычки')
        if s[i] == '"':                        
            break
    i += 1
    return i


def parse_values(s: str, i: int):
    # понять чтО это - объект, массив, число или строка. и вызвать соответствующую функцию
    while i < len(s):
        # массив
        if s[i] == '[': 
            new_i = findEndOfBrackets(s, i, '[]')
            return parse_array(s[i : new_i]), new_i
        # объект
        elif s[i] == '{': 
            new_i = findEndOfBrackets(s, i, '{}')
            return parse_object(s[i : new_i]), new_i
        # число, строка
        elif s[i].isalnum() or s[i] == '_':
            cur = ''
            # найти конец этого значения
            while i < len(s) and (s[i].isalnum() or s[i] == '_'):
                cur += s[i]
                i += 1

            if i >= len(s):
                error('Число, строка или булево значение не заканчивается!') 

            return parse_smth(cur), i
        
        else:
            i += 1

    error('Неизвестный тип значения')


    

def parse_array(s : str):
    s = s.strip()[1:-1] # delete [, ]
    res = []
    i = 0
    while i < len(s):
        if s[i] == '"':
            new_i = findEndQoute()
            res.append(s[i + 1 : new_i])
            i = new_i
        elif s[i] in '{[' or s[i].isalnum() or s[i] == '_':
            value, i = parse_values(s, i)
            res.append(value)
        else:
            i += 1
    return res



def parse_object(s : str):
    s = s.strip()[1:-1] # delete {, }
    res = {}
    key = None
    i = 0
    while i < len(s):        
        if s[i] == '"': # найти закрывающую кавычку. понять - это ключ или уже значение                

            new_i = findEndQoute(s, i)
            # если это ключ
            if key is None: 
                key = s[i + 1 : new_i - 1]
            # если это значение
            else:
                res[key] = s[i + 1 : new_i - 1]
                key = None

            i = new_i

        elif  s[i] == '[' or s[i] == '{' or (s[i].isalnum() or s[i] == '_'):
            if key is None:
                error('Не найден ключ')
            values, i = parse_values(s, i)
            res[key] = values
            key = None

        else:
            i += 1
                
    return res

def json2dict(data: str):
    data = data.strip()
    if len(data) < 2 or data[0] != '{' or data[-1] != '}':
        error('Некорректный JSON файл')
    return parse_object(data)


def list2yaml(l: list, tab: str, isFirst: bool):
    res = ""
    for i, e in zip(range(len(l)), l):
        if isinstance(e, list):
            tab += ' '
            res += f"{tab[:-2]}- {list2yaml(e, tab, True)}\n"
            tab = tab[:-1]
        elif isinstance(e, dict):
            tab += ' '
            res += f"{tab[:-2]}- {dict2yaml(e, tab, True)}\n"
            tab = tab[:-1]
        else:
            res += f"{tab[:-2]}- {str(e)}\n"
    return res


def dict2yaml(d: dict, tab: str, isFirst: bool):
    res = ""
    for i, (key, value) in zip(range(len(d)), d.items()):
        if isinstance(value, dict):
            tab += ' '
            if not isFirst:
                res += f"{tab[:-1]}{key}:\n{dict2yaml(value, tab, i == 0)}"
            else: # если флаг = 1, то отступ не нужен
                res += f"{key}:\n{dict2yaml(value, tab, i == 0)}"
            tab = tab[:-1]
        elif isinstance(value, list):
            tab += ' '
            if not isFirst:
                res += f"{tab[:-1]}{key}:\n{list2yaml(value, tab, i == 0)}"
            else: # если флаг = 1, то отступ не нужен
                res += f"{key}:\n{list2yaml(value, tab, i == 0)}"         
            #res += f"{tab[:-1]}{key}:\n{list2yaml(value, tab, i == 0)}"
            tab = tab[:-1]
        else:
            if isFirst:
                #res += f"{tab[:-1]}{key}: {str(value)}\n"
                res += f"{key}: {str(value)}\n"
                isFirst = False
            else:
                res += f"{tab}{key}: {str(value)}\n"
    return res

def dump2yaml(d: dict):
    return f"---\n{dict2yaml(d, '', 0)}"
   



def main():
    with open("/home/grigory/itmo/labs/infa/lab4/data/input_hard.json", "r") as f:
        json_data = f.read()

    yaml_data = dump2yaml(json2dict(json_data))

    with open('/home/grigory/itmo/labs/infa/lab4/data/output_hard.yml', "w") as f:
        f.write(yaml_data)
    

if __name__ == "__main__":
    main()