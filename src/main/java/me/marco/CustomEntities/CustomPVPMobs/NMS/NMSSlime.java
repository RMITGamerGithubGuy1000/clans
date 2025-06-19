//package me.marco.CustomEntities.CustomPVPMobs.NMS;
//
//import com.google.common.annotations.VisibleForTesting;
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.monster.Slime;
//import net.minecraft.world.level.Level;
//import org.bukkit.Location;
//import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
//import org.bukkit.event.entity.CreatureSpawnEvent;
//
//import java.lang.reflect.Field;
//
//public class NMSSlime extends Slime {
//
//    public NMSSlime(Location loc, int size) {
//        super(EntityType.SLIME, ((CraftWorld) loc.getWorld()).getHandle());
//        Level craftWorld = ((CraftWorld) loc.getWorld()).getHandle();
////
////        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
//
//        this.setPos(loc.getX(), loc.getY(), loc.getZ());
//        craftWorld.getWorld().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
//
//        this.persist = true;
//        this.setSize(size, false);
//        this.getBoundingBox().setMaxX(0);
//        this.getBoundingBox().setMaxY(0);
//        this.getBoundingBox().setMaxZ(0);
//        this.getBoundingBox().setMinX(0);
//        this.getBoundingBox().setMinY(0);
//        this.getBoundingBox().setMinZ(0);
//    }
//
//    @Override
//    public void setSize(int i, boolean flag){
//        int j = i;
//        Field dataWatcher = null;
//        try {
//            dataWatcher = Slime.class.
//                    getDeclaredField("bT");
//            dataWatcher.setAccessible(true);
//
//            EntityDataAccessor<Integer> fieldValue = (EntityDataAccessor<Integer>) dataWatcher.get(Slime.class);
//
//            this.entityData.set(fieldValue, j);
//            this.reapplyPosition();
//            this.refreshDimensions();
//            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)(j * j));
//            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)(0.2F + 0.1F * (float)j));
//            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((double)j);
//            if (flag) {
//                this.setHealth(this.getMaxHealth());
//            }
//
//            this.xpReward = j;
//        } catch (NoSuchFieldException | IllegalAccessException noSuchFieldException) {
//            noSuchFieldException.printStackTrace();
//        }
//
//    }
//
//
//}
