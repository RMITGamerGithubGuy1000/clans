package me.marco.Admin.Commands.SubCommands.Fields;

import me.marco.Admin.Commands.AdminCommand;
import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Client.ClientRank;
import me.marco.Events.Clans.CustomEvents.Land.LandClaimEvent;
import me.marco.Fields.FieldsManager;
import me.marco.Handlers.ClanManager;
import me.marco.Client.Client;
import me.marco.Commands.ICommand;
import me.marco.Handlers.ClientManager;
import me.marco.Handlers.LandManager;
import me.marco.Utility.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class AdminFieldsMode extends AdminCommand implements ICommand {

    private Chat chat;
    private Core instance;

    public AdminFieldsMode(Core instance){
        this.instance = instance;
        this.chat = instance.getChat();
    }

    public void run(Player player, String[] args) {
        if(args.length > 1){
            chat.sendClans(player, "Too many arguments" + getCommandExample());
            return;
        }

        instance.getFieldsManager().toggleFieldsMode(player);

    }

    public String getName() {
        return "Fields";
    }

    public String getDescription() {
        return chat.commandDescription("Toggle fieldsmode");
    }

    public String getCommandExample() {
        return chat.commandExample("x fieldsmode");
    }

    @Override
    public ClientRank getRankRequired() {
        return ClientRank.ADMIN;
    }
}
