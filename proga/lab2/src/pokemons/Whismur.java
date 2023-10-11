package pokemons;

import moves.Aeroblast;
import ru.ifmo.se.pokemon.Type;

public class Whismur extends Ledian {
    public Whismur(String name, int level){
        super(name, level);
        setType(Type.NORMAL);
        addMove(new Aeroblast());
    }
}
