package me.marco.Cosmetics.Objects;

import org.bukkit.Material;

public abstract class Cosmetic {

    private String cosmeticTag;
    private Material representation;

    public Cosmetic(String cosmeticTag, Material representation){
        this.cosmeticTag = cosmeticTag;
        this.representation = representation;
    }

    public String getCosmeticTag() {
        return cosmeticTag;
    }

    public Material getRepresentation() {
        return representation;
    }
}
