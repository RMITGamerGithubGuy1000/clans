package me.marco.PvPItems;

import me.marco.Base.Core;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class AbilityItem extends PvPItem implements Listener {

    public AbilityItem(Core core, String name, Material itemType) {
        super(core, name, itemType);
        core.getServer().getPluginManager().registerEvents(this, (Plugin) core);
    }

}
