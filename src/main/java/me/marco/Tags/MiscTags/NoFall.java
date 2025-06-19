package me.marco.Tags.MiscTags;

import me.marco.Base.Core;
import me.marco.Client.Client;
import me.marco.Tags.Tag;

public class NoFall extends Tag {

    public NoFall(Client owner, long expiry, Core instance) {
        super(owner, "NoFall", expiry, instance);
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onExpiry() {

    }
}
