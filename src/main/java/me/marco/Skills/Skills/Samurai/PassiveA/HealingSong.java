package me.marco.Skills.Skills.Samurai.PassiveA;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.CustomEntities.NMSFrog;
import me.marco.CustomEntities.NMSParrot;
import me.marco.Events.PotionEffects.PotionEffectEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Events.Skills.ClassDequipEvent;
import me.marco.Events.Skills.ClassEquipEvent;
import me.marco.Events.Skills.ClassPassiveAddEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.PassiveSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.PassiveA;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.N;

import java.util.HashMap;
import java.util.UUID;

public class HealingSong extends Skill implements PassiveSkill, PassiveA {

    public HealingSong(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public boolean useageCheck(Player player) {
        return true;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Summon a " + ChatColor.AQUA + "Parrot" + ChatColor.YELLOW + " that sits on your shoulder, ",
                ChatColor.YELLOW + "when reaching " + ChatColor.GREEN + "10" + ChatColor.YELLOW + " health it will sing a song",
                ChatColor.YELLOW + "and leave you, returning back " + ChatColor.GREEN + this.formatNumber(getCooldown(level), 1) + "" + ChatColor.YELLOW + " seconds later. ",
        };
    }

    @Override
    public double getCooldown(int level) {
        return 30 - level * 5;
    }

    @Override
    public int getMana(int level) {
        return 0;
    }

    private HashMap<UUID, Parrot> parrotMap = new HashMap<UUID, Parrot>();

    public void onEquip(Player player){
        equipParrot(player);
    }

    public void onDequip(Player player){
        handleParrotDelete(player, getLevel(player), false);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event){
        if(event.isCancelled()) return;
        Entity entity = event.getEntity();
        if(!(entity instanceof Player)) return;
        Player player = (Player) entity;
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasSkill(this)) return;
        Parrot parrot = parrotMap.get(player.getUniqueId());
        if(!parrotMap.containsKey(player.getUniqueId())) return;
        double dmgAfter = player.getHealth() - event.getDamage();
        if(dmgAfter > 0 && dmgAfter <= 10){
            handleParrotDelete(player, getLevel(player), true);
            UtilParticles.playInstantBreak(player.getLocation(), PotionEffectType.REGENERATION);
            getInstance().getServer().getPluginManager().callEvent(new PotionEffectEvent(player, player, PotionEffectType.REGENERATION, 140, 1));
        }
        parrot.remove();
//        new BukkitRunnable(){
//            public void run(){
//                player.setShoulderEntityLeft(parrot);
//            }
//        }.runTaskLater(getInstance(), 1);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) return;
        Entity entity = event.getEntity();
        if (entity instanceof Parrot) {
            Parrot parrot = (Parrot) entity;
            AnimalTamer tamer = parrot.getOwner();
            if(tamer instanceof Player){
                Player player = (Player) tamer;
                UUID puuid = player.getUniqueId();
                entity.remove();
                player.setShoulderEntityLeft(parrotMap.get(puuid));
            }
        }
    }

    private void equipParrot(Player player){
        if(getInstance().getCooldownManager().isCooling(player, getName())) return;
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasSkill(this)) return;
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PARROT_AMBIENT, 2, 1);
        getInstance().getChat().sendModule(player, "Your " + ChatColor.GREEN + "Parrot" + getInstance().getChat().textColour + " has returned to you!", getClassTypeName());
        NMSParrot nmsParrot = new NMSParrot(player.getLocation());
        Parrot parrot = (Parrot) nmsParrot.getBukkitEntity();
        player.setShoulderEntityLeft(parrot);
        parrot.setOwner(player);
        parrotMap.put(player.getUniqueId(), parrot);
        parrot.setCustomName(ChatColor.YELLOW + player.getName() + "'s Parrot");
        parrot.setCustomNameVisible(true);
        parrot.setAI(false);
    }

    private void handleParrotDelete(Player player, int level, boolean returnParrot){
        UUID uuid = player.getUniqueId();
        if(!parrotMap.containsKey(uuid)) return;
        Parrot parrot = parrotMap.get(uuid);
        parrotMap.remove(uuid);
        getInstance().getChat().sendModule(player, "You lost your " + ChatColor.GREEN + "Parrot", getClassTypeName());
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PARROT_DEATH, 2, 1);
        getInstance().getCooldownManager().add(player, this, this.getClassTypeName(), level, this.getCooldown(level), false);
        parrot.remove();
        if(returnParrot){
            new BukkitRunnable(){
                public void run(){
                    equipParrot(player);
                }
            }.runTaskLater(getInstance(), (long) getCooldown(level) * 20);
        }
    }

}
