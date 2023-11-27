package pokemons;

import moves.Aeroblast;
import ru.ifmo.se.pokemon.Type;

public class Whismur extends Ledian {
    public Whismur(String name, int level){
        super(name, level);
        setType(Type.NORMAL);
        setStats(64, 51, 23, 51, 23, 28);
        addMove(new Aeroblast());
    }
}
