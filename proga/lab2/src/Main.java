import pokemons.*;
import ru.ifmo.se.pokemon.Battle;

public class Main {
  public static void main(String[] args) {

    Battle battle = new Battle();

    // battle.addAlly(new Omanyte("omanyte", 1));
    // battle.addAlly(new Lugia("lugia", 1));
    // battle.addAlly(new Ivysaur("ivysaur", 1));

    // battle.addFoe(new Bulbasaur("bulbasaur", 1));
    // battle.addFoe(new Jynx("jynx", 1));
    // battle.addFoe(new Venusaur("venusaur", 1));

    battle.go();
  }
}