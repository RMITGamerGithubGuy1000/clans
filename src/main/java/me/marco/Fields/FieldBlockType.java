package me.marco.Fields;

import org.bukkit.Material;

public enum FieldBlockType {

    COAL_ORE(Material.COAL_ORE, 35, 8, 1, Material.COAL, 1),
    IRON_ORE(Material.IRON_ORE, 35, 5, 1, Material.IRON_INGOT, 1),
    DEEPSLATE_IRON_ORE(Material.DEEPSLATE_IRON_ORE, 35, 5, 1, Material.IRON_INGOT, 1),

    REDSTONE_ORE(Material.REDSTONE_ORE, 35, 6, 1, Material.REDSTONE, 1),
    DEEPSLATE_REDSTONE_ORE(Material.DEEPSLATE_REDSTONE_ORE, 35, 6, 1, Material.REDSTONE, 1),

    LAPIS_ORE(Material.LAPIS_ORE, 35, 8, 1, Material.LAPIS_LAZULI, 2),
    DEEPSLATE_LAPIS_ORE(Material.DEEPSLATE_LAPIS_ORE, 35, 8, 1, Material.LAPIS_LAZULI, 2),

    DIAMOND_ORE(Material.DIAMOND_ORE, 35, 5, 1, Material.DIAMOND, 3),
    DEEPSLATE_DIAMOND_ORE(Material.DEEPSLATE_DIAMOND_ORE, 35, 5, 1, Material.DIAMOND, 3),

    GOLD_ORE(Material.GOLD_ORE, 35, 5, 1, Material.GOLD_INGOT, 2),
    DEEPSLATE_GOLD_ORE(Material.DEEPSLATE_GOLD_ORE, 35, 5, 1, Material.GOLD_INGOT, 2),

    COPPER_ORE(Material.COPPER_ORE, 35, 5, 1, Material.COPPER_INGOT, 1),
    DEEPSLATE_COPPER_ORE(Material.DEEPSLATE_COPPER_ORE, 35, 5, 1, Material.COPPER_INGOT, 1),

    EMERALD_ORE(Material.EMERALD_ORE, 35, 5, 1, Material.EMERALD, 1),
    DEEPSLATE_EMERALD_ORE(Material.DEEPSLATE_EMERALD_ORE, 35, 5, 1, Material.EMERALD, 1),

    EMERALD_BLOCK(Material.EMERALD_BLOCK, 5, 12, 6, Material.EMERALD, 6),
    REDSTONE_BLOCK(Material.REDSTONE_BLOCK, 5, 12, 6, Material.REDSTONE, 6),
    GOLD_BLOCK(Material.GOLD_BLOCK, 5, 12, 6, Material.GOLD_INGOT, 6),
    IRON_BLOCK(Material.IRON_BLOCK, 5, 12, 6, Material.IRON_INGOT, 6),
    LAPIS_BLOCK(Material.LAPIS_BLOCK, 5, 12, 6, Material.LAPIS_BLOCK, 6),
    DIAMOND_BLOCK(Material.DIAMOND_BLOCK, 5, 10, 6, Material.DIAMOND, 8),
    NETHERITE_BLOCK(Material.NETHERITE_BLOCK, 5, 8, 6, Material.NETHERITE_INGOT, 12),

    PUMPKIN(Material.PUMPKIN, 100, 1, 1, Material.PUMPKIN, 1),
    SUGAR_CANE(Material.SUGAR_CANE, 100, 1, 1, Material.SUGAR_CANE, 1),
    MELON(Material.MELON, 100, 1, 1, Material.MELON, 1),
    ;

    private String name;
    private Material mat;
    private int chance;
    private int maxDrop;
    private int minDrop;
    private Material dropItem;
    private int ticksRequired;

    FieldBlockType(Material mat, int chance, int maxDrop, int minDrop, Material dropItem, int ticksRequired){
        this.mat = mat;
        this.chance = chance;
        this.maxDrop = maxDrop;
        this.minDrop = minDrop;
        this.name = mat.toString();
        this.dropItem = dropItem;
        this.ticksRequired = ticksRequired;
    }

    public Material getMat() {
        return mat;
    }

    public int getChance() {
        return chance;
    }

    public int getMaxDrop() {
        return maxDrop;
    }

    public int getMinDrop() {
        return minDrop;
    }

    public String getName() {
        return name;
    }

    public Material getDropItem() {
        return dropItem;
    }

    public int getTicksRequired() {
        return ticksRequired;
    }
}
