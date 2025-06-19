package me.marco.Skills.Skills.Mage.Axe;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Events.PvP.PureDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.ChannelSkill;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.AxeSkill;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Flamethrower extends ChannelSkill implements AxeSkill, InteractSkill {

    private HashMap<Fireball, Client> shooterMap = new HashMap<Fireball, Client>();

    public Flamethrower(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public float requiredEnergy(int level) {
        return (float) (.04 - level * .005);
        //return .025f;
    }

    @Override
    public float requiredReserve(int level) {
        return (float) (.4 - .1 * level);
    }

    @Override
    public double toggleCooldown(int level) {
        return 2;
    }

    @Override
    public int getTicks() {
        return 5;
    }

    @Override
    public void activate(Player player, int level) {
//        if(this.getCDManager().isCooling(player, this.getName())){
//            this.getCDManager().sendRemaining(player, getName());
//            return;
//        }
//        if(!checkChannelingForToggle(player)) if(!useageCheck(player)) return;
//        if(this.isChanneling(player)) return;
        castChannelAbility(player, getLevel(player));
        toggleChannel(player);
    }

    @Override
    public boolean useageCheck(Player player) {
        int level = getLevel(player);
        return canCastChannel(player, true, false, level);
    }

    private double getDamage(int level){
        return .5 * level;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "While channeling, shoot fireballs that deal " + ChatColor.GREEN + this.formatNumber(getDamage(level), 1) + "" + ChatColor.YELLOW + " hearts ",
                ChatColor.YELLOW + "of " + ChatColor.LIGHT_PURPLE + "Pure Damage" + ChatColor.YELLOW + " and " + ChatColor.AQUA + "Ignite" + ChatColor.YELLOW + " targets for " + ChatColor.GREEN + "2" + ChatColor.YELLOW + " seconds",
        };
    }

    @Override
    public double getCooldown(int level) {
        return 15 - level;
    }

    @Override
    public int getMana(int level) {
        return 45 - level * 5;
    }

    @Override
    public boolean runTick(Player player){
        Fireball fireball = (Fireball) player.getWorld().spawnEntity(player.getLocation().add(0, 1.5, 0), EntityType.SMALL_FIREBALL);
        Vector tosend = player.getEyeLocation().getDirection().normalize().multiply(1.2);
        fireball.setVelocity(tosend);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 5);
        shooterMap.put(fireball, getInstance().getClientManager().getClient(player));
        return true;
    }

    @Override
    public void cleanup(Player player) {

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Fireball)) return;
        Player target = (Player) event.getEntity();
        Fireball fireball = (Fireball) event.getDamager();
        if(!shooterMap.containsKey(fireball)) return;
        Client shooter = shooterMap.get(fireball);
        Client hit = getInstance().getClientManager().getClient(target);
        if(shooter.isFriendly(hit)){
            event.setCancelled(true);
            return;
        }
        event.setDamage(0);
        event.setCancelled(true);
        fireball.remove();
        getInstance().getServer().getPluginManager().callEvent(new PureDamageEvent(getInstance().getServer().getPlayer(shooter.getUUID()),
                target, getDamage(shooter.getActiveBuild().getSkillLevel(this)), getName()));
        new BukkitRunnable(){
            public void run(){
                target.setFireTicks(40);
            }
        }.runTaskLater(getInstance(), 1);
    }


}
