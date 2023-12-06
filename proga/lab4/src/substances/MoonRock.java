package substances;

import java.util.Random;

public class MoonRock extends Substance implements CanGlow{
    private Boolean glowing;
    private static int total = 0;

    public MoonRock(){
        super("Лунный камень", "???", TypeOfSubstance.MOONROCK, 1.5, 0.002);
        this.glowing = false;
        total++;
    }

    @Override
    public Boolean isItGlowing(){
        Random random = new Random();
        glowing = random.nextDouble() > 0.5;
        return glowing;
    }

    public static class Statistics {
        public String getStatistics(){
            return  "Статистика MoonRock:" +
                    "\n\tСоздано камней: " + total ;
        }
    }
}
