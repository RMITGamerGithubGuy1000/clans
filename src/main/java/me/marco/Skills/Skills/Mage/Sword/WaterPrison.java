package me.marco.Skills.Skills.Mage.Sword;

import me.marco.Base.Core;
import me.marco.BlockChange.BlockChange;
import me.marco.Client.Client;
import me.marco.Events.PvP.ArrowDamageEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Events.PvP.PureDamageEvent;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.ChannelSkill;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.AxeSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.SwordSkill;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WaterPrison extends ChannelSkill implements SwordSkill, InteractSkill {

    private HashMap<String, WaterPrisonData> targetData = new HashMap<String, WaterPrisonData>();
    private HashMap<String, WaterPrisonCasterData> targetMap = new HashMap<String, WaterPrisonCasterData>();

    public WaterPrison(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    private final int range = 12;

    @Override
    public float requiredEnergy(int level) {
        return .01f;
    }

    @Override
    public float requiredReserve(int level) {
        return .3f;
    }

    @Override
    public double toggleCooldown(int level) {
        return 16 - 2 * level ;
    }

    @Override
    public int getTicks() {
        return 5;
    }

    @Override
    public boolean useageCheck(Player player) {
        int level = getLevel(player);
        return canCastChannel(player, true, false, level);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Activate by targeting a player and launching them up. ",
                ChatColor.YELLOW + "You must maintain " + ChatColor.LIGHT_PURPLE + "Line of Sight" + ChatColor.YELLOW + " on your target. ",
                ChatColor.AQUA + "Water Prison" + ChatColor.YELLOW + " traps your target and prevents them ",
                ChatColor.YELLOW + "from taking any " + ChatColor.LIGHT_PURPLE + "Damage" + ChatColor.YELLOW + " whilst imprisoned, ",
                ChatColor.YELLOW + "as well dealing " + ChatColor.GREEN + "0.5" + ChatColor.LIGHT_PURPLE + " Pure Damage" + ChatColor.YELLOW + " damage periodically. ",
        };
    }

    @Override
    public double getCooldown(int level) {
        return 30 - level * 5;
    }

    @Override
    public int getMana(int level) {
        return 55 - level * 5;
    }

    @Override
    public boolean runTick(Player player) {
        WaterPrisonCasterData casterData = targetMap.get(player.getUniqueId().toString());
        Player target = casterData.getTarget();
        if(target != null) {
            if (!targetData.containsKey(target.getUniqueId().toString())) {
                if (casterData.canMakePrison()) {
                    makePrison(player, target);
                    return true;
                }
            }
        }
        WaterPrisonData data = targetData.get(target.getUniqueId().toString());
        if(data == null) return true;
        if(data.getToPullTo() == null) return true;
        if(!isMaintainingPlayer(player, target, range)){
            return false;
        }
        pullPlayer(player, target, data.getToPullTo());
        if(data.checkDamage()){
            getInstance().getServer().getPluginManager().callEvent(new PureDamageEvent(player, target, 1, getName()));
        }
        return true;
    }

    @Override
    public void cleanup(Player player) {
        if(targetMap.containsKey(player.getUniqueId().toString())){
            WaterPrisonCasterData casterData = targetMap.get(player.getUniqueId().toString());
            Player target = casterData.getTarget();
            WaterPrisonData waterPrisonData = targetData.get(target.getUniqueId().toString());
            if(waterPrisonData == null) return;
            waterPrisonData.wipePrison();
            targetData.remove(target.getUniqueId().toString());
        }
        getInstance().getChat().sendModule(player, "Your " + getInstance().getChat().highlightText + getName() +
                getInstance().getChat().textColour + " broke", getClassTypeName());
        this.targetMap.remove(player.getUniqueId().toString());
    }

    public void pullPlayer(Player caster, Player target, Location toPullTo){
        UtilParticles.drawLine(target.getLocation().add(0, 1, 0), caster.getLocation().add(0, 1, 0), Particle.FISHING,
                .3, 0, 0, 0, 0);
//        target.teleport(new Location(toPullTo.getWorld(), toPullTo.getX(), toPullTo.getY(), toPullTo.getZ(), target.getLocation().getYaw(), target.getLocation().getPitch()));
    }

    @Override
    public void activate(Player player, int level) {
//        if(this.getCDManager().isCooling(player, this.getName())){
//            this.getCDManager().sendRemaining(player, getName());
//            return;
//        }
//        if(getInstance().getClientManager().getClient(player).getIsChanneling() == this){
//            this.isChanneling(player);
//            return;
//        }
        Player target = getTarget(player, range);
        if(target == null){
            getInstance().getChat().sendModule(player, "You do not have a target for " + getInstance().getChat().highlightText + getName(), getClassTypeName());
            return;
        }
        Client targetClient = getInstance().getClientManager().getClient(target);
        Client playerClient = getInstance().getClientManager().getClient(player);
        if(targetClient.isFriendly(playerClient)){
            getInstance().getChat().sendModule(player, "You can not cast " + getInstance().getChat().highlightText + getName() +
                    getInstance().getChat().textColour + " on " + getInstance().getChat().getClanRelation(targetClient, playerClient) + target.getName(), getClassTypeName());
            return;
        }
        if(targetData.containsKey(target.getUniqueId().toString())){
            getInstance().getChat().sendModule(player, getInstance().getChat().getClanRelation(targetClient, playerClient) + target.getName() +
                    getInstance().getChat().textColour + " is already in a " + getInstance().getChat().highlightText + getName(), getClassTypeName());
            return;
        }
        castChannelAbility(player, getLevel(player));
        target.setSprinting(false);
        target.setVelocity(new Vector(0, .1, 0));
        initialCast(player, target, 1.0);
        toggleChannel(player);
    }

    public void initialCast(Player player, Player target, double velocitySpeed){
        target.setVelocity(target.getVelocity().setY(velocitySpeed));
        targetMap.put(player.getUniqueId().toString(), new WaterPrisonCasterData(target));
//        new BukkitRunnable(){
//            public void run(){
//                makePrison(player, target);
//            }
//        }.runTaskLater(getInstance(), 10);
    }

    public Player getTarget(Player player, int range){
        BlockIterator iterator = new BlockIterator(player.getWorld(), player
                .getLocation().add(0, 1, 0).toVector(), player.getEyeLocation()
                .getDirection(), 0, range);
        List<Player> nearPlayers = new ArrayList<Player>();
        for(Entity entity : player.getNearbyEntities(range, range, range)){
            if(!(entity instanceof Player)) continue;
            Player found = (Player) entity;
            nearPlayers.add(found);
        }
        Player target = null;
        Block block;
        Location loc;
        int bx, by, bz;
        int ex, ey, ez;
        while(iterator.hasNext()){
            block = iterator.next();
            if(block.getType().isOccluding() && !block.isPassable()){
                break;
            }
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            for(Player found : nearPlayers){
                loc = found.getLocation();
                ex = loc.getBlock().getX();
                ey = loc.getBlock().getY();
                ez = loc.getBlock().getZ();
                if ((bx == ex && ex == bx) && (bz == ez && ez == bz) && (by - 1 <= ey && ey <= by + 1)) {
                    target = found;
                    break;
                }
            }
        }
        return target;
    }

    public boolean isMaintainingPlayer(Player player, Player target, int range){
        BlockIterator iterator = new BlockIterator(player.getWorld(), player
                .getLocation().add(0, 1, 0).toVector(), player.getEyeLocation()
                .getDirection(), 0, range);
        Block block;
        Location loc;
        int bx, by, bz;
        int ex, ey, ez;
        boolean isLooking = false;
        while(iterator.hasNext()){
            block = iterator.next();
            if(block.getType().isOccluding() && !block.isPassable()){
                break;
            }
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            loc = target.getLocation();
            ex = loc.getBlock().getX();
            ey = loc.getBlock().getY();
            ez = loc.getBlock().getZ();
            if ((bx - 1 <= ex && ex <= bx + 1) && (bz - 1 <= ez && ez <= bz + 1) && (by - 2 <= ey && ey <= by + 2)) {
                isLooking = true;
                break;
            }
        }
        return isLooking;
    }

    public void makePrison(Player caster, Player player) {
        if(!checkChannelingForToggle(caster)) return;
        WaterPrisonData waterPrisonData = new WaterPrisonData(player, player.getLocation().getBlock().getLocation().add(.5, 0, .5));
        Location topull = waterPrisonData.getToPullTo();
        Location toTp = new Location(topull.getWorld(), topull.getX(), topull.getY(), topull.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        player.teleport(toTp);
        targetData.put(player.getUniqueId().toString(), waterPrisonData);
        HashMap<Block, Material> blockMap = new HashMap<Block, Material>();
        // filling up  middle line
        makeWater(player.getLocation(), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(0, 2, 0), waterPrisonData, blockMap);makeWater(player.getLocation().add(0, 1, 0), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(0, -1, 0), waterPrisonData, blockMap);makeWater(player.getLocation().add(0, -2, 0), waterPrisonData, blockMap);
        // making center circle
        makeWater(player.getLocation().add(0, 0, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(-1, 0, 0), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(1, 0, 0), waterPrisonData, blockMap);makeWater(player.getLocation().add(0, 0, -1), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(1, 0, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(-1, 0, -1), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(-1, 0, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(1, 0, -1), waterPrisonData, blockMap);
        // making y+1 center circle
        makeWater(player.getLocation().add(0, 1, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(-1, 1, 0), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(1, 1, 0), waterPrisonData, blockMap);makeWater(player.getLocation().add(0, 1, -1), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(1, 1, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(-1, 1, -1), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(-1, 1, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(1, 1, -1), waterPrisonData, blockMap);
        // making y-1 center circle
        makeWater(player.getLocation().add(0, -1, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(-1, -1, 0), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(1, -1, 0), waterPrisonData, blockMap);makeWater(player.getLocation().add(0, -1, -1), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(1, -1, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(-1, -1, -1), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(-1, -1, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(1, -1, -1), waterPrisonData, blockMap);
        // making y+2 center circle
        makeWater(player.getLocation().add(0, 2, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(-1, 2, 0), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(1, 2, 0), waterPrisonData, blockMap);makeWater(player.getLocation().add(0, 2, -1), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(1, 2, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(-1, 2, -1), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(-1, 2, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(1, 2, -1), waterPrisonData, blockMap);
        // making y-2 center circle
        makeWater(player.getLocation().add(0, -2, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(-1, -2, 0), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(1, -2, 0), waterPrisonData, blockMap);makeWater(player.getLocation().add(0, -2, -1), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(1, -2, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(-1, -2, -1), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(-1, -2, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(1, -2, -1), waterPrisonData, blockMap);
        // making center outside faces
        makeWater(player.getLocation().add(2, 0, 0), waterPrisonData, blockMap);makeWater(player.getLocation().add(2, 0, 1), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(2, 0, -1), waterPrisonData, blockMap);makeWater(player.getLocation().add(0, 0, -2), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(1, 0, -2), waterPrisonData, blockMap);makeWater(player.getLocation().add(-1, 0, -2), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(0, 0, 2), waterPrisonData, blockMap);makeWater(player.getLocation().add(1, 0, 2), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(-1, 0, 2), waterPrisonData, blockMap);makeWater(player.getLocation().add(-2, 0, 0), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(-2, 0, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(-2, 0, -1), waterPrisonData, blockMap);
        // making y+1 outside faces
        makeWater(player.getLocation().add(2, 1, 0), waterPrisonData, blockMap);makeWater(player.getLocation().add(2, 1, 1), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(2, 1, -1), waterPrisonData, blockMap);makeWater(player.getLocation().add(0, 1, -2), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(1, 1, -2), waterPrisonData, blockMap);makeWater(player.getLocation().add(-1, 1, -2), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(0, 1, 2), waterPrisonData, blockMap);makeWater(player.getLocation().add(1, 1, 2), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(-1, 1, 2), waterPrisonData, blockMap);makeWater(player.getLocation().add(-2, 1, 0), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(-2, 1, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(-2, 1, -1), waterPrisonData, blockMap);
        // making y-1 outside faces
        makeWater(player.getLocation().add(2, -1, 0), waterPrisonData, blockMap);makeWater(player.getLocation().add(2, -1, 1), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(2, -1, -1), waterPrisonData, blockMap);makeWater(player.getLocation().add(0, -1, -2), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(1, -1, -2), waterPrisonData, blockMap);makeWater(player.getLocation().add(-1, -1, -2), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(0, -1, 2), waterPrisonData, blockMap);makeWater(player.getLocation().add(1, -1, 2), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(-1, -1, 2), waterPrisonData, blockMap);makeWater(player.getLocation().add(-2, -1, 0), waterPrisonData, blockMap);
        makeWater(player.getLocation().add(-2, -1, 1), waterPrisonData, blockMap);makeWater(player.getLocation().add(-2, -1, -1), waterPrisonData, blockMap);
        BlockChange blockChange = new BlockChange(blockMap, 100);
        getInstance().getBlockChangeManager().addBlockChange(blockChange);
        waterPrisonData.setBlockChange(blockChange);
    }

    public void makeWater(Location location, WaterPrisonData data, HashMap<Block, Material> blockMap){
        Block block = location.getBlock();
        if(block.getType() != Material.AIR) return;
        addToMap(block, Material.WATER, blockMap);
    }

    public void addToMap(Block block, Material type, HashMap<Block, Material> blockMap){
        blockMap.put(block, type);
    }

    public boolean isWaterPrisonBlock(Block block){
        for(WaterPrisonData waterPrisonData : this.targetData.values()){
            if(waterPrisonData.isBlock(block)){
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if(isWaterPrisonBlock(event.getBlock())) event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(this.targetData.containsKey(player.getUniqueId().toString())){
            WaterPrisonData waterPrisonData = targetData.get(player.getUniqueId().toString());
            if(waterPrisonData.getToPullTo().distance(event.getTo()) > 1) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPureDmg(PureDamageEvent event){
        if(targetData.containsKey(event.getTarget().getUniqueId().toString()) &&
                !event.getCause().equalsIgnoreCase(getName())) event.setCancelled(true);
    }

    @EventHandler
    public void onArrowDmg(ArrowDamageEvent event){
        if(targetData.containsKey(event.getTarget().getUniqueId().toString()) &&
                !event.getCause().equalsIgnoreCase(getName())) event.setCancelled(true);
    }

    @EventHandler
    public void onPhysicalDamage(PhysicalDamageEvent event){
        if(targetData.containsKey(event.getTarget().getUniqueId().toString()) &&
                !event.getCause().equalsIgnoreCase(getName())) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(targetData.containsKey(event.getEntity().getUniqueId().toString())) event.setCancelled(true);
    }

}
