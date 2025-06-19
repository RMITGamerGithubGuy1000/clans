package me.marco.Cosmetics.GUI.MainMenu;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Cosmetics.CosmeticMenuButton;
import me.marco.Cosmetics.CosmeticProfile;
import me.marco.Cosmetics.GUI.CosmeticButton;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CosmeticPetsMenu extends DynamicMenu {

    public CosmeticPetsMenu(Core core) {
        super(core, ChatColor.GOLD + ChatColor.BOLD.toString() + "Pets", 54);
    }

    @Override
    public Inventory generateUniqueMenu(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
        Client client = getInstance().getClientManager().getClient(player);
        List<Button> uniqueButtons = new ArrayList<Button>();

        uniqueButtons.add(new CosmeticMenuButton(getInstance().getCosmeticMenuManager().getCosmeticMainMenu(), getInstance(),
                getInstance().getItemManager().createItemStack(Material.REDSTONE_BLOCK, ChatColor.RED + ChatColor.BOLD.toString() + "Back", false,
                        Arrays.asList(ChatColor.YELLOW + "Main Menu")),
                0));
        CosmeticProfile cosmeticProfile = getInstance().getClientManager().getClient(player).getCosmeticProfile();
        int index = 20;
        for(Pet pet : getInstance().getCosmeticManager().getPetCostmetics()){
            setButton(cosmeticProfile, pet, index - 1, uniqueButtons);
            index++;
            if(index % 9 == 0) index+=2;
        }
        return generateInventory(client, uniqueButtons);
    }

    public void setButton(CosmeticProfile cosmeticProfile, Cosmetic cosmetic, int slot, List<Button> uniqueButtons){
        String tag = cosmeticProfile.getPet() != null && cosmeticProfile.getPet().equals(cosmetic) ? ChatColor.GREEN + cosmetic.getCosmeticTag() :
                ChatColor.RED + cosmetic.getCosmeticTag();
        uniqueButtons.add(new CosmeticButton(cosmetic, this.getInstance(), slot, tag, Arrays.asList(""), cosmetic.getRepresentation()));
    }


}
