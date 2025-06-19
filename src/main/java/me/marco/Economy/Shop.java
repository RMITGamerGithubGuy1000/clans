package me.marco.Economy;

import me.marco.CustomEntities.CustomEntity;
import me.marco.CustomEntities.NMSShopkeeper;
import me.marco.Economy.Menu.ShopMenu;
import me.marco.GUI.Menu;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class Shop extends CustomEntity {

    private String name;
    private ShopMenu shopMenu;
    private Location shopLoc;

    public Shop(String name, Location shopLoc, ShopMenu shopMenu){
        super(name, shopLoc);
        this.name = name;
        this.shopMenu = shopMenu;
        this.shopLoc = shopLoc;
    }

    public void openShop(Player player) {
        this.shopMenu.openShopMenu(player);
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.name);
        JSONObject locationJSON = new JSONObject();
        locationJSON.put("x", this.shopLoc.getX());
        locationJSON.put("y", this.shopLoc.getY());
        locationJSON.put("z", this.shopLoc.getZ());
        jsonObject.put("location", locationJSON);
        jsonObject.put("shopData", this.shopMenu.toJSON());
        return jsonObject;
    }

    public String getName() {
        return name;
    }

    public Menu getShopMenu() {
        return this.shopMenu;
    }

    public LivingEntity spawnMob(Location location) {
        return (LivingEntity) new NMSShopkeeper(location).getBukkitEntity();
    }

    public void postSpawnEffects() {

    }
}
