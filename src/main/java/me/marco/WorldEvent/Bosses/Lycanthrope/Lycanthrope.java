package me.marco.WorldEvent.Bosses.Lycanthrope;

import me.marco.Base.Core;
import me.marco.Items.Legendary.CustomItem;
import me.marco.Utility.UtilParticles;
import me.marco.WorldEvent.BossSkill;
import me.marco.WorldEvent.Bosses.NMS.NMSLycan;
import me.marco.WorldEvent.Bosses.NMS.NMSZombie;
import me.marco.WorldEvent.Bosses.WorldBoss;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

public class Lycanthrope extends WorldBoss {

    private boolean formChange = false;

    public Lycanthrope(Core instance, double health, ArrayList<CustomItem> dropList, ArrayList<BossSkill> bossSkillList, String name,
                        Location spawnLoc) {
        super(instance, health, dropList, bossSkillList, name, spawnLoc);
    }

    @Override
    public boolean runTick() {
        if(!this.getEntity().isDead()) this.castSkill();
        return this.getEntity().isDead();
    }

    @Override
    public void castSkill() {
        if(!this.timePassed(5)) return;
        this.setLastCastTimeNow();
        Random rand = new Random();

        // Get a random index and select an ability
        int randomIndex = rand.nextInt(this.getBossSkillList().size());
        int amp = this.formChange ? 2 : 1;
        this.getBossSkillList().get(randomIndex).cast(this, getInstance(), amp);
    }

    @Override
    public void onSummon() {

    }

    @Override
    public void onDeath(Player killer) {
        if(formChange){
            this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.ENTITY_WOLF_DEATH, 5, 5);
        }else{
            this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.ENTITY_ZOMBIE_DEATH, 5, 5);
        }
        Location loc = getEntity().getLocation();
        new BukkitRunnable(){
            int count = 0;
            public void run(){
                if(count >= 9){
                    this.cancel();
                }
                count++;
                getEntity().getWorld().strikeLightningEffect(loc);
                loc.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 2, 10);
                loc.getWorld().playSound(loc, Sound.ENTITY_WOLF_HURT, 1, 1);
                FallingBlock fallingIron = getEntity().getWorld().spawnFallingBlock(
                        loc,
                        Material.IRON_BLOCK.createBlockData()
                );
                double randomX = (Math.random() * 1.4) - 0.7;  // -0.7 to 0.7
                double randomZ = (Math.random() * 1.4) - 0.7;  // -0.7 to 0.7
                double randomY = 0.5 + (Math.random() * 0.8);  // 0.5 to 1.3
                UtilParticles.playBlockParticle(loc, Material.IRON_BLOCK);
                fallingIron.setVelocity(new Vector(randomX, randomY, randomZ));
                fallingIron.setDropItem(false); // Optional: block won't drop as item when it lands
                getEntity().getWorld().playEffect(getEntity().getLocation(), Effect.STEP_SOUND, Material.IRON_BLOCK);
                fallingIron.setMetadata("noplace", new FixedMetadataValue(getInstance(), true));
            }
        }.runTaskTimer(getInstance(), 0, 10);
    }

    private double getHealthThreshold(){
        return this.getMaxHealth() * .70;
    }

    @Override
    public void onDamage(Player damager) {
        this.setHealth(this.getEntity().getHealth());
        if(this.formChange) return;
        if(this.getHealth() <= this.getHealthThreshold()){
            double health = this.getEntity().getHealth();
            this.getEntity().remove();
            NMSLycan nmsWolf = new NMSLycan(this.getEntity().getLocation(), this.getSpawnLoc());
            Wolf wolf = (Wolf) nmsWolf.getBukkitEntity();
            UtilParticles.playBlockParticle(wolf.getLocation(), Material.IRON_BLOCK);
            wolf.getWorld().strikeLightningEffect(wolf.getLocation());
            wolf.getWorld().playSound(wolf.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 2, 1);
            wolf.getWorld().playSound(wolf.getLocation(), Sound.ENTITY_WOLF_HOWL, 1, 1);
            wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
            wolf.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
            wolf.setAngry(true);
            wolf.setAdult();
            Player closest = null;
            double distanceMax = 100;
            for(Entity e : wolf.getNearbyEntities(20, 20, 20)){
                if(e instanceof Player){
                    Player near = (Player) e;
                    double distance = near.getLocation().distance(wolf.getLocation());
                    if(distance < distanceMax){
                        distanceMax = distanceMax;
                        closest = near;
                    }
                }
            }
            if(closest != null){
                wolf.setTarget(closest);
            }
            this.setEntity(wolf);
            this.setData(wolf);
            this.getEntity().setHealth(health);
            this.formChange = true;
        }
    }

    @Override
    public void onRemove() {

    }

    public void spawnBlood(double damage){
        double amount = Math.round(damage*100.0)/100.0;
        int time = 5990;
        Item item = formChange ?
                this.getEntity().getWorld().dropItem(this.getEntity().getLocation(), new ItemStack(Material.BONE_MEAL))
                : this.getEntity().getWorld().dropItem(this.getEntity().getLocation(), new ItemStack(Material.ROTTEN_FLESH));
        item.setPickupDelay(time);
        item.setTicksLived(time);
        item.setCustomName(ChatColor.RED + "-" + amount + "");
        item.setCustomNameVisible(true);
    }

    @Override
    protected LivingEntity spawnEntity() {
        NMSZombie nmsZombie = new NMSZombie(this.getSpawnLoc());
        Zombie zombie = (Zombie) nmsZombie.getBukkitEntity();
        zombie.setAdult();
        zombie.getEquipment().setHelmet(new ItemStack(Material.TURTLE_HELMET, 1));
        zombie.setAdult();
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
        return zombie;
    }

    @Override
    public double getDamageValue(Entity entity) {
        return 2;
    }

    public boolean isFormChange() {
        return formChange;
    }
}
