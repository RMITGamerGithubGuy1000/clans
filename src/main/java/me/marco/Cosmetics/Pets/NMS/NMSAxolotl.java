package me.marco.Cosmetics.Pets.NMS;

import me.marco.CustomEntities.PathFinders.FollowGoal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;

public class NMSAxolotl extends Axolotl {

    private LivingEntity owner;

    public NMSAxolotl(Player owner, Location loc) {
        super(EntityType.AXOLOTL, ((CraftWorld) loc.getWorld()).getHandle());
        Level craftWorld = ((CraftWorld) loc.getWorld()).getHandle();
//        this.owner = ((LivingEntity) owner).get;
        this.goalSelector.getAvailableGoals().clear();
//        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new FollowGoal(this, ((CraftPlayer)owner).getHandle(), 2, 1, 1,true));
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
        craftWorld.getWorld().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    public boolean hurtServer(ServerLevel var1, DamageSource var2, float var3){
        return false;
    }

    public LivingEntity getPassenger() {
        if(this.hasExactlyOnePlayerPassenger()) {
            return (LivingEntity) this.getPassengers().iterator().next();
        }
        return null;
    }

    public boolean passengerJumping(){
        Field field = null;
        try {
            field = LivingEntity.class.
                    getDeclaredField("bk");
            field.setAccessible(true);

            if(field != null && this.getPassenger() != null){
                return field.getBoolean(this.getPassenger());
            }
        } catch (NoSuchFieldException | IllegalAccessException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
        }
        return false;
    }

//    @Override
//    public void move(MoverType enummovetype, Vec3 vec3d){
//        if(this.isAlive()){
//            if(this.getPassenger() != null){
//                if (this.onGround() && passengerJumping()) {
//                    this.getJumpControl().jump();
//                    this.playSound(SoundEvents.SHEEP_AMBIENT, 1F, 5F);
//                }
//                LivingEntity passenger = this.getPassenger();
//                float yaw = Location.normalizeYaw(passenger.getYHeadRot());
//                this.setYRot(yaw);
//                float pitch = Location.normalizePitch(passenger.xRotO);
//                this.setXRot(pitch);
//                this.setRot(this.getYRot(), this.getXRot());
//
//                this.yRotO = yaw;
//                this.xRotO = pitch;
//                this.setYHeadRot(yaw);
//
//                double f = passenger.xo * 0.5F;
//                double f1 = passenger.zo;
//
//                if (f1 <= 0.0F) {
//                    f1 *= 0.25F;
//                }
//
////                if (this.onGround() && this.fK() && !this.cw) {
////                    f = 0.0F;
////                    f1 = 0.0F;
////                }
//
////                this.bb = this.ew() * 0.1F;
//                super.move(MoverType.SELF, new Vec3((double)f, 0.0, (double)f1));
////                this.a(this, false);
////                this.as();
//            }else{
//                super.move(enummovetype, vec3d);
//            }
//        }
//    }



}
