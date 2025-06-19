package me.marco.Cosmetics.GUI;

import me.marco.Base.Core;
import me.marco.Cosmetics.GUI.MainMenu.CosmeticMainMenu;
import me.marco.Cosmetics.GUI.MainMenu.CosmeticPetsMenu;
import me.marco.Cosmetics.GUI.MainMenu.CosmeticTrailsMenu;
import me.marco.GUI.Menu;
import org.bukkit.entity.Player;

public class CosmeticMenuManager {

    private Core instance;

    private CosmeticMainMenu cosmeticMainMenu;
    private CosmeticPetsMenu cosmeticPetsMenu;
    private CosmeticTrailsMenu cosmeticTrailsMenu;

    public CosmeticMenuManager(Core instance){
        this.instance = instance;
        this.cosmeticMainMenu = new CosmeticMainMenu(getInstance());
        this.cosmeticPetsMenu = new CosmeticPetsMenu(getInstance());
        this.cosmeticTrailsMenu = new CosmeticTrailsMenu(getInstance());
        getInstance().getMenuManager().addMenu(this.cosmeticMainMenu);
        getInstance().getMenuManager().addMenu(this.cosmeticPetsMenu);
        getInstance().getMenuManager().addMenu(this.cosmeticTrailsMenu);
    }

    public void openCosmeticMenu(Player player){
        this.cosmeticMainMenu.openMenu(player);
    }

    public Core getInstance() {
        return instance;
    }

    public CosmeticMainMenu getCosmeticMainMenu() {
        return cosmeticMainMenu;
    }

    public CosmeticPetsMenu getCosmeticPetsMenu() {
        return this.cosmeticPetsMenu;
    }

    public CosmeticTrailsMenu getCosmeticTrailsMenu() {
        return this.cosmeticTrailsMenu;
    }
}
