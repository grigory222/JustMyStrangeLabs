import pokemons.Alomomola;
import pokemons.Diglett;
import pokemons.Dugtrio;
import pokemons.Ledian;
import pokemons.Loudred;
import pokemons.Whismur;
import ru.ifmo.se.pokemon.Battle;

public class Main {
  public static void main(String[] args) {

    Battle battle = new Battle();

    battle.addAlly(new Alomomola("alomomola", 1));
    battle.addAlly(new Diglett("diglett", 1));
    battle.addAlly(new Dugtrio("dugtrio", 1));

    battle.addFoe(new Ledian("ledian", 1));
    battle.addFoe(new Whismur("whismur", 1));
    battle.addFoe(new Loudred("loudred", 1));

    battle.go();
  }
}