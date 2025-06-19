package me.marco.Skills.Skills.Ranger.Bow;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.ArrowDamageEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Events.PvP.PureDamageEvent;
import me.marco.Events.Skills.ClassPassiveAddEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.BowSkill;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class MagicArrows extends Skill implements BowSkill, InteractSkill, PassiveSkill {

    public MagicArrows(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract, true);
        setSilent();
    }

    private final double SLOW_DURATION = 2;
    private final double FIRE_DURATION = 2;

    private HashMap<String, MagicArrowType> toggleMap = new HashMap<String, MagicArrowType>();
    private HashMap<String, MagicArrowData> arrowMap = new HashMap<String, MagicArrowData>();

    public HashMap<String, MagicArrowType> getToggleMap(){
        return this.toggleMap;
    }

    public HashMap<String, MagicArrowData> getArrowMap(){
        return this.arrowMap;
    }

    @Override
    public void activate(Player player, int level) {
        toggleArrows(player);
        String arrow = getToggle(player.getUniqueId().toString()).getName() + " Arrows";
        getChat().sendModule(player, "You toggled: " + arrow, this.getClassTypeName());
    }

    public void onEquip(Player player){
        addPlayerToggle(player);
    }

    public void onDequip(Player player){
        removePlayer(player);
    }

    @EventHandler
    public void onProjShoot(ProjectileLaunchEvent event){
        Entity entity = event.getEntity();
        if(!(entity instanceof Arrow)) return;
        Arrow arrow = (Arrow) entity;
        ProjectileSource source = arrow.getShooter();
        if(!(source instanceof Player)) return;
        Player player = (Player) source;
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasSkill(this)) return;
        setArrowData(client, player.getLocation(), arrow);
    }

    @EventHandler
    public void onArrowHit(ArrowDamageEvent event){
        if(event.isCancelled()) return;
        Player shooter = event.getShooter();
        Arrow arrow = event.getArrow();
        Player hitPlayer = event.getTarget();
        Client client = getInstance().getClientManager().getClient(shooter);
        if(!client.hasSkill(this)) return;
        int level = client.getActiveBuild().getSkillLevel(this);
        double damage = applyEffect(arrow, hitPlayer, shooter, level);
        event.setDamage(damage);
        event.setCause(arrowMap.get(arrow.getUniqueId().toString()).getMagicArrowType().getDisplayName());
    }

    public void setArrowData(Client shooter, Location shotFrom, Arrow arrow){
        arrowMap.put(arrow.getUniqueId().toString(), new MagicArrowData(shooter, shotFrom, toggleMap.get(shooter.getUUID().toString())));
    }

    public void setArrowData(Client shooter, Location shotFrom, Arrow arrow, MagicArrowType magicArrowType){
        arrowMap.put(arrow.getUniqueId().toString(), new MagicArrowData(shooter, shotFrom, magicArrowType));
    }

    public void cloneArrowData(Arrow oldArrow, Arrow newArrow){
        if(arrowMap.containsKey(oldArrow.getUniqueId().toString()))
        arrowMap.put(newArrow.getUniqueId().toString(), arrowMap.get(oldArrow.getUniqueId().toString()));
    }

    private double applyEffect(Arrow arrow, Player hitPlayer, Player shooter, int level){
        if(!arrowMap.containsKey(arrow.getUniqueId().toString())) return 0;
        Client hitClient = getInstance().getClientManager().getClient(hitPlayer);
        String arrowID = arrow.getUniqueId().toString();
        MagicArrowType magicType = arrowMap.get(arrowID).getMagicArrowType();
        if(magicType == MagicArrowType.FIRE){
            new BukkitRunnable(){
                public void run(){
                    hitPlayer.setFireTicks(calculateDuration(MagicArrowType.FIRE, 1));
                }
            }.runTaskLater(getInstance(), 1);
            playParticles(magicType, hitPlayer.getLocation().add(0, 1, 0));
            return 1;
        }
        if(magicType == MagicArrowType.SLOW){
            hitClient.removePotionEffect(PotionEffectType.SLOWNESS);
            //hitClient.addPotionEffect(PotionEffectType.SLOW, calculateDuration(MagicArrowType.SLOW, 1), 1);
            getInstance().getServer().getPluginManager().callEvent(
                    new PotionEffectEvent(hitPlayer, shooter,
                            PotionEffectType.SLOWNESS, calculateDuration(MagicArrowType.SLOW, level),
                            1));
            playParticles(magicType, hitPlayer.getLocation().add(0, 1, 0));
            return 1;
        }
        if(magicType == MagicArrowType.LONGSHOT){
            playParticles(magicType, hitPlayer.getLocation().add(0, 1, 0));
            MagicArrowData magicArrowData = arrowMap.get(arrowID);
            Location finishedAt = arrow.getLocation();
            Location shotFrom = magicArrowData.getShotFrom();
            double distance = shotFrom.distance(finishedAt);
            double damage = distance / 4;
            if(damage > 8) damage = 8;
            return damage;
        }
        return 0;
    }

    private MagicArrowType getToggle(String uuid){
        if(this.toggleMap.containsKey(uuid)) return this.toggleMap.get(uuid);
        toggleMap.put(uuid, MagicArrowType.FIRE);
        return MagicArrowType.FIRE;
    }

    private void addPlayerToggle(Player player){
        getInstance().getChat().sendModule(player, "You gained the ability of " +
                getInstance().getChat().highlightText + getName() + getInstance().getChat().textColour + ".", getClassTypeName());
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 5, 1);
        this.toggleMap.put(player.getUniqueId().toString(), MagicArrowType.FIRE);
    }

    private void removePlayer(Player player){
        getInstance().getChat().sendModule(player, "You lost the ability of " +
                getInstance().getChat().highlightText + getName() + getInstance().getChat().textColour + ".", getClassTypeName());
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1, 1);
        this.toggleMap.remove(player);
    }

    private int calculateDuration(MagicArrowType magicType, int level){
        if(magicType == MagicArrowType.FIRE) return (int) Math.round(calcFireTime(level) * 20);
        if(magicType == MagicArrowType.SLOW) return (int) Math.round(calcSlowTime(level) * 20);
        return 0;
    }

    private double calcFireTime(int level){
        return 2 + level;
    }

    private double calcSlowTime(int level){
        return 2 * level;
    }

    private double calcMaxDamage(int level){
        return 4 + 2 * level;
    }

    private double calculateDamage(double distance, int level){
        double damage = distance / 4;
        double maxDamage = calcMaxDamage(level);
        if(damage > maxDamage) damage = maxDamage;
        return damage;
    }

    public void toggleArrows(Player player){
        String uuid = player.getUniqueId().toString();
        if(!this.toggleMap.containsKey(uuid)){
            addPlayerToggle(player);
            playParticles(MagicArrowType.FIRE, player.getLocation());
            return;
        }
        if(toggleMap.get(uuid) == MagicArrowType.FIRE){
            toggleMap.put(uuid, MagicArrowType.SLOW);
            playParticles(MagicArrowType.SLOW, player.getLocation());
            return;
        }
        if(toggleMap.get(uuid) == MagicArrowType.SLOW){
            toggleMap.put(uuid, MagicArrowType.LONGSHOT);
            playParticles(MagicArrowType.LONGSHOT, player.getLocation());
            return;
        }
        toggleMap.put(uuid, MagicArrowType.FIRE);
        playParticles(MagicArrowType.FIRE, player.getLocation());
    }

    private void playParticles(MagicArrowType toggle, Location loc){
        if(toggle == MagicArrowType.FIRE){
            UtilParticles.playBlockParticle(loc, Material.LAVA, false);
            loc.getWorld().playSound(loc, Sound.ITEM_FLINTANDSTEEL_USE, 1, 1);
        }
        if(toggle == MagicArrowType.SLOW) {
            UtilParticles.playBlockParticle(loc, Material.OBSIDIAN, false);
            loc.getWorld().playSound(loc, Sound.BLOCK_STONE_BREAK, 1, 1);
        }
        if(toggle == MagicArrowType.LONGSHOT) {
            UtilParticles.playBlockParticle(loc, Material.GLASS, true);
        }
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.LIGHT_PURPLE + "Left click" + ChatColor.YELLOW + " your " +
                        ChatColor.AQUA + "Bow" + ChatColor.YELLOW + " to select your " + ChatColor.AQUA + getName() + "" + ChatColor.YELLOW + ", ",
                "",
                MagicArrowType.FIRE.getDisplayName() + "" + ChatColor.DARK_GRAY + ": " + ChatColor.YELLOW +
                        ChatColor.LIGHT_PURPLE + "Ignite" + ChatColor.YELLOW + " your targets for " +
                        ChatColor.GREEN + this.formatNumber(calcFireTime(level), 1) + ChatColor.YELLOW + " seconds. ",

                MagicArrowType.SLOW.getDisplayName() + "" + ChatColor.DARK_GRAY + ": " + ChatColor.YELLOW +
                        ChatColor.LIGHT_PURPLE + "Inflict " + ChatColor.AQUA + "Slow II" + ChatColor.YELLOW + " on your targets for " +
                        ChatColor.GREEN + this.formatNumber(calcSlowTime(level), 1) + ChatColor.YELLOW + " seconds. ",

                MagicArrowType.LONGSHOT.getDisplayName() + "" + ChatColor.DARK_GRAY + ": " + ChatColor.YELLOW + "Deal greater damage to enemies the ",
                ChatColor.YELLOW + "                       further away you are from them, ",
                ChatColor.YELLOW + "                       with the damage capped out at " + ChatColor.GREEN + this.formatNumber(calcMaxDamage(level), 1) + "" + ChatColor.YELLOW + " health."

        };
    }

    @Override
    public double getCooldown(int level) {
        return .1;
    }

    @Override
    public int getMana(int level) {
        return 0;
    }
}
