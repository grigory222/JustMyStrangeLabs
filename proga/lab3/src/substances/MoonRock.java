package substances;

public class MoonRock extends Substance implements CanGlow{
    private Boolean glowing;

    public MoonRock(){
        super("Лунный камень", "???", TypeOfSubstance.MOONROCK);
        this.glowing = false;
    }

    @Override
    public Boolean isItGlowing(){
        glowing = Math.random() > 0.5 ? true : false;
        return glowing;
    }

}
