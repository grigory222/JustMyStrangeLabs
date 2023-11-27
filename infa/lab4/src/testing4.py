import time
import main_parser, re_parser, lib_parser

def test(foo):
    st = time.time()
    for _ in range(1000):
        foo()
    return time.time() - st

print(f"\n My own parser: {test(main_parser.main)} seconds\n",
      f"My regex parser: {test(re_parser.main)} seconds\n",
      f"Lib parser: {test(lib_parser.main)} seconds")
