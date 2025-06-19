package me.marco.Skills.Skills.Guardian.Axe;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.AxeSkill;
import me.marco.Skills.Data.Skill;
import me.marco.Tags.MiscTags.NoFall;
import me.marco.Utility.UtilParticles;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SeismicSlam extends Skill implements AxeSkill, InteractSkill {

    private List<FallingBlock> fallingBlockList = new ArrayList<FallingBlock>();

    public SeismicSlam(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
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
        return 20;
    }

    @Override
    public int getMana(int level) {
        return 20;
    }

    public void activate(Player player, int level){
        Client client = getInstance().getClientManager().getClient(player);
        getInstance().getTagManager().addTag(client, new NoFall(client, 6, getInstance()));
            player.setVelocity(player.getVelocity().normalize().setY(1));
            new BukkitRunnable(){
                public void run(){
                    Vector tosend = player.getLocation().getDirection().normalize();
                    tosend.multiply(0.5);
                    tosend.setY(-0.4);
                    player.setVelocity(tosend);
                    new BukkitRunnable(){
                        int y = (int) player.getLocation().getY();
                        public void run(){
                            if(((Entity) player).isOnGround()){
                                useSlam(player, (int) (y - player.getLocation().getY()));
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(getInstance(), 0, 1);
                }
            }.runTaskLater(getInstance(), 15);
    }

    public void useSlam(Player player, int distance){
        if (((Entity) player).isOnGround()) {
            int range = (int) (5 + (distance / 3));
            if(range > 7){
                range = 7;
            }
            UtilParticles.drawParticleCircle(player.getLocation(), Particle.GLOW_SQUID_INK, range, 0, 0, 0, 0, 0, 0);
            for (final Entity entity : player.getNearbyEntities(range, 2.0, range)) {
                if (entity instanceof Player) {
                    Player p2 = (Player) entity;
                    double knockup = 1 + (range / 100);
                    if(knockup > 1.1){
                        knockup = 1.1;
                    }
                    if (!entity.getUniqueId().equals(player.getUniqueId())) {
                        if(p2.getLocation().getBlock().getType() == Material.AIR){
                            final Block block = entity.getLocation().getBlock().getRelative(BlockFace.DOWN);
                            FallingBlock t = block.getWorld().spawnFallingBlock(block.getLocation().add(0, 1, 0), block.getType().createBlockData());
                            t.setDropItem(false);
                            t.setVelocity(t.getVelocity().setY(.8));
                            entity.getWorld().playEffect(entity.getLocation(), Effect.STEP_SOUND, block.getType());
                            t.setMetadata("noplace", new FixedMetadataValue(getInstance(), true));
                        }
                        p2.setVelocity(p2.getVelocity().setY(knockup));
                        p2.damage(2);
                        getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(p2, player, PotionEffectType.SLOWNESS, 80, 0));

                    }
                }
            }
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1.0F, 0.5F);
        }
    }

//    @EventHandler
//    public void onBlockLand(EntityChangeBlockEvent event){
//        Entity entity = event.getEntity();
//        if(!(entity instanceof FallingBlock)) return;
//        FallingBlock fb = (FallingBlock) entity;
//        if(!isFallingBlock(fb)) return;
//        fb.remove();
//        event.setCancelled(true);
//    }

}
