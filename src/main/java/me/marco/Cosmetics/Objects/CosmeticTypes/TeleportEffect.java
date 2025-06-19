package me.marco.Cosmetics.Objects.CosmeticTypes;

import me.marco.Cosmetics.Objects.Cosmetic;
import org.bukkit.Location;
import org.bukkit.Material;

public abstract class TeleportEffect extends Cosmetic {

    public TeleportEffect(String cosmeticTag, Material representation) {
        super(cosmeticTag, representation);
    }

    public abstract void playEffect(Location from, Location to);

}
