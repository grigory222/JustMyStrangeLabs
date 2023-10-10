package moves;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class ThunderShock extends SpecialMove{
    public ThunderShock() {
        super(Type.ELECTRIC, 40, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon poke) {
        if (Math.random() <= 0.1) {
            Effect.paralyze(poke);
        }
    }
    
    @Override
    protected String describe(){
        return "uses Thunder Shock";
    }
}
