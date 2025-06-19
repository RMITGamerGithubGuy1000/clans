package me.marco.GUI;

import me.marco.Base.Core;
import me.marco.Quests.GUI.DynamicMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MenuManager {

    private Core instance;

    public MenuManager(Core instance) {
        this.instance = instance;
    }

    private List<Menu> menuList = new ArrayList<Menu>();

    public void addMenu(Menu menu) {
        this.menuList.add(menu);
    }

    public boolean menuExists(String name) {
        return this.menuList.stream().filter(menu -> menu.getName().equalsIgnoreCase(name)).findFirst().orElse(null) != null;
    }

    public Menu getMenu(String name) {
        return this.menuList.stream().filter(menu -> ChatColor.stripColor(menu.getName()).equalsIgnoreCase(ChatColor.stripColor(name))).findFirst().orElse(null);
    }

    public Core getInstance() {
        return instance;
    }

}
