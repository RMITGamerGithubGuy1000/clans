package me.marco.Base;

import me.marco.Admin.Commands.AdminCommandManager;
import me.marco.Clans.Commands.ClanCommandManager;
import me.marco.Client.Client;
import me.marco.Cosmetics.CosmeticManager;
import me.marco.Cosmetics.CosmeticsCommand;
import me.marco.Cosmetics.CosmeticsListener;
import me.marco.Cosmetics.GUI.CosmeticMenuManager;
import me.marco.CustomEntities.CustomEntityManager;
import me.marco.Economy.ShopListener;
import me.marco.Economy.ShopManager;
import me.marco.Events.*;
import me.marco.Events.PotionEffects.PotionEffectListener;
import me.marco.Fields.FieldsManager;
import me.marco.GUI.GUIListener;
import me.marco.GUI.MenuManager;
import me.marco.Handlers.*;
import me.marco.Items.Legendary.LegendaryItemListener;
import me.marco.Items.Legendary.LegendaryItemManager;
import me.marco.Kits.KitCommandManager;
import me.marco.Kits.KitManager;
import me.marco.Events.PvPItem.PvPItemListener;
import me.marco.PvPItems.PvPItemManager;
import me.marco.Quests.QuestListener;
import me.marco.Quests.QuestManager;
import me.marco.SQL.SQLRepoManager;
import me.marco.SQL.SQLSlave;
import me.marco.Tags.TagManager;
import me.marco.Utility.Chat;
import me.marco.Utility.Cooldowns.CooldownManager;
import me.marco.Utility.Mana.ManaManager;
import me.marco.Utility.UtilInvisibility;
import me.marco.Utility.UtilItem;
import me.marco.Utility.UtilPvP;
import me.marco.WorldEvent.Bosses.WorldBossManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.io.File;

public class Core extends JavaPlugin {

    private Chat chat = new Chat(this);

    private AdminManager adminManager = new AdminManager(this);
    private InviteHandler inviteHandler = new InviteHandler(this);
    private SQLSlave sqlSlave = new SQLSlave();
    private SQLRepoManager sqlRepoManager = new SQLRepoManager(this);
    private ClanManager clanManager = new ClanManager(this);
    private ClientManager clientManager = new ClientManager(this);
    private LandManager landManager = new LandManager(this);
    private AllianceManager allianceManager = new AllianceManager(this);
    private EnemyManager enemyManager = new EnemyManager(this);
    private PillageManager pillageManager = new PillageManager(this);

    private EventManager eventManager = new EventManager(this);

    private CooldownManager cooldownManager = new CooldownManager(this);

    private SkillManager skillManager = new SkillManager(this);

    private ItemManager itemManager = new ItemManager(this);

    private ScoreboardManager scoreboardManager = new ScoreboardManager(this);

    private EconomyManager economyManager = new EconomyManager(this);

    private ManaManager manaManager = new ManaManager(this);

    private TagManager tagManager = new TagManager(this);

    private KitManager kitManager = new KitManager(this);

    private UtilItem utilItem = new UtilItem(this);

    private UtilInvisibility utilInvisibility = new UtilInvisibility(this);

    private UtilPvP utilPvP = new UtilPvP(this);

    private ShopManager shopManager = new ShopManager(this);

    private MenuManager menuManager = new MenuManager(this);

    private PvPItemManager pvpItemManager = new PvPItemManager(this);

    private QuestManager questManager = new QuestManager(this);

    private CustomEntityManager customEntityManager = new CustomEntityManager(this);

    private BlockChangeManager blockChangeManager = new BlockChangeManager(this);

    private FieldsManager fieldsManager = new FieldsManager(this);

    private CosmeticManager cosmeticManager = new CosmeticManager(this);
    private CosmeticMenuManager cosmeticMenuManager = new CosmeticMenuManager(this);

    private WorldBossManager worldBossManager = new WorldBossManager(this);
    private LegendaryItemManager legendaryItemManager = new LegendaryItemManager(this);

//    private CustomPVPMobManager customPVPMobManager = new CustomPVPMobManager(this);

    public void onEnable(){
        for(Entity entity : getServer().getWorld("world").getEntities()){
            if(entity instanceof Arrow) entity.remove();
            if(!(entity instanceof LivingEntity)) continue;
            if(entity instanceof Player) continue;
            entity.remove();
        }
        File configFile = new File(this.getDataFolder(), "config.yml");
        if(!configFile.exists()){
            FileConfiguration config = this.getConfig();
            config.addDefault("sql.username", "<enterusename>");
            config.addDefault("sql.password", "<enterpassword>");
            config.addDefault("sql.IP", "<enterIP>");
            config.addDefault("sql.port", "<enterport>");
            config.addDefault("sql.databasename", "<enterdatabasename>");
            config.options().copyDefaults(true);
            saveConfig();
        }else{
            sqlSlave.setUsername(this.getConfig().getString("sql.username"));
            sqlSlave.setPassword(this.getConfig().getString("sql.password"));
            sqlSlave.setIP(this.getConfig().getString("sql.IP"));
            sqlSlave.setPort(this.getConfig().getString("sql.port"));
            sqlSlave.setDatabaseName(this.getConfig().getString("sql.databasename"));
        }
        shopManager.initialiseFile();
        getServer().getWorld("world").setStorm(false);
        getServer().getWorld("world").setWeatherDuration(0);
        skillManager.registerSkills();
        questManager.initialiseQuests();
        sqlSlave.connect();
        sqlRepoManager.initialise();
        sqlRepoManager.loadRepos();
        sqlSlave.runQueries(this);
        eventManager.runTimers();
//        skillManager.checkClasses();
//        itemManager.handleItems();
        scoreboardManager.loadScoreboards();
        pvpItemManager.initialiseItems();

        new ClanListener(this);
        new NormalListener(this);
        new SkillListener(this);
        new PvPItemListener(this);
        new CustomPvPListener(this);
        new ShopListener(this);
        new PotionEffectListener(this);
        new QuestListener(this);
        new GUIListener(this);
        new AdminListener(this);
        new MobListener(this);
        new WorldEventListener(this);
        new LegendaryItemListener(this);
        new CosmeticsListener(this);
//        new CustomPVPMobListener(this);

        getCommand("clan").setExecutor(new ClanCommandManager(this));
        getCommand("admin").setExecutor(new AdminCommandManager(this));
        getCommand("kit").setExecutor(new KitCommandManager(this));
        getCommand("cosmetics").setExecutor(new CosmeticsCommand(this));

        for(Player p : getServer().getOnlinePlayers()){
            Client client = clientManager.getClient(p);
            p.closeInventory();
            for(PotionEffect potionEffect : p.getActivePotionEffects()){
                p.removePotionEffect(potionEffect.getType());
            }
        }

        getKitManager().initialise();
        getUtilInvisibility().revealOnlinePlayers();
        getCosmeticManager().initialiseCosmetics();
        getLegendaryItemManager().registerCustomItems();
    }

    public LegendaryItemManager getLegendaryItemManager() {
        return legendaryItemManager;
    }

    public WorldBossManager getWorldBossManager() {
        return worldBossManager;
    }

    public FieldsManager getFieldsManager() {
        return this.fieldsManager;
    }

    public Chat getChat() { return this.chat; }

    public SQLSlave getSQL() { return this.sqlSlave; }

    public AdminManager getAdminManager(){ return this.adminManager; }

    public InviteHandler getInviteHandler() {
        return inviteHandler;
    }

    public SQLRepoManager getSqlRepoManager() { return sqlRepoManager; }

    public ClanManager getClanManager() { return this.clanManager; }

    public ClientManager getClientManager() { return this.clientManager; }

    public LandManager getLandManager() { return this.landManager; }

    public AllianceManager getAllianceManager() { return this.allianceManager; }

    public EnemyManager getEnemyManager() { return this.enemyManager; }

    public PillageManager getPillageManager() { return this.pillageManager; }

    public CooldownManager getCooldownManager() { return this.cooldownManager; }

    public SkillManager getSkillManager() { return this.skillManager; }

    public ItemManager getItemManager() { return this.itemManager; }

    public ScoreboardManager scoreboardManager(){ return this.scoreboardManager; }

    public EconomyManager getEconomyManager(){ return this.economyManager; }

    public ManaManager getManaManager() {
        return manaManager;
    }

    public TagManager getTagManager() {
        return tagManager;
    }

    public BlockChangeManager getBlockChangeManager() {
        return blockChangeManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public UtilItem getUtilItem() {
        return utilItem;
    }

    public UtilInvisibility getUtilInvisibility() {
        return utilInvisibility;
    }

    public UtilPvP getUtilPvP() {
        return utilPvP;
    }

    public ShopManager getShopManager(){
        return this.shopManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public PvPItemManager getPvPItemManager() {
        return pvpItemManager;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public CustomEntityManager getCustomEntityManager() {
        return customEntityManager;
    }

    public CosmeticManager getCosmeticManager() {
        return cosmeticManager;
    }

    public CosmeticMenuManager getCosmeticMenuManager() {
        return cosmeticMenuManager;
    }

    //    public CustomPVPMobManager getCustomPVPMobManager(){
//        return this.customPVPMobManager;
//    }

}
