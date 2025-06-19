package me.marco.Cosmetics.Pets.OneOfs;

import me.marco.Cosmetics.Objects.CosmeticTypes.Pet;
import me.marco.Cosmetics.Pets.NMS.NMSChicken;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;

public class ChickenPet extends Pet {

    public ChickenPet(String cosmeticTag) {
        super(cosmeticTag, Material.EGG);
    }

    @Override
    public void spawnPet(Player player) {
        NMSChicken nmsChicken = new NMSChicken(player, player.getLocation());
        Chicken chicken = (Chicken) nmsChicken.getBukkitEntity();
        chicken.setBaby();
        chicken.setCustomNameVisible(true);
        chicken.setAgeLock(true);
        String pName = player.getName();
        String possession = pName.endsWith("s") ? "'" : "'s";
        chicken.setCustomName(ChatColor.GOLD + pName + possession + "" + ChatColor.DARK_AQUA + " Chicken");
        setActivated(chicken);
    }
}
