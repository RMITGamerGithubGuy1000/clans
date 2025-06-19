package me.marco.Cosmetics;

import me.marco.Client.Client;
import me.marco.Cosmetics.Objects.Cosmetic;
import me.marco.Cosmetics.Objects.CosmeticTypes.ArrowTrail;
import me.marco.Cosmetics.Objects.CosmeticTypes.KillStreak;
import me.marco.Cosmetics.Objects.CosmeticTypes.Pet;
import me.marco.Cosmetics.Objects.CosmeticTypes.TeleportEffect;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CosmeticProfile {

    private Client owner;
    private Pet pet;
    private ArrowTrail arrowTrail;
    private KillStreak killStreak;
    private TeleportEffect teleportEffect;

    private List<ArrowTrail> arrowTrailList = new ArrayList<>();
    private List<KillStreak> killStreakList = new ArrayList<>();
    private List<Pet> petList = new ArrayList<>();
    private List<TeleportEffect> teleportEffectList = new ArrayList<>();

    public CosmeticProfile(Client owner){
        this.owner = owner;
    }

    public void equipCosmetic(Player player, Cosmetic cosmetic){
        if(cosmetic instanceof Pet){
            Pet newPet = (Pet) cosmetic;
//            if(!this.ownsPet(newPet)){
//                player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "You do not own " + cosmetic.getCosmeticTag());
//                return;
//            }
            if(this.pet != null) this.pet.deactivatePet();

            this.pet = newPet;
            newPet.spawnPet(player);
            playEquipped(player, cosmetic);
            return;
        }

        if(cosmetic instanceof ArrowTrail){
            ArrowTrail arrowTrail = (ArrowTrail) cosmetic;
//            if(!this.ownsArrowTrail(arrowTrail)){
//                player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "You do not own " + cosmetic.getCosmeticTag());
//                return;
//            }
            this.arrowTrail = null;
            this.arrowTrail = arrowTrail;
            playEquipped(player, cosmetic);
            return;
        }

        if(cosmetic instanceof TeleportEffect){
            TeleportEffect teleportEffect = (TeleportEffect) cosmetic;
            if(!this.ownsTeleportEffect(teleportEffect)){
                player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "You do not own " + cosmetic.getCosmeticTag());
                return;
            }
            this.teleportEffect = null;
            this.teleportEffect = teleportEffect;
            playEquipped(player, cosmetic);
            return;
        }

    }

    public void playEquipped(Player player, Cosmetic cosmetic){
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Equipped: " + cosmetic.getCosmeticTag());
    }

    public boolean ownsArrowTrail(ArrowTrail arrowTrail){
        return this.arrowTrailList.contains(arrowTrail);
    }

    public boolean ownsKillStreak(KillStreak killStreak){
        return this.killStreakList.contains(killStreak);
    }

    public boolean ownsPet(Pet pet){
        return this.petList.contains(pet);
    }

    public boolean ownsTeleportEffect(TeleportEffect teleportEffect){
        return this.teleportEffectList.contains(teleportEffect);
    }

    public void addOwnedArrowTrail(ArrowTrail arrowTrail){
        this.arrowTrailList.add(arrowTrail);
    }

    public void addOwnedKillStreak(KillStreak killStreak){
        this.killStreakList.add(killStreak);
        this.killStreakList.add(killStreak);
    }

    public void addOwnedPet(Pet pet){
        this.petList.add(pet);
    }

    public void addOwnedTeleportEffect(TeleportEffect teleportEffect){
        this.teleportEffectList.add(teleportEffect);
    }

    public void deactivateCosmetic(Player player, Cosmetic cosmetic) {
        if(cosmetic instanceof Pet){
            if(this.pet != cosmetic) return;
            this.pet.deactivatePet();
            this.pet = null;
            playDeactivate(player, cosmetic);
            return;
        }

        if(cosmetic instanceof ArrowTrail){
            if(this.arrowTrail != cosmetic) return;
            this.arrowTrail = null;
            playDeactivate(player, cosmetic);
            return;
        }

        if(cosmetic instanceof TeleportEffect){
            if(this.teleportEffect != cosmetic) return;
            this.teleportEffect = null;
            playDeactivate(player, cosmetic);
            return;
        }
    }

    public void playDeactivate(Player player, Cosmetic cosmetic){
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Deactivated: " + cosmetic.getCosmeticTag());
    }

    public Client getOwner() {
        return owner;
    }

    public Pet getPet() {
        return pet;
    }

    public ArrowTrail getArrowTrail() {
        return arrowTrail;
    }

    public KillStreak getKillStreak() {
        return killStreak;
    }

    public TeleportEffect getTeleportEffect() {
        return teleportEffect;
    }

    public List<ArrowTrail> getArrowTrailList() {
        return arrowTrailList;
    }

    public List<KillStreak> getKillStreakList() {
        return killStreakList;
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public List<TeleportEffect> getTeleportEffectList() {
        return teleportEffectList;
    }

    public List<Cosmetic> getOwnedCosmetics(){
        List<Cosmetic> cosmetics = new ArrayList<>();
        cosmetics.addAll(this.getPetList());
        cosmetics.addAll(this.getArrowTrailList());
        cosmetics.addAll(this.getKillStreakList());
        return cosmetics;
    }

}
