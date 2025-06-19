package me.marco.Cosmetics.LootBox;

import me.marco.Cosmetics.Objects.Cosmetic;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LootboxAnimation {

    private Cosmetic finalRoll;
    private int finalRollIndex;
    private Random random = new Random();
    private List<Cosmetic> available;

    public LootboxAnimation(List<Cosmetic> available){
        this.available = available;
        int finalRollIndex = ThreadLocalRandom.current().nextInt(available.size());
        finalRoll = available.get(finalRollIndex);
    }




}
