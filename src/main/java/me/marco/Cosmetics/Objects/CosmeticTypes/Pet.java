package me.marco.Cosmetics.Objects.CosmeticTypes;

import me.marco.Cosmetics.Objects.Cosmetic;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public abstract class Pet extends Cosmetic {

    private boolean isActivated;
    private LivingEntity petEntity;

    public Pet(String cosmeticTag, Material representation) {
        super(cosmeticTag, representation);
    }

    public abstract void spawnPet(Player player);

    public void setActivated(LivingEntity petEntity){
        this.petEntity = petEntity;
        this.isActivated = true;
    }

    public void deactivatePet(){
        if(this.petEntity != null)this.petEntity.remove();
        this.petEntity = null;
        this.isActivated = false;
    }

    public boolean isActivated(){
        return this.isActivated;
    }

}
