package me.marco.Cosmetics.LootBox;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Cosmetics.CosmeticProfile;
import me.marco.Cosmetics.Objects.Cosmetic;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LootboxManager {

    private Core instance;

    public LootboxManager(Core instance){
        this.instance = instance;
    }

    /**
     * >Place
     * >Animation
     * >Give
     *
     * Place: Listener
     * Animation: Here?
     * Give: SQL+Here
     */

    public void openBox(Player player){
        Client client = getInstance().getClientManager().getClient(player);
        CosmeticProfile cosmeticProfile = client.getCosmeticProfile();
        List<Cosmetic> remaining = getRemainingCosmetics(cosmeticProfile);

    }

    public List<Cosmetic> getRemainingCosmetics(CosmeticProfile cosmeticProfile){
        List<Cosmetic> allList = new ArrayList<>(getInstance().getCosmeticManager().getAllCosmeticsList());
        allList.removeAll(cosmeticProfile.getOwnedCosmetics());
        return allList;
    }

    public Core getInstance() {
        return instance;
    }
}
