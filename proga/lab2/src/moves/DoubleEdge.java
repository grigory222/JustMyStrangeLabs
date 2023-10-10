package moves;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class DoubleEdge extends PhysicalMove{
    public DoubleEdge() {
        super(Type.NORMAL, 120, 100);
    }
       
    @Override
    protected void applySelfEffects(Pokemon p){
        p.setMod(Stat.HP, (int)Math.round(p.getStat(Stat.ATTACK) / 3));
    }

    @Override
    protected String describe() {
        return "uses Double-Edge";
    } 
}
