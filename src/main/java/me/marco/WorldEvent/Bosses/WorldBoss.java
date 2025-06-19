package me.marco.WorldEvent.Bosses;

import me.marco.Base.Core;
import me.marco.CustomEntities.StaticKeys;
import me.marco.Items.Legendary.CustomItem;
import me.marco.WorldEvent.BossSkill;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public abstract class WorldBoss {

    private double maxHealth, health;
    private ArrayList<BossSkill> bossSkillList;
    private String name;
    private LivingEntity entity;
    private BossBar bossBar;
    private Location spawnLoc;
    private Core instance;
    private long lastCastTime = 0;
    private ArrayList<CustomItem> dropList;

    public WorldBoss(Core instance, double health, ArrayList<CustomItem> dropList, ArrayList<BossSkill> bossSkillList, String name,
                     Location spawnLoc){
        this.maxHealth = health;
        this.health = health;
        this.bossSkillList = bossSkillList;
        this.name = name;
        this.spawnLoc = spawnLoc;
        this.instance = instance;
        this.dropList = dropList;
    }

    public abstract void castSkill();
    public abstract void onSummon();
    public abstract void onDeath(Player killer);
    public abstract void onDamage(Player damager);
    public abstract void onRemove();
    public abstract boolean runTick();
    public abstract void spawnBlood(double damage);

    protected abstract LivingEntity spawnEntity();

    public abstract double getDamageValue(Entity entity);

    private ArrayList<Player> bossBarList = new ArrayList<>();

    public void handleEntitySpawn(){
        this.entity = spawnEntity();
        this.setData(this.entity);
        handleBossBar();
    }

    public void handleBossBar(){
        bossBar = getInstance().getServer().createBossBar(getName(), BarColor.RED, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG);
        new BukkitRunnable(){
            public void run(){
                if(getEntity().getHealth() <= 0){
                    new BukkitRunnable(){
                        public void run(){
                            bossBar.setProgress(0);
                            bossBar.removeAll();
                        }
                    }.runTaskLater(getInstance(), 100);
                    this.cancel();
                }
                ArrayList<Player> nearList = new ArrayList<>();
                for(Entity e : getEntity().getNearbyEntities(40, 20, 40)){
                    if(e instanceof Player){
                        Player near = (Player) e;
                        bossBar.addPlayer(near);
                        nearList.add(near);
                        bossBarList.add(near);
                    }
                }
                ArrayList<Player> toRemove = new ArrayList<>();
                for(Player player : bossBarList){
                    if(!nearList.contains(player)){
                        toRemove.add(player);
                        bossBar.removePlayer(player);
                    }
                }
                for(Player player : toRemove){
                    bossBarList.remove(player);
                }
            }
        }.runTaskTimer(getInstance(), 0, 1);
    }

    protected void setData(LivingEntity livingEntity){
        this.entity.setCustomNameVisible(true);
        this.entity.setCustomName(ChatColor.DARK_RED + this.getName());
        this.entity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(this.maxHealth);
        livingEntity.getPersistentDataContainer().set(StaticKeys.damageableNMSKey, PersistentDataType.BYTE, (byte) (1));
        livingEntity.getPersistentDataContainer().set(StaticKeys.damageableWordBossKey, PersistentDataType.BYTE, (byte) (1));
    }

    public CustomItem getRandomDrop() {
        if (this.dropList.isEmpty()) {
            return null; // Return null if the droplist is empty
        }

        // Randomly select an item from the droplist
        Random random = new Random();
        int index = random.nextInt(this.dropList.size()); // Random index
        return this.dropList.get(index); // Return the randomly selected item
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public ArrayList<CustomItem> getDropList() {
        return dropList;
    }

    public void setHealth(double newHealth){
        this.health = newHealth;
    }

    public void takeDamage(double damage){
        this.health -= damage;
    }

    public void heal(double toHeal){
        this.health += toHeal;
    }

    public double getHealth() {
        return health;
    }

    public ArrayList<BossSkill> getBossSkillList() {
        return bossSkillList;
    }

    public void setBossSkillList(ArrayList<BossSkill> bossSkillList) {
        this.bossSkillList = bossSkillList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public void setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
    }

    public Location getSpawnLoc() {
        return spawnLoc;
    }

    public Core getInstance() {
        return instance;
    }

    protected long getLastCastTime(){
        return this.lastCastTime;
    }

    protected void setLastCastTimeNow(){
        this.lastCastTime = System.currentTimeMillis();
    }

    protected boolean timePassed(double timePassed) {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - this.lastCastTime;
        return elapsed >= (long)(timePassed * 1000); // convert seconds to milliseconds
    }

    public void updateBossBar(){
        bossBar.setProgress(getHealth() / getMaxHealth());
    }

}
