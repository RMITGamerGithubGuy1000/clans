package me.marco.PvPItems.Consumeables.HomingChrystal;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Client.Client;
import me.marco.PvPItems.Consumeables.Consumeable;
import me.marco.Utility.UtilParticles;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerMoveEvent;

public class HomingChrystal extends Consumeable implements Listener {

    public HomingChrystal(Core core) {
        super(core, "Homing Chrystal", Material.AMETHYST_SHARD, true);
        getInstance().getServer().getPluginManager().registerEvents(this, getInstance());
    }

    @Override
    public boolean useageCheck(Player player, Action action) {
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasClan()){
            getInstance().getChat().sendModule(player, "You cannot use a " + getInstance().getChat().highlightText + getPvPItemName() + "" +
                    getInstance().getChat().textColour + " without a clan", getPvPItemName());
            return false;
        }
        Clan clan = client.getClan();
        if(!clan.hasHome()){
            getInstance().getChat().sendModule(player, "You cannot use a " + getInstance().getChat().highlightText + getPvPItemName() + "" +
                    getInstance().getChat().textColour + " without a " + getInstance().getChat().highlightText + "Clan Home", getPvPItemName());
            return false;
        }
        Location home = clan.getHome();

        Chunk homeChunk = home.getChunk();

        if(!getInstance().getLandManager().isClaimed(homeChunk)){
            getInstance().getChat().sendClans(player, "Your home location is no longer in your clan's possession");
            return false;
        }

        Land homeLand = getInstance().getLandManager().getLand(homeChunk);
        Clan homeOwner = getInstance().getLandManager().getOwningClan(homeLand);

        if(homeOwner != clan){
            getInstance().getChat().sendClans(player, "Your home location is no longer in your clan's possession");
            return false;
        }
        if(!hasMana(player, action)) return false;
        if(!this.getCDManager().add(player, this, action, true)) return false;
        if(!canConsumeItem(player)) return false;
        return true;
    }

    @Override
    public void onUseage(Player player, Action action) {
        if(!useageCheck(player, action)) return;
        channelChrystal(player);
    }

    public void channelChrystal(Player player){
        Client client = getInstance().getClientManager().getClient(player);
        Clan clan = client.getClan();
        Location home = clan.getHome();
        getInstance().getTagManager().addTag(client, new HomingChrystalTag(client, player, player.getInventory().getHeldItemSlot(), home, getInstance()));
    }

    @Override
    public double getCooldown(Action action) {
        return 120;
    }

    @Override
    public int getMana(Action action) {
        return 40;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Block toBlock = event.getTo().getBlock();
        Block fromBlock = event.getFrom().getBlock();
        if(fromBlock.getX() == toBlock.getX() &&
                fromBlock.getZ() == toBlock.getZ() &&
                fromBlock.getY() == toBlock.getY()) return;
        Player player = event.getPlayer();
        Client client = getInstance().getClientManager().getClient(player);
        if(!client.hasTag("HomingChrystalTag")) return;
        client.removeTag("HomingChrystalTag");
    }
}
