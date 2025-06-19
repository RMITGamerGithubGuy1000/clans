package me.marco.Skills.Skills.Ranger.Sword;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Tags.MiscTags.NoFall;
import me.marco.Events.Items.ItemBlockCollisionEvent;
import me.marco.Items.iThrowable;
import me.marco.Skills.Builders.eClassType;
import me.marco.Skills.Data.ISkills.SkillTypes.InteractSkill;
import me.marco.Skills.Data.ISkills.WeaponTypes.SwordSkill;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilParticles;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Hookshot extends Skill implements SwordSkill, InteractSkill {

    private HashMap<iThrowable, Client> hookMap = new HashMap<iThrowable, Client>();

    private final double BASE_VELOCITY = 1.0;

    public Hookshot(String name, Core instance, eClassType classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(name, instance, classType, items, actions, maxLevel, showRecharge, useInteract);
    }

    @Override
    public void activate(Player player, int level) {
        useHookshot(player,level);
    }

    @EventHandler
    public void onHookCollision(ItemBlockCollisionEvent event){
        iThrowable throwable = event.getCollided();
        Block block = event.getCollidedOn();
        if(!this.hookMap.containsKey(throwable)) return;
        Client toPull = hookMap.get(throwable);
        Player player = toPull.getPlayer();
        Item item = throwable.getItem();
        launchPlayer(player, item);
        UtilParticles.playBlockParticle(item.getLocation(), block.getType(), true);
        UtilParticles.playBlockParticle(player.getLocation(), block.getType(), true);
        item.remove();
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 10);
        this.hookMap.remove(throwable);
    }

    private void useHookshot(Player player, int level){
        iThrowable hook = getInstance().getItemManager().createThrowable(10, false, true, false,
                player.getLocation().add(0, 2, 0), Material.TRIPWIRE_HOOK, player.getName() + "Hookshot", false, null);
        launchHook(hook, player, level);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 2);
    }

    private void launchHook(iThrowable hook, Player player, int level){
        hook.getItem().setVelocity(player.getEyeLocation().getDirection().multiply(calculateLaunch(level)));
        hookMap.put(hook, getInstance().getClientManager().getClient(player));
    }

    private void launchPlayer(Player player, Item item){
        double d = item.getLocation().distance(player.getLocation());
        double g = -0.08;
        double x = (1.0 + 0.07 * d) * (item.getLocation().getX() - player.getLocation().getX()) / d;
        double y = (1.0 + 0.03 * d) * (item.getLocation().getY() - player.getLocation().getY()) / d - 1 * g * d;
        double z = (1.0 + 0.07 * d) * (item.getLocation().getZ() - player.getLocation().getZ()) / d;
        Vector v = item.getLocation().toVector();
        v.setX(x);
        v.setY(y);
        v.setZ(z);
        v.multiply(.7);
        player.setVelocity(v);
        item.getWorld().playSound(item.getLocation(), Sound.ENTITY_PLAYER_SMALL_FALL, 1, 10);
        Client client = getInstance().getClientManager().getClient(player);
        getInstance().getTagManager().addTag(client, new NoFall(client, 6, getInstance()));
    }

    private double calculateLaunch(int level){
        return BASE_VELOCITY + .3 * level;
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                ChatColor.YELLOW + "Launch a " +  ChatColor.AQUA + "Hookshot" + ChatColor.YELLOW + " that launches you ",
                ChatColor.YELLOW + "towards it at a velocity of " + ChatColor.GREEN + calculateLaunch(level) + "" + ChatColor.YELLOW + ". ",
                ChatColor.YELLOW + "You gain 6 seconds of " + ChatColor.LIGHT_PURPLE + "No-Fall" + ChatColor.YELLOW + " on trigger. "
        };
    }

    @Override
    public boolean useageCheck(Player player) {
        return hasMana(player);
    }

    @Override
    public double getCooldown(int level) {
        return 25 - (.5 * level);
    }

    @Override
    public int getMana(int level) {
        return 30;
    }
}
