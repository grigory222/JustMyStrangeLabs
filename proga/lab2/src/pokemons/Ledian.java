package pokemons;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import moves.Endeavor;
import moves.ThunderShock;

public class Ledian extends Pokemon {
    public Ledian(String name, int level){
        super(name, level);
        setType(Type.BUG, Type.FLYING);
        setMove(new Endeavor(), new ThunderShock());
    }
}
