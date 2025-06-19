package me.marco.Fields;

import me.marco.Base.Core;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class FieldsManager {

    private final Material FIELDS_WAND = Material.BLAZE_ROD;

    private Core instance;
    private final Random random = new Random();
    private final int min = 1;
    private final int max = 100;

    private ArrayList<Block> blockList = new ArrayList<Block>();
    private ArrayList<String> fieldsModeList = new ArrayList<String>();
    private ArrayList<FieldsTask> fieldsTasks = new ArrayList<FieldsTask>();

    public FieldsManager(Core instance){
        this.instance = instance;
    }

    public void addFieldBlock(Block block) {
        this.blockList.add(block);
    }

    public void handleTasks(){
        ArrayList<FieldsTask> toRemove = new ArrayList<>();
        this.fieldsTasks.stream().forEach(fieldsTask -> {
            if(fieldsTask.runTick()){
                toRemove.add(fieldsTask);
            }
        });
        this.fieldsTasks.removeAll(toRemove);
    }

    public void breakBlock(Player player, Block block){
        if(block.getType() == Material.STONE) return;
        FieldBlockType fieldBlockType = FieldBlockType.valueOf(block.getType().toString());
            int chance = fieldBlockType.getChance();
            if(chance == 100){
                FieldsTask fieldsTask = new FieldsTask(block, fieldBlockType.getTicksRequired());
                this.fieldsTasks.add(fieldsTask);
                block.breakNaturally();
                return;
            }
            int result = random.nextInt(max-min) + min;
            if(result <= chance){
                int minDrop = fieldBlockType.getMinDrop();
                int maxDrop = fieldBlockType.getMaxDrop();
                int amount = random.nextInt(maxDrop-minDrop) + minDrop;
                ItemStack itemStack = instance.getUtilItem().createNaturalItem(fieldBlockType.getDropItem());
                itemStack.setAmount(amount);
                block.getWorld().dropItemNaturally(block.getLocation(), itemStack);
                FieldsTask fieldsTask = new FieldsTask(block, fieldBlockType.getTicksRequired());
                this.fieldsTasks.add(fieldsTask);
                block.setType(Material.STONE);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            }else {
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }
    }

    public boolean isFieldsBlock(Block block){
        return this.blockList.stream().filter(fieldsBlock -> (
                fieldsBlock.getX() == block.getX() &&
                        fieldsBlock.getY() == block.getY() &&
                        fieldsBlock.getZ() == block.getZ()
                )).findFirst().orElse(null) != null;
    }

    public void addFieldBlockAndSQL(Block block){
        addFieldBlock(block);
        instance.getSqlRepoManager().getFieldsRepo().createFieldBlock(block);
    }

    public void toggleFieldsMode(Player player) {
        String name = player.getName();
        if(fieldsModeList.contains(name)){
            instance.getChat().sendModule(player, "Fields mode toggled " + ChatColor.RED + "OFF", "Fields");
            fieldsModeList.remove(name);
            return;
        }
        instance.getChat().sendModule(player, "Fields mode toggled " + ChatColor.GREEN + "ON", "Fields");
        fieldsModeList.add(name);
    }

    public boolean isFieldsMode(Player player){
        return this.fieldsModeList.contains(player.getName());
    }

    public boolean hasWand(Player player){
        return player.getInventory().getItemInMainHand().getType() == FIELDS_WAND;
    }

    public boolean isFieldsModeActive(Player player) {
        return hasWand(player) && isFieldsMode(player);
    }

    public void removeFieldsBlockAndSQL(Block block) {
        this.blockList.remove(block);
        instance.getSqlRepoManager().getFieldsRepo().removeFieldBlock(block);
    }
}
