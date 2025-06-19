package me.marco.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class CListener<T> implements Listener {

    private T instance;

    public CListener(T instance) {
        Bukkit.getPluginManager().registerEvents(this, (Plugin) instance);
        this.instance = instance;
    }

    protected T getInstance() {
        return instance;
    }

}