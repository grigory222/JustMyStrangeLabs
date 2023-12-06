package substances;

public class Alcohol extends Substance {
    public Alcohol(double weight, double volume){
        super("Спирт", "C2H6O", TypeOfSubstance.WATER, weight, volume);
    }
}