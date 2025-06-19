package me.marco.Events;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Client.Client;
import me.marco.Cosmetics.Pets.NMS.NMSSheep;
import me.marco.Events.PvP.ArrowDamageEvent;
import me.marco.Tags.PvPTag;
import me.marco.Events.Clans.CustomEvents.Relations.ClanDominanceUpdateEvent;
import me.marco.Events.PvP.PhysicalDamageEvent;
import me.marco.Handlers.LandManager;
import me.marco.Utility.UtilParticles;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.w3c.dom.Attr;

public class NormalListener extends CListener<Core> {

    public NormalListener(Core instance) {
        super(instance);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        Item item = event.getItem();
        getInstance().getItemManager().handleItemStackNormalisation(item.getItemStack());
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (event.getBlock().getType() == Material.ICE ||
                event.getBlock().getType() == Material.FROSTED_ICE ||
                event.getBlock().getType() == Material.PACKED_ICE ||
                event.getBlock().getType() == Material.BLUE_ICE) {
            event.setCancelled(true); // Prevent it from melting
        }
    }

    @EventHandler
    public void onItemCraft(CraftItemEvent event){
        ItemStack is = event.getCurrentItem();
        getInstance().getItemManager().handleItemStackNormalisation(is);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (!getInstance().getLandManager().canBreakBlock(player, block)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (!getInstance().getLandManager().canPlaceBlock(player, block)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBlockLand(EntityChangeBlockEvent event){
        Entity entity = event.getEntity();
        if(!(entity instanceof FallingBlock)) return;
        FallingBlock fb = (FallingBlock) entity;
        if(!fb.hasMetadata("noplace")) return;
        fb.remove();
        event.setCancelled(true);
    }

    @EventHandler
    public void onProjHit(ProjectileHitEvent event){
        Projectile projectile = event.getEntity();
        if(projectile instanceof Arrow){
            Arrow arrow = (Arrow) projectile;
            if(arrow.getPickupStatus() == AbstractArrow.PickupStatus.DISALLOWED)
                arrow.remove();
            return;
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event){
        Chunk chunk = event.getChunk();
        if(!getInstance().getCustomEntityManager().isCustomEntityChunk(chunk)) return;
        getInstance().getCustomEntityManager().respawnEntities(chunk);
    }

//    public void test(Player player){
////        getInstance().getCustomPVPMobManager().spawnTest(player);
////        NMSSlime nmsSlime = new NMSSlime(player.getLocation(), -5);
////        nmsSlime.getBukkitEntity().setCustomName("test");
////        nmsSlime.getBukkitEntity().setCustomNameVisible(true);
////        nmsSlime.getBukkitEntity().setGravity(false);
//        NMSSheep nmsSheep = new NMSSheep(player, player.getLocation());
//        Sheep sheep = (Sheep) nmsSheep.getBukkitEntity();
//        sheep.setColor(DyeColor.CYAN);
//        sheep.setBaby();
//        sheep.setAgeLock(true);
//        sheep.setCustomNameVisible(true);
//        sheep.setCustomName(ChatColor.GOLD + player.getName() + "'s" + ChatColor.DARK_AQUA + " Sheep");
//    }

//    @EventHandler
//    public void onEntityInteract(PlayerInteractEntityEvent event){
//        Entity entity = event.getRightClicked();
//        if(!entity.isCustomNameVisible()) return;
//        String name = ChatColor.stripColor(entity.getCustomName());
//        if(!event.getHand().equals(EquipmentSlot.HAND)) return;
//        if(name.contains(event.getPlayer().getName())){
//            entity.addPassenger(event.getPlayer());
//        }
//    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if(player.getInventory().getItemInMainHand().getType() == Material.CORNFLOWER){
            getInstance().getWorldBossManager().test();
//            getInstance().getLegendaryItemManager().test(player);
            return;
        }
        if(player.getInventory().getItemInMainHand().getType() == Material.STICK){
            getInstance().getQuestManager().assignRandomQuest(player);
            return;
        }
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) {
            Block block = event.getClickedBlock();
            if (block != null) {
                if (getInstance().getLandManager().isWhitelistedInteractBlock(block.getType())) {
                    if (!getInstance().getLandManager().canInteract(player, block)) {
                        event.setCancelled(true);
                        return;
                    }
                }
                if(block.getType() == Material.ENCHANTING_TABLE){
                    event.setCancelled(true);
                    getInstance().getSkillManager().openArmourViewMenu(player);
                }
            }
        }
        if(action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR){
            getInstance().getUtilInvisibility().checkNearbyInvisible(player);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Client client = getInstance().getClientManager().getClient(player);
        if (client.hasClan()) {
            Clan clan = client.getClan();
            if (player.getKiller() != null && player.getKiller() instanceof Player) {
                Player pker = player.getKiller();
                Client killer = getInstance().getClientManager().getClient(pker);
                if (killer.hasClan()) {
                    Clan killerClan = killer.getClan();
                    if (clan.isEnemied(killerClan)) {
                        getInstance().getServer().getPluginManager().callEvent(new ClanDominanceUpdateEvent(killerClan, clan));
                    }
                }
            } else {
                Land land = getInstance().getLandManager().getLand(player.getLocation().getChunk());
                if (land != null) {
                    Clan owningClan = getInstance().getLandManager().getOwningClan(land);
                    if (clan.isEnemied(owningClan)) {
                        getInstance().getServer().getPluginManager().callEvent(new ClanDominanceUpdateEvent(owningClan, clan));
                    }
                }
            }
        }
        event.setDeathMessage(null);
        if (client.hasPvPTag()) {
            PvPTag pvptag = client.getPvPTag();
            getInstance().getChat().handleDeathMessage(pvptag);
            getInstance().getEconomyManager().handleDeathSteal(player, pvptag.getDamager());
            return;
        }
        getInstance().getChat().handleDeathMessage(player);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Client client = getInstance().getClientManager().getClient(player);
        event.setCancelled(true);
        for (Player players : getInstance().getServer().getOnlinePlayers()) {
            Client receivingClient = getInstance().getClientManager().getClient(players);
            getInstance().getChat().sendChatMessage(players, client, receivingClient, event.getMessage());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
//        player.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(.2);
        getInstance().getServer().broadcastMessage(player.getWalkSpeed() + " walk");
        getInstance().getServer().broadcastMessage(player.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue() + "");
        player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(16);
        event.setJoinMessage(getInstance().getChat().joinMessage(player));
        if (!getInstance().getClientManager().clientExists(player)) {
            Client client = new Client(player);
            getInstance().getClientManager().addClient(client);
            getInstance().getSkillManager().generateDefaults(client);
            getInstance().getSqlRepoManager().getClassBuildRepo().createClientBuild(client);
            getInstance().getSqlRepoManager().getClientRepo().createClient(client);
            getInstance().getSqlRepoManager().getQuestRepo().createProfile(client);
            getInstance().getLandManager().trackClient(client, getInstance().getLandManager().getOwningClan(player.getLocation().getChunk()));
            return;
        }
        handlePlayerJoin(player);
    }

    public void handlePlayerJoin(Player player) {
        Client client = getInstance().getClientManager().getClientAlsoNew(player);
        getInstance().scoreboardManager().setScoreboard(client);
        getInstance().getLandManager().trackClient(client, getInstance().getLandManager().getOwningClan(player.getLocation().getChunk()));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(getInstance().getChat().leaveMessage(player));
    }

    @EventHandler
    public void onMoveChunk(PlayerMoveEvent event) {
        LandManager landManager = getInstance().getLandManager();
        Player player = event.getPlayer();
        Client client = getInstance().getClientManager().getClient(player);
        if (event.getFrom().getChunk() != event.getTo().getChunk()) {
            landManager.trackClient(client, landManager.getOwningClan(event.getTo().getChunk()));
        } else {
            if (!landManager.isTracked(client)) {
                landManager.trackClient(client, landManager.getOwningClan(event.getTo().getChunk()));
                return;
            }
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        event.setDroppedExp(0);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Client client = getInstance().getClientManager().getClient(player);
        if (getInstance().getLandManager().isInSafezone(player)) {
            if (client.hasCombatTag()) return;
            event.setCancelled(true);
        }
        if(client.hasTag("Invulnerability")){
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onFall(EntityDamageEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof Player){
            Player player = (Player) entity;
            Client client = getInstance().getClientManager().getClient(player);
            if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
                if(client.hasTag("NoFall")) {
                    event.setCancelled(true);
                    client.removeTag("NoFall");
                }
                if(client.hasTag("EnderpearlTag")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        event.setRespawnLocation(new Location(event.getPlayer().getWorld(), 0, 14, 0, -180, 0));
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event){
        Entity shot = event.getDamager();
        if(!(shot instanceof Arrow)) return;
        Arrow arrow = (Arrow) shot;
        ProjectileSource source = arrow.getShooter();
        if(!(source instanceof Player)) return;
        Player shooter = (Player) source;
        Entity hit = event.getEntity();
        if(!(hit instanceof Player)) return;
        Player hitPlayer = (Player) hit;
        event.setCancelled(true);
        arrow.remove();
        getInstance().getServer().getPluginManager().callEvent(new ArrowDamageEvent(shooter, arrow, hitPlayer, event.getDamage()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPvP(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            if (event.getDamage() > 2) event.setDamage(2);
            return;
        }
        if (event.getDamager() instanceof Player &&
                event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Client damagerClient = getInstance().getClientManager().getClient(damager);
            if(damagerClient.isChanneling()){
                event.setCancelled(true);
                getInstance().getChat().sendClans(damager, "You cannot attack whilst channeling an ability");
                return;
            }
            Player target = (Player) event.getEntity();
            Client targetClient = getInstance().getClientManager().getClient(target);
            if(targetClient.hasTag("Invulnerability")){
                event.setCancelled(true);
                getInstance().getChat().sendClans(damager, getInstance().getChat().getClanRelation(damagerClient, targetClient) + target.getName() + "" +
                        getInstance().getChat().textColour + " is being " + getInstance().getChat().highlightText + "Bewitched");
                return;
            }
            if (getInstance().getLandManager().isInSafezone(target)) {
                if (targetClient.hasCombatTag()) return;
                event.setCancelled(true);
                getInstance().getChat().sendNoHarm(damager, target);
                return;
            }
            if (damagerClient.isFriendly(targetClient)) {
                event.setCancelled(true);
                getInstance().getChat().sendClans(damager, "You cannot harm " +
                        getInstance().getChat().getClanRelation(damagerClient, targetClient) + target.getName() + "");
                return;
            }
            event.setCancelled(true);
//            if(getInstance().getCooldownManager().isCooling(target, CustomPvPListener.TAKE_DAMAGE_PREFIX)) return;
            event.setDamage(getInstance().getUtilPvP().handleSwordDamage(damager, target));
            getInstance().getServer().getPluginManager().callEvent(
                    new PhysicalDamageEvent(damager, target, event.getDamage(),
                            getInstance().getUtilPvP().getPhysicalDamageCause(damager)));
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

}
