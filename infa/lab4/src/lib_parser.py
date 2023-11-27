import json
import yaml

def main():
    dict_ = json.loads(open('/home/grigory/itmo/labs/infa/lab4/data/input_hard.json').read())
    f = open('/home/grigory/itmo/labs/infa/lab4/data/output_hard.yml', 'w', encoding='utf-8')
    yaml.dump(dict_, f, encoding='utf-8', allow_unicode=True)
