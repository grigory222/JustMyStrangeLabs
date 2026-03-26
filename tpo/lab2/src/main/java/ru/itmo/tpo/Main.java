package ru.itmo.tpo;

import ru.itmo.tpo.log.Ln;
import ru.itmo.tpo.log.Log;
import ru.itmo.tpo.system.FunctionSystem;
import ru.itmo.tpo.system.LogPart;
import ru.itmo.tpo.system.TrigPart;
import ru.itmo.tpo.trig.Cos;
import ru.itmo.tpo.trig.Cot;
import ru.itmo.tpo.trig.Sin;
import ru.itmo.tpo.trig.Tan;

public class Main {

    public static void main(String[] args) throws Exception {
        Sin sin = new Sin();
        Cos cos = new Cos(sin);
        TrigPart trig = new TrigPart(new Tan(sin, cos), cos, new Cot(sin, cos));

        Ln ln = new Ln();
        LogPart log = new LogPart(ln, new Log(ln, 2), new Log(ln, 3),
                new Log(ln, 5), new Log(ln, 10));

        FunctionSystem system = new FunctionSystem(trig, log);

        CsvWriter.write(system, -2 * Math.PI, 10.0, 0.00005, "function.csv");

        System.out.println("Готово: function.csv");
    }
}
