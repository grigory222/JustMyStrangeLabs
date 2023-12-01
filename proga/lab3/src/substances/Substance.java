package substances;

import java.util.Objects;

public abstract class Substance {
    private final String name;
    private final String formula;
    private final TypeOfSubstance type;

    protected Substance(String name, String formula, TypeOfSubstance type){
        this.name = name;
        this.formula = formula;
        this.type = type;
    }

    public String getName(){
        return name;
    }

    public TypeOfSubstance getType(){
        return type;
    }
    private String getFormula(){
        return formula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Substance substance = (Substance) o;
        return Objects.equals(name, substance.name) && Objects.equals(formula, substance.formula) && type == substance.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, formula, type);
    }

    @Override
    public String toString() {
        return "Substance{" +
                "name='" + name + '\'' +
                ", formula='" + formula + '\'' +
                ", type=" + type +
                '}';
    }
}
