package me.marco.Items.Legendary;

import me.marco.Base.Core;
import me.marco.Events.CListener;
import me.marco.Skills.Data.Skill;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public abstract class CustomItem extends CListener<Core> {

    private String name;
    private Material material;
    private int maxAmount;
    private List<String> lore;
    private LegendaryItemSkill ability;

    public CustomItem(Core instance, String name, Material material, int maxAmount, List<String> lore, LegendaryItemSkill ability) {
        super(instance);
        this.name = name;
        this.material = material;
        this.maxAmount = maxAmount;
        this.lore = lore;
        this.ability = ability;
    }

    public ItemStack createItem(){
        ItemStack itemstack = new ItemStack(this.getMaterial());
        ItemMeta im = itemstack.getItemMeta();
        im.setMaxStackSize(this.getMaxAmount());
        if(getMaterial() == Material.POTION){
            PotionMeta pmeta = (PotionMeta) im;
            if(pmeta != null) pmeta.setBasePotionType(PotionType.WATER);
        }
        im.setDisplayName(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + getName());
        im.setLore(this.getLore());
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemstack.setItemMeta(im);
        return itemstack;
    }

    public void dropLegendaryItem(Location location){
        Item dropped = location.getWorld().dropItem(location, createItem());
        dropped.setVelocity(new Vector(0, 0.1, 0)); // Initial upward push (optional)
        dropped.setGravity(false); // Disable default falling
        dropped.setPickupDelay(300);
        dropped.setCustomNameVisible(true);
        dropped.setCustomName(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + getName());
        new BukkitRunnable(){
            int count = 20;
            public void run(){
                if(count <= 0){
                    this.cancel();
                }
                count--;
                dropped.getWorld().playSound(dropped.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 1);
            }
        }.runTaskTimer(getInstance(), 0, 5);
        new BukkitRunnable() {
            int count = 100;
            public void run() {
                if (dropped.isDead() || !dropped.isValid() || isItemGrounded(dropped) || count <= 0) {
                    dropped.setPickupDelay(40);
                    spawnFirework(dropped.getLocation());
                    this.cancel();
                    return;
                }
                count--;
                dropped.setFireTicks(0);
                Location loc = dropped.getLocation();
                Vector vel = dropped.getVelocity();
                // Simulate slow falling by gently pulling down
                dropped.setVelocity(new Vector(vel.getX(), -0.05, vel.getZ()));
            }
        }.runTaskTimer(getInstance(), 0, 1);
    }

    public void spawnFirework(Location location){
        Firework firework = location.getWorld().spawn(location, Firework.class);

        FireworkMeta meta = firework.getFireworkMeta();

        // Create the firework effect
        FireworkEffect effect = FireworkEffect.builder()
                .with(FireworkEffect.Type.BALL) // Type of firework (BALL, STAR, BURST, CREEPER)
                .withColor(Color.NAVY, Color.TEAL) // Colors of the firework
                .withFade(Color.FUCHSIA) // Fade color after initial explosion
                .flicker(true) // Make it flicker
                .trail(true) // Add a trail effect
                .build();

        // Apply the effect to the firework
        meta.addEffect(effect);
        meta.setPower(0); // Set power (height of the firework)

        // Set the firework meta and launch the firework
        firework.setFireworkMeta(meta);
    }

    public boolean isItemGrounded(Item item) {
        Vector velocity = item.getVelocity();
        Location loc = item.getLocation();
        Block below = loc.clone().subtract(0, 0.1, 0).getBlock();

        boolean lowVelocity = Math.abs(velocity.getY()) < 0.01;
        boolean isOnSolid = below.getType().isSolid();

        return lowVelocity && isOnSolid;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public List<String> getLore() {
        return lore;
    }

    public LegendaryItemSkill getAbility() {
        return ability;
    }
}
