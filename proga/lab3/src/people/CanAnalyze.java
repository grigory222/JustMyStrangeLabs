package people;

import substances.Substance;
import substances.TypeOfSubstance;


public interface CanAnalyze {
    Boolean isItSolid(Substance s);
    Boolean isItLiquid(Substance s);
    String analyze(Substance s);
    String joinSubstances(Substance s1, Substance s2);
}
