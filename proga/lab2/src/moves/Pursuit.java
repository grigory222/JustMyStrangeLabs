package moves;

import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Status;

public class Pursuit extends PhysicalMove{
    public Pursuit(){
        super(Type.DARK, 40, 100);    
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        // Pursuit deals damage, and hits with double power if the target is switching out
        if (p.getCondition() == Status.PARALYZE)
            p.setMod(Stat.HP, (int)Math.round(p.getStat(Stat.ATTACK) * 2));
    }

    @Override
    protected String describe() {
        return "uses Pursuit";
    } 

}   