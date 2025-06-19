package me.marco.Cosmetics.GUI;

import me.marco.Base.Core;
import me.marco.Cosmetics.Objects.Cosmetic;
import me.marco.GUI.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CosmeticButton extends Button {

    private Cosmetic cosmetic;

    public CosmeticButton(Cosmetic cosmetic, Core core, int slot, String name, List<String> description, Material material) {
        super(core, slot, name, description, material);
        this.cosmetic = cosmetic;
    }

    @Override
    public void onClick(Player player, ItemStack clickedItem, ClickType clickType) {
        if(clickType.isLeftClick()){
            getInstance().getCosmeticManager().activateCosmetic(player, this.getCosmetic());
            return;
        }
        if(clickType.isRightClick()){
            getInstance().getCosmeticManager().deActivateCosmetic(player, this.getCosmetic());
            return;
        }
    }

    public Cosmetic getCosmetic() {
        return cosmetic;
    }
}
