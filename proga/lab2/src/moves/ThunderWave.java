package moves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class ThunderWave extends StatusMove {
    public ThunderWave(){
        super(Type.ELECTRIC, 0, 90);
    }
    
    @Override
    protected void applyOppEffects(Pokemon p){
        // Thunder Wave парализует врага
        Effect.paralyze(p);        
    }

    @Override
    protected String describe(){
        return "uses Thunder Wave";
    }
}