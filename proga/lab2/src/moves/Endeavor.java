package moves;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class Endeavor extends PhysicalMove {

    public Endeavor() {
        super(Type.NORMAL, 0, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon poke) {
        poke.setMod(Stat.HP, 3);
    }

    @Override
    protected String describe() {
        return "uses Endeavor";
    }

}