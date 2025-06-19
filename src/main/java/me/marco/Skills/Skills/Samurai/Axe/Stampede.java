package me.marco.Skills.Skills.Samurai.Axe;

import me.marco.Base.Core;
import me.marco.CustomEntities.NMSRavager;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.ChannelSkill;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.AxeSkill;
import me.marco.Utility.UtilParticles;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Ravager;
import org.bukkit.event.block.Action;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class Stampede extends ChannelSkill implements AxeSkill, InteractSkill {

    public Stampede(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public float requiredEnergy(int level) {
        return .01f;
    }

    @Override
    public float requiredReserve(int level) {
        return .5f;
    }

    @Override
    public double toggleCooldown(int level) {
        return 2;
    }

    @Override
    public int getTicks() {
        return 30;
    }

    private HashMap<UUID, Ravager> ravagerMap = new HashMap<UUID, Ravager>();
    private HashMap<Ravager, Boolean> flagMap = new HashMap<Ravager, Boolean>();

    private double getSlamRange(int level){
        return 4 + level * .5;
    }

    @Override
    public boolean runTick(Player player) {

        Ravager ravager = ravagerMap.get(player.getUniqueId());
        if(flagMap.containsKey(ravager) && flagMap.get(ravager)) return true;
        ravager.getWorld().playSound(ravager.getLocation(), Sound.ENTITY_RAVAGER_ATTACK, 2, 2);
        ravager.setVelocity(ravager.getVelocity().normalize().setY(.5));
        flagMap.put(ravager, true);
        int level = getLevel(player);
        new BukkitRunnable(){
            public void run(){
                Vector tosend = player.getLocation().getDirection().normalize();
                tosend.setY(-.8);
                ravager.setVelocity(tosend);
                new BukkitRunnable(){
                    public void run(){
                        if((ravager.isOnGround())){
                            useSlam(ravager, player, getSlamRange(level), level);
                            flagMap.put(ravager, false);
                            this.cancel();
                        }
                    }
                }.runTaskTimer(getInstance(), 0, 1);
            }
        }.runTaskLater(getInstance(), 10);
        return true;
    }

    private double getLaunchVelocity(int level){
        return .8;
    }

    public void useSlam(Ravager ravager, Player owner, double range, int level){
        if ((ravager).isOnGround()) {
            UtilParticles.drawParticleCircle(ravager.getLocation(), Particle.ELECTRIC_SPARK, range, 0, 0, 0, 0, 0, 0);
            for (final Entity entity : ravager.getNearbyEntities(range, 1.0, range)) {
                if (entity instanceof Player) {
                    Player p2 = (Player) entity;
                    if (!entity.getUniqueId().equals(owner.getUniqueId())) {
                        if(p2.getLocation().getBlock().getType() == Material.AIR){
                            final Block block = entity.getLocation().getBlock().getRelative(BlockFace.DOWN);
                            FallingBlock t = block.getWorld().spawnFallingBlock(block.getLocation().add(0, 1, 0), block.getType().createBlockData());
                            t.setDropItem(false);
                            t.setVelocity(t.getVelocity().setY(.8));
                            entity.getWorld().playEffect(entity.getLocation(), Effect.STEP_SOUND, block.getType());
                            t.setMetadata("noplace", new FixedMetadataValue(getInstance(), true));
                        }
                        getInstance().getServer().getPluginManager().callEvent(
                                new PhysicalDamageEvent(owner, p2, 2,
                                        getName()));
                        new BukkitRunnable(){
                            public void run(){
                                p2.setVelocity(p2.getVelocity().setY(getLaunchVelocity(level)));
                            }
                        }.runTaskLater(getInstance(), 1);

                    }
                }
            }
            ravager.getWorld().playSound(ravager.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1.0F, 0.5F);
        }
    }

    @Override
    public void cleanup(Player player) {
        flagMap.remove(ravagerMap.get(player.getUniqueId()));
        ravagerMap.get(player.getUniqueId()).remove();
        ravagerMap.remove(player.getUniqueId());
    }

    @Override
    public boolean useageCheck(Player player) {
        int level = getLevel(player);
        return canCastChannel(player, true, false, level);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                org.bukkit.ChatColor.YELLOW + "Summon a " + org.bukkit.ChatColor.AQUA + "Ravager" + org.bukkit.ChatColor.YELLOW + " and mount it. While channeling, the",
                ChatColor.AQUA + "Ravager" + ChatColor.YELLOW + " continuously performs a " + ChatColor.AQUA + "Stampede ",
                ChatColor.YELLOW + "launching itself up and down, " + ChatColor.LIGHT_PURPLE + "knocking " + ChatColor.YELLOW + "nearby ",
                ChatColor.YELLOW + "players in a " + ChatColor.GREEN + this.formatNumber(getSlamRange(level), 1) + "" + ChatColor.YELLOW + " radius upwards by a force of " + ChatColor.GREEN + this.formatNumber(getLaunchVelocity(level), 1) + "" + ChatColor.YELLOW + ".",
        };
    }

    @Override
    public double getCooldown(int level) {
        return 40;
    }

    @Override
    public int getMana(int level) {
        return 50;
    }

    public void activate(Player player, int level) {
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_3, 2, 2);
        NMSRavager nmsRavager = new NMSRavager(player.getLocation().add(0, 2, 0));
        Ravager ravager = (Ravager) nmsRavager.getBukkitEntity();
        ravager.setCustomName(ChatColor.RED + player.getName() + " Ravager");
        ravager.setCustomNameVisible(true);
        ravagerMap.put(player.getUniqueId(), ravager);
        flagMap.put(ravager, false);
        ravager.addPassenger(player);

        castChannelAbility(player, getLevel(player));
        toggleChannel(player);
    }

}
