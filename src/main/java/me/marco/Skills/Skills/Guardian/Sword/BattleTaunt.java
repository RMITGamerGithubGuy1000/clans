package me.marco.Skills.Skills.Guardian.Sword;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.SwordSkill;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilBlock;
import me.marco.Utility.UtilParticles;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BattleTaunt extends Skill implements SwordSkill, InteractSkill {

    public BattleTaunt(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    private final double BATTLE_TAUNT_RANGE = 4.0;
    private final int BATTLE_TAUNT_COUNT = 2;
    private final int TICK_RATE = 10; // 4 ticks a second

    private double calculateRange(int level){
        return BATTLE_TAUNT_RANGE + (.5 * level);
    }

    private int calculateTimes(int level){
        return BATTLE_TAUNT_COUNT + level;
    }

    @Override
    public void activate(Player player, int level) {
        shoutTaunt(player, level);
    }

    private void shoutTaunt(Player player, int level){
        double range = calculateRange(level);
        doBattleTaunt(player, range, true);
        new BukkitRunnable(){
            int count = calculateTimes(level) * TICK_RATE;
            public void run(){
                if(count <= 0){
                    this.cancel();
                    return;
                }
                doBattleTaunt(player, range, false);
                count--;
            }
        }.runTaskTimer(getInstance(), 0, 20 / TICK_RATE);

    }

    private void doBattleTaunt(Player player, double range, boolean inform){
        if(inform) player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 5f);
        UtilParticles.playBlockParticle(player, Material.DIAMOND_BLOCK, true);
        for (Entity entity : player.getNearbyEntities(range, range, range)){
            if ((entity instanceof Player)){
                Player target = (Player) entity;
                Client targetClient = getInstance().getClientManager().getClient(target);
                Client playerClient = getInstance().getClientManager().getClient(player);
                if(targetClient.isFriendly(playerClient)) return;
                if(inform) getChat().sendClans(target, getChat().getClanRelation(player, target) + player.getName() + "" +
                        getChat().textColour + " shouted a battle taunt");
                target.setSprinting(false);
                Vector direction = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                direction.setX(direction.getX() * -0.7D);
                direction.setZ(direction.getZ() * -0.7D);
                direction.setY(direction.getY() * -.1);
                if(direction.getY() < 0) direction.setY(0);
                target.setVelocity(direction);
            }
        }
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[0];
    }

    @Override
    public double getCooldown(int level) {
        return 23 + level;
    }

    @Override
    public int getMana(int level) {
        return 25;
    }
}
