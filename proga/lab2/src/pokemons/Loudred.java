package pokemons;

import moves.Pursuit;
import ru.ifmo.se.pokemon.Type;

public class Loudred extends Whismur {
    public Loudred(String name, int level){
        super(name, level);
        setType(Type.NORMAL);
        addMove(new Pursuit());
    }
}