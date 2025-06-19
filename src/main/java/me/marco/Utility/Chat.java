package me.marco.Utility;

import me.marco.Admin.AdminClans.AdminClan;
import me.marco.Base.Core;
import me.marco.Clans.Objects.Relations.Alliance;
import me.marco.Clans.Objects.Relations.Enemy;
import me.marco.Client.Client;
import me.marco.Clans.Objects.Invites.Invite;
import me.marco.Clans.Objects.Invites.InviteType;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Client.ClientRank;
import me.marco.Tags.PvPTag;
import me.marco.Commands.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class Chat {

    private Core instance;

    public Chat(Core instance){
        this.instance = instance;
    }

    public ChatColor prefixColour = ChatColor.BLUE;
    public final ChatColor textColour = ChatColor.GRAY;
    public ChatColor highlightName = ChatColor.BLUE;
    public ChatColor highlightText = ChatColor.GREEN;
    public ChatColor highlightNumber = ChatColor.LIGHT_PURPLE;

    public String joinMessage(Player player){
        return ChatColor.DARK_AQUA + "Join" + ChatColor.DARK_GRAY + "> " + ChatColor.GREEN + player.getName();
    }

    public String leaveMessage(Player player){
        return ChatColor.DARK_AQUA + "Quit" + ChatColor.DARK_GRAY + "> " + ChatColor.RED + player.getName();
    }

    public void sendAdminModeToggle(Client client){
        String onOff = client.isAdminMode() ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF";
        sendAdminMessage(client.getPlayer(), "Admin mode has been toggled " + onOff);
    }

    public void sendClans(Player player, String text) {
        player.sendMessage(prefixColour + "Clans> " + textColour + text);
    }

    public void sendModule(Player player, String text, String module) {
        player.sendMessage(prefixColour + "" + module + "> " + textColour + text);
    }

    public void broadcastMessage(String text, String module) {
        instance.getServer().broadcastMessage(prefixColour + "" + module + "> " + textColour + text);
    }

    public void broadcastClanCreation(String creatorName, Clan clan) {
        for(Player player : instance.getServer().getOnlinePlayers()){
            Client client = instance.getClientManager().getClient(player);
            player.sendMessage(prefixColour + "Clans> " + getClanRelation(client, clan) + creatorName
                    + textColour + " created clan " + getClanRelation(client, clan) + clan.getName());
        }
    }

    public void broadcastClanDisband(Client disbander, Clan clan) {
        for(Player player : instance.getServer().getOnlinePlayers()){
            Client client = instance.getClientManager().getClient(player);
            player.sendMessage(prefixColour + "Clans> " + getClanRelation(client, disbander) + disbander.getName()
                    + textColour + " disbanded clan " + getClanRelation(client, clan) + clan.getName());
        }
    }

    public void sendInviteMessage(Invite invite) {
        InviteType type = invite.getInviteType();
        if (type == InviteType.CLAN) {
            Client invited = (Client) invite.getInvited();
            Clan clan = (Clan) invite.getInviter();
            sendClans(invited.getPlayer(), "You have been invited to clan " + ChatColor.GOLD + clan.getName());
            instance.getClanManager().sendClanAnnouncement(clan, ChatColor.GOLD + invited.getName() + "" + textColour + " has been invited to the clan");
            return;
        }
        if (type == InviteType.ALLY) {
            Clan toAlly = (Clan) invite.getInvited();
            Clan requesting = (Clan) invite.getInviter();
            instance.getClanManager().sendClanAnnouncement(toAlly, "An alliance request has been made from clan " + getClanRelation(toAlly, requesting) + requesting.getName());
            instance.getClanManager().sendClanAnnouncement(requesting, "Your alliance request to clan " + getClanRelation(toAlly, requesting) + toAlly.getName() + ""
                    + textColour + " has been sent");
            return;
        }
        if (type == InviteType.TRUST) {
            Clan toAlly = (Clan) invite.getInvited();
            Clan requesting = (Clan) invite.getInviter();
            instance.getClanManager().sendClanAnnouncement(toAlly, "A trust request has been made from clan " + getClanRelation(toAlly, requesting) + requesting.getName());
            instance.getClanManager().sendClanAnnouncement(requesting, "Your trust request to clan " + getClanRelation(toAlly, requesting) + toAlly.getName() + ""
                    + textColour + " has been sent");
            return;
        }
        if (type == InviteType.NEUTRAL) {
            Clan toNeutral = (Clan) invite.getInvited();
            Clan requesting = (Clan) invite.getInviter();
            instance.getClanManager().sendClanAnnouncement(toNeutral, "A neutrality request has been made from clan " + getClanRelation(toNeutral, requesting) + requesting.getName());
            instance.getClanManager().sendClanAnnouncement(requesting, "Your neutrality request to clan " + getClanRelation(toNeutral, requesting) + toNeutral.getName() + ""
                    + textColour + " has been sent");
            return;
        }
    }

    public void sendInviteExpiry(Invite invite) {
        InviteType type = invite.getInviteType();
        if (type == InviteType.CLAN) {
            Client invited = (Client) invite.getInvited();
            Clan clan = (Clan) invite.getInviter();
            sendClans(invited.getPlayer(), "Your invite to clan " + getClanRelation(invited, clan) + clan.getName() + ""
                    + textColour + " has expired");
            return;
        }
        Clan toSend = (Clan) invite.getInvited();
        Clan requesting = (Clan) invite.getInviter();
        instance.getClanManager().sendClanAnnouncement(toSend,"The " + type.getName() + " request from clan "
                + getClanRelation(toSend, requesting) + requesting.getName() + textColour + " has expired");

        instance.getClanManager().sendClanAnnouncement(requesting,"Your " + type.getName() + " request to clan "
                + getClanRelation(toSend, requesting) + toSend.getName() + textColour + " has expired");
    }

    public void sendCommandSummary(Player player, ICommand command){
        player.sendMessage(ChatColor.RED + "| " + command.getDescription());
        player.sendMessage(ChatColor.RED + "| " + command.getCommandExample());
    }

    public String commandDescription(String text){
        return ChatColor.AQUA + text;
    }

    public String commandExample(String text){
        return ChatColor.GREEN + "Usage: /" + text;
    }

    public String isOnline(Client client){
        return client.isOnline() ? ChatColor.GREEN + client.getName() : ChatColor.RED + client.getName();
    }

    public String clanRankPrefix(Client client){
        return ChatColor.GOLD + client.getClanRank().getPrefix() + ".";
    }

    public String membersString(List<Client> clientList){
        String start = "";
        int listSize = clientList.size();
        for(Client client : clientList){
            String splitter = listSize == 1 ? "" : ChatColor.YELLOW + ", ";
            start += clanRankPrefix(client) + isOnline(client) + splitter;
            listSize--;
        }
        return start;
    }

    public String alliesString(Client checking, List<Alliance> allianceList){
        String start = "";
        int listSize = allianceList.size();
        for(Alliance alliance : allianceList){
            String splitter = listSize == 1 ? "" : ChatColor.YELLOW + ", ";
            Clan allianceWith = alliance.getAllianceWith();
            start += getClanRelation(checking, allianceWith) + allianceWith.getName() + splitter;
            listSize--;
        }
        return start;
    }

    public String enemiesString(Client checking, List<Enemy> allianceList){
        String start = "";
        int listSize = allianceList.size();
        for(Enemy enemy : allianceList){
            String splitter = listSize == 1 ? "" : ChatColor.YELLOW + ", ";
            Clan enemyWith = enemy.getEnemyWith();
            String dominance = dominanceString(enemy, enemyWith.getEnemy(enemy.getEnemyOwner()));
            start += getClanRelation(checking, enemyWith) + enemyWith.getName() + " " + dominance + splitter;
            listSize--;
        }
        return start;
    }

    public String dominanceString(Enemy clanGreen, Enemy clanRed){
        return ChatColor.GOLD + "[" +
                ChatColor.GREEN + clanGreen.getDominance() +
                ChatColor.GOLD + ":" +
                ChatColor.RED + clanRed.getDominance() +
                ChatColor.GOLD + "] ";
    }

    public String findPlayerMatchesString(List<Client> clientList){
        String start = "";
        int listSize = clientList.size();
        for(Client client : clientList){
            String splitter = listSize == 1 ? "" : ChatColor.YELLOW + ", ";
            start += ChatColor.BLUE + client.getName() + "" + splitter;
            listSize--;
        }
        return start;
    }

    public String findClansMatchesString(List<Clan> clanList){
        String start = "";
        int listSize = clanList.size();
        for(Clan clan : clanList){
            String splitter = listSize == 1 ? "" : ChatColor.YELLOW + ", ";
            start += ChatColor.BLUE + clan.getName() + "" + splitter;
            listSize--;
        }
        return start;
    }

    public void sendChatMessage(Player receiving, Client sendingClient, Client receivingClient, String message){
        if(sendingClient.hasClan()){
            if(!receivingClient.hasClan()) {
                receiving.sendMessage(ChatColor.GOLD + sendingClient.getClan().getName() + " " + ChatColor.YELLOW
                        + sendingClient.getName() + ChatColor.GOLD + "> " + ChatColor.WHITE + message);
                return;
            }
            ChatColor mainColour = getPlayerRelationMain(sendingClient, receivingClient);
            ChatColor subColour = getPlayerRelationSub(sendingClient, receivingClient);
            receiving.sendMessage(mainColour + sendingClient.getClan().getName() + " " + subColour
                    + sendingClient.getName() + mainColour + "> " + ChatColor.WHITE + message);
            return;
        }
        receiving.sendMessage(ChatColor.YELLOW + sendingClient.getName() + ChatColor.GOLD + "> " + ChatColor.WHITE + message);
    }

    public ChatColor getPlayerRelationMain(Client client1, Client client2){
        if(!client1.hasClan()) return ChatColor.GOLD;
        if(!client2.hasClan()) return ChatColor.GOLD;
        Clan clan1 = client1.getClan();
        Clan clan2 = client2.getClan();
        return getClanRelation(clan1, clan2);
    }

    public ChatColor getPlayerRelationSub(Client client1, Client client2){
        if(!client1.hasClan()) return ChatColor.YELLOW;
        if(!client2.hasClan()) return ChatColor.YELLOW;
        Clan clan1 = client1.getClan();
        Clan clan2 = client2.getClan();
        return getClanRelationSub(clan1, clan2);
    }

    public ChatColor getPlayerRelationMain(Client client1, Clan clan){
        if(!client1.hasClan()) return ChatColor.GOLD;
        Clan clan1 = client1.getClan();
        return getClanRelation(clan1, clan);
    }

    public ChatColor getPlayerRelationSub(Client client1, Clan clan){
        if(!client1.hasClan()) return ChatColor.YELLOW;
        Clan clan1 = client1.getClan();
        return getClanRelationSub(clan1, clan);
    }

    public ChatColor getClanRelation(Client client, Clan clan2){
        if(client.hasClan()){
            return getClanRelation(client.getClan(), clan2);
        }
        return ChatColor.GOLD;
    }

    public ChatColor getClanRelation(Player player1, Player player2){
        Client client = instance.getClientManager().getClient(player1);
        Client client2 = instance.getClientManager().getClient(player2);
        if(client.hasClan() && client2.hasClan()){
            return getClanRelation(client.getClan(), client2.getClan());
        }
        return ChatColor.GOLD;
    }

    public ChatColor getClanRelation(Client client, Client client2){
        if(client.hasClan() && client2.hasClan()){
            return getClanRelation(client.getClan(), client2.getClan());
        }
        return ChatColor.GOLD;
    }

    public ChatColor getClanRelation(Clan clan1, Clan clan2){
        if(clan1 == clan2){ return ChatColor.DARK_AQUA; }
        if(clan1.isAllied(clan2)){ return ChatColor.DARK_GREEN; }
        if(clan1.isEnemied(clan2)){ return ChatColor.DARK_RED; }
        if(clan1.isPillageRelation(clan2)){ return ChatColor.DARK_PURPLE; }
        return ChatColor.GOLD;
    }

    public ChatColor getClanRelationSub(Clan clan1, Clan clan2){
        if(clan1 == clan2){ return ChatColor.AQUA; }
        if(clan1.isAllied(clan2)){ return ChatColor.GREEN; }
        if(clan1.isEnemied(clan2)){ return ChatColor.RED; }
        if(clan1.isPillageRelation(clan2)){ return ChatColor.LIGHT_PURPLE; }
        return ChatColor.YELLOW;
    }

    public void sendLandMessage(Client client, Clan clan){
        Player player = client.getPlayer();
        if(player == null){ return; }
        if(clan instanceof AdminClan){
            AdminClan adminClan = (AdminClan) clan;
            String isSafe = adminClan.isSafe() ? ChatColor.GREEN + "Safe" : ChatColor.RED + "Unsafe";
            player.sendMessage(prefixColour + "Clans> " + textColour + "Territory: " + ChatColor.GREEN + clan.getName() + " " +
                    ChatColor.GRAY + "(" + isSafe + ChatColor.GRAY + ")");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 3);
            return;
        }
        if(clan == null){
            player.sendMessage(prefixColour + "Clans> " + textColour + "Territory: " + ChatColor.DARK_GREEN + "Wilderness");
            return;
        }
        if(!client.hasClan()){
            player.sendMessage(prefixColour + "Clans> " + textColour + "Territory: " + ChatColor.GOLD + clan.getName());
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 1, 3);
            return;
        }
        Clan clientClan = client.getClan();
        if(clientClan.isEnemied(clan)){
            player.sendMessage(prefixColour + "Clans> " + textColour + "Territory: " + getClanRelation(clientClan, clan) + clan.getName() +
                    " " + dominanceString(clientClan.getEnemy(clan), clan.getEnemy(clientClan)));
            player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 1, 3);
            return;
        }
        if(clientClan.isTrusted(clan)){
            player.sendMessage(prefixColour + "Clans> " + textColour + "Territory: " + getClanRelation(clientClan, clan) + clan.getName() + " "
                    + ChatColor.GOLD + "(" + ChatColor.LIGHT_PURPLE + "Trusted" + ChatColor.GOLD + ")");
            player.playSound(player.getLocation(), Sound.ENTITY_HORSE_GALLOP, 1, 3);
            return;
        }
        player.sendMessage(prefixColour + "Clans> " + textColour + "Territory: " + getClanRelation(clientClan, clan) + clan.getName());
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 3);
    }

    public void sendKicked(Player player, Client kicker, Clan clan){
        if(player != null) {
            player.sendMessage(prefixColour + "Clans>" + textColour + " You have been kicked from clan "
                    + ChatColor.GOLD + clan.getName() + "" + textColour + " by " + ChatColor.GOLD + kicker.getName());
        }
    }
    public void sendLeftConfirmation(Player player, Clan clan){
        if(player != null) {
            player.sendMessage(prefixColour + "Clans>" + textColour + " You have left clan " + ChatColor.GOLD + clan.getName());
        }
    }

    public String formatMaterial(Material material){
        String name = material.name();
        String[] split = name.split("_");
        String newName = "";
        for(String word : split){
            newName+= word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
        }
        return newName.trim();
    }

    public void sendAdminMessage(Player player, String message){
        sendModule(player, message, "Admin");
    }

    public void sendUseage(Player player, String skill, String useageCat, int level){
        if(level < 1){
            sendModule(player, "You used " + highlightText + skill + "", useageCat);
            return;
        }
        sendModule(player, "You used " + highlightText + skill + " " + level + "", useageCat);
    }

    public void sendUseageNoLevel(Player player, String skill, String useageCat){
        sendModule(player, "You used " + highlightText + skill + " " + "", useageCat);
    }

    public void sendUseage(Player player, String skill, String useageCat){
        sendModule(player, "You used " + highlightText + skill + " ", useageCat);
    }

    public void handleDeathMessage(PvPTag pvptag) {
        Player target = pvptag.getTarget();
        Player damager = pvptag.getDamager();
        String cause = pvptag.getCause();
        for(Player player : instance.getServer().getOnlinePlayers()){
            player.sendMessage(prefixColour + "Death> " + getClanRelation(player, target) + target.getName()
                    + textColour + " was killed by " + getClanRelation(player, damager) + damager.getName() +
                    textColour + " with " + ChatColor.LIGHT_PURPLE + cause);
        }
    }

    public void handleDeathMessage(Player dead){
        for(Player player : instance.getServer().getOnlinePlayers()){
            player.sendMessage(prefixColour + "Death> " + getClanRelation(player, dead) + dead.getName()
                    + textColour + " died ");
        }
    }

    public void handleDeathStealMessage(Player killed, Player killer, double amount) {
        ChatColor relation = getClanRelation(killed, killer);
        sendModule(killed, relation + killer.getName() + "" + textColour + " stole " + ChatColor.RED + "$" + amount + "" + textColour + " from you", "Money");
        sendModule(killer, "You stole " + ChatColor.GREEN + "$" + amount + "" + textColour + " from " + relation + killed.getName(), "Money");
    }

    public void sendNoHarm(Player damager, Player target) {
        sendClans(damager, "You can not harm " + getClanRelation(damager, target) + target.getName() + "" + textColour + " in a " + ChatColor.GREEN + "Safe Zone");
    }

    public void sendNoHarmInSafeZone(Player hitting, Player toHit){
        if(!getInstance().getLandManager().isInSafezone(toHit)) return;
        if(getInstance().getClientManager().getClient(toHit).hasCombatTag()) return;
        Clan clan = getInstance().getLandManager().getOwningClan(toHit.getLocation().getChunk());
        getInstance().getChat().sendClans(hitting, "You can not harm " + getClanRelation(hitting, toHit) + toHit.getName() +
                getInstance().getChat().textColour + " in " + highlightText + clan.getName());
    }

    public Core getInstance() {
        return instance;
    }

    public void sendAdminPowerRequirement(Player player, Client client, ClientRank clientRank) {
        sendModule(player,
                "This requires " + ChatColor.BLACK + "[" + clientRank.getColour() +  clientRank.getName() + "" +
                        ChatColor.BLACK + "]" + textColour + " level permissions to action", "Permissions");
    }
}
