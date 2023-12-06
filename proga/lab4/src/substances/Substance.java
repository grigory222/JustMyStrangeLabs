package substances;

import exceptions.IncorrectParameterException;
import java.util.Objects;

public abstract class Substance {
    private final String name;
    private final String formula;
    private final TypeOfSubstance type;
    protected double weight; // масса в кг
    protected double volume; // объём в кубометрах

    protected Substance(String name, String formula, TypeOfSubstance type, double weight, double volume){
        if (volume < 0 || weight < 0) throw new IncorrectParameterException("Объём или масса не может быть отрицательным числом");
        if (type == TypeOfSubstance.UNKNOWN) throw new IncorrectParameterException("Неизвестный тип вещества");

        this.name = name;
        this.formula = formula;
        this.type = type;
        this.weight = weight;
        this.volume = volume;
    }

    public double getWeight() {
        return weight;
    }
    public double getVolume() {
        return volume;
    }
    private String getFormula(){
        return formula;
    }
    public String getName(){
        return name;
    }
    public TypeOfSubstance getType(){
        return type;
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
