package moves;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;


//Aeroblast deals damage and has an increased critical hit ratio (1⁄8 instead of 1⁄24).
public class Aeroblast extends SpecialMove{
    public Aeroblast() {
        super(Type.NORMAL, 100, 95);
    }

    @Override
    protected double calcCriticalHit(Pokemon var1, Pokemon var2) {        
        // Slash has an increased critical hit ratio (1⁄8 instead of 1⁄24).
        if (var1.getStat(Stat.SPEED) / 512.0 > Math.random()) {        
            return 2.0 * 1.125;
        } else {
            return 1.0;
        }
    }

    @Override
    protected String describe(){
        return "uses Aeroblast";
    }
}
