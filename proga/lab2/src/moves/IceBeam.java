package moves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class IceBeam extends SpecialMove {
    public IceBeam() {
        super(Type.ICE, 90, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon poke) {
        if (Math.random() <= 0.1) {
            Effect.freeze(poke);
        }
    }
    
    @Override
    protected String describe(){
        return "uses Ice Beam";
    }
}