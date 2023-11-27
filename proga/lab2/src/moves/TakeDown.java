package moves;
    
import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;


public class TakeDown extends PhysicalMove {

    public TakeDown() {
        super(Type.NORMAL, 90, 85);
    }
       
    @Override
    protected void applySelfEffects(Pokemon p){
        p.setMod(Stat.HP, (int)Math.round(p.getStat(Stat.ATTACK) / 4));
    }

    @Override
    protected String describe() {
        return "uses Slash";
    }
}