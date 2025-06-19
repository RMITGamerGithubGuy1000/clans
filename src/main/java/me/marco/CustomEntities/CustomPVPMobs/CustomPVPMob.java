//package me.marco.CustomEntities.CustomPVPMobs;
//
//import me.marco.Base.Core;
//import me.marco.CustomEntities.CustomEntity;
//import me.marco.CustomEntities.CustomPVPMobs.NMS.NMSSlime;
//import me.marco.Utility.UtilParticles;
//import org.bukkit.*;
//import org.bukkit.attribute.Attribute;
//import org.bukkit.entity.*;
//import org.bukkit.metadata.FixedMetadataValue;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.util.Vector;
//
//public abstract class CustomPVPMob extends CustomEntity {
//
//    private ArmorStand healthStand;
//    private ArmorStand nameStand;
//    private Slime healthAdjuster;
//    private Slime nameAdjuster;
//    private boolean isSmallName;
//    private boolean isSmallHealth;
//    private String name;
//    private int healthSize;
//    private int nameSize;
//    private Core instance;
//    private double maxHP;
//    private long lastHit;
//
//    public CustomPVPMob(LivingEntity entity, Location spawnLoc, int healthSize, int nameSize,
//                        boolean isSmallName, boolean isSmallHealth, String name, int maxHP, Core instance){
//        super(entity, name, spawnLoc);
//        this.maxHP = maxHP;
//        this.instance = instance;
//        this.name = name;
//        this.nameSize = nameSize;
//        this.healthSize = healthSize;
//        this.isSmallHealth = isSmallHealth;
//        this.isSmallName = isSmallName;
////        spawnHealthStand();
//    }
//
//    public abstract void onDeath();
//    public abstract void onHit();
//
//    public void registerLastHitTime(){
//        this.lastHit = System.currentTimeMillis() + 800;
//    }
//
//    public boolean canBeHit(){
//        return System.currentTimeMillis() >= this.lastHit;
//    }
//
//    public boolean isEntityFromThis(Entity entity){
//        return this.getEntity() == entity || this.getHealthAdjuster() == entity || this.getNameAdjuster() == entity ||
//                this.getHealthStand() == entity || this.getNameStand() == entity;
//    }
//
//    public void refreshStands(){
////        spawnHealthStand();
////        spawnNameStand();
//    }
//
//    private void spawnHealthStand(){
//        ArmorStand armorStand = (ArmorStand) this.getEntity().getWorld().spawnEntity(this.getEntity().getLocation(), EntityType.ARMOR_STAND);
//        armorStand.setSmall(this.isSmallHealth);
////        armorStand.setInvisible(true);
//        armorStand.setInvulnerable(true);
//        armorStand.setCustomName(healthString());
//        armorStand.setCustomNameVisible(true);
//        armorStand.setCollidable(false);
//        this.healthStand = armorStand;
//
//        this.healthAdjuster = (Slime) new NMSSlime(this.getEntity().getLocation(), this.healthSize).getBukkitEntity();
//
//        this.getEntity().addPassenger(healthAdjuster);
//        this.healthAdjuster.addPassenger(armorStand);
////        this.healthAdjuster.setInvisible(true);
//        this.healthAdjuster.setInvulnerable(true);
//        this.healthAdjuster.setCollidable(false);
//    }
//
////    private void spawnNameStand(){
////        ArmorStand armorStand = (ArmorStand) this.getEntity().getWorld().spawnEntity(this.getEntity().getLocation(), EntityType.ARMOR_STAND);
////        armorStand.setSmall(this.isSmallName);
////        armorStand.setInvulnerable(true);
//////        armorStand.setInvisible(true);
////        armorStand.setCustomName(this.getName());
////        armorStand.setCustomNameVisible(true);
////        armorStand.setCollidable(false);
////        this.nameStand = armorStand;
////
////        this.nameAdjuster = (Slime) new NMSSlime(this.getEntity().getLocation(), this.nameSize).getBukkitEntity();
////
////        this.getEntity().addPassenger(nameAdjuster);
////        this.nameAdjuster.addPassenger(armorStand);
//////        this.nameAdjuster.setInvisible(true);
////        this.nameAdjuster.setInvulnerable(true);
////        this.nameAdjuster.setCollidable(false);
////    }
//
//    public String healthString(){
//        double maxHealth = this.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
//        double health = this.getEntity().getHealth();
//        ChatColor currHealthColour = health == maxHealth ? ChatColor.GREEN : ChatColor.RED;
//        return currHealthColour.toString() + (int) health + ""
//                + ChatColor.GOLD + ChatColor.BOLD.toString() + "/"
//                + ChatColor.RESET.toString() + ChatColor.GREEN + (int) maxHealth + "";
//    }
//
//
//    public String healthString(double damage){
//        double maxHealth = this.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
//        double health = this.getEntity().getHealth() - damage;
//        ChatColor currHealthColour = health == maxHealth ? ChatColor.GREEN : ChatColor.RED;
//        return currHealthColour.toString() + (int) health + ""
//                + ChatColor.GOLD + ChatColor.BOLD.toString() + "/"
//                + ChatColor.RESET.toString() + ChatColor.GREEN + (int) maxHealth + "";
//    }
//
//    public void updateHealth(){
//        this.getHealthStand().setCustomName(healthString());
//    }
//
//    @Override
//    public void postSpawnEffects() {
//
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public ArmorStand getHealthStand() {
//        return healthStand;
//    }
//
//    public ArmorStand getNameStand() {
//        return nameStand;
//    }
//
//    public void setHealthStand(ArmorStand healthStand) {
//        this.healthStand = healthStand;
//    }
//
//    public Core getInstance(){
//        return this.instance;
//    }
//
//    public void cleanup(){
//        this.getInstance().getCustomPVPMobManager().removeCustomEntity(this);
//
//        this.getEntity().remove();
//
//        this.getNameStand().remove();
//        this.getNameAdjuster().remove();
//        this.getHealthStand().remove();
//        this.getHealthAdjuster().remove();
//    }
//
//    public Slime getHealthAdjuster() {
//        return healthAdjuster;
//    }
//
//    public Slime getNameAdjuster() {
//        return nameAdjuster;
//    }
//
//    public int getHealthSize() {
//        return healthSize;
//    }
//
//    public int getNameSize() {
//        return nameSize;
//    }
//
//    public double getMaxHP() {
//        return maxHP;
//    }
//
//    public void createBlocksBleed(Location l, int amountper, Material m, byte data, int duration, double periods, double delay){
//        new BukkitRunnable(){
//            double count = 20 * duration / periods;
//            public void run(){
//                if(count <= 0){
//                    this.cancel();
//                }else{
//                    for(int i = 0; i < amountper; i++){
//                        @SuppressWarnings("deprecation")
//                        FallingBlock fb = l.getWorld().spawnFallingBlock(l, m, data);
//                        fb.setDropItem(false);
//                        fb.setHurtEntities(false);
//                        float x = -0.5F + (float) (Math.random() * 1.0D);
//                        float y = -0.5F + (float) (Math.random() * 2.0D);
//                        float z = -0.5F + (float) (Math.random() * 1.0D);
//                        fb.setVelocity(new Vector(x, y, z).multiply(1.2));
//                        fb.setHurtEntities(false);
//                        fb.setDropItem(false);
//                        fb.setMetadata("noplace", new FixedMetadataValue(getInstance(), true));
//                    }
//                }
//                count--;
//            }
//        }.runTaskTimer(getInstance(), (long) delay * 20, (long) periods);
//    }
//
//    public void playEffectRepeating(Location l, Particle e, int size, int duration, double periods, double delay){
//        new BukkitRunnable(){
//            double count = 20 * duration / periods;
//            public void run(){
//                if(count <= 0){
//                    this.cancel();
//                }else{
//                    UtilParticles.playEffect(l, e, 0, 0, 0, 0, size);
//                }
//                count--;
//            }
//        }.runTaskTimer(getInstance(), (long) delay * 20, (long) periods);
//    }
//
//    public void playChime(Location l, Sound s, float volume, float pitch, int duration, double periods, double delay){
//        new BukkitRunnable(){
//            double count = 20 * duration / periods;
//            public void run(){
//                if(count <= 0){
//                    this.cancel();
//                }else{
//                    l.getWorld().playSound(l, s, volume, pitch);
//                }
//                count--;
//            }
//        }.runTaskTimer(getInstance(), (long) delay * 20, (long) periods);
//    }
//
//    public void removeHealth(int damage){
//
//    }
//
//    public void setNameStand(ArmorStand nameStand) {
//        this.nameStand = nameStand;
//    }
//
//    public void setHealthAdjuster(Slime healthAdjuster) {
//        this.healthAdjuster = healthAdjuster;
//    }
//
//    public void setNameAdjuster(Slime nameAdjuster) {
//        this.nameAdjuster = nameAdjuster;
//    }
//
//    public void setSmallName(boolean smallName) {
//        isSmallName = smallName;
//    }
//
//    public void setSmallHealth(boolean smallHealth) {
//        isSmallHealth = smallHealth;
//    }
//
//    public void setHealthSize(int healthSize) {
//        this.healthSize = healthSize;
//    }
//
//    public void setNameSize(int nameSize) {
//        this.nameSize = nameSize;
//    }
//
//    public void setInstance(Core instance) {
//        this.instance = instance;
//    }
//
//    public void setMaxHP(double maxHP) {
//        this.maxHP = maxHP;
//    }
//}