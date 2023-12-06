package substances;

import static substances.TypeOfSubstance.SULFURIC_ACID;

public class Gold extends Metal{
    public Gold(double weight, double volume){
        super("Золото", "Au", TypeOfSubstance.GOLD, weight, volume);
    }
}
