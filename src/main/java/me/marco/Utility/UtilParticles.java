package me.marco.Utility;

import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class UtilParticles {

    public static void playBlockParticle(Location location, Material mat){
        location.getWorld().spawnParticle(Particle.BLOCK, location, 30, mat.createBlockData());
    }

    public static void playBlockParticle(Player player, Material mat, boolean sound){
        Location loc = player.getLocation().add(0, 1, 0);
        BlockData bd = mat.createBlockData();
        if(sound){
            loc.getWorld().playSound(loc, bd.getSoundGroup().getBreakSound(), 1, 1);
        }
        loc.getWorld().spawnParticle(Particle.BLOCK, loc, 30, bd);
    }

    public static void playBlockParticle(Location loc, Material mat, boolean sound){
        BlockData bd = mat.createBlockData();
        if(sound){
            loc.getWorld().playSound(loc, bd.getSoundGroup().getBreakSound(), 1, 1);
        }
        loc.getWorld().spawnParticle(Particle.BLOCK, loc, 30, bd);
    }

    public static void playBlockParticle(Location location, int count, Material mat){
        location.getWorld().spawnParticle(Particle.BLOCK, location, count, mat.createBlockData());
    }

    public static void playInstantBreak(Location location, PotionEffectType potionEffectType){
        location.getWorld().playEffect(location, Effect.INSTANT_POTION_BREAK, potionEffectType.getColor().asRGB());
    }

    public static void playPotionBreak(Location location, PotionEffectType potionEffectType){
        location.getWorld().playEffect(location, Effect.POTION_BREAK, potionEffectType.getColor().asRGB());
    }

    public static void drawColourCircle(Location center, double radius, double pitch, double yaw, int r, int g, int b, float size) {
        yaw = Math.toRadians(yaw);
        pitch = Math.toRadians(pitch);
        Vector right = new Vector(Math.cos(yaw + (Math.PI/2.0)), 0, Math.sin(yaw + (Math.PI/2.0)));
        Vector up = new Vector(Math.cos(yaw), Math.sin(pitch), Math.sin(yaw));
        Location temp = center.clone();
        double a = 0;
        for (int i = 0; i < 360; i = i + 2) {
            a = Math.toRadians(i);
            temp.setX(center.getX() + (radius * Math.cos(a) * right.getX()) + (radius * Math.sin(a) * up.getX()));
            temp.setY(center.getY() + (radius * Math.cos(a) * right.getY()) + (radius * Math.sin(a) * up.getY()));
            temp.setZ(center.getZ() + (radius * Math.cos(a) * right.getZ()) + (radius * Math.sin(a) * up.getZ()));
            center.getWorld().spawnParticle(Particle.DUST, temp, 1, new Particle.DustOptions(Color.fromRGB(r, g, b), size));
        }
    }

    public static void drawRedstoneParticle(Location loc, int r, int g, int b, float size) {
        loc.getWorld().spawnParticle(Particle.DUST, loc, 1, new Particle.DustOptions(Color.fromRGB(r, g, b), size));
    }

    public static void drawLine(Location point1, Location point2, double space, int r, int g, int b, float size) {
        World world = point1.getWorld();
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;
        for (; length < distance; p1.add(vector)) {
            world.spawnParticle(Particle.DUST, p1.getX(), p1.getY(), p1.getZ(), 1, new Particle.DustOptions(Color.fromRGB(r, g, b), size));
            length += space;
        }
    }

    public static void drawLine(Location point1, Location point2, Particle particle, double space, int r, int g, int b, float size) {
        World world = point1.getWorld();
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;
        for (; length < distance; p1.add(vector)) {
            world.spawnParticle(particle, p1.getX(), p1.getY(), p1.getZ(), 1, r, g, b, size);
            length += space;
        }
    }

    public static void drawParticleCircle(Location center, Particle particle, double radius, double pitch, double yaw, int r, int g, int b, float size) {
        yaw = Math.toRadians(yaw);
        pitch = Math.toRadians(pitch);
        Vector right = new Vector(Math.cos(yaw + (Math.PI/2.0)), 0, Math.sin(yaw + (Math.PI/2.0)));
        Vector up = new Vector(Math.cos(yaw), Math.sin(pitch), Math.sin(yaw));
        Location temp = center.clone();
        double a = 0;
        for (int i = 0; i < 360; i = i + 10) {
            a = Math.toRadians(i);
            temp.setX(center.getX() + (radius * Math.cos(a) * right.getX()) + (radius * Math.sin(a) * up.getX()));
            temp.setY(center.getY() + (radius * Math.cos(a) * right.getY()) + (radius * Math.sin(a) * up.getY()));
            temp.setZ(center.getZ() + (radius * Math.cos(a) * right.getZ()) + (radius * Math.sin(a) * up.getZ()));
            center.getWorld().spawnParticle(particle, temp, 0, r, g, b, size);
        }
    }

    public static void drawCircleBlast(Player player, Particle particle, int test, double radius, double pitch, double yaw, float size) {
        yaw = Math.toRadians(yaw);
        pitch = Math.toRadians(pitch);
        Vector right = new Vector(Math.cos(yaw + (Math.PI/2.0)), 0, Math.sin(yaw + (Math.PI/2.0)));
        Vector up = new Vector(Math.cos(yaw), Math.sin(pitch), Math.sin(yaw));
        Location center = player.getLocation().add(0, 1, 0);
        Location temp = center.clone();
        Vector vector = player.getEyeLocation().getDirection();
        double a = 0;
        for (int i = 0; i < 360; i = i + 10) {
            a = Math.toRadians(i);
            temp.setX(center.getX() + (radius * Math.cos(a) * right.getX()) + (radius * Math.sin(a) * up.getX()));
            temp.setY(center.getY() + (radius * Math.cos(a) * right.getY()) + (radius * Math.sin(a) * up.getY()));
            temp.setZ(center.getZ() + (radius * Math.cos(a) * right.getZ()) + (radius * Math.sin(a) * up.getZ()));
            center.getWorld().spawnParticle(particle, temp, test, vector.getX(), vector.getY(), vector.getZ(), size);
        }
    }

    public static void drawCircleWithVector(Location start, Particle particle, double radius, double pitch, double yaw, float size, Vector direction){
        yaw = Math.toRadians(yaw);
        pitch = Math.toRadians(pitch);
        Vector right = new Vector(Math.cos(yaw + (Math.PI/2.0)), 0, Math.sin(yaw + (Math.PI/2.0)));
        Vector up = new Vector(Math.cos(yaw), Math.sin(pitch), Math.sin(yaw));
        Location temp = start.clone();
        double a = 0;
        for (int i = 0; i < 360; i = i + 10) {
            a = Math.toRadians(i);
            temp.setX(start.getX() + (radius * Math.cos(a) * right.getX()) + (radius * Math.sin(a) * up.getX()));
            temp.setY(start.getY() + (radius * Math.cos(a) * right.getY()) + (radius * Math.sin(a) * up.getY()));
            temp.setZ(start.getZ() + (radius * Math.cos(a) * right.getZ()) + (radius * Math.sin(a) * up.getZ()));
            start.getWorld().spawnParticle(Particle.CLOUD, temp.getX(), temp.getY(), temp.getZ(),
                    0, (float) direction.getX(), (float) direction.getY(), (float) direction.getZ(), size, null);
        }
    }

    public static void playEffect(Location loc, Particle particle, int x1, int r, int g, int b, float size){
        loc.getWorld().spawnParticle(particle, loc, x1, r, g, b, size);
    }

    public static void playPotionSwirl(Location location, PotionEffectType potionEffectType){
        location.getWorld().playEffect(location, Effect.POTION_BREAK, potionEffectType.getColor().asRGB());
    }

    public static void smallerCircleParticle(Location center, Particle particle, double radius, double pitch, double yaw, int r, int g, int b, float size) {
        yaw = Math.toRadians(yaw);
        pitch = Math.toRadians(pitch);
        Vector right = new Vector(Math.cos(yaw + (Math.PI/2.0)), 0, Math.sin(yaw + (Math.PI/2.0)));
        Vector up = new Vector(Math.cos(yaw), Math.sin(pitch), Math.sin(yaw));
        Location temp = center.clone();
        double a = 0;
        for (int i = 0; i < 360; i = i + 20) {
            a = Math.toRadians(i);
            temp.setX(center.getX() + (radius * Math.cos(a) * right.getX()) + (radius * Math.sin(a) * up.getX()));
            temp.setY(center.getY() + (radius * Math.cos(a) * right.getY()) + (radius * Math.sin(a) * up.getY()));
            temp.setZ(center.getZ() + (radius * Math.cos(a) * right.getZ()) + (radius * Math.sin(a) * up.getZ()));
            center.getWorld().spawnParticle(particle, temp, 0, r, g, b, size);
        }
    }

    public static void smallerCircle(Location center, double radius, double pitch, double yaw, int r, int g, int b, float size) {
        yaw = Math.toRadians(yaw);
        pitch = Math.toRadians(pitch);
        Vector right = new Vector(Math.cos(yaw + (Math.PI/2.0)), 0, Math.sin(yaw + (Math.PI/2.0)));
        Vector up = new Vector(Math.cos(yaw), Math.sin(pitch), Math.sin(yaw));
        Location temp = center.clone();
        double a = 0;
        for (int i = 0; i < 360; i = i + 20) {
            a = Math.toRadians(i);
            temp.setX(center.getX() + (radius * Math.cos(a) * right.getX()) + (radius * Math.sin(a) * up.getX()));
            temp.setY(center.getY() + (radius * Math.cos(a) * right.getY()) + (radius * Math.sin(a) * up.getY()));
            temp.setZ(center.getZ() + (radius * Math.cos(a) * right.getZ()) + (radius * Math.sin(a) * up.getZ()));
            center.getWorld().spawnParticle(Particle.DUST, temp, 1, new Particle.DustOptions(Color.fromRGB(r, g, b), size));
        }
    }

}
