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
    // атакующий покемон должен получать 25% от урона, который он наносит

    @Override
    protected String describe() {
        return "uses Slash";
    }

}