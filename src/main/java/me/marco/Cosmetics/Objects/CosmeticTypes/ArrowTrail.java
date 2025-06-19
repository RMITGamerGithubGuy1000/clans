package me.marco.Cosmetics.Objects.CosmeticTypes;

import me.marco.Base.Core;
import me.marco.Cosmetics.Objects.Cosmetic;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;

public abstract class ArrowTrail extends Cosmetic {

    public ArrowTrail(String cosmeticTag, Material representation) {
        super(cosmeticTag, representation);
    }

    public abstract void playEffect(Core instance, Arrow arrow);

}
