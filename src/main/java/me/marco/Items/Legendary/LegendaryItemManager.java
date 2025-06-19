package me.marco.Items.Legendary;

import me.marco.Base.Core;
import me.marco.Items.Legendary.LightningScythe.LightningScythe;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Optional;

public class LegendaryItemManager {

    private Core instance;

    public LegendaryItemManager(Core instance){
        this.instance = instance;
    }

    private ArrayList<CustomItem> customItemList = new ArrayList<>();

    public void registerCustomItems(){
        this.customItemList.add(new LightningScythe(getInstance()));
    }

    public void test(Player player){
        this.customItemList.get(0).getAbility().activate(player);
    }

    public ArrayList<CustomItem> getCustomItemList(){
        return this.customItemList;
    }

    public Core getInstance() {
        return instance;
    }

    public CustomItem findByName(String name) {
        Optional<CustomItem> item = customItemList.stream()
                .filter(customItem -> customItem.getName().equals(name)) // Filter by name
                .findFirst();
        return item.orElse(null);
    }
}
