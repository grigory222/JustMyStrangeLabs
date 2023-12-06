package substances;

public class Water extends Substance {
    public Water(double weight, double volume){
        super("Вода", "H2O", TypeOfSubstance.WATER, weight, volume);
    }
}