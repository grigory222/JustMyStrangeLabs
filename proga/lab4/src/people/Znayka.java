package people;

import exceptions.ZeroSubstancePropertyException;
import additional.MyHashMap;
import substances.Substance;
import substances.TypeOfSubstance;


import static substances.TypeOfSubstance.*;

public class Znayka extends Person implements CanAnalyze{

    //private HashMap<TypeOfSubstance, String> formulas = new HashMap<>();
    private MyHashMap<TypeOfSubstance, String> formulas = new MyHashMap<>();
    private final Crucible crucible = new Crucible();
    private final Flame flame = new Flame();

    public Znayka() {
        super("Знайка", 152);
        formulas.put(GOLD, "Au");
        formulas.put(PLATINUM, "Pt");
        formulas.put(SULFURIC_ACID, "H2SO4");
        formulas.put(NITRIC_ACID, "HNO3");
        formulas.put(MOONROCK, "???");
        formulas.put(WATER, "H2O");
        formulas.put(ALCOHOL, "C2H6O");
    }

    @Override
    public Boolean isItSolid(Substance s) {
        TypeOfSubstance type = s.getType();
        return type == GOLD || type == PLATINUM || type == MOONROCK;
    }

    @Override
    public Boolean isItLiquid(Substance s){
        TypeOfSubstance type = s.getType();
        return type != GOLD && type != PLATINUM && type != MOONROCK;
    }

    @Override
    public String analyze(Substance s) {
        // Знайка анализирует химическую тип и получает химическую формулу вещ-ва
        return formulas.get(s.getType());
    }

    @Override
    public String joinSubstances(Substance s1, Substance s2) throws ZeroSubstancePropertyException {

        if (s1.getWeight() < 2*Double.MIN_VALUE || s2.getWeight() == 0)
            throw new ZeroSubstancePropertyException("Невозможно соединить вещество(а) с нулевой массой");

        if (s1.getVolume() < 2*Double.MIN_VALUE || s2.getVolume() == 0)
            throw new ZeroSubstancePropertyException("Невозможно соединить вещество(а) с нулевым объёмом");

        if (isItLiquid(s1) && isItSolid(s2)){
            Substance x = s1;
            s1 = s2;
            s2 = x;
        }

        if ( isItSolid(s1) && isItSolid(s2) ){
            return "Нельзя смешать два твердых вещества";
        }

        if ( isItSolid(s1) && isItLiquid(s2) ){
            if (s1.getType() != MOONROCK && s2.getType() == SULFURIC_ACID || s2.getType() == NITRIC_ACID && s1.getType() == METAL){
                return s1.getName() + " растворилось в " + s2.getName();
            }
            return s1.getName() + " не растворилось в " + s2.getName();
        }

        if (isItLiquid(s1) && isItLiquid(s2)){
            return s1.getName() + " смешали с " + s2.getName();
        }

        return "Не удаётся получить результат смешивания";
    }

    @Override
    public String heatAnalyze(Substance substance) {
        return "Термический анализ:" +
                "\n\t" + flame.heat(substance) +
                "\n\t" + crucible.warm(substance);
    }

    // Внутренний класс тигель
    private class Crucible{
        private String warm(Substance substance){
            return "Вещество " + substance.getName() + " нагрето";
        }
    }

    // Внутренний класс пламя
    private class Flame{
        private String heat(Substance substance){
            if (substance.getType() == TypeOfSubstance.MOONROCK)
                return "Не удаётся сжечь " + substance.getName();
            else
                return substance.getName() + " cожжён";
        }
    }
}
