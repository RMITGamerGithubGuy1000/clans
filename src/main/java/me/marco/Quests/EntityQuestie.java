package me.marco.Quests;

import me.marco.CustomEntities.CustomEntity;
import me.marco.CustomEntities.NMSPanda;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Panda;

public class EntityQuestie extends CustomEntity {

    public EntityQuestie(String name, Location homeLoc) {
        super(name, homeLoc);
    }

    @Override
    public LivingEntity spawnMob(Location location) {
        return (LivingEntity) new NMSPanda(location).getBukkitEntity();
    }

    public void postSpawnEffects(){
        Panda panda = (Panda) getEntity();
        panda.setMainGene(Panda.Gene.PLAYFUL);
        panda.setHiddenGene(Panda.Gene.PLAYFUL);
    }

}
