package me.marco.Skills.Skills.Ranger.Axe;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.ArrowDamageEvent;
import me.marco.Events.PvP.PureDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.ChannelSkill;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.AxeSkill;
import me.marco.Skills.Skills.Ranger.Bow.MagicArrowType;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Ricochet extends ChannelSkill implements AxeSkill, InteractSkill {

    public Ricochet(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
        this.setNeedsHoldItem(false);
         ricochetKey = new NamespacedKey(getInstance(), "ricochet");
    }

    @Override
    public float requiredEnergy(int level) {
        return .0125f;
    }

    @Override
    public float requiredReserve(int level) {
        return .3f;
    }

    @Override
    public double toggleCooldown(int level) {
        return 5;
    }

    @Override
    public int getTicks() {
        return 20;
    }

    private NamespacedKey ricochetKey;

    public NamespacedKey getRicochetKey() {
        return ricochetKey;
    }

    @Override
    public boolean runTick(Player player) {
        int level = getLevel(player);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);

        getInstance().getServer().getPluginManager().callEvent(
                new PotionEffectEvent(player, player,
                        PotionEffectType.SPEED,
                        21, 0));
        return true;
    }

    @Override
    public void cleanup(Player player) {
    }

    @Override
    public boolean useageCheck(Player player) {
        int level = getLevel(player);
        return canCastChannel(player, true, false, level);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Whilst active, " + ChatColor.AQUA + "Arrows" + ChatColor.YELLOW + " you hit " + ChatColor.AQUA + "Ricochet" + ChatColor.YELLOW + "  ",
                ChatColor.YELLOW + "up to " + ChatColor.GREEN + getMaxAmount(level) + "" + ChatColor.YELLOW + " enemies within a " + ChatColor.GREEN + this.formatNumber(getRange(level), 1) + "" + ChatColor.YELLOW + " block radius. ",
        };
    }

    @Override
    public double getCooldown(int level) {
        return 20;
    }

    @Override
    public int getMana(int level) {
        return 30;
    }

    public void activate(Player player, int level) {
        castChannelAbility(player, getLevel(player));
        toggleChannel(player);
        runTick(player);
    }

    private double getRange(int level){
        return 1 + level;
    }

    private int getMaxAmount(int level){
        return level;
    }

    @EventHandler
    public void onArrowHit(ArrowDamageEvent event){
        if(event.isCancelled()) return;
        Arrow arrow = event.getArrow();
        if (arrow.getPersistentDataContainer().has(ricochetKey, PersistentDataType.INTEGER)) return;
        Player shooter = event.getShooter();
        Player hitPlayer = event.getTarget();

        Client shooterClient = getInstance().getClientManager().getClient(shooter);
        if(!shooterClient.hasSkill(this)) return;
        if(!(shooterClient.isChanneling() && shooterClient.getIsChanneling() == this)) return;
        int level = shooterClient.getActiveBuild().getSkillLevel(this);
        double range = getRange(level);
        int count = 0;
        int max = getMaxAmount(level);
        Vector originalVelocity = arrow.getVelocity();
        Vector from = hitPlayer.getLocation().toVector();
        Location spawnArrowFrom = hitPlayer.getLocation().add(0, 1.5, 0);
        MagicArrowType magicArrowType = null;
        if(getInstance().getSkillManager().getMagicArrows().hasThisSkill(shooterClient)){
            magicArrowType = getInstance().getSkillManager().getMagicArrows().getToggleMap().get(shooterClient.getUUID().toString());
        }
        for(Entity nearby : hitPlayer.getNearbyEntities(range, range, range)){
            if(count >= max) break;
            if(!(nearby instanceof Player)) continue;
            Player nearbyPlayer = (Player) nearby;
            if(nearbyPlayer == shooter) continue;
            count++;
            Vector toTarget = nearbyPlayer.getLocation().toVector().subtract(from).normalize();
            Vector newVelocity = toTarget.multiply(originalVelocity.length()); // Same speed
            Arrow newArrow = hitPlayer.getWorld().spawnArrow(
                    spawnArrowFrom,
                    newVelocity, // Use the stored direction
                    (float) newVelocity.length(), // Convert strength to velocity (Spigot uses force * 3.0)
                    0.0f
                    );

            newArrow.setShooter(shooter);
            newArrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
            newArrow.getPersistentDataContainer().set(ricochetKey, PersistentDataType.INTEGER, 1);
            if(magicArrowType != null){
                getInstance().getSkillManager().getMagicArrows().setArrowData(shooterClient, spawnArrowFrom, newArrow, magicArrowType);
            }
        }
    }

//    @EventHandler
//    public void onProjShoot(EntityShootBowEvent event){
//        Entity entity = event.getEntity();
//        if(!(entity instanceof Player)) return;
//        Entity shot = event.getProjectile();
//        if(!(shot instanceof Arrow)) return;
//        Player player = (Player) entity;
//        Arrow arrow = (Arrow) shot;
//        Client client = getInstance().getClientManager().getClient(player);
//        if(!client.hasSkill(this)) return;
//        int level = client.getActiveBuild().getSkillLevel(this);
////        if(getInstance().getCooldownManager().isCooling(player, "Ricochet Arrow"))
////        getInstance().getCooldownManager().add(player, this, "Ricochet Arrow", level, getDelay(level) * 2, false);
//        float force = event.getForce();
//        MagicArrowType magicArrowType = null;
//        if(this.getInstance().getSkillManager().getMagicArrows().hasThisSkill(client)){
//            magicArrowType = getInstance().getSkillManager().getMagicArrows().getToggleMap().get(client.getUUID().toString());
//        }
//        MagicArrowData magicArrowData = getInstance().getSkillManager().getMagicArrows().getArrowMap().get(client.getUUID().toString())
//        final int targetCount = 2;
//
//        MagicArrowType finalMagicArrowType = magicArrowType;
//        new BukkitRunnable(){
//            int arrowCount = 0;
//            public void run() {
//                arrowCount++;
//                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 5, 1);
//                if (player.getInventory().getItemInMainHand().getType() == Material.BOW || player.getInventory().getItemInMainHand().getType() == Material.CROSSBOW) {
//                    Location eye = player.getEyeLocation();
//                    Arrow newArrow = player.getWorld().spawnArrow(
//                            eye,
//                            eye.getDirection().normalize(), // Use the stored direction
//                            force, // Convert strength to velocity (Spigot uses force * 3.0)
//                            0.0f
//                    );
//                    newArrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
//                    newArrow.setShooter(player);
//                    if (finalMagicArrowType != null) {
//                        getInstance().getSkillManager().getMagicArrows().setArrowData(client, player.getLocation(), newArrow);
//                    }
//                    if (arrowCount >= targetCount) {
//                        getInstance().getChat().sendModule(player, "You can use " + getInstance().getChat().highlightText + "Rapidfire" + getInstance().getChat().textColour + " again.", getClassTypeName());
//                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 5, 1);
//                        rapidfireMap.put(player.getUniqueId().toString(), false);
//                        this.cancel();
//                    }
//                }
//            }
//        }.runTaskTimer(getInstance(), getDelay(level), getDelay(level));
//    }

}