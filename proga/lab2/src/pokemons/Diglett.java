package pokemons;

import moves.DoubleEdge;
import moves.LightScreen;
import moves.ThunderWave;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;


public class Diglett extends Pokemon {
    public Diglett(String name, int level){
        super(name, level);
        setType(Type.GROUND);
        setStats(10, 55, 25, 35, 45, 95);
        setMove(new ThunderWave(), new LightScreen(), new DoubleEdge());
    }
}