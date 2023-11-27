import main_parser
import csv

def main():

    with open('/home/grigory/itmo/labs/infa/lab4/data/input.json') as f:
        text = f.read()
    
    my_dict = main_parser.json2dict(text)

    with open('/home/grigory/itmo/labs/infa/lab4/data/output.csv', 'w') as f:
        w = csv.DictWriter(f, my_dict.keys())
        w.writeheader()
        w.writerow(my_dict)


if __name__ == '__main__':
    main()