package me.marco.Cosmetics.Pets.Cats;

import me.marco.Cosmetics.Objects.CosmeticTypes.Pet;
import me.marco.Cosmetics.Pets.NMS.NMSAxolotl;
import me.marco.Cosmetics.Pets.NMS.NMSCat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Player;

public class BlackCat extends Pet {

    public BlackCat(String cosmeticTag) {
        super(cosmeticTag, Material.BLACK_BUNDLE);
    }

    @Override
    public void spawnPet(Player player) {
        NMSCat nmsCat = new NMSCat(player, player.getLocation());
        Cat axolotl = (Cat) nmsCat.getBukkitEntity();
        axolotl.setBaby();
        axolotl.setAgeLock(true);
        axolotl.setCustomNameVisible(true);
        axolotl.setCatType(Cat.Type.BLACK);
        String pName = player.getName();
        String possession = pName.endsWith("s") ? "'" : "'s";
        axolotl.setCustomName(ChatColor.GOLD + pName + possession + "" + ChatColor.DARK_AQUA + " Cat");
        setActivated(axolotl);
    }
}
