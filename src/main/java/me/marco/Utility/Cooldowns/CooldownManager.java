package me.marco.Utility.Cooldowns;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.PvPItems.PvPItem;
import me.marco.Skills.Builders.BuildSkill;
import me.marco.Skills.Builders.ClassBuild;
import me.marco.Skills.Data.ISkills.ISkill;
import me.marco.Skills.Data.ISkills.SkillTypes.ChannelSkill;
import me.marco.Skills.Data.ISkills.SkillTypes.ChannelSkillData;
import me.marco.Skills.Data.Skill;
import me.marco.Utility.UtilTime;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {

    private Core core;

    public CooldownManager(Core core) {
        this.core = core;
    }

    private ConcurrentHashMap<String, List<Cooldown>> cooldownMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, List<Cooldown>> getCooldowns() {
        return cooldownMap;
    }

    public List<Cooldown> getCooldowns(Player player) {
        if (cooldownMap.containsKey(player.getName())) {
            return cooldownMap.get(player.getName());
        }
        return new ArrayList<Cooldown>();
    }

    public void sendRemaining(Player player, String ability){
        core.getChat().sendModule(player ,"You cannot use " +
                core.getChat().highlightText + ability + core.getChat().textColour + " for " +
                core.getChat().highlightNumber + "" + Math.max(0, getAbilityRecharge(player.getName(), ability).getRemaining()) +
               core.getChat().textColour + " seconds", "Cooldown");
    }

    public boolean add(Player player, String skill, String useageCat, int level, double d, boolean inform) {
        return add(player, skill, useageCat, level, d, inform, true);
    }

    public boolean add(Player player, Skill skill, String useageCat, int level, double d, boolean inform) {
        return add(player, skill, useageCat, level, d, inform, true);
    }

    public boolean add(Player player, Skill skill, String useageCat, int level, double d, boolean inform, boolean removeOnDeath) {
        if (isCooling(player.getName(), skill.getName())) {
            if (inform) {
                sendRemaining(player, skill.getName());
            }
            return false;
        }

        if (!cooldownMap.containsKey(player.getName())) {
            cooldownMap.put(player.getName(), new ArrayList<Cooldown>());
        }
        if(inform && !(skill instanceof ChannelSkill))core.getChat().sendUseage(player, skill.getName(), useageCat, level);
        cooldownMap.get(player.getName()).add(new Cooldown(skill.getName(), d, System.currentTimeMillis(), removeOnDeath, inform));
        skill.removeMana(player);
        return true;
    }

    public boolean add(Player player, PvPItem item, Action action, boolean inform) {
        if (isCooling(player.getName(), item.getName(action))) {
            if (inform) {
                sendRemaining(player, item.getName(action));
            }
            return false;
        }

        if (!cooldownMap.containsKey(player.getName())) {
            cooldownMap.put(player.getName(), new ArrayList<Cooldown>());
        }
        if(inform) core.getChat().sendUseageNoLevel(player, item.getName(action), "Item");
        cooldownMap.get(player.getName()).add(new Cooldown(item.getName(action), item.getCooldown(action), System.currentTimeMillis(), true, inform));
        item.removeMana(player, action);
        return true;
    }

    public boolean channelExpireAdd(ChannelSkillData channelSkillData){
        Player player = channelSkillData.getCaster();
        ChannelSkill skill = channelSkillData.getToCast();
        boolean inform = true;
        int level = channelSkillData.getLevel();
        double d = skill.getCooldown(level);
        if (isCooling(player.getName(), skill.getName())) {
            return false;
        }
        if (!cooldownMap.containsKey(player.getName())) {
            cooldownMap.put(player.getName(), new ArrayList<Cooldown>());
        }
        if(inform && !(skill instanceof ChannelSkill))core.getChat().sendUseage(player, skill.getName(), skill.getClassTypeName(), level);
        cooldownMap.get(player.getName()).add(new Cooldown(skill.getName(), d, System.currentTimeMillis(), false, inform));
        return true;
    }

    public boolean channelAdd(Player player, Skill skill, String useageCat, int level, double d, boolean inform, boolean removeOnDeath){
        if (isCooling(player.getName(), skill.getName())) {
            if (inform) {
                sendRemaining(player, skill.getName());
            }
            return false;
        }
        if (!cooldownMap.containsKey(player.getName())) {
            cooldownMap.put(player.getName(), new ArrayList<Cooldown>());
        }
        if(inform && !(skill instanceof ChannelSkill))core.getChat().sendUseage(player, skill.getName(), skill.getClassTypeName(), level);
        cooldownMap.get(player.getName()).add(new Cooldown(skill.getName(), d, System.currentTimeMillis(), false, inform));
        return true;
    }

    public boolean add(Player player, String skill, String useageCat, int level, double d, boolean inform, boolean removeOnDeath) {
        if (isCooling(player.getName(), skill)) {
            if (inform) {
                sendRemaining(player, skill);
            }
            return false;
        }
        if (!cooldownMap.containsKey(player.getName())) {
            cooldownMap.put(player.getName(), new ArrayList<Cooldown>());
        }
        if(inform) core.getChat().sendUseage(player, skill, useageCat, level);
        cooldownMap.get(player.getName()).add(new Cooldown(skill, d, System.currentTimeMillis(), removeOnDeath, inform));
        return true;
    }

    public synchronized boolean isCooling(String player, String ability) {
        if (!cooldownMap.containsKey(player)) {
            return false;
        }
        for (Cooldown cd : cooldownMap.get(player)) {
            if (cd.getAbility().toLowerCase().equalsIgnoreCase(ability.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean isCooling(Player playerObj, String ability) {
        String player = playerObj.getName();
        if (!cooldownMap.containsKey(player)) {
            return false;
        }
        for (Cooldown cd : cooldownMap.get(player)) {
            if (cd.getAbility().toLowerCase().equalsIgnoreCase(ability.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public synchronized Cooldown getCooldown(Player playerObj, String skill) {
        String player = playerObj.getName();
        if (!cooldownMap.containsKey(player)) {
            return null;
        }
        for (Cooldown cd : cooldownMap.get(player)) {
            if (cd.getAbility().toLowerCase().equalsIgnoreCase(skill.toLowerCase())) {
                return cd;
            }
        }
        return null;
    }

    public double getRemaining(String player, String ability) {
        for (Cooldown cd : cooldownMap.get(player)) {
            if (cd.getAbility().equalsIgnoreCase(ability)) {
                return UtilTime.convert((cd.getTime() + cd.getSystime()) - System.currentTimeMillis(), UtilTime.TimeUnit.MINUTES, 1);
            }
        }
        return 0;
    }

    public double getRemaining(Player player, String ability) {
        for (Cooldown cd : cooldownMap.get(player.getName())) {
            if (cd.getAbility().equalsIgnoreCase(ability)) {
                return UtilTime.convert((cd.getTime() + cd.getSystime()) - System.currentTimeMillis(), UtilTime.TimeUnit.MINUTES, 1);
            }
        }
        return 0;
    }

    public String getRemainingString(String player, String ability) {
        for (Cooldown cd : cooldownMap.get(player)) {
            if (cd.getAbility().equalsIgnoreCase(ability)) {
                return UtilTime.getTime((cd.getTime() + cd.getSystime()) - System.currentTimeMillis(), UtilTime.TimeUnit.BEST, 1);
            }
        }
        return "";
    }

    public String getRemainingString(Player player, String ability) {
        for (Cooldown cd : cooldownMap.get(player.getName())) {
            if (cd.getAbility().equalsIgnoreCase(ability)) {
                return UtilTime.getTime((cd.getTime() + cd.getSystime()) - System.currentTimeMillis(), UtilTime.TimeUnit.BEST, 1);
            }
        }
        return "";
    }

    public Cooldown getAbilityRecharge(String player, String ability) {
        if (cooldownMap.containsKey(player)) {
            ListIterator<Cooldown> it = cooldownMap.get(player).listIterator();
            while (it.hasNext()) {
                Cooldown cd = it.next();
                if (cd.getAbility().equalsIgnoreCase(ability.toLowerCase())) {
                    return cd;
                }
            }
        }
        return null;
    }

    public void removeCooldown(String player, String ability, boolean silent) {
        Cooldown c = getAbilityRecharge(player, ability);
        if (c == null) return;
        cooldownMap.get(player).remove(c);

        if (!silent) {
            Player toGet = core.getServer().getPlayer(player);
            if (toGet != null) {
                core.getChat().sendModule(toGet ,core.getChat().highlightText + ability + core.getChat().textColour + " has been recharged","Cooldown");
            }
        }
    }

    public synchronized void handleCooldowns() {
        Iterator<Map.Entry<String, List<Cooldown>>> it = cooldownMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<Cooldown>> next = it.next();
            ListIterator<Cooldown> ability = next.getValue().listIterator();
            while (ability.hasNext()) {
                Cooldown ab = ability.next();
                if (ab == null) {
                    it.remove();
                    continue;
                }
                if (ab.getRemaining() <= 0) {
                    Player p = core.getServer().getPlayer(next.getKey());
                    if (p != null) {
                        if (ab.isInform()) {
                            core.getChat().sendModule(p ,core.getChat().highlightText + ab.getAbility() + core.getChat().textColour + " has been recharged","Cooldown");
                        }
                    }
                    ability.remove();
                    if (next.getValue().isEmpty()) {
                        it.remove();
                    }
                }
            }
        }
    }

    public void handleActionBars(){
        for(Player player : core.getServer().getOnlinePlayers()) {
            Client client = core.getClientManager().getClient(player);
            ClassBuild classBuild = client.getActiveBuild();
            PvPItem pvpItem = getInstance().getPvPItemManager().isConsumeable(player.getInventory().getItemInMainHand());
            if(pvpItem != null){
                actionBar(player, pvpItem.getName(Action.RIGHT_CLICK_AIR));
            }
            if(classBuild == null) continue;
            if (Arrays.stream(ISkill.getSwords).anyMatch(
                    material -> getItemInHand(player, EquipmentSlot.HAND).getType() == material)) {
                if (!classBuild.hasSwordSkill()) return;
                actionBar(player, classBuild.getSwordSkill().getSkill().getName());
                continue;
            }
            if (Arrays.stream(ISkill.getAxes).anyMatch(
                    material -> getItemInHand(player, EquipmentSlot.HAND).getType() == material)) {
                if (!classBuild.hasAxeSkill()) return;
                actionBar(player, classBuild.getAxeSkill().getSkill().getName());
                continue;
            }
        }
    }

    public ItemStack getItemInHand(Player player, EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.HAND) {
            return player.getInventory().getItemInMainHand();
        }
        if (equipmentSlot == EquipmentSlot.OFF_HAND) {
            return player.getInventory().getItemInOffHand();
        }
        return null;
    }

    public void actionBar(Player player, String skill){
        if(!isCooling(player, skill)){
            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new TextComponent(ChatColor.GREEN + ChatColor.BOLD.toString() + skill + "" + ChatColor.GOLD + ChatColor.BOLD.toString() + " Ready")
            );
            return;
        }
        Cooldown cooldown = core.getCooldownManager().getAbilityRecharge(player.getName(), skill);
        player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent(skillPrefix(cooldown) + " " + generateBar(cooldown) + " " + remainingSeconds(cooldown))
        );
    }

    public String remainingSeconds(Cooldown cooldown){
        return ChatColor.GREEN + ChatColor.BOLD.toString() + cooldown.getRemaining() + "" + ChatColor.GOLD + " seconds";
    }

    public String skillPrefix(Cooldown cooldown){
        return ChatColor.GOLD + ChatColor.BOLD.toString() + cooldown.getAbility() + ChatColor.RESET.toString();
    }

    public String generateBar(Cooldown cooldown){
        String bar = "";
        for(int i = 0; i < 20; i++){
            if(i <= calcBarsActive(cooldown)) {
                bar += ChatColor.GREEN + "\u258C";
                continue;
            }
            bar += ChatColor.RED + "\u258C";
        }
        return bar;
    }

    public int calcBarsActive(Cooldown cooldown){
        double percentage = cooldown.getRemaining() / (cooldown.getTime() / 1000);
        int count = 0;
        for(double x = 1; percentage < x; x-=.05){
            if(percentage > x) break;
            count++;
        }
        return count;
    }

    public synchronized boolean reduceCooldown(Player player, String ability, double reductionPercent, boolean inform) {
        // Retrieve the cooldown for the specified ability
        Cooldown cd = getCooldown(player, ability);

        if (cd == null) {
            return false; // No cooldown found
        }
        double timeLeft = cd.getTime();
        double reduceBy = timeLeft * reductionPercent;
        double newTime = timeLeft - reduceBy;
        // Calculate the new reduced cooldown time
        double newCooldownTime = Math.max(0, newTime);

        // Ensure thread-safety by updating the cooldown time
        cd.setTime(newCooldownTime);

        // Optionally notify the player
        if (inform) {
            core.getChat().sendModule(player, core.getChat().highlightText + ability + core.getChat().textColour + " cooldown reduced.", "Cooldown");
        }

        return true;
    }

    public Core getInstance(){
        return this.core;
    }

}
