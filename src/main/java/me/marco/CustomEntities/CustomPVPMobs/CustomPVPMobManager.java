//package me.marco.CustomEntities.CustomPVPMobs;
//
//import me.marco.Base.Core;
//import me.marco.WorldEvent.Bosses.Lycanthrope.Lycanthrope;
//import org.bukkit.Material;
//import org.bukkit.entity.*;
//import org.bukkit.inventory.ItemStack;
//import java.util.ArrayList;
//
//public class CustomPVPMobManager {
//
//    private Core instance;
//
//    private ArrayList<CustomPVPMob> mobList = new ArrayList<>();
//
//    public CustomPVPMobManager(Core instance){
//        this.instance = instance;
//    }
//
//    public void addCustomMob(CustomPVPMob customPVPMob){
//        this.mobList.add(customPVPMob);
//    }
//
//    private Core getInstance(){
//        return this.instance;
//    }
//
//    public CustomPVPMob getCustomEntity(Entity entity){
//        return this.mobList.stream().filter(customEntity -> customEntity.isEntityFromThis(entity)).findFirst().orElse(null);
//    }
//
//    public void addEntity(CustomPVPMob customEntity){
//        this.mobList.add(customEntity);
//    }
//
//    public void removeCustomEntity(CustomPVPMob customEntity){
//        this.mobList.remove(customEntity);
//    }
//
//
//    public void spawnTest(Player player) {
//        //Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
//        Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
//        zombie.setSilent(true);
////        zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999999, 1));
//        zombie.getEquipment().setHelmet(new ItemStack(Material.ENDER_CHEST));
//        //    public Lycanthrope(Core instance, List<BossSkill> bossSkillList, Location spawnPoint, int maxHP, CustomPVPMob entity) {
//        Lycanthrope lycanthrope = new Lycanthrope(getInstance(), null, player.getLocation(), 10, zombie);
//        zombie.getLocation().getWorld().strikeLightningEffect(zombie.getLocation());
//        this.mobList.add(lycanthrope);
//    }
//}
