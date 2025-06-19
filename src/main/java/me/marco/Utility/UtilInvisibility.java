package me.marco.Utility;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Tags.MiscTags.InvisibilityTag;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;

public class UtilInvisibility {

    private Core instance;

    public UtilInvisibility(Core instance){
        this.instance = instance;
    }

    public void hidePlayer(Player player, long duration){
        Client client = getInstance().getClientManager().getClient(player);
        getInstance().getTagManager().addTag(client, new InvisibilityTag(player, client, duration, getInstance()));
        getInstance().getServer().getOnlinePlayers().forEach(players -> players.hidePlayer(getInstance(), player));
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 4);
    }

    public void removeInvisHit(Player player){
        Client client = getInstance().getClientManager().getClient(player);
        revealPlayer(player);
        client.removeTag("Invisibility");
        UtilParticles.playEffect(player.getLocation().add(0, 1, 0).getBlock().getLocation(), Particle.EXPLOSION, 0, 0, 0, 0, 1);
    }

    public void removeInvis(Player player){
        Client client = getInstance().getClientManager().getClient(player);
        client.removeTag("Invisibility");
        revealPlayer(player);
    }

    public void revealPlayer(Player toHide) {
        toHide.getWorld().playSound(toHide.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 4);
        getInstance().getServer().getOnlinePlayers().forEach(players -> players.showPlayer(getInstance(), toHide));
        getInstance().getChat().sendModule(toHide, "You are no longer " + getInstance().getChat().highlightText + "Invisible", "Invisibility");
    }

    public void revealOnlinePlayers() {
        for(Player online : getInstance().getServer().getOnlinePlayers()){
            for(Player otherOnline : getInstance().getServer().getOnlinePlayers()){
                if(online == otherOnline) continue;
                online.showPlayer(getInstance(), otherOnline);
            }
        }
    }

    private final int ATTACK_RANGE = 3;

    public void checkNearbyInvisible(Player player){
        List<Player> playerList = new ArrayList<Player>();
        for(Entity entity : player.getNearbyEntities(ATTACK_RANGE, ATTACK_RANGE + 1, ATTACK_RANGE)){
            if(entity instanceof Player){
                playerList.add((Player) entity);
            }
        }
        BlockIterator iterator = new BlockIterator(player, ATTACK_RANGE);
        while(iterator.hasNext()){
            Block block = iterator.next();
            if(block.getType().isSolid()){
                break;
            }
            Player found = playerList.stream().filter(hit -> checkRemoveInvisHit(hit, block)).findFirst().orElse(null);
            if(found != null){
                removeInvisHit(found);
                callPhysicalDamageEvent(player, found);
                break;
            }
        }
    }

    private void callPhysicalDamageEvent(Player damager, Player target){
        UtilPvP utilPvP = getInstance().getUtilPvP();
        getInstance().getServer().getPluginManager().callEvent(
                new PhysicalDamageEvent(damager, target, utilPvP.handleSwordDamage(damager, target),
                        utilPvP.getPhysicalDamageCause(damager)));
    }

    public boolean checkRemoveInvisHit(Player hit, Block block){
        Client client = getInstance().getClientManager().getClient(hit);
        if(!client.hasTag("Invisibility")) return false;
        return checkIsInBlock(hit, block);
    }

    public boolean checkIsInBlock(Player player, Block block){
        Location loc = player.getLocation();
        Block locBlock = player.getLocation().getBlock();
        int pX = locBlock.getX();
        int pY = locBlock.getY();
        int pZ = locBlock.getZ();
        return block.getX() == pX && block.getZ() == pZ && (block.getY() == pY || block.getY() + 1 == pY || block.getY() - 1 == pY);
    }

    public Core getInstance() {
        return instance;
    }

    public void checkInvisibility(Player damager) {
        Client client = getInstance().getClientManager().getClient(damager);
        if(!client.hasTag("Invisibility")) return;
        removeInvis(damager);
    }
}
