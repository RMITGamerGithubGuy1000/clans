package me.marco.Cosmetics.Pets.Axolotls;

import me.marco.Cosmetics.Objects.CosmeticTypes.Pet;
import me.marco.Cosmetics.Pets.NMS.NMSAxolotl;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Player;

public class PinkAxolotl extends Pet {

    public PinkAxolotl(String cosmeticTag) {
        super(cosmeticTag, Material.BRAIN_CORAL);
    }

    @Override
    public void spawnPet(Player player) {
        NMSAxolotl nmsAxolotl = new NMSAxolotl(player, player.getLocation());
        Axolotl axolotl = (Axolotl) nmsAxolotl.getBukkitEntity();
        axolotl.setBaby();
        axolotl.setAgeLock(true);
        axolotl.setCustomNameVisible(true);
        axolotl.setVariant(Axolotl.Variant.LUCY);
        String pName = player.getName();
        String possession = pName.endsWith("s") ? "'" : "'s";
        axolotl.setCustomName(ChatColor.GOLD + pName + possession + "" + ChatColor.DARK_AQUA + " Axolotl");
        setActivated(axolotl);
    }
}
