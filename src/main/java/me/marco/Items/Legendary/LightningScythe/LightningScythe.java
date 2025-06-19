package me.marco.Items.Legendary.LightningScythe;

import me.marco.Base.Core;
import me.marco.Items.Legendary.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;

public class LightningScythe extends CustomItem {
    public LightningScythe(Core instance) {
        super(instance,
                "Lightning Scythe",
                Material.DIAMOND_HOE,
                1,
                Arrays.asList(
                        ChatColor.YELLOW + ChatColor.ITALIC.toString() +
                                "Forged in storm-tossed peaks, ",
                        ChatColor.YELLOW + ChatColor.ITALIC.toString() +
                                "the scythe was shaped by lightning’s wrath. ",
                        ChatColor.YELLOW + ChatColor.ITALIC.toString() +
                                "Crafted by those who whisper to the skies, ",
                        ChatColor.YELLOW + ChatColor.ITALIC.toString() +
                                "it hums with ancient power, ",
                        ChatColor.YELLOW + ChatColor.ITALIC.toString() +
                                "its edge crackling with the storm’s fury. "
                ),
                new LightningScytheSkill(instance));
    }

}
