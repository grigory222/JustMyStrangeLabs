package people;

import substances.Alcohol;
import additional.MyHashMap;
import substances.Substance;
import substances.TypeOfSubstance;


import static substances.TypeOfSubstance.*;

public class Znayka extends Person implements CanAnalyze, Cloneable{

    //private HashMap<TypeOfSubstance, String> formulas = new HashMap<>();
    private MyHashMap<TypeOfSubstance, String> formulas = new MyHashMap<>();

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
        if (type == GOLD || type == PLATINUM || type == MOONROCK)
            return true;
        return false;
    }

    @Override
    public Boolean isItLiquid(Substance s){
        TypeOfSubstance type = s.getType();
        if (type == GOLD || type == PLATINUM || type == MOONROCK)
            return false;
        return true;
    }

    @Override
    public String analyze(Substance s) {
        // Знайка анализирует химическую тип и получает химическую формулу вещ-ва
        return formulas.get(s.getType());

//        switch (s.getType()){
//            case WATER:
//                return "H2O";
//            case ALCOHOL:
//                return "C2H6O";
//            case GOLD:
//                return "Au";
//            case PLATINUM:
//                return "Pt";
//            case NITRIC_ACID:
//                return "HNO3";
//            case SULFURIC_ACID:
//                return "H2SO4";
//            case MOONROCK:
//                return "???";
//        }
//        return "Неизвестная формула";
    }


    @Override
    public String joinSubstances(Substance s1, Substance s2) {
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


}
