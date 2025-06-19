package me.marco.WorldEvent.Bosses;

import me.marco.Base.Core;
import me.marco.Events.WorldBosses.BossSpawnEvent;
import me.marco.Items.Legendary.CustomItem;
import me.marco.Items.Legendary.LightningScythe.LightningScythe;
import me.marco.WorldEvent.BossSkill;
import me.marco.WorldEvent.Bosses.Lycanthrope.Lycanthrope;
import me.marco.WorldEvent.Bosses.Lycanthrope.Skills.BoneRush.Bonerush;
import me.marco.WorldEvent.Bosses.Lycanthrope.Skills.WolfSpawn.WolfSpawn;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;

public class WorldBossManager {

    private Core instance;
    private Location spawnLoc;

    public WorldBossManager(Core instance){
        this.instance = instance;
        this.spawnLoc = new Location(instance.getServer().getWorld("world"), -224.5, 71, -176.5);
    }

    private ArrayList<WorldBoss> worldBossList = new ArrayList<>();

    public WorldBoss getByEntity(Entity entity) {
        return worldBossList.stream()
                .filter(worldBoss -> worldBoss.getEntity().equals(entity))
                .findFirst()
                .orElse(null);
    }

    public void handleTicks(){
        ArrayList<WorldBoss> toRemove = new ArrayList<>();
        worldBossList.stream().forEach(worldBoss -> {
            if(worldBoss.runTick()){
                worldBoss.onRemove();
                toRemove.add(worldBoss);
            }
        });
        toRemove.forEach(worldBoss -> worldBossList.remove(worldBoss));
    }

    public void test() {
        ArrayList<BossSkill> bossSkills = new ArrayList<>();
        bossSkills.add(new WolfSpawn());
        bossSkills.add(new Bonerush());
        ArrayList<CustomItem> customItems = new ArrayList<CustomItem>();
        customItems.add(new LightningScythe(getInstance()));
        Lycanthrope lycanthrope = new Lycanthrope(getInstance(), 20.0, customItems, bossSkills, "Lycanthrope", getSpawnLoc());
        this.worldBossList.add(lycanthrope);
        getInstance().getServer().getPluginManager().callEvent(new BossSpawnEvent(lycanthrope, getSpawnLoc()));
    }

    public Core getInstance() {
        return instance;
    }

    public Location getSpawnLoc() {
        return spawnLoc;
    }

    public ArrayList<WorldBoss> getWorldBossList() {
        return worldBossList;
    }
}
