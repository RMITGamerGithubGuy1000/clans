package me.marco.Kits;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Client.ClientRank;
import me.marco.Events.Skills.ClassDequipEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommandManager implements CommandExecutor {

    private Core core;

    public KitCommandManager(Core core) {
        this.core = core;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Need 2 be a player m8");
            return true;
        }
        Player player = (Player) sender;
        Client client = getInstance().getClientManager().getClient(player);
        if(!(client.getClientRank() == ClientRank.OWNER || client.getClientRank() == ClientRank.ADMIN)){
            getInstance().getChat().sendModule(player, "You must be Admin+", "Permissions");
            return true;
        }
        if(args == null || args.length == 0){
            sendHelp(player);
            return true;
        }
//        if(!getInstance().getLandManager().isInSafezone(player)){
//            getInstance().getChat().sendModule(player, "You can only equip a kit in spawn", "Kit");
//            return true;
//        }
        Kit kit = getInstance().getKitManager().getKit(args[0].toLowerCase());
        if(kit == null){
            sendHelp(player);
            return true;
        }
        getInstance().getServer().getPluginManager().callEvent(new ClassDequipEvent(player));
        kit.equip(player);
        return true;
    }

    public void sendHelp(Player player){
        getInstance().getChat().sendModule(player, "All kits " + ChatColor.GREEN + "==========================", "Kit");
        player.sendMessage(ChatColor.GRAY + ChatColor.BOLD.toString() + "Ranger");
        player.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Mage");
        player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Rogue");
        player.sendMessage(ChatColor.BOLD.toString() + "Warrior");
        player.sendMessage(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Guardian");
        player.sendMessage(ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() +  "Druid");
        getInstance().getChat().sendModule(player, "Usage: " + ChatColor.GREEN + "/kit <kitname>", "Kit");
    }

    public Core getInstance() {
        return this.core;
    }

}
