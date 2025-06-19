package me.marco.Commands;

import org.bukkit.entity.Player;

public interface ICommand {

    void run(Player player, String[] args);

    String getName();

    String getDescription();

    String getCommandExample();

}
