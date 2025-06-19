package me.marco.PvPItems.Consumeables.HomingChrystal;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Tags.Tag;
import me.marco.Utility.UtilParticles;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HomingChrystalTag extends Tag {

    private Location home;
    private Player player;
    private Location initialLoc;
    double phi = 0;
    double phi2 = 0;
    private int count = 0;
    private boolean isDone = false;
    private int slot;

    public HomingChrystalTag(Client owner, Player player, int slot, Location home, Core instance) {
        super(owner, "HomingChrystalTag", 0, false, instance);
        this.player = player;
        this.home = home;
        this.initialLoc = player.getLocation();
        this.slot = slot;
    }

    public boolean checkExpiry(){
        if(isForceExpire()) return true;
        return !this.player.isOnline() || isDone;
    }

    @Override
    public void onTick() {
        if(player.getInventory().getHeldItemSlot() != this.slot){
            this.setForceExpired();
            return;
        }
        if(count % 5 == 0){
            spawnParticles();
//            spawnParticles2();
        }
        if(count % 10 == 0) playSound();
        count++;
        if(count == 200){
            isDone = true;
        }
    }

    public void teleportHome(){
        player.teleport(home.clone().add(0, .5, 0));
    }

    public void playSound(){
        UtilParticles.playBlockParticle(this.initialLoc, Material.AMETHYST_BLOCK, true);
    }

    public void spawnParticles(){
        Location loc = this.initialLoc.clone();
        phi = phi + Math.PI/8;
        double x, y, z;
        for (double t = 0; t <= 2*Math.PI; t = t + Math.PI/16){
            for (double i = 0; i <= 1; i = i + 1){
                x = 0.4*(2*Math.PI-t)*0.5*Math.cos(t + phi + i*Math.PI);
                y = 0.5*t;
                z = 0.4*(2*Math.PI-t)*0.5*Math.sin(t + phi + i*Math.PI);
                loc.add(x, y, z);
                UtilParticles.drawRedstoneParticle(loc, 210, 10, 240, .5f);
                loc.subtract(x,y,z);
            }
        }
    }

//    public void spawnParticles2(){
//        Location loc = this.initialLoc.clone();
//        phi2 = phi2 + Math.PI/8;
//        double x, y, z;
//        for (double t = 0; t <= 2*Math.PI; t = t + Math.PI/16){
//            for (double i = 0; i <= 1; i = i + 1){
//                x = 0.4*(2*Math.PI-t)*0.5*Math.sin(t + phi2 + i*Math.PI);
//                y = 0.5*t;
//                z = 0.4*(2*Math.PI-t)*0.5*Math.cos(t + phi2 + i*Math.PI);
//                loc.add(x, y, z);
//                UtilParticles.drawRedstoneParticle(loc, 210, 10, 240, .5f);
//                loc.subtract(x,y,z);
//            }
//        }
//    }

    @Override
    public void onExpiry() {
        if(isForceExpire()) return;
        Location locBefore = player.getLocation();
        locBefore.getWorld().playEffect(locBefore, Effect.ENDER_SIGNAL, 1);
        locBefore.getWorld().playSound(locBefore, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 3f);
        teleportHome();
        locBefore.getWorld().playEffect(locBefore, Effect.ENDER_SIGNAL, 1);
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 3f);
        consumeItem(player);
    }

    public void setForceExpired(){
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
        UtilParticles.playBlockParticle(player.getLocation(), Material.NETHER_PORTAL, true);
        getInstance().getChat().sendModule(player, "Your " + getInstance().getChat().highlightText + "Homing Chrystal" +
                getInstance().getChat().textColour + " was cancelled", "Homing Chrystal");
        this.setIsForceExpired(true);
    }

    public void consumeItem(Player player){
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        int amount = itemStack.getAmount();
        int newAmount = amount - 1;
        if(newAmount < 1){
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }else{
            itemStack.setAmount(newAmount);
        }
        player.updateInventory();
    }

}
