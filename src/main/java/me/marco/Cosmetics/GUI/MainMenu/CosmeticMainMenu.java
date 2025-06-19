package me.marco.Cosmetics.GUI.MainMenu;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Cosmetics.CosmeticMenuButton;
import me.marco.Cosmetics.CosmeticProfile;
import me.marco.Cosmetics.Objects.Cosmetic;
import me.marco.Cosmetics.Objects.CosmeticTypes.Pet;
import me.marco.GUI.Button;
import me.marco.GUI.Menu;
import me.marco.Quests.GUI.DynamicMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CosmeticMainMenu extends DynamicMenu {

    public CosmeticMainMenu(Core core) {
        super(core, "Cosmetics", 27);
        setInventory(generateInventory());
    }

    @Override
    public Inventory generateUniqueMenu(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
        Client client = getInstance().getClientManager().getClient(player);
        List<Button> uniqueButtons = new ArrayList<Button>();
        uniqueButtons.add(new CosmeticMenuButton(null, getInstance(), getHead(client), 4));

        uniqueButtons.add(new CosmeticMenuButton(getInstance().getCosmeticMenuManager().getCosmeticPetsMenu(), getInstance(),
                getInstance().getItemManager().createItemStack(Material.TURTLE_EGG, ChatColor.GREEN + ChatColor.BOLD.toString() + "Pets", false,
                        Arrays.asList(ChatColor.YELLOW + "Pets Menu")),
                22));

        uniqueButtons.add(new CosmeticMenuButton(getInstance().getCosmeticMenuManager().getCosmeticTrailsMenu(), getInstance(),
                getInstance().getItemManager().createItemStack(Material.SPECTRAL_ARROW, ChatColor.GREEN + ChatColor.BOLD.toString() + "Arrow Trails", false,
                        Arrays.asList(ChatColor.YELLOW + "Arrow Trail Menu")),
                20));

//        uniqueButtons.add(new CosmeticMenuButton(null, getInstance(),
//                getInstance().getItemManager().createItemStack(Material.SPECTRAL_ARROW, ChatColor.GREEN + ChatColor.BOLD.toString() + "Arrow Trails", false,
//                        Arrays.asList(ChatColor.YELLOW + "Arrow Trails Menu")),
//                21));

//        uniqueButtons.add(new CosmeticMenuButton(null, getInstance(),
//                getInstance().getItemManager().createItemStack(Material.SKELETON_SKULL, ChatColor.GREEN + ChatColor.BOLD.toString() + "Kill Streaks", false,
//                        Arrays.asList(ChatColor.YELLOW + "Kill Streaks Menu")),
//                23));

//        uniqueButtons.add(new CosmeticMenuButton(null, getInstance(),
//                getInstance().getItemManager().createItemStack(Material.ENDER_EYE, ChatColor.GREEN + ChatColor.BOLD.toString() + "Teleport Effects", false,
//                        Arrays.asList(ChatColor.YELLOW + "Teleport Effects Menu")),
//                25));


        return generateInventory(client, uniqueButtons);
    }

    public ItemStack getHead(Client client) {
        HashMap<UUID, ItemStack> skullMap = getInstance().getUtilItem().getSkullMap();
        if (skullMap.containsKey(client.getUUID())) return skullMap.get(client.getUUID());
        return getInstance().getUtilItem().headItem(client);
    }

}
