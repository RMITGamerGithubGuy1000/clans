package me.marco.Economy;

import com.google.gson.*;
import me.marco.Base.Core;
import me.marco.Economy.Menu.ShopMenu;
import me.marco.GUI.Button;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.json.simple.JSONArray;

import java.io.*;
import java.util.*;

public class ShopManager {

    private Core instance;

    public ShopManager(Core instance){
        this.instance = instance;
    }

    public Core getInstance() {
        return instance;
    }

    private final String shopFileName = "./shops.json";

    private List<Shop> shopList = new ArrayList<Shop>();

    public void initialiseFile(){
        if(new File(getInstance().getDataFolder(), shopFileName).isFile()){
            loadFile();
            return;
        }
        File newFile = new File(getInstance().getDataFolder(), shopFileName);
        World world = getInstance().getServer().getWorld("world");
        Location location = new Location(world, 58.5, 9, 144.5);
        ShopMenu shopMenu = new ShopMenu(getInstance(), "Mohammed",
                Arrays.asList(new ShopButton(getInstance(), 1, "Gold Block",
                        1000, 500, Material.GOLD_BLOCK)));
        Shop shop = new Shop("Mohammed", location, shopMenu);
        try{
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(shop.toJSON());
            FileWriter fw = new FileWriter(newFile);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement jsonElement = JsonParser.parseString(jsonArray.toString());
            fw.write(gson.toJson(jsonElement));
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFile(){
        File file = new File(getInstance().getDataFolder(), shopFileName);
        try{
            FileReader fileReader = new FileReader(file);
            Object shopObjects = JsonParser.parseReader(fileReader);
            JsonArray shopArray = (JsonArray) shopObjects;
            shopArray.forEach(shop -> {
                JsonObject jsonData = shop.getAsJsonObject();
                Shop shopObject = fromJSON(jsonData);
                this.shopList.add(shopObject);
                getInstance().getMenuManager().addMenu(shopObject.getShopMenu());
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public Shop fromJSON(JsonObject jsonObject){
        //String name, Location shopLoc, ShopMenu shopMenu
        Gson gson = new Gson();
        String name = jsonObject.get("name").getAsString();
        JsonObject locationObject = jsonObject.get("location").getAsJsonObject();
        double x = locationObject.get("x").getAsDouble();
        double y = locationObject.get("y").getAsDouble();
        double z = locationObject.get("z").getAsDouble();
        World world = getInstance().getServer().getWorld("world");
        JsonObject shopObject = jsonObject.get("shopData").getAsJsonObject();
        Location location = new Location(world, x, y, z);
        System.out.println(location.toString() + " location");
        Shop shop = new Shop(name, location, menuFromJSON(shopObject));
        getInstance().getCustomEntityManager().addCustomEntity(shop);
        return shop;
    }

    public ShopMenu menuFromJSON(JsonObject shopDataObject){
        //Core core, String name, List<Button> buttonList
        JsonArray buttonData = shopDataObject.getAsJsonArray("buttons");
        String name = shopDataObject.get("name").getAsString();
        ShopMenu shopMenu = new ShopMenu(getInstance(), name, shopButtonListFromJSON(buttonData));
        return shopMenu;
    }

    public List<Button> shopButtonListFromJSON(JsonArray buttonData){
        //Core core, int slot, double buyPrice, double sellPrice, Material material
        List<Button> buttonList = new ArrayList<Button>();
        buttonData.forEach(button -> {
            buttonList.add(buttonFromJSON(button.getAsJsonObject()));
        });
        return buttonList;
    }

    public ShopButton buttonFromJSON(JsonObject buttonJson){
        JsonArray lore = buttonJson.getAsJsonArray("description");
        double buyPrice = Integer.MAX_VALUE;
        double sellPrice = 1;
        for(JsonElement element : lore){
            String data = element.getAsString();
            if(data.contains("Buy Price:")){
                buyPrice = Double.valueOf(data.split("\\$")[1]);
            }else if(data.contains("Sell Price:")){
                sellPrice = Double.valueOf(data.split("\\$")[1]);
            }
        }
        return new ShopButton(getInstance(), buttonJson.get("slot").getAsInt(), buttonJson.get("name").getAsString(),
                buyPrice, sellPrice, Material.valueOf(buttonJson.get("material").getAsString()));
    }

    public Shop getShop(String name){
        return this.shopList.stream().filter(shop -> ChatColor.stripColor(shop.getName()).equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
