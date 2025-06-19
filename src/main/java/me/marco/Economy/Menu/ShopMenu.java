package me.marco.Economy.Menu;

import me.marco.Base.Core;
import me.marco.GUI.Button;
import me.marco.GUI.Menu;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class ShopMenu extends Menu {

    public ShopMenu(Core core, String name, List<Button> buttonList) {
        super(core, name, 54, buttonList);
    }

    public void openShopMenu(Player player) {
        openMenu(player);
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", getName());
        JSONArray buttonArray = new JSONArray();
        this.getButtons().forEach(button -> buttonArray.add(button.toJSON()));
        jsonObject.put("buttons", buttonArray);
        return jsonObject;
    }
}
