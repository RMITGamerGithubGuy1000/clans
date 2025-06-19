package me.marco.Handlers;

import me.marco.Admin.AdminClans.AdminClan;
import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Client.Client;
import me.marco.Utility.Chat;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class LandManager {

    private Core instance;

    public LandManager(Core instance){
        this.instance = instance;
    }

    public HashMap<Client, Clan> moveMap = new HashMap<Client, Clan>();

    public boolean isTracked(Client client){
        return moveMap.containsKey(client);
    }

    public void getChunkOutlines(Chunk chunk, Player player){
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (z == 15 || z == 0 || x == 15 || x == 0) {
                    Block block = chunk.getBlock(x, 0, z);
                    Block highest = chunk.getWorld().getHighestBlockAt(block.getLocation());
                    Location ploc = player.getLocation();
                        Material type = highest.getType();
                        if(type == Material.GRASS_BLOCK
                                || type == Material.DIRT
                                || type == Material.STONE
                                || type == Material.GRAVEL
                                || type == Material.SAND
                                || type == Material.ANDESITE
                                || type == Material.DIORITE) {
                            highest.setType(Material.GLOWSTONE);
                    }
                }
            }
        }
    }

    public boolean nearbyClaims(Chunk chunk, Clan clan){
        World world = chunk.getWorld();
        int x = chunk.getX();
        int z = chunk.getZ();
        Land landMZ = getLand(world.getChunkAt(x, z - 1));
        if(landMZ != null && getOwningClan(landMZ) != clan){
            return true;
        }
        Land landPZ = getLand(world.getChunkAt(x, z + 1));
        if(landPZ != null && getOwningClan(landPZ) != clan){
            return true;
        }
        Land landMX = getLand(world.getChunkAt(x - 1, z));
        if(landMX != null && getOwningClan(landMX) != clan){
            return true;
        }
        Land landPX = getLand(world.getChunkAt(x + 1, z));
        if(landPX != null && getOwningClan(landPX) != clan){
            return true;
        }
        return false;
    }

    public void trackClient(Client client, Clan clan){
        if(isTracked(client) && moveMap.get(client) != clan){
            moveMap.put(client, clan);
            sendTrack(client);
        }else if(!isTracked(client)){
            moveMap.put(client, clan);
            sendTrack(client);
        }
    }

    public void sendTrack(Client client){
        Clan clan = moveMap.get(client);
        instance.getChat().sendLandMessage(client, clan);
        client.getScoreboard().updateLand(clan);
    }

    public boolean isClaimed(Chunk chunk){
        for(Clan clan : instance.getClanManager().clanList){
            for(Land land : clan.getLand()){
                if(land.getX() == chunk.getX() && land.getZ() == chunk.getZ()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Land getLand(Chunk chunk){
        for(Clan clan : instance.getClanManager().clanList){
            for(Land land : clan.getLand()){
                if(land.getX() == chunk.getX() && land.getZ() == chunk.getZ()) {
                    return land;
                }
            }
        }
        return null;
    }

    public Clan getOwningClan(Land land){
        for(Clan clan : instance.getClanManager().clanList){
            for(Land cLand : clan.getLand()){
                if(cLand == land) {
                    return clan;
                }
            }
        }
        return null;
    }

    public Clan getOwningClan(Chunk chunk){
        Land land = getLand(chunk);
        if(land != null) {
            for (Clan clan : instance.getClanManager().clanList) {
                for (Land cLand : clan.getLand()) {
                    if (cLand == land) {
                        return clan;
                    }
                }
            }
        }
        return null;
    }

    public boolean canBreakBlock(Player player, Block block){
        Client client = instance.getClientManager().getClient(player);
        if(client.isAdminMode()) return true;
        if(instance.getFieldsManager().isFieldsBlock(block)){
            instance.getFieldsManager().breakBlock(player, block);
            return false;
        }
        Clan blockClan = getOwningClan(block.getChunk());
        if(blockClan == null) {
            return true;
        }
        if(client.hasClan()){
            Clan clan = client.getClan();
            if(clan == blockClan) return true;
            if(clan.isPillaging(blockClan)) return true;
        }
        if(block.getType() == Material.FIRE) return true;
        instance.getChat().sendClans(player, "You cannot break " + instance.getChat().highlightText
                + instance.getChat().formatMaterial(block.getType()) + "" + instance.getChat().textColour + " in clan "
                + instance.getChat().getPlayerRelationMain(client, blockClan) + blockClan.getName());
        return false;
    }

    public boolean canPlaceBlock(Player player, Block block){
        Client client = instance.getClientManager().getClient(player);
        if(client.isAdminMode()) return true;
        Clan blockClan = getOwningClan(block.getChunk());
        if(blockClan == null) {
            return true;
        }
        if(client.hasClan()){
            Clan clan = client.getClan();
            if(clan == blockClan) return true;
            if(clan.isPillaging(blockClan)) return true;
        }
        instance.getChat().sendClans(player, "You cannot place " + instance.getChat().highlightText
                + instance.getChat().formatMaterial(block.getType()) + "" + instance.getChat().textColour + " in clan "
                + instance.getChat().getPlayerRelationMain(client, blockClan) + blockClan.getName());
        return false;
    }

    public boolean canInteract(Player player, Block block){
        Client client = instance.getClientManager().getClient(player);
        if(client.isAdminMode()) return true;
        Clan blockClan = getOwningClan(block.getChunk());
        if(blockClan == null) {
            return true;
        }
        if(client.hasClan()){
            Clan clan = client.getClan();
            if(clan == blockClan) return true;
            if(clan.isTrusted(blockClan) && !isTrustedWhitelist(block.getType())) return true;
            if(clan.isPillaging(blockClan)) return true;
        }
        instance.getChat().sendClans(player, "You cannot interact with " + instance.getChat().highlightText
                + instance.getChat().formatMaterial(block.getType()) + "" + instance.getChat().textColour + " in clan "
                + instance.getChat().getPlayerRelationMain(client, blockClan) + blockClan.getName());
        return false;
    }

    public boolean isWhitelistedInteractBlock(Material material){
        return (material.name().toLowerCase().contains("button") ||
                material.name().toLowerCase().contains("door") ||
                material == Material.LEVER ||
                material.name().toLowerCase().contains("chest") ||
                material.name().toLowerCase().contains("gate"));
    }

    public boolean isTrustedWhitelist(Material material){
        return (material.name().toLowerCase().contains("chest") ||
                material == Material.BARREL);
    }

    public boolean isInSafezone(Player player){
        Land land = getLand(player.getLocation().getChunk());
        if(land != null){
            Clan clan = getOwningClan(land);
            if(clan instanceof AdminClan){
                return ((AdminClan) clan).isSafe();
            }
        }
        return false;
    }

}
