package pokemons;

import moves.SweetKiss;
import ru.ifmo.se.pokemon.Type;

public class Dugtrio extends Diglett {
    public Dugtrio(String name, int level){
        super(name, level);
        setType(Type.GROUND);
        addMove(new SweetKiss());
    }
}