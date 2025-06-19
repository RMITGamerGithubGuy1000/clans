package me.marco.Skills.Skills.Samurai.Sword;

import me.marco.Base.Core;
import me.marco.CustomEntities.NMSBeastCallPig;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.SwordSkill;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class BeastCall extends Skill implements SwordSkill, InteractSkill {

    private Random rand = new Random();

    private enum BeastCallEnum {
        WOLF(25, "Dire Wolves"),
        OCELOT(25, "Meow Boost"),
        SHEEP(25, "Baa Baa"),
        PIG(25, "Piggy");

        private String name;
        private double chance;

        BeastCallEnum(double chance, String name){
            this.chance = chance;
            this.name = name;
        }

        public double getChange(){
            return this.chance;
        }

        public String getName() { return this.name; }

    }

    public BeastCall(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "pig",
        };
    }

    @Override
    public double getCooldown(int level) {
        return 10 - (.5 * level);
    }

    @Override
    public int getMana(int level) {
        return 20;
    }

    @Override
    public void activate(Player player, int level) {
        activateBeastCall(player, level);
    }

    private void activateBeastCall(Player player, int level){
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PIG_AMBIENT, 5, 5);
        BeastCallEnum beastCallEnum = randomEnum();
        ArrayList<Pig> pigList = new ArrayList<>();
        ArrayList<Item> itemList = new ArrayList<>();
        for(Entity entity : player.getNearbyEntities(15, 15, 15)){
            if(!(entity instanceof Player)) continue;
            Player nearby = (Player) entity;
            NMSBeastCallPig nmsBeastCallPig = new NMSBeastCallPig(player.getLocation(), nearby);
            Item tnt = player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.TNT, 1));
            tnt.setPickupDelay(Integer.MAX_VALUE);
            itemList.add(tnt);
            Pig pig = (Pig) nmsBeastCallPig.getBukkitEntity();
            pig.addPassenger(tnt);
            pigList.add(pig);
            tnt.setCustomNameVisible(true);
            tnt.setCustomName(ChatColor.DARK_RED + "Prosciutto");
        }

        new BukkitRunnable(){
            public void run(){
                for(Pig pig : pigList){
                    explodePig(player, pig);
                }
                for(Item tnt : itemList){
                    tnt.remove();
                }
            }
        }.runTaskLater(getInstance(), 110);
    }

    private void explodePig(Player player, Pig pig){
        for(Entity entity : pig.getNearbyEntities(1.5, 1.5, 1.5)) {
            if (!(entity instanceof Player)) continue;
            Player nearby = (Player) entity;
            getInstance().getServer().getPluginManager().callEvent(
                    new PhysicalDamageEvent(player, nearby, 8,
                            "Exploding Pig"));
        }
        pig.getWorld().playSound(pig.getLocation(), Sound.ENTITY_PIG_DEATH, 5, 5);
        pig.getWorld().playSound(pig.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 5);
        UtilParticles.playEffect(pig.getLocation().add(0, 1, 0).getBlock().getLocation(), Particle.EXPLOSION, 0, 0, 0, 0, 1);
        UtilParticles.playInstantBreak(pig.getLocation(), PotionEffectType.FIRE_RESISTANCE);
        UtilParticles.playBlockParticle(pig.getLocation().add(0, 2, 0), Material.TNT);
        pig.remove();
    }

    private BeastCallEnum randomEnum() {
        return BeastCallEnum.values()[rand.nextInt(BeastCallEnum.values().length)];
    }


}
