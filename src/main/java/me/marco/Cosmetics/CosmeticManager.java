package me.marco.Cosmetics;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Cosmetics.ArrowTrails.*;
import me.marco.Cosmetics.GUI.MainMenu.CosmeticMainMenu;
import me.marco.Cosmetics.Objects.Cosmetic;
import me.marco.Cosmetics.Objects.CosmeticTypes.ArrowTrail;
import me.marco.Cosmetics.Objects.CosmeticTypes.KillStreak;
import me.marco.Cosmetics.Objects.CosmeticTypes.Pet;
import me.marco.Cosmetics.Objects.CosmeticTypes.TeleportEffect;
import me.marco.Cosmetics.Pets.*;
import me.marco.Cosmetics.Pets.Axolotls.*;
import me.marco.Cosmetics.Pets.Cats.BlackCat;
import me.marco.Cosmetics.Pets.Cats.RedCat;
import me.marco.Cosmetics.Pets.Cats.WhiteCat;
import me.marco.Cosmetics.Pets.Sheep.CyanSheep;
import me.marco.Cosmetics.Pets.Sheep.RedSheep;
import me.marco.Cosmetics.TeleportEffects.SplashTeleport;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CosmeticManager {

    private Core instance;
    private CosmeticMainMenu cosmeticMainMenu;

    public CosmeticManager(Core instance){
        this.instance = instance;
    }

    private List<ArrowTrail> arrowTrailList = new ArrayList<>();
    private List<KillStreak> killStreakList = new ArrayList<>();
    private List<Pet> petList = new ArrayList<>();
    private List<TeleportEffect> teleportEffectList = new ArrayList<>();

    public List<Cosmetic> allCosmeticsList = new ArrayList<>();

    public void initialiseCosmetics(){
        initialiseArrowTrails();
//        initialiseKillStreaks();
        initialisePets();
        allCosmeticsList.addAll(arrowTrailList);
        allCosmeticsList.addAll(petList);
//        initialiseTeleportEffects();
    }

    public List<Cosmetic> getAllCosmeticsList() {
        return allCosmeticsList;
    }

    public List<Pet> getPetCostmetics(){
        return this.petList;
    }

    public List<KillStreak> getKillStreakList(){
        return this.killStreakList;
    }

    public List<TeleportEffect> getTeleportEffectList(){
        return this.teleportEffectList;
    }

    public List<ArrowTrail> getArrowTrailList(){
        return this.arrowTrailList;
    }

//    public void initialiseMenus(){
//        List<Button> buttonList = new ArrayList<Button>();
//        buttonList.add();
//        this.cosmeticMainMenu = new CosmeticMainMenu(getInstance(), ChatColor.RED + ChatColor.BOLD.toString() + "Cosmetics", 27, );
//    }
//
//    private ItemStack getHead(Client client){
//        HashMap<UUID, ItemStack> skullMap = getInstance().getUtilItem().getSkullMap();
//        if(skullMap.containsKey(client.getUUID())) return skullMap.get(client.getUUID());
//        return getInstance().getUtilItem().headItem(client);
//    }

    public void initialiseArrowTrails(){
        this.arrowTrailList.add(new FlameTrail("Flame Trail"));
        this.arrowTrailList.add(new SoulTrail("Soul Trail"));
        this.arrowTrailList.add(new SparkTrail("Spark Trail"));
        this.arrowTrailList.add(new LoveTrail("Love Trail"));
        this.arrowTrailList.add(new SakuraTrail("Sakura Trail"));
    }

    public void initialiseKillStreaks(){

    }

    public void initialisePets(){
        this.petList.add(new CyanSheep("Cyan Baby Sheep"));
        this.petList.add(new RedSheep("Red Baby Sheep"));
        this.petList.add(new BlueAxolotl("Blue Axolotl"));
        this.petList.add(new BrownAxolotl("Brown Axolotl"));
        this.petList.add(new CyanAxolotl("Cyan Axolotl"));
        this.petList.add(new PinkAxolotl("Pink Axolotl"));
        this.petList.add(new GoldAxolotl("Gold Axolotl"));
        this.petList.add(new ChickenPet("Chicken"));
        this.petList.add(new BlackCat("Black Cat"));
        this.petList.add(new RedCat("Red Cat"));
        this.petList.add(new WhiteCat("White Cat"));

    }

    public void initialiseTeleportEffects(){
        this.teleportEffectList.add(new SplashTeleport("Splash"));
    }

    public void activateCosmetic(Player player, Cosmetic cosmetic) {
        Client client = getInstance().getClientManager().getClient(player);
        CosmeticProfile cosmeticProfile = client.getCosmeticProfile();
        cosmeticProfile.equipCosmetic(player, cosmetic);
    }

    public void deActivateCosmetic(Player player, Cosmetic cosmetic) {
        Client client = getInstance().getClientManager().getClient(player);
        CosmeticProfile cosmeticProfile = client.getCosmeticProfile();
        cosmeticProfile.deactivateCosmetic(player, cosmetic);
    }

    public Core getInstance() {
        return instance;
    }
}
