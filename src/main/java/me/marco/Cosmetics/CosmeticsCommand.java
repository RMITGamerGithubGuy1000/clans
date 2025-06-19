package me.marco.Cosmetics;

import me.marco.Base.Core;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CosmeticsCommand implements CommandExecutor {

    private Core instance;

    public CosmeticsCommand(Core instance){
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            getInstance().getCosmeticMenuManager().openCosmeticMenu(player);
        }
        return true;
    }

    public Core getInstance() {
        return instance;
    }
}
