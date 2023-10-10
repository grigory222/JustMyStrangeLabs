package moves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class SweetKiss extends StatusMove{
    public SweetKiss(){
        super(Type.FAIRY, 0, 75);
    }
    
    @Override
    protected void applyOppEffects(Pokemon p){        
        Effect.confuse(p);
    }

    @Override
    protected String describe(){
        return "uses Sweet Kiss";
    } 
}
