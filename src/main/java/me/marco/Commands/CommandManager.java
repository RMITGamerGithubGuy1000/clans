package me.marco.Commands;

import me.marco.Base.Core;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {

    private Core core;
    private List<ICommand> commands;

    public CommandManager(Core core){
        this.core = core;
        this.commands = new ArrayList<ICommand>();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        return false;
    }

    public Core getInstance(){
        return this.core;
    }

    public List<ICommand> getCommands(){ return this.commands; }

    public void addCommand(ICommand icommand){
        this.commands.add(icommand);
    }

    public int getCommandPages(){
        int size = getCommands().size();
        float pages = size / 6f;
        float remainder = pages % 1;
        if(remainder != 0){
            pages -= remainder;
            pages++;
        }
        return (int) pages;
    }

    public void sendHelp(Player player, int page){
        int pages = getCommandPages();
        int max = page * 6;
        int min = max - 6;
        getInstance().getChat().sendClans(player, ChatColor.YELLOW + "Clans help " + ChatColor.BLUE + "Page " + page + "/" + pages + ChatColor.RED + " ==========================");
        for(ICommand command : getCommands()){
            int index = getCommands().indexOf(command);
            if(index >= min && index < max) getInstance().getChat().sendCommandSummary(player, command);
        }
        getInstance().getChat().sendClans(player, ChatColor.RED + "============================================");
    }

    public ICommand getCommand(String sub) {
        return getCommands().stream().filter(s -> s.getName().equalsIgnoreCase(sub)).findFirst().orElse(null);
    }

}
