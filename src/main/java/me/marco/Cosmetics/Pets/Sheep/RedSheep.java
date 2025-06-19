package me.marco.Cosmetics.Pets.Sheep;

import me.marco.Cosmetics.Objects.CosmeticTypes.Pet;
import me.marco.Cosmetics.Pets.NMS.NMSSheep;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

public class RedSheep extends Pet {

    public RedSheep(String cosmeticTag) {
        super(cosmeticTag, Material.RED_DYE);
    }

    @Override
    public void spawnPet(Player player) {
        NMSSheep nmsSheep = new NMSSheep(player, player.getLocation());
        Sheep sheep = (Sheep) nmsSheep.getBukkitEntity();
        sheep.setColor(DyeColor.RED);
        sheep.setBaby();
        sheep.setAgeLock(true);
        sheep.setCustomNameVisible(true);
        String pName = player.getName();
        String possession = pName.endsWith("s") ? "'" : "'s";
        sheep.setCustomName(ChatColor.GOLD + pName + possession + "" + ChatColor.RED + " Sheep");
        setActivated(sheep);
    }
}
