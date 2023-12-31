package pokemons;

import moves.Aeroblast;
import moves.IceBeam;
import moves.Slash;
import moves.TakeDown;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;


public class Alomomola extends Pokemon {
    public Alomomola(String name, int level){
        super(name, level);
        setType(Type.WATER);
        setStats(165, 75, 80, 40, 45, 65);
        setMove(new Aeroblast(), new Slash(), new IceBeam(), new TakeDown());
    }
}